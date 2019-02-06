/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler;

import java.util.*;

import compiler.component.*;
import compiler.keyword.*;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;

public enum AssemblyCompiler implements IComponentManager
{
    INSTANCE;

    // These are ordered by priority
    private static final List<IKeyword> KEYWORDS = Arrays.asList(new KeywordCompile(), new KeywordMain(), new KeywordIf(), new KeywordElse(), new KeywordWhile(), new KeywordEnd(), new KeywordCall(), new KeywordFunction(), new KeywordReturn(), new KeywordRegisterExpression(), new KeywordVariable(), new KeywordVariableStore(), new KeywordComment());

    private final Map<String, String> declaredConstants = new HashMap<>();
    private final Stack<IComponent> controlStack = new Stack<>();
    private final List<IComponent> componentsAlignedVars = new ArrayList<>();
    private final List<IComponent> componentsDefaultVars = new ArrayList<>();
    private final List<IComponent> componentsFunctions = new ArrayList<>();
    private IComponent componentCompile;
    private IComponent componentMain;
    private IComponent componentCurrent;
    private String currentLine;
    private int currentLineNumber;

    public String compile(String input)
    {
        reset();
        try
        {
            List<String> inputLines = Helpers.getLinesUnformatted(input);
            inputLines.forEach(this::compileLine);
        }
        catch (InvalidAssemblyException e)
        {
            fatal(e);
            throw e;
        }
        finally
        {
            // Errors at this point have no line associated to them
            currentLineNumber = -1;
            currentLine = "n/a";
        }
        return buildAssembly();
    }

    @Override
    public void addComponent(IComponent.Type type, IComponent component)
    {
        switch (type)
        {
            case COMPILE:
                if (componentCompile != null)
                    throw new InvalidAssemblyException("error.message.duplicate_compile");
                componentCompile = component;
                break;
            case MAIN:
                if (componentMain != null) throw new InvalidAssemblyException("error.message.duplicate_main");
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
                return null;
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

    public String getLineNumber()
    {
        return currentLineNumber == -1 ? "?" : String.valueOf(currentLineNumber);
    }

    public String getLine()
    {
        return currentLine;
    }

    public void fatal(InvalidAssemblyException e)
    {
        AssemblyInterface.getLog().log("error.level.fatal", e.getMessage(), AssemblyCompiler.INSTANCE.getLineNumber(), AssemblyCompiler.INSTANCE.getLine());
    }

    public void error(String message, Object... args)
    {
        AssemblyInterface.getLog().log("error.level.error", AssemblyInterface.getLog().format(message, args), AssemblyCompiler.INSTANCE.getLineNumber(), AssemblyCompiler.INSTANCE.getLine());
    }

    public void warn(String message, Object... args)
    {
        AssemblyInterface.getLog().log("error.level.warn", AssemblyInterface.getLog().format(message, args), AssemblyCompiler.INSTANCE.getLineNumber(), AssemblyCompiler.INSTANCE.getLine());
    }

    private void reset()
    {
        this.controlStack.clear();
        this.declaredConstants.clear();
        this.componentsFunctions.clear();
        this.componentsAlignedVars.clear();
        this.componentsDefaultVars.clear();
        this.componentCompile = null;
        this.componentMain = null;
        this.currentLine = "";
        this.currentLineNumber = 0;

        KEYWORDS.forEach(IKeyword::reset);
    }

    private void compileLine(String line)
    {
        StringBuilder inputBuilder = new StringBuilder(line);
        StringBuilder keywordBuilder = new StringBuilder();

        this.currentLine = line;
        this.currentLineNumber++;

        while (inputBuilder.length() > 0)
        {
            // Pop a character into the keyword buffer
            keywordBuilder.append(Helpers.nextChar(inputBuilder));

            String keyword = keywordBuilder.toString();

            if (keyword.equals(";"))
            {
                warn("error.message.extra_semicolon");
                keywordBuilder.deleteCharAt(0);
                continue;
            }

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

        if (keywordBuilder.length() > 0)
        {
            if (keywordBuilder.length() == 1 && keywordBuilder.charAt(0) == ';')
            {
                warn("error.message.extra_semicolon");
            }
            else
            {
                error("error.message.extra_keyword", keywordBuilder);
            }
        }
    }

    private String buildAssembly()
    {
        StringBuilder outputBuilder = new StringBuilder();

        outputBuilder.append("# Generated by Assembly Auto-Compiler by Alex O'Neill\n" +
                "# Setup\n");

        // Safety checks - make sure current, compile, and main are null, and not-null respectively
        if (componentCurrent != null)
        {
            error("error.message.missing_end");
            // KeywordEnd index = 5
            KEYWORDS.get(5).apply("end", null, this);
        }

        if (componentCompile == null)
        {
            error("error.message.missing_compile_assumption");
            componentCompile = new ComponentCompile();
        }
        outputBuilder.append(componentCompile.compile());

        if (componentMain == null)
        {
            error("error.message.missing_main");
            componentMain = new ComponentMain();
        }
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
        return outputBuilder.toString().replace("\t", "    ");
    }
}
