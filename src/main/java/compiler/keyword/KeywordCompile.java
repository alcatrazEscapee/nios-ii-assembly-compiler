/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword;

import compiler.component.ComponentCompile;
import compiler.component.IComponentManager;
import compiler.util.InvalidAssemblyException;
import compiler.util.pattern.Patterns;

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
        String compileFlag = Patterns.END_OF_LINE.andThen(Patterns.TRIM_SPACE_ALL).apply(inputBuilder).getString();
        if (compileFlag.equals("nios-iide0"))
        {
            compiler.addComponent(new ComponentCompile());
        }
        else
        {
            throw new InvalidAssemblyException("error.message.invalid_compile");
        }
    }
}
