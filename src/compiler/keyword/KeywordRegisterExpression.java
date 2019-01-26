package compiler.keyword;

import compiler.Compiler;
import compiler.component.ComponentStatic;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;
import compiler.util.RegisterExpressions;

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
        IComponent parent = compiler.getComponent(IComponent.Type.CURRENT);
        StringBuilder source = Helpers.nextLine(inputBuilder);
        boolean byteFlag = false, ioFlag = false;

        if (parent == null)
        {
            throw new InvalidAssemblyException("Register expression found outside function");
        }

        System.out.println("Matching a line " + source);
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
                // Casting flags
                if (lhs.length() >= 4 && lhs.startsWith("(io)"))
                {
                    ioFlag = true;
                    lhs = lhs.substring(4);
                }
                if (lhs.length() >= 6 && lhs.startsWith("(byte)"))
                {
                    byteFlag = true;
                    lhs = lhs.substring(6);
                }
                if (lhs.length() >= 8 && lhs.startsWith("(byteio)"))
                {
                    byteFlag = ioFlag = true;
                    lhs = lhs.substring(8);
                }


                // Case: rX = variable / rX = immediate / rX = &variable / rX = &rY
                if (lhs.length() == 0 && source.charAt(0) == '&')
                {
                    // Remove the '&'
                    source.deleteCharAt(0);
                    String rhs = getArg(source, ALL);
                    if (REGISTERS.contains(rhs))
                    {
                        // Case rX = &rY / rX = &rY[offset]
                        String offset = "0";

                        if (source.length() > 0 && source.charAt(0) == '[')
                        {
                            // Remove leading '['
                            source.deleteCharAt(0);
                            offset = getArg(source, "]");
                            // Remove ending ']'
                            source.deleteCharAt(0);
                        }

                        String cmd = makeLoad(byteFlag, ioFlag);
                        String result = IComponent.format(cmd, String.format("%s, %s(%s)\n", keyword, offset, rhs));
                        parent.add(new ComponentStatic(result, keyword));
                    }
                    else
                    {
                        // Case: rX = &variable
                        String result = IComponent.format("movia", keyword + ", " + rhs + "\n");
                        parent.add(new ComponentStatic(result, keyword));
                    }
                }
                else
                {
                    try
                    {
                        System.out.println("Trying to parse " + lhs);
                        // Account for constants
                        String var = Compiler.INSTANCE.getConstant(lhs);
                        if (var.equals(""))
                        {
                            var = lhs;
                        }
                        // Account for characters (i.e. 'G'), which are immeadiates, but don't pass Integer#parseInt
                        if (!(var.length() == 3 && var.startsWith("'") && var.endsWith("'")))
                        {
                            Integer.parseInt(var);
                        }
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
                            String cmd = makeLoad(byteFlag, ioFlag);
                            String result = IComponent.format(cmd, keyword + ", " + lhs + "(r0)\n");
                            parent.add(new ComponentStatic(result, keyword));
                        }
                        else
                        {
                            // Case: rX = (cast?) &r3
                        }
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
                throw new InvalidAssemblyException("Unknown operator with assignment " + op + " " + source);
            }
            else
            {
                // Cases: rX OP= imm / rX OP= rY
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

    private String makeLoad(boolean byteFlag, boolean ioFlag)
    {
        return "ld" + (byteFlag ? "b" : "w") + (ioFlag ? "io" : "");
    }
}
