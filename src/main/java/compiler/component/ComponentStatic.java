/*
 * Part of AssemblyCompiler by Alex O'Neill
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.component;

public class ComponentStatic implements IComponent
{
    private final String result;
    private final String flags;
    private final Type type;

    public ComponentStatic(String result)
    {
        this(result, "", Type.SUB);
    }

    public ComponentStatic(String result, String flags)
    {
        this(result, flags, Type.SUB);
    }

    public ComponentStatic(String result, String flags, Type type)
    {
        this.type = type;
        this.result = result;
        this.flags = flags;
    }

    @Override
    public Type getType()
    {
        return type;
    }

    @Override
    public String compile()
    {
        return result;
    }

    @Override
    public String getFlag()
    {
        return flags;
    }
}
