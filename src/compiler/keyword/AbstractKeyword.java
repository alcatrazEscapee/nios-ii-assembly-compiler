package compiler.keyword;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractKeyword implements IKeyword
{
    static final String[] ALL = {"<<", ">>", "?^", "?|", "?&", "?/", "+", "-", "*", "/", "=", "&", "|", "^", "[", "]"};
    static final String[] OPERATORS = {"<<", ">>", "?^", "?|", "?&", "?/", "+", "-", "*", "/", "=", "&", "|", "^"};
    static final String[] COMPARATORS = {"<=", ">=", "!=", "==", "<", ">"};
    static final Set<String> REGISTERS = new HashSet<>(Arrays.asList("r0", "r1", "r2", "r3", "r4", "r5", "r6", "r7", "r8", "r9", "r10", "r11", "r12", "r13", "r14", "r15", "r16", "r17", "r18", "r19", "r20", "r21", "r22", "r23", "et", "bt", "gp", "sp", "fp", "ea", "sstatus", "ra", "status", "estatus", "bstatus", "ienable", "ipending"));

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

    String getOp(StringBuilder source)
    {
        return getOp(source, OPERATORS);
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
