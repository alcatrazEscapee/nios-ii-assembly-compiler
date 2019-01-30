/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.component;

public class ComponentLabel extends AbstractComponent
{
    private final String formatString;

    public ComponentLabel(String formatString, String label)
    {
        this.formatString = formatString;
        setFlag(Flag.LABEL, label);
    }

    @Override
    public Type getType()
    {
        return Type.SUB;
    }

    @Override
    public String compile()
    {
        return String.format(formatString, getFlag(Flag.LABEL));
    }
}
