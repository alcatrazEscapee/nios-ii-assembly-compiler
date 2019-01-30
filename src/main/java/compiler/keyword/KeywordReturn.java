/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword;

import compiler.component.ComponentStatic;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;

import static compiler.component.IComponent.Flag.FUNCTION_PREFIX;
import static compiler.component.IComponent.Flag.WRITE_REGISTER;

public class KeywordReturn implements IKeyword
{
    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, "return");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        StringBuilder source = Helpers.nextLine(inputBuilder);
        IComponent parent = compiler.getComponent(IComponent.Type.CURRENT);
        if (parent == null)
        {
            throw new InvalidAssemblyException("Unexpected return outside of function");
        }
        if (source.length() != 0)
        {
            String reg = source.toString();
            if (!REGISTERS.contains(reg))
            {
                throw new InvalidAssemblyException("Return can only return registers " + reg);
            }

            // Add the quick move flag
            String result = IComponent.format("mov", "r2, " + reg + "\n");
            parent.add(new ComponentStatic(result).setFlag(WRITE_REGISTER, "r2"));
        }

        // Add a default return
        String functionName = parent.getFlag(FUNCTION_PREFIX);
        String result = IComponent.format("br", functionName + "_ret\n");
        parent.add(new ComponentStatic(result).setFlag(WRITE_REGISTER, "return"));
    }
}
