/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util;

import java.util.HashMap;
import java.util.Map;

import compiler.component.IComponent;

public final class RegisterExpressions
{
    private static final Map<String, String> EXPRESSIONS = new HashMap<>();
    private static final Map<String, String> COMPARISONS = new HashMap<>();
    private static final Map<String, String> COMPARISONS_INVERTED = new HashMap<>();

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

        COMPARISONS_INVERTED.put("<=", "bgt");
        COMPARISONS_INVERTED.put(">=", "blt");
        COMPARISONS_INVERTED.put("==", "bne");
        COMPARISONS_INVERTED.put("!=", "beq");
        COMPARISONS_INVERTED.put("<", "bge");
        COMPARISONS_INVERTED.put(">", "ble");
        COMPARISONS_INVERTED.put("?<=", "bgtu"); // Unsigned comparisons
        COMPARISONS_INVERTED.put("?>=", "bltu");
        COMPARISONS_INVERTED.put("?<", "bgeu");
        COMPARISONS_INVERTED.put("?>", "bleu");
    }

    public static String of(String rX, String rY, String op, String rZ)
    {
        if (!EXPRESSIONS.containsKey(op))
        {
            throw new InvalidAssemblyException("Unknown operation " + op);
        }
        if (op.equals("?|") || op.equals("?&") || op.equals("?^"))
        {
            throw new InvalidAssemblyException("'hi' operators can only be used with immediate values");
        }
        return IComponent.format(EXPRESSIONS.get(op), String.format("%s, %s, %s\n", rX, rY, rZ));
    }

    public static String ofImm(String rX, String rY, String op, String imm)
    {
        if (op.equals("/"))
        {
            throw new InvalidAssemblyException("Division cannot be done with immediate values");
        }
        return IComponent.format(EXPRESSIONS.get(op) + "i", String.format("%s, %s, %s\n", rX, rY, imm));
    }

    public static String ofComp(String rX, String rY, String op, String label)
    {
        if (!COMPARISONS.containsKey(op))
        {
            throw new InvalidAssemblyException("Unknown comparison " + op);
        }
        return IComponent.format(COMPARISONS.get(op), String.format("%s, %s, %s\n", rX, rY, label));
    }
}
