/*
 * Part of AssemblyCompiler by Alex O'Neill
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword;

import compiler.component.ComponentStatic;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.util.Helpers;

public class KeywordCall extends AbstractKeyword
{
    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, "call");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        StringBuilder source = Helpers.nextLine(inputBuilder);
        IComponent parent = compiler.getComponent(IComponent.Type.CURRENT);
        // This needs to signal that it writes to the return address register
        parent.add(new ComponentStatic(IComponent.format("call", source + "\n"), "ra"));
    }
}
