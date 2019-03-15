/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.pattern;

import java.util.Arrays;
import java.util.Collection;

import compiler.util.literal.PatternResult;

public class MatchExact implements IPattern
{
    private final Collection<String> tokens;

    MatchExact(String... tokens)
    {
        this(Arrays.asList(tokens));
    }

    MatchExact(Collection<String> tokens)
    {
        this.tokens = tokens;
    }

    @Override
    public PatternResult apply(StringBuilder input)
    {
        return new MatchExactResult(input);
    }

    class MatchExactResult extends PatternResult
    {
        MatchExactResult(StringBuilder input)
        {
            super(input);
        }

        @Override
        public boolean isValidToken(StringBuilder token)
        {
            return getMatch(token.toString()) != null;
        }

        @Override
        public void afterMatch(StringBuilder input, StringBuilder token)
        {
            String t = token.toString();
            String s = getMatch(t);
            if (s != null)
            {
                // Trim token + replace characters in input
                if (token.length() > s.length())
                {
                    String appendStr = token.substring(s.length());
                    input.insert(0, appendStr);
                    token.delete(s.length(), token.length());
                }
            }
        }

        private String getMatch(String t)
        {
            long amount = tokens.stream().filter(s -> s.startsWith(t)).count();
            String match = tokens.stream().filter(t::startsWith).findFirst().orElse(null);
            return amount <= 1 ? match : null;
        }
    }
}
