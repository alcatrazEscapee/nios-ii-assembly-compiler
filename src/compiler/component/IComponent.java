package compiler.component;

import compiler.Compiler;

public interface IComponent
{
    static String format(String p1, String p2)
    {
        return String.format(Compiler.FORMAT_STRING_FIELDS, p1, p2);
    }

    Type getType();

    String compile();

    /**
     * Adds a sub-component to this one. Used in functions and main where the component has many sub instructions.
     * Does nothing on other components
     *
     * @param sub the single instruction / component to add
     */
    default void add(IComponent sub) {}

    /**
     * Gets the name of the register that this component writes to, if any.
     * Used to determine how many registers to save in a function header
     *
     * @return the register if it writes to it, else none
     */
    default String getWriteRegister()
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
