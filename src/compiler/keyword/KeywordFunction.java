/*
 * Part of AssemblyCompiler by Alex O'Neill
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword;

import compiler.component.ComponentFunction;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;

public class KeywordFunction extends AbstractKeyword
{
    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, "function") || IKeyword.matchKeyword(keyword, inputBuilder, "void function");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        StringBuilder source = Helpers.nextLine(inputBuilder, ':', false);
        if (source.length() == 0)
        {
            throw new InvalidAssemblyException("Function name must be non empty");
        }
        compiler.addComponent(IComponent.Type.CURRENT, new ComponentFunction(source.toString(), keyword.startsWith("void")));

    }
}
