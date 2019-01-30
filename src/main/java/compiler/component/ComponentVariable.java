/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.component;

public class ComponentVariable extends AbstractComponent
{
    private final String result;
    private final boolean isWordAligned;

    public ComponentVariable(String result, boolean isWordAligned)
    {
        this.result = result;
        this.isWordAligned = isWordAligned;
    }

    public boolean isWordAligned()
    {
        return isWordAligned;
    }

    @Override
    public Type getType()
    {
        return Type.VARIABLE;
    }

    @Override
    public String compile()
    {
        return result;
    }
}
