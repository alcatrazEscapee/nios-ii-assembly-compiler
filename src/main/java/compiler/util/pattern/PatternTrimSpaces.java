/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.pattern;

public class PatternTrimSpaces implements IPattern
{
    public static final PatternTrimSpaces ALL = new PatternTrimSpaces(IPattern.EMPTY);
    public static final PatternTrimSpaces SINGLE_QUOTE_STRINGS = new PatternTrimSpaces(PatternIgnoreString.SINGLE_QUOTE);

    private final IPattern stringPattern;

    private PatternTrimSpaces(IPattern stringPattern)
    {
        this.stringPattern = stringPattern;
    }

    @Override
    public void after(StringBuilder result)
    {
        stringPattern.clear();
        for (int i = 0; i < result.length(); i++)
        {
            char c = result.charAt(i);
            stringPattern.accept(c);
            if (c == ' ' && !(result.length() > i + 1 && !stringPattern.ends(result.charAt(i + 1))))
            {
                result.deleteCharAt(i);
                i--;
            }
        }
    }
}
