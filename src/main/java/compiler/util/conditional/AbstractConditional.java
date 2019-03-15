/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.conditional;

import java.util.EnumMap;
import java.util.Map;

import compiler.component.IComponent;
import compiler.util.Helpers;

abstract class AbstractConditional implements IConditional
{
    private final Map<Flag, String> flags;

    AbstractConditional()
    {
        this.flags = new EnumMap<>(Flag.class);
    }

    @Override
    public String compile()
    {
        return Helpers.reduceCollection(build(), IComponent::compile);
    }

    @Override
    public String getFlag(Flag type)
    {
        return flags.getOrDefault(type, "");
    }

    @Override
    public IComponent setFlag(Flag type, String flag)
    {
        flags.put(type, flag);
        return this;
    }
}
