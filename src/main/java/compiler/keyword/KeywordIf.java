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
import compiler.util.Helpers;
import compiler.util.conditional.IConditional;

import static compiler.component.IComponent.Flag.FUNCTION_PREFIX;

public class KeywordIf implements IKeyword
{
    private final Map<String, Integer> counter = new HashMap<>();

    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, "if");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        StringBuilder source = Helpers.nextLine(inputBuilder, ':', false);
        IComponent parent = compiler.getComponent(IComponent.Type.CURRENT);
        Stack<IComponent> controlStack = compiler.getControlStack();

        Helpers.requireNonNull(parent, "error.message.extra_keyword", "if");

        // get the counter for this function
        String functionName = parent.getFlag(FUNCTION_PREFIX);
        int value = counter.getOrDefault(functionName, 1);

        String label = functionName + "_if" + value;
        IConditional condition = new IConditional.Builder(label).build(source);
        condition.build().forEach(parent::add);
        // Label for the true section
        parent.add(Components.label(label + "_a_t"));
        // Stack label for the false section
        controlStack.add(Components.label(label + "_a_f"));

        // Increment the counter in the map
        counter.put(functionName, value + 1);
    }

    @Override
    public void reset()
    {
        this.counter.clear();
    }
}
