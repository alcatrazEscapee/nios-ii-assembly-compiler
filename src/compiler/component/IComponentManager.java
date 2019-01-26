package compiler.component;

import java.util.Stack;

public interface IComponentManager
{
    default void addComponent(IComponent component)
    {
        addComponent(component.getType(), component);
    }

    void addComponent(String name, IComponent component);

    IComponent getComponent(String name);

    Stack<IComponent> getControlStack();
}
