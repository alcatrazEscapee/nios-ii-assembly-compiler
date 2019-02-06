/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.conditionals;

import java.util.List;

import compiler.component.IComponent;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;

public interface IConditional extends IComponent
{
    String[] OPERATORS = {"not", "and", "or"};

    List<IComponent> build();

    String getName();

    @Override
    default Type getType()
    {
        return Type.CONDITIONAL;
    }

    class Builder
    {
        private final String baseName;
        private int suffixCounter = 0;

        public Builder(String name)
        {
            this.baseName = name;
        }

        public IConditional build(StringBuilder source)
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
                    throw new InvalidAssemblyException("error.message.unary_not");
                }
                source.delete(0, op.length());
                return new ConditionalBinary.Not(nextName(), build(trim(source)));
            }
            else
            {
                source.delete(0, op.length());
                if (op.equals("or"))
                {
                    return new ConditionalLogical.Or(nextName(), build(trim(lhs)), build(trim(source)));
                }
                else if (op.equals("and"))
                {
                    return new ConditionalLogical.And(nextName(), build(trim(lhs)), build(trim(source)));
                }
            }
            throw new InvalidAssemblyException("error.message.invalid_conditional");
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
            return baseName + "_" + Helpers.alphabetSuffix(++suffixCounter);
        }
    }
}
