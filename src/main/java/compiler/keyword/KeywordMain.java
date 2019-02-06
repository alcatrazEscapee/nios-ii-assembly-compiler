/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword;

import compiler.AssemblyCompiler;
import compiler.component.ComponentMain;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.util.Helpers;

public class KeywordMain implements IKeyword
{
    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, "main");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        Helpers.advanceToNextWord(inputBuilder);
        if (inputBuilder.length() > 0 && inputBuilder.charAt(0) == ':')
        {
            inputBuilder.deleteCharAt(0);
        }
        else
        {
            AssemblyCompiler.INSTANCE.warn("error.message.expected_colon_main");
        }

        compiler.addComponent(IComponent.Type.CURRENT, new ComponentMain());
    }
}
