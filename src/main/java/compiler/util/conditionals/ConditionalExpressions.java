/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.conditionals;

import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;

public final class ConditionalExpressions
{
    public static IConditional create(String name, StringBuilder source)
    {
        Solver wrapper = new ConditionalExpressions.Solver(name);
        return wrapper.parse(source);
    }

    private static class Solver
    {
        private static final String[] OPERATORS = {"not", "and", "or"};
        private int suffixCounter = 0;
        private String name;

        Solver(String name)
        {
            this.name = name;
        }

        IConditional parse(StringBuilder source)
        {
            StringBuilder lhs = new StringBuilder();
            String op = null;
            int bracketDepth = 0;
            while (source.length() > 0 && (bracketDepth > 0 || (op = logicalOperator(source)) == null))
            {
                char c = source.charAt(0);
                if (c == '(')
                {
                    bracketDepth++;
                }
                else if (c == ')')
                {
                    bracketDepth--;
                }
                lhs.append(c);
                source.deleteCharAt(0);
            }

            if (op == null)
            {
                return new ConditionalBase(nextName(), trim(lhs));
            }
            else if (op.equals("not"))
            {
                if (lhs.length() > 0)
                {
                    throw new InvalidAssemblyException("Can't use NOT as a logical operator.");
                }
                source.delete(0, op.length());
                return new ConditionalBinary.Not(nextName(), parse(trim(source)));
            }
            else
            {
                source.delete(0, op.length());
                if (op.equals("or"))
                {
                    return new ConditionalLogical.Or(nextName(), parse(trim(lhs)), parse(trim(source)), op);
                }
                else if (op.equals("and"))
                {
                    return new ConditionalLogical.And(nextName(), parse(trim(lhs)), parse(trim(source)), op);
                }
            }
            throw new InvalidAssemblyException("Invalid Conditional expression: parsing reached the end without terminating");
        }

        private String logicalOperator(StringBuilder source)
        {
            for (String op : OPERATORS)
            {
                if (source.length() >= op.length() && source.substring(0, op.length()).equals(op))
                {
                    return op;
                }
            }
            return null;
        }

        private StringBuilder trim(StringBuilder source)
        {
            while (source.charAt(0) == '(' && source.charAt(source.length() - 1) == ')')
            {
                source.deleteCharAt(source.length() - 1);
                source.deleteCharAt(0);
            }
            return source;
        }

        private String nextName()
        {
            return name + "_" + Helpers.alphabetSuffix(++suffixCounter);
        }
    }
}
