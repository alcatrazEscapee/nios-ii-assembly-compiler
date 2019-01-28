/*
 * Part of AssemblyCompiler by Alex O'Neill
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.component;

public class ComponentMain extends AbstractComponent
{
    @Override
    public Type getType()
    {
        return Type.MAIN;
    }

    @Override
    public String compile()
    {
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
