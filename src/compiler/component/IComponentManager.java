package compiler.component;

import java.util.Stack;

public interface IComponentManager
{
    default void addComponent(IComponent component)
    {
        addComponent(component.getType(), component);
    }

    void addComponent(IComponent.Type type, IComponent component);

    IComponent getComponent(IComponent.Type type);

    Stack<INamedComponent> getControlStack();

    String getConstant(String name);

    void addConstant(String name, String value);
}
