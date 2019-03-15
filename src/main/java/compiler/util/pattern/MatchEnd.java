/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import compiler.util.literal.PatternResult;

public class MatchEnd implements IPattern
{
    private final List<String> endSequences;

    MatchEnd(char... endSequences)
    {
        this.endSequences = new ArrayList<>();
        for (char c : endSequences) this.endSequences.add(String.valueOf(c));
    }

    MatchEnd(String... endSequences)
    {
        this.endSequences = Arrays.asList(endSequences);
    }

    @Override
    public PatternResult apply(StringBuilder input)
    {
        return new MatchEndResult(input);
    }

    class MatchEndResult extends PatternResult
    {
        MatchEndResult(StringBuilder input)
        {
            super(input);
        }

        @Override
        public boolean isValidToken(StringBuilder token)
        {
            String t = token.toString();
            return endSequences.stream().anyMatch(t::endsWith);
        }

        @Override
        public void afterMatch(StringBuilder input, StringBuilder token)
        {
            // Replace the ending on the input buffer, and remove it from the token
            String t = token.toString();
            for (String s : endSequences)
            {
                if (t.endsWith(s))
                {
                    input.insert(0, s);
                    token.delete(token.length() - s.length(), token.length());
                    return;
                }
            }
        }
    }
}
