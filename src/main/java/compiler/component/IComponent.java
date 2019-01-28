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
     * Gets a flag for the component
     * Based on usage, various methods will try and parse these flags.
     *  - if passed to a function, these will be used to determine the write register of the instruction
     *  - if passed to the control stack, these will be used as the instruction label (for if-else linking)
     *  - if taken from a function, this will be the name of the function
     * @return the flag for this component type
     */
    default String getFlag()
    {
        return "";
    }

    enum Type
    {
        COMPILE,
        MAIN,
        VARIABLE,
        FUNCTION,
        CURRENT,
        SUB
    }
}
