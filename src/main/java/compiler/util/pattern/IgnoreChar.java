/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.pattern;

import compiler.util.literal.PatternResult;

public class IgnoreChar implements IPattern
{
    private final char charMatch;
    private final int posMatch;

    IgnoreChar(char charMatch, int posMatch)
    {
        this.charMatch = charMatch;
        this.posMatch = posMatch;
    }

    @Override
    public PatternResult apply(StringBuilder input)
    {
        return new IgnoreCharResult(input);
    }

    class IgnoreCharResult extends PatternResult
    {
        IgnoreCharResult(StringBuilder input)
        {
            super(input);
        }

        @Override
        public boolean isValidToken(StringBuilder token)
        {
            return token.length() != posMatch + 1 || token.charAt(posMatch) != charMatch;
        }
    }
}
