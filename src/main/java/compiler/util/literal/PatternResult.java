/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.literal;

import compiler.util.pattern.IPattern;

/**
 * This represents the result of a pattern match attempted on the input string builder
 * Each {@link IPattern} should return a unique instance which matches the pattern in question
 * The method {@link IPattern#andThen} will delegate to {@link PatternResult#andThen(PatternResult)} which allows patterns to be chained
 */
public abstract class PatternResult
{
    protected final StringBuilder input;

    public PatternResult(StringBuilder input)
    {
        this.input = input;
    }

    public final StringBuilder get()
    {
        StringBuilder token = new StringBuilder();
        while (input.length() > 0 && !isValidToken(token))
        {
            char c = input.charAt(0);
            input.deleteCharAt(0);
            if (acceptChar(token, c))
            {
                token.append(c);
            }
        }
        afterMatch(input, token);
        return token;
    }

    public final String getString()
    {
        return get().toString();
    }

    public final PatternResult andThen(PatternResult other)
    {
        return new PatternResult(input)
        {
            @Override
            public boolean acceptChar(StringBuilder token, char c)
            {
                return PatternResult.this.acceptChar(token, c) && other.acceptChar(token, c);
            }

            @Override
            public boolean isValidToken(StringBuilder token)
            {
                return PatternResult.this.isValidToken(token) && other.isValidToken(token);
            }

            @Override
            public void afterMatch(StringBuilder input, StringBuilder token)
            {
                PatternResult.this.afterMatch(input, token);
                other.afterMatch(input, token);
            }
        };
    }

    public boolean acceptChar(StringBuilder token, char c)
    {
        return true;
    }

    public boolean isValidToken(StringBuilder token)
    {
        return true;
    }

    public void afterMatch(StringBuilder input, StringBuilder token) {}

}
