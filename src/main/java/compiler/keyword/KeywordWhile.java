/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import compiler.component.Components;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.util.InvalidAssemblyException;
import compiler.util.conditional.IConditional;
import compiler.util.pattern.Patterns;

import static compiler.component.IComponent.Flag.FUNCTION_PREFIX;

public class KeywordWhile implements IKeyword
{
    private final Map<String, Integer> counter = new HashMap<>();

    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, "while");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        StringBuilder source = Patterns.END_COLON.andThen(Patterns.TRIM_SPACE_ALL).apply(inputBuilder).get();
        IComponent parent = compiler.getComponent(IComponent.Type.CURRENT);
        Stack<IComponent> controlStack = compiler.getControlStack();

        if (parent == null)
        {
            throw new InvalidAssemblyException("error.message.extra_keyword", "while");
        }

        if (source.toString().equals("true"))
        {
            // get the counter for this function
            String functionName = parent.getFlag(FUNCTION_PREFIX);
            int value = counter.getOrDefault(functionName, 1);

            String label = functionName + "_while" + value + "_a_t";
            parent.add(Components.label(label));
            controlStack.add(Components.br(label));

            // Increment the counter in the map
            counter.put(functionName, value + 1);
            return;
        }

        // This is almost identical to the if statement logic, except the component placement is reversed (label first, break after)
        // get the counter for this function
        String functionName = parent.getFlag(FUNCTION_PREFIX);
        int value = counter.getOrDefault(functionName, 1);

        String label = functionName + "_while" + value;
        IConditional condition = new IConditional.Builder(label).build(source);
        parent.add(Components.label(label + "_a_t"));
        controlStack.add(condition);

        // Increment the counter in the map
        counter.put(functionName, value + 1);
    }

    @Override
    public void reset()
    {
        this.counter.clear();
    }
}
