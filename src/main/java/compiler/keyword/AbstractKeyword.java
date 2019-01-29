/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword;

public abstract class AbstractKeyword implements IKeyword
{
    static final String[] ALL = {"<", ">", "?", "+", "-", "*", "/", "=", "&", "|", "^", "[", "]", "!"};
    static final String[] OPERATORS = {"?>>", "?<=", "?>=", ">=", "<=", "?<", "?>", "==", "!=", "<<", ">>", "?^", "?|", "?&", "?/", ">", "<", "+", "-", "*", "/", "=", "&", "|", "^"};
    static final String[] COMPARATORS = {"?<=", "?>=", "?<", "?>", "<=", ">=", "!=", "==", "<", ">"};

    String getArg(StringBuilder source, String... delimiters)
    {
        StringBuilder arg = new StringBuilder();
        while (source.length() > 0 && validWordAhead(source, delimiters))
        {
            arg.append(source.charAt(0));
            source.deleteCharAt(0);
        }
        return arg.toString();
    }

    String getOp(StringBuilder source, String... operators)
    {
        // This needs to check longer operators first, as they might be prefixed by others (i.e. <= and <)
        for (String op : operators)
        {
            if (source.length() >= op.length() && source.substring(0, op.length()).equals(op))
            {
                source.delete(0, op.length());
                return op;
            }
        }
        return "";
    }

    private boolean validWordAhead(StringBuilder source, String... delimiters)
    {
        for (String d : delimiters)
        {
            if (source.length() >= d.length() && d.equals(source.substring(0, d.length())))
            {
                return false;
            }
        }
        return true;
    }
}
