/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword;

import compiler.component.ComponentStatic;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;
import compiler.util.RegisterExpressions;

/**
 * This class is responsible for all manner of register expressions
 * Each expression must be one of the following forms:
 *
 * rX = rY              ->      mov rX, rY
 * rX = IMM             ->      movi rX, IMM
 * rX = &VAR            ->      movia rX, VAR
 * rX = (cast) VAR      ->      ld(w/b)(io/) rX, VAR(r0)
 * rX = (cast) &rY      ->      ld(w/b)(io/) rX, 0(rY)
 * rX = (cast) &rY[OFF] ->      ld(w/b)(io/) rX, OFF(rY)
 * rX = rY OP rZ        ->      OP rX, rY, rZ
 * rX = rY OP IMM       ->      OPi rX, rY, IMM
 * rX OP= rY            ->      OP rX, rX, rY
 * rX OP= IMM           ->      OPi rX, rX, IMM
 * rX UOP               ->      OPi rX, rX, 1 (for ++ / --)
 *
 * See {@link RegisterExpressions}
 */
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

        if (source.charAt(0) == '=')
        {
            source.deleteCharAt(0);
            String lhs = getArg(source, ALL);

            if (REGISTERS.contains(lhs))
            {
                // Cases: rX = rY OP rz / rX = rY OP IMM / rX = rY
                String op = getOp(source, OPERATORS);
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
                    // Case: rX = rY OP IMM
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


                // Case: rX = IMM / rX = (cast) &VAR / rX = (cast) &rY
                if (lhs.length() == 0 && source.charAt(0) == '&')
                {
                    // Remove the '&'
                    source.deleteCharAt(0);
                    String rhs = getArg(source, ALL);
                    if (REGISTERS.contains(rhs))
                    {
                        String offset = "0";

                        if (source.length() > 0 && source.charAt(0) == '[')
                        {
                            // Remove leading '['
                            source.deleteCharAt(0);
                            offset = getArg(source, "]");
                            // Remove ending ']'
                            source.deleteCharAt(0);
                        }

                        // Case rX = (cast) &rY / rX = (cast) &rY[OFF]
                        String cmd = makeLoad(byteFlag, ioFlag);
                        String result = IComponent.format(cmd, String.format("%s, %s(%s)\n", keyword, offset, rhs));
                        parent.add(new ComponentStatic(result, keyword));
                    }
                    else
                    {
                        // Case: rX = &VAR
                        String result = IComponent.format("movia", keyword + ", " + rhs + "\n");
                        parent.add(new ComponentStatic(result, keyword));
                    }
                }
                else
                {
                    try
                    {
                        // Account for constants
                        String var = compiler.getConstant(lhs);
                        if (var.equals(""))
                        {
                            var = lhs;
                        }
                        // Account for characters (i.e. 'G'), which are immediate, but don't pass Integer#parseInt
                        if (!(var.length() == 3 && var.startsWith("'") && var.endsWith("'")))
                        {
                            //noinspection ResultOfMethodCallIgnored
                            Integer.decode(var);
                        }
                        // Case rX = IMM
                        String result = IComponent.format("movi", keyword + ", " + lhs + "\n");
                        parent.add(new ComponentStatic(result, keyword));
                    }
                    catch (NumberFormatException e)
                    {
                        // It wasn't an immediate
                        if (source.length() == 0)
                        {
                            // Case: rX = (cast) VAR
                            String cmd = makeLoad(byteFlag, ioFlag);
                            String result = IComponent.format(cmd, keyword + ", " + lhs + "(r0)\n");
                            parent.add(new ComponentStatic(result, keyword));
                        }
                    }
                }
            }
        }
        else if (source.toString().equals("++") || source.toString().equals("--"))
        {
            // Case: rX UOP
            String result = RegisterExpressions.ofImm(keyword, keyword, String.valueOf(source.charAt(0)), "1");
            parent.add(new ComponentStatic(result, keyword));
        }
        else
        {
            // Cases: rX OP= imm / rX OP= rY
            String op = getOp(source, OPERATORS);
            if (source.charAt(0) != '=')
            {
                throw new InvalidAssemblyException("Unknown operator with assignment " + op + " " + source);
            }
            // Remove the '='
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
                // Case: rX OP= IMM
                String result = RegisterExpressions.ofImm(keyword, keyword, op, rhs);
                parent.add(new ComponentStatic(result, keyword));
            }
        }
    }

    private String makeLoad(boolean byteFlag, boolean ioFlag)
    {
        return "ld" + (byteFlag ? "b" : "w") + (ioFlag ? "io" : "");
    }
}
