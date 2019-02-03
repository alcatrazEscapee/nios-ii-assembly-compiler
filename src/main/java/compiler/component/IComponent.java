/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.component;

import compiler.AssemblyCompiler;

public interface IComponent
{
    static String format(String p1, String p2)
    {
        return String.format(AssemblyCompiler.FORMAT_STRING_FIELDS, p1, p2);
    }

    /**
     * The type of the component. Used for accessing from Compile#getComponent
     *
     * @return a type
     */
    Type getType();


    /**
     * This will compile this component recursively (sub-components) into linear assembly text data
     * @return a string representing the compiled data
     */
    String compile();

    /**
     * Adds a sub-component to this one. Used in functions and main where the component has many sub instructions.
     * Does nothing on other components
     *
     * @param sub the single instruction / component to add
     */
    default void add(IComponent sub) {}

    /**
     * Gets various flags for the component
     *
     * @param type An identifier for what flag to get
     * @return the flag associated to the name, or "" if not
     */
    String getFlag(Flag type);

    /**
     * Sets a flag associated to name to the value flag
     * @param type the identifier for the flag
     * @param flag the value for the flag
     * @return the component for method chaining
     */
    IComponent setFlag(Flag type, String flag);

    enum Type
    {
        COMPILE,
        MAIN,
        VARIABLE,
        FUNCTION,
        CURRENT,
        SUB,
        CONDITIONAL
    }

    enum Flag
    {
        TYPE,
        FUNCTION_NAME,
        FUNCTION_PREFIX,
        LABEL,
        WRITE_REGISTER
    }
}
