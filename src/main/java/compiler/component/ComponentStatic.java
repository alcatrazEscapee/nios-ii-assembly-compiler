/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.component;

import compiler.util.InvalidAssemblyException;

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

    @Override
    public void add(IComponent sub)
    {
        throw new InvalidAssemblyException("Can't add a sub-statement to a static component");
    }

    @Override
    public IComponent setFlag(Flag type, String flag)
    {
        // Update label
        if (type == Flag.LABEL)
        {
            String oldLabel = getFlag(Flag.LABEL);
            if (!oldLabel.equals(""))
            {
                result = result.replace(getFlag(Flag.LABEL), flag);
            }
        }
        return super.setFlag(type, flag);
    }
}
