/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.component;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractComponent implements IComponent
{
    final List<IComponent> components;
    private final Map<Flag, String> flags;

    AbstractComponent()
    {
        this.components = new ArrayList<>();
        this.flags = new EnumMap<>(Flag.class);
    }

    @Override
    public void add(IComponent sub)
    {
        components.add(sub);
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
