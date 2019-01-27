package compiler.util;

import java.util.*;

import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.component.INamedComponent;

public class IComponentManagerStub implements IComponent, IComponentManager
{
    private final List<IComponent> components = new ArrayList<>();
    private final Stack<INamedComponent> controlStack = new Stack<>();
    private final Map<String, String> constants = new HashMap<>();

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
    public Stack<INamedComponent> getControlStack()
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
