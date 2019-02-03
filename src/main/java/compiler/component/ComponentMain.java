/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.component;

import compiler.util.Optimizer;

public class ComponentMain extends AbstractComponent
{
    public ComponentMain()
    {
        setFlag(Flag.FUNCTION_NAME, "main");
        setFlag(Flag.FUNCTION_PREFIX, "main");
    }

    @Override
    public Type getType()
    {
        return Type.MAIN;
    }

    @Override
    public String compile()
    {
        // Optimizer!
        Optimizer.accept(components);

        StringBuilder output = new StringBuilder();
        output.append("# Entry point\n").append("_start:\n").append(IComponent.format("movia", "sp, LAST_RAM_WORD\n"));
        for (IComponent cmp : components)
        {
            output.append(cmp.compile());
        }
        output.append("_end:\n").append(IComponent.format("br", "_end\n"));
        return output.toString();
    }
}
