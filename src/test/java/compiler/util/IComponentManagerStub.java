/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util;

import java.util.*;

import compiler.component.IComponent;
import compiler.component.IComponentManager;

public class IComponentManagerStub implements IComponent, IComponentManager
{
    private final List<IComponent> components = new ArrayList<>();
    private final Stack<IComponent> controlStack = new Stack<>();
    private final Map<String, String> constants = new HashMap<>();
    private final Map<Flag, String> flags = new EnumMap<>(Flag.class);

    @Override
    public Type getType()
    {
        return null;
    }

    @Override
    public String compile()
    {
        return components.stream().map(IComponent::compile).reduce((x, y) -> x + y).orElse("").replaceAll("\t", "    ");
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

    @Override
    public void addComponent(Type type, IComponent component)
    {
        components.add(component);
    }

    @Override
    public IComponent getComponent(Type type)
    {
        return this;
    }

    @Override
    public Stack<IComponent> getControlStack()
    {
        return controlStack;
    }

    @Override
    public String getConstant(String name)
    {
        return constants.getOrDefault(name, "");
    }

    @Override
    public void addConstant(String name, String value)
    {
        constants.put(name, value);
    }
}
