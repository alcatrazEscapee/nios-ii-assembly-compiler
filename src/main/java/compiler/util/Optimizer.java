/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util;

import java.util.List;
import java.util.function.Consumer;

import compiler.component.IComponent;

import static compiler.component.IComponent.Flag.LABEL;
import static compiler.component.IComponent.Flag.TYPE;

public final class Optimizer
{
    public static void accept(List<IComponent> base)
    {
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

            // Repeat until no possible optimizations are found
        } while (optimizer != null);
    }
}
