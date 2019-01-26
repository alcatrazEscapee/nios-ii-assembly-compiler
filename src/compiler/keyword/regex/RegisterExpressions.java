package compiler.keyword.regex;

import java.util.HashMap;
import java.util.Map;

import compiler.component.IComponent;
import compiler.util.InvalidAssemblyException;

public class RegisterExpressions
{
    private static final Map<String, String> EXPRESSIONS = new HashMap<>();

    static
    {
        EXPRESSIONS.put("+", "add");
        EXPRESSIONS.put("-", "sub");
        EXPRESSIONS.put("*", "mul");
        EXPRESSIONS.put("/", "div");
        EXPRESSIONS.put("|", "or");
        EXPRESSIONS.put("&", "and");
    }

    public static String of(String rX, String rY, String op, String rZ)
    {
        if (!EXPRESSIONS.containsKey(op))
        {
            throw new InvalidAssemblyException("Unknown operation " + op);
        }
        return IComponent.format(EXPRESSIONS.get(op), String.format("%s, %s, %s\n", rX, rY, rZ));
    }

    public static String ofImm(String rX, String rY, String op, String imm)
    {
        if (op.equals("/"))
        {
            throw new InvalidAssemblyException("Division cannot be done with immeadiate values");
        }
        return IComponent.format(EXPRESSIONS.get(op) + "i", String.format("%s, %s, %s\n", rX, rY, imm));
    }
}
