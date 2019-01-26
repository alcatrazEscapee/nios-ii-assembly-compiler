package compiler.keyword;

import compiler.component.ComponentStatic;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.keyword.regex.RegisterExpressions;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;

public class KeywordRegisterExpression extends AbstractKeyword
{
    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        for (String reg : REGISTERS)
        {
            if (IKeyword.matchKeyword(keyword, inputBuilder, reg))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        IComponent parent = compiler.getComponent("current");
        StringBuilder source = Helpers.nextLine(inputBuilder);

        if (parent == null)
        {
            throw new InvalidAssemblyException("Register expression found outside function");
        }

        System.out.println("Matching a line + " + source);
        if (source.charAt(0) == '=')
        {
            source.deleteCharAt(0);
            String lhs = getArg(source, ALL);

            if (REGISTERS.contains(lhs))
            {
                // Cases: rX = rY OP rz / rX = rY OP immediate / rX = rY
                String op = getOp(source);
                String rhs = getArg(source, ALL);

                if (op.equals(""))
                {
                    // Case: rX = rY

                    String result = IComponent.format("mov", keyword + ", " + lhs + "\n");
                    parent.add(new ComponentStatic(result, keyword));
                }
                else if (REGISTERS.contains(rhs))
                {
                    // Case: rX = rY OP rZ
                    String result = RegisterExpressions.of(keyword, lhs, op, rhs);
                    parent.add(new ComponentStatic(result, keyword));
                }
                else
                {
                    // Case: rX = rY OP immediate
                    String result = RegisterExpressions.ofImm(keyword, lhs, op, rhs);
                    parent.add(new ComponentStatic(result, keyword));
                }
            }
            else
            {
                // Case: rX = variable / rX = variable[offset register] / rX = immediate
                try
                {
                    System.out.println("Trying to parse " + lhs);
                    int x = Integer.parseInt(lhs);
                    // Case rX = immediate
                    String result = IComponent.format("movi", keyword + ", " + lhs + "\n");
                    parent.add(new ComponentStatic(result, keyword));
                }
                catch (NumberFormatException e)
                {
                    System.out.println("Caught, " + e);
                    // It wasn't an immediate
                    if (source.length() == 0)
                    {
                        // Case: rX = variable
                        String result = IComponent.format("ldw", keyword + ", " + lhs + "(r0)\n");
                        parent.add(new ComponentStatic(result, keyword));
                    }
                }
            }
        }
        else
        {
            // Cases: rX OP= imm / rX OP= rY
            String op = getOp(source);
            if (source.charAt(0) != '=')
            {
                throw new InvalidAssemblyException("Unknown operator with assignment " + op + source);
            }
            source.deleteCharAt(0);
            String rhs = getArg(source, ALL);
            if (REGISTERS.contains(rhs))
            {
                // Case: rX OP= rY
                String result = RegisterExpressions.of(keyword, keyword, op, rhs);
                parent.add(new ComponentStatic(result, keyword));
            }
            else
            {
                // Case: rX OP= immediate
                String result = RegisterExpressions.ofImm(keyword, keyword, op, rhs);
                parent.add(new ComponentStatic(result, keyword));
            }
        }
    }
}
