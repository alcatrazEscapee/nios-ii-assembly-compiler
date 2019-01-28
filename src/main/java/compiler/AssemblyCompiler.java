/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler;

import java.util.*;

import compiler.component.ComponentVariable;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.keyword.*;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;

public enum AssemblyCompiler implements IComponentManager
{
    INSTANCE;

    public static final String FORMAT_STRING_FIELDS = "\t%-16s%s";
    public static final int SPACES_PER_TAB = 4;

    // These are ordered by priority
    private static final List<IKeyword> KEYWORDS = Arrays.asList(new KeywordCompile(), new KeywordMain(), new KeywordIf(), new KeywordElse(), new KeywordWhile(), new KeywordEnd(), new KeywordCall(), new KeywordFunction(), new KeywordReturn(), new KeywordRegisterExpression(), new KeywordVariable(), new KeywordVariableStore(), new KeywordComment());

    private Map<String, String> declaredConstants;
    private Stack<IComponent> controlStack;
    private List<IComponent> componentsAlignedVars;
    private List<IComponent> componentsDefaultVars;
    private List<IComponent> componentsFunctions;
    private IComponent componentCompile;
    private IComponent componentMain;
    private IComponent componentCurrent;

    public String compile(String input)
    {
        this.componentsFunctions = new ArrayList<>();
        this.componentsAlignedVars = new ArrayList<>();
        this.componentsDefaultVars = new ArrayList<>();
        this.componentCompile = null;
        this.componentMain = null;
        this.controlStack = new Stack<>();
        this.declaredConstants = new HashMap<>();

        KEYWORDS.forEach(IKeyword::reset);

        List<String> inputLines = Helpers.getLinesUnformatted(input);

        for (String line : inputLines)
        {
            StringBuilder inputBuilder = new StringBuilder(line);
            StringBuilder keywordBuilder = new StringBuilder();

            while (inputBuilder.length() > 0)
            {
                // Pop a character into the keyword buffer
                keywordBuilder.append(Helpers.nextChar(inputBuilder));

                String keyword = keywordBuilder.toString();

                for (IKeyword keywordMatcher : KEYWORDS)
                {
                    if (keywordMatcher.matches(keyword, inputBuilder))
                    {
                        keywordMatcher.apply(keyword, inputBuilder, this);

                        // Reset keyword and input
                        keywordBuilder = new StringBuilder();
                        Helpers.advanceToNextWord(inputBuilder);
                    }
                }
            }
        }

        StringBuilder outputBuilder = new StringBuilder(input.length() * 2);

        outputBuilder.append("# Generated by Assembly Auto-Compiler by Alex O'Neill\n" +
                "# Setup\n");

        if (componentCompile == null) throw new InvalidAssemblyException("No compile statement found");
        outputBuilder.append(componentCompile.compile());

        if (componentMain == null) throw new InvalidAssemblyException("No main function defined");
        outputBuilder.append(componentMain.compile());

        if (!componentsFunctions.isEmpty())
        {
            for (IComponent cmp : componentsFunctions)
            {
                outputBuilder.append(cmp.compile());
            }
        }

        if (!componentsAlignedVars.isEmpty())
        {
            outputBuilder.append("\n# Word-Aligned Variables\n");
            outputBuilder.append(IComponent.format(".org", "0x00001000\n\n"));

            for (IComponent cmp : componentsAlignedVars)
            {
                outputBuilder.append(cmp.compile());
            }
        }

        if (!componentsDefaultVars.isEmpty())
        {
            outputBuilder.append("\n# Random Variables\n\n");

            for (IComponent cmp : componentsDefaultVars)
            {
                outputBuilder.append(cmp.compile());
            }
        }

        outputBuilder.append("\n# End of Assembly Source\n\t.end");
        // Replace tabs with spaces - not optional ;)
        return outputBuilder.toString().replace("\t", new String(new char[SPACES_PER_TAB]).replace('\0', ' '));
    }

    @Override
    public void addComponent(IComponent.Type type, IComponent component)
    {
        switch (type)
        {
            case COMPILE:
                if (componentCompile != null)
                    throw new InvalidAssemblyException("Multiple compile statements not allowed");
                componentCompile = component;
                break;
            case MAIN:
                if (componentMain != null) throw new InvalidAssemblyException("Multiple main statements not allowed");
                componentMain = component;
                break;
            case VARIABLE:
                if (((ComponentVariable) component).isWordAligned())
                {
                    componentsAlignedVars.add(component);
                }
                else
                {
                    componentsDefaultVars.add(component);
                }
                break;
            case FUNCTION:
                componentsFunctions.add(component);
                break;
            case CURRENT:
                this.componentCurrent = component;
                break;
            default:
                throw new InvalidAssemblyException("Unknown component type " + type);

        }
    }

    @Override
    public IComponent getComponent(IComponent.Type type)
    {
        switch (type)
        {
            case COMPILE:
                return componentCompile;
            case MAIN:
                return componentMain;
            case CURRENT:
                return componentCurrent;
            default:
                throw new InvalidAssemblyException("Unknown component type to access " + type);
        }
    }

    @Override
    public Stack<IComponent> getControlStack()
    {
        return controlStack;
    }

    @Override
    public String getConstant(String name)
    {
        return declaredConstants.getOrDefault(name, "");
    }

    @Override
    public void addConstant(String name, String value)
    {
        declaredConstants.put(name, value);
    }

}
