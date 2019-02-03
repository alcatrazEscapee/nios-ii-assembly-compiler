/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util;

import java.util.*;
import java.util.function.Consumer;

import compiler.component.ComponentLabel;
import compiler.component.IComponent;

import static compiler.component.IComponent.Flag.LABEL;
import static compiler.component.IComponent.Flag.TYPE;

public final class Optimizer
{
    private static final String[] ORIGINAL = {"blt", "bgt", "ble", "bge", "bne", "beq"};

    private static final class LabelMap
    {
        private final Map<String, Map<Integer, List<String>>> labels = new HashMap<>();
        private final Set<String> allLabels = new HashSet<>();
        private final Map<String, String> simplifiedLabels = new HashMap<>();
        private String functionName = null;

        LabelMap()
        {
            labels.put("while", new HashMap<>());
            labels.put("if", new HashMap<>());
            labels.put("else", new HashMap<>());
        }

        void add(String label)
        {
            if (allLabels.contains(label))
            {
                return;
            }
            allLabels.add(label);

            // functionName_loopType##_sub_truthy
            // Note _sub_truthy might not exist for simple conditionals
            String[] args = label.split("_");
            if (functionName == null)
            {
                functionName = args[0];
            }

            Map<Integer, List<String>> map = labels.get(args[1].replaceAll("[0-9]", ""));
            int count = Integer.valueOf(args[1].replaceAll("[A-Za-z]", ""));
            if (!map.containsKey(count))
            {
                map.put(count, new ArrayList<>());
            }
            if (args.length <= 2)
            {
                map.get(count).add("");
            }
            else
            {
                map.get(count).add("_" + args[2] + "_" + args[3]);
            }
        }

        void build()
        {
            for (Map.Entry<String, Map<Integer, List<String>>> e1 : labels.entrySet())
            {
                String loopType = e1.getKey();
                for (Map.Entry<Integer, List<String>> e2 : e1.getValue().entrySet())
                {
                    int count = e2.getKey();
                    List<String> suffixes = e2.getValue();
                    suffixes.sort(String.CASE_INSENSITIVE_ORDER);

                    for (int i = 0; i < suffixes.size(); i++)
                    {
                        String suffix = i == 0 ? "" : Helpers.alphabetSuffix(i);
                        String oldLabel = String.format("%s_%s%d%s", functionName, loopType, count, suffixes.get(i));
                        String newLabel = String.format("%s_%s%d%s", functionName, loopType, count, suffix);
                        simplifiedLabels.put(oldLabel, newLabel);
                    }
                }
            }
        }

        String get(String label)
        {
            return simplifiedLabels.get(label);
        }
    }

    private static final String[] INVERTED = {"bge", "ble", "bgt", "blt", "beq", "bne"};

