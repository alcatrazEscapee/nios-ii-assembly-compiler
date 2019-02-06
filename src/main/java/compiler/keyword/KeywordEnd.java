/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword;

import java.util.Stack;

import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.util.InvalidAssemblyException;
import compiler.util.conditionals.IConditional;

public class KeywordEnd implements IKeyword
{
    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, "end");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        IComponent parent = compiler.getComponent(IComponent.Type.CURRENT);
        if (parent == null)
        {
            throw new InvalidAssemblyException("error.message.extra_keyword", "end");
        }

        Stack<IComponent> controlStack = compiler.getControlStack();
        if (!controlStack.isEmpty())
        {
            IComponent cmp = controlStack.pop();
            if (cmp instanceof IConditional)
            {
                ((IConditional) cmp).build().forEach(parent::add);
            }
            else
            {
                parent.add(cmp);
            }
        }
        else
        {
            compiler.addComponent(parent);
            compiler.addComponent(IComponent.Type.CURRENT, null);
        }
    }
}
