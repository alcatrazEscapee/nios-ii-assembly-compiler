/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword;

import java.util.Stack;

import compiler.AssemblyCompiler;
import compiler.component.Components;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.util.InvalidAssemblyException;

import static compiler.component.IComponent.Flag.LABEL;

public class KeywordElse implements IKeyword
{
    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, "else");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        // Optional colon
        if (inputBuilder.length() > 0 && inputBuilder.charAt(0) == ':')
        {
            AssemblyCompiler.INSTANCE.warn("error.message.extra_colon");
            inputBuilder.deleteCharAt(0);
        }

        IComponent parent = compiler.getComponent(IComponent.Type.CURRENT);
        Stack<IComponent> controlStack = compiler.getControlStack();
        if (parent == null)
        {
            throw new InvalidAssemblyException("error.message.extra_keyword", "else");
        }
        if (controlStack.isEmpty() || !controlStack.peek().getFlag(LABEL).contains("_if"))
        {
            throw new InvalidAssemblyException("error.message.unexpected_control_stack", "_if", controlStack.peek().getFlag(LABEL));
        }
        IComponent componentIf = controlStack.pop();
        String labelIf = componentIf.getFlag(LABEL);
        String labelElse = labelIf.replaceAll("_if", "_else");

        controlStack.add(Components.label(labelElse));
        parent.add(Components.br(labelElse));
        parent.add(componentIf);
    }
}