    public static void accept(List<IComponent> base, String... ignoreFlags)
    {
        //if (true) return;
        final List<String> flags = Arrays.asList(ignoreFlags);
        final boolean simplifyNames = !flags.contains("simplify_names");
        final boolean invertConditionals = !flags.contains("invert_conditionals");

        Consumer<List<IComponent>> optimizer = list -> {};
        do
        {
            // Run the last found optimization
            optimizer.accept(base);
            // Reset the optimizer
            optimizer = null;

            // Search for potential optimizations between two statements
            for (int i = 0; i < base.size() - 1; i++)
            {
                // Get components
                final IComponent first = base.get(i), second = base.get(i + 1);
                final int index = i;

                // Multiple Consecutive Labels
                if (first.getFlag(TYPE).equals("label") && second.getFlag(TYPE).equals("label"))
                {
                    optimizer = list -> {
                        // Remove the second label and replace it with the first
                        String labelToRemove = list.get(index + 1).getFlag(LABEL);
                        String labelToReplace = list.get(index).getFlag(LABEL);
                        list.remove(index + 1);
                        for (IComponent cmp : list)
                        {
                            if (cmp.getFlag(LABEL).equals(labelToRemove))
                            {
                                cmp.setFlag(LABEL, labelToReplace);
                            }
                        }
                    };
                    break;
                }

                // Unreachable Statement
                if (first.getFlag(TYPE).equals("break") && !second.getFlag(TYPE).equals("label"))
                {
                    optimizer = list -> list.remove(index + 1);
                    break;
                }

                // Consecutive Break - Label
                if ((first.getFlag(TYPE).equals("break") || first.getFlag(TYPE).equals("break_conditional")) && second.getFlag(TYPE).equals("label") && first.getFlag(LABEL).equals(second.getFlag(LABEL)))
                {
                    optimizer = list -> list.remove(index);
                    break;
                }

                // Consecutive Label - Break
                if (first.getFlag(TYPE).equals("label") && second.getFlag(TYPE).equals("break") && !first.getFlag(LABEL).equals(second.getFlag(LABEL)))
                {
                    optimizer = list -> {
                        // Remove the first label and replace it with the second
                        String labelToRemove = list.get(index).getFlag(LABEL);
                        String labelToReplace = list.get(index + 1).getFlag(LABEL);
                        list.remove(index);
                        for (IComponent cmp : list)
                        {
                            if (cmp.getFlag(LABEL).equals(labelToRemove))
                            {
                                cmp.setFlag(LABEL, labelToReplace);
                            }
                        }
                    };
                    break;
                }
            }

            // Search for various optimizations based on single statements
            for (int i = 0; i < base.size(); i++)
            {
                String label = base.get(i).getFlag(LABEL);
                if (!label.equals(""))
                {
                    // Count instances of label appearance
                    long count = base.stream().filter(x -> x.getFlag(LABEL).equals(label)).count();
                    if (count <= 1)
                    {
                        final int index = i;
                        optimizer = list -> list.remove(index);
                        break;
                    }
                }
            }

            // Search for potential optimizations in three statements
            if (invertConditionals)
            {
                for (int i = 0; i < base.size() - 2; i++)
                {
                    final IComponent first = base.get(i), second = base.get(i + 1), third = base.get(i + 2);
                    final int index = i;

                    if (first.getFlag(TYPE).equals("break_conditional") && second.getFlag(TYPE).equals("break") && third.getFlag(TYPE).equals("label") && first.getFlag(LABEL).equals(third.getFlag(LABEL)))
                    {
                        // Original Statements:
                        // br X to A
                        // br to B
                        // label A
                        // Replace with:
                        // br not X to B
                        IComponent replacement = invertBreak(first, second.getFlag(LABEL));
                        optimizer = list -> {
                            list.set(index, replacement);
                            list.remove(index + 1);
                        };
                        break;
                    }
                }
            }

            // Repeat until no possible optimizations are found
        } while (optimizer != null);

        // Single Time Optimizations
        if (simplifyNames)
        {
            LabelMap labels = new LabelMap();
            for (IComponent cmp : base)
            {
                String label = cmp.getFlag(LABEL);
                if (!label.equals(""))
                {
                    labels.add(label);
                }
            }

            labels.build();


            for (IComponent cmp : base)
            {
                String label = cmp.getFlag(LABEL);
                if (!label.equals(""))
                {
                    cmp.setFlag(LABEL, labels.get(label));
                }
            }
        }
    }

    private static IComponent invertBreak(IComponent original, String label)
    {
        // \tb??? <spaces> rX, rY, label
        String[] parts = original.compile().replaceAll("\\s+", " ").split(" ");
        String newBreak = "";
        for (int i = 0; i < ORIGINAL.length; i++)
        {
            if (parts[1].equals(ORIGINAL[i]))
            {
                newBreak = INVERTED[i];
                break;
            }
        }
        return new ComponentLabel(IComponent.format(newBreak, parts[2] + " " + parts[3] + " %s\n"), label).setFlag(TYPE, "break_conditional");
    }
}
