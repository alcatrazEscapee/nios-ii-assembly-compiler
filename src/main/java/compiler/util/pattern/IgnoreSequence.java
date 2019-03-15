/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.pattern;

import compiler.util.literal.PatternResult;

public class IgnoreSequence implements IPattern
{
    private final boolean useEscapes;
    private final char delimChar, escapeChar;

    IgnoreSequence(boolean useEscapes, char delimChar, char escapeChar)
    {
        this.useEscapes = useEscapes;
        this.delimChar = delimChar;
        this.escapeChar = escapeChar;
    }

    @Override
    public PatternResult apply(StringBuilder input)
    {
        return new IgnoreSequenceResult(input);
    }

    class IgnoreSequenceResult extends PatternResult
    {
        private boolean inString, inEscape;

        IgnoreSequenceResult(StringBuilder input)
        {
            super(input);
        }

        @Override
        public boolean acceptChar(StringBuilder token, char c)
        {
            if (inEscape)
            {
                inEscape = false;
            }
            else
            {
                if (useEscapes && c == escapeChar)
                {
                    inEscape = true;
                }
                else if (c == delimChar)
                {
                    inString = !inString;
                }
            }
            return true;
        }

        @Override
        public boolean isValidToken(StringBuilder token)
        {
            return !inString;
        }
    }
}
