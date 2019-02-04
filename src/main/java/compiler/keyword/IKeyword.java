/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import compiler.component.IComponentManager;

public interface IKeyword
{
    Set<String> REGISTERS = new HashSet<>(Arrays.asList("r0", "r1", "r2", "r3", "r4", "r5", "r6", "r7", "r8", "r9", "r10", "r11", "r12", "r13", "r14", "r15", "r16", "r17", "r18", "r19", "r20", "r21", "r22", "r23", "et", "bt", "gp", "sp", "fp", "ea", "sstatus", "ra", "status", "estatus", "bstatus", "ienable", "ipending"));
    char[] DELIMITERS = {'<', '>', '?', '+', '-', '*', '/', '=', '&', '|', '^', '[', ']', '!'};
    String[] OPERATORS = {"?>>", "?<=", "?>=", ">=", "<=", "?<", "?>", "==", "!=", "<<", ">>", "?^", "?|", "?&", "?/", ">", "<", "+", "-", "*", "/", "=", "&", "|", "^"};
    String[] COMPARATORS = {"?<=", "?>=", "?<", "?>", "<=", ">=", "!=", "==", "<", ">"};

    static boolean matchKeyword(String keyword, StringBuilder inputBuilder, String keywordMatcher)
    {
        if (keyword.equals(keywordMatcher))
        {
            if (inputBuilder.length() == 0)
            {
                return true;
            }
            char c = inputBuilder.charAt(0);
            return c != '_' && !Character.isLetterOrDigit(c);
        }
        return false;
    }

    boolean matches(String keyword, StringBuilder inputBuilder);

    void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler);

    default void reset() {}
}
