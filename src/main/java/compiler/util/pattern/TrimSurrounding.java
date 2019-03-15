/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.pattern;

import compiler.util.literal.PatternResult;

public class TrimSurrounding implements IPattern
{
    private final char target;

    TrimSurrounding(char target)
    {
        this.target = target;
    }

    @Override
    public PatternResult apply(StringBuilder input)
    {
        return new TrimSurroundingResult(input);
    }

    class TrimSurroundingResult extends PatternResult
    {
        TrimSurroundingResult(StringBuilder input)
        {
            super(input);
        }

        @Override
        public void afterMatch(StringBuilder input, StringBuilder token)
        {
            if (token.length() > 1 && token.charAt(0) == target && token.charAt(token.length() - 1) == target)
            {
                token.deleteCharAt(token.length() - 1);
                token.deleteCharAt(0);
            }
        }
    }
}
