/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword;

import compiler.component.ComponentCompile;
import compiler.component.IComponentManager;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;

public class KeywordCompile implements IKeyword
{
    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, "compile");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        Helpers.advanceToNextWord(inputBuilder);
        // Match the compile flag
        if (inputBuilder.substring(0, 11).equals("nios-ii de0"))
        {
            inputBuilder.delete(0, 11);
            compiler.addComponent(new ComponentCompile());
        }
        else
        {
            throw new InvalidAssemblyException("error.message.invalid_compile");
        }
    }
}
