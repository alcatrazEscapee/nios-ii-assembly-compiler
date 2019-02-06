/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.component;

public class ComponentStatic extends AbstractComponent
{
    private final Type type;
    private String result;

    public ComponentStatic(String result)
    {
        this.type = Type.SUB;
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
}
