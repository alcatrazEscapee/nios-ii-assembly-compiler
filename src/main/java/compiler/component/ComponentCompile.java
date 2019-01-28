/*
 * Part of AssemblyCompiler by Alex O'Neill
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.component;

public class ComponentCompile extends AbstractComponent
{
    @Override
    public Type getType()
    {
        return Type.COMPILE;
    }

    @Override
    public String compile()
    {
        StringBuilder output = new StringBuilder();

        output.append(IComponent.format(".equ", "LAST_RAM_WORD, 0x007FFFFC\n"));

        for (IComponent cmp : components)
        {
            output.append(cmp.compile());
        }

        output.append(IComponent.format(".global", "_start\n"));
        output.append(IComponent.format(".org", "0x00000000\n"));
        output.append("\t.text\n\n");
        return output.toString();
    }
}
