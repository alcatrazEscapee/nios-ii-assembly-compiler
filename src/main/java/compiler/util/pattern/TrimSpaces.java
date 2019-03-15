/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.pattern;

import compiler.util.literal.PatternResult;

public class TrimSpaces implements IPattern
{
    private final IPattern validator;

    TrimSpaces(IPattern validator)
    {
        this.validator = validator == null ? input -> new PatternResult(input) {} : validator;
    }

    @Override
    public PatternResult apply(StringBuilder input)
    {
        return new TrimSpacesResult(input);
    }

    class TrimSpacesResult extends PatternResult
    {
        private final PatternResult result;

        TrimSpacesResult(StringBuilder input)
        {
            super(input);

            this.result = validator.apply(input);
        }

        @Override
        public void afterMatch(StringBuilder input, StringBuilder token)
        {
            for (int i = 0; i < token.length(); i++)
            {
                char c = token.charAt(i);
                result.acceptChar(token, c);
                if (c == ' ' && result.isValidToken(null))
                {
                    token.deleteCharAt(i);
                    i--;
                }
            }
        }
    }
}
