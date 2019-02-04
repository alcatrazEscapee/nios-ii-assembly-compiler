/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.component;

import java.util.HashMap;
import java.util.Map;

import compiler.util.InvalidAssemblyException;

import static compiler.component.IComponent.Flag.TYPE;
import static compiler.component.IComponent.Flag.WRITE_REGISTER;

/**
 * This is a bunch of wrapper functions for basic component creation
 * It simplifies some component declarations that are used repeatedly
 */
public final class Components
{
    private static final Map<String, String> EXPRESSIONS = new HashMap<>();
    private static final Map<String, String> COMPARISONS = new HashMap<>();

    static
    {
        EXPRESSIONS.put("+", "add");
        EXPRESSIONS.put("-", "sub");
        EXPRESSIONS.put("*", "mul");
        EXPRESSIONS.put("/", "div");
        EXPRESSIONS.put("?/", "divu"); // Unsigned Division
        EXPRESSIONS.put("|", "or");
        EXPRESSIONS.put("&", "and");
        EXPRESSIONS.put("^", "xor");
        EXPRESSIONS.put("?&", "andh"); // High half-word operators
        EXPRESSIONS.put("?|", "orh");
        EXPRESSIONS.put("?^", "xorh");
        EXPRESSIONS.put("<<", "sll");
        EXPRESSIONS.put(">>", "sra"); // Signed (arithmetic)
        EXPRESSIONS.put("?>>", "srl"); // Unsigned (logical)
        EXPRESSIONS.put("==", "cmpeq"); // Logical Comparisons (signed values)
        EXPRESSIONS.put("!=", "cmpne");
        EXPRESSIONS.put(">=", "cmpge");
        EXPRESSIONS.put(">", "cmpgt");
        EXPRESSIONS.put("<", "cmplt");
        EXPRESSIONS.put("<=", "cmple");
        EXPRESSIONS.put("?>=", "cmpgeu"); // Unsigned >=
        EXPRESSIONS.put("?<=", "cmpleu"); // Unsigned <=
        EXPRESSIONS.put("?<", "cmpltu"); // Unsigned <
        EXPRESSIONS.put("?>", "cmpgtu"); // Unsigned >

        COMPARISONS.put("<=", "ble");
        COMPARISONS.put(">=", "bge");
        COMPARISONS.put("==", "beq");
        COMPARISONS.put("!=", "bne");
        COMPARISONS.put("<", "blt");
        COMPARISONS.put(">", "bgt");
        COMPARISONS.put("?<=", "bleu"); // Unsigned comparisons
        COMPARISONS.put("?>=", "bgeu");
        COMPARISONS.put("?<", "bltu");
        COMPARISONS.put("?>", "bgtu");
    }

    public static IComponent label(String label)
    {
        return new ComponentLabel("%s:\n", label).setFlag(TYPE, "label");
    }

    public static IComponent br(String label)
    {
        return new ComponentLabel(IComponent.format("br", "%s\n"), label).setFlag(TYPE, "break");
    }

    public static IComponent brOp(String rX, String op, String rY, String label)
    {
        if (!COMPARISONS.containsKey(op))
        {
            throw new InvalidAssemblyException("Unknown comparison " + op);
        }
        return new ComponentLabel(IComponent.format(COMPARISONS.get(op), String.format("%s, %s, %s\n", rX, rY, "%s")), label).setFlag(TYPE, "break_conditional");
    }

    public static IComponent noop()
    {
        return new ComponentStatic(IComponent.format("add", "r0, r0, r0\n"));
    }

    public static IComponent op(String rX, String rY, String op, String rZ)
    {
        if (!EXPRESSIONS.containsKey(op))
        {
            throw new InvalidAssemblyException("Unknown operation " + op);
        }
        if (op.equals("?|") || op.equals("?&") || op.equals("?^"))
        {
            throw new InvalidAssemblyException("'hi' operators can only be used with immediate values");
        }
        return new ComponentStatic(IComponent.format(EXPRESSIONS.get(op), String.format("%s, %s, %s\n", rX, rY, rZ))).setFlag(WRITE_REGISTER, rX);
    }

    public static IComponent opi(String rX, String rY, String op, String imm)
    {
        if (!EXPRESSIONS.containsKey(op))
        {
            throw new InvalidAssemblyException("Unknown operation " + op);
        }
        if (op.equals("/"))
        {
            throw new InvalidAssemblyException("Division cannot be done with immediate values");
        }
        return new ComponentStatic(IComponent.format(EXPRESSIONS.get(op) + "i", String.format("%s, %s, %s\n", rX, rY, imm))).setFlag(WRITE_REGISTER, rX);
    }

    private Components() {}
}
