/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.component;

import compiler.util.InvalidAssemblyException;

public class ComponentStatic extends AbstractComponent
{
    private final String result;
    private final Type type;

    public ComponentStatic(String result)
    {
        this(result, Type.SUB);
    }

    public ComponentStatic(String result, Type type)
    {
        this.type = type;
        this.result = result;
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
    public void add(IComponent sub)
    {
        throw new InvalidAssemblyException("Can't add a sub-statement to a static component");
    }
}
