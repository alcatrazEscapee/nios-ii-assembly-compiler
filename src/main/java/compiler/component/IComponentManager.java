/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.component;

import java.util.Stack;

public interface IComponentManager
{
    default void addComponent(IComponent component)
    {
        addComponent(component.getType(), component);
    }

    void addComponent(IComponent.Type type, IComponent component);

    /**
     * Gets the component of a certain type.
     *
     * @param type One of CURRENT, MAIN, or COMPILE
     * @return the component for the type specified. Null if it doesn't exist or is an invalid type
     */
    IComponent getComponent(IComponent.Type type);

    Stack<IComponent> getControlStack();

    String getConstant(String name);

    void addConstant(String name, String value);
}
