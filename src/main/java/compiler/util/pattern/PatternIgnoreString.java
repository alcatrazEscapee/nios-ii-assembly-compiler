/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.pattern;

public class PatternIgnoreString implements IPattern
{
    public static final PatternIgnoreString DOUBLE_QUOTE = new PatternIgnoreString('\"', '\\', true);
    public static final PatternIgnoreString SINGLE_QUOTE = new PatternIgnoreString('\'', '\\', true);

    private final boolean useEscapes;
    private final char delimChar, escapeChar;
    private boolean inString, inEscape;

    private PatternIgnoreString(char delimChar, char escapeChar, boolean useEscapes)
    {
        this.delimChar = delimChar;
        this.escapeChar = escapeChar;
        this.useEscapes = useEscapes;
    }

    @Override
    public boolean ends(char nextChar)
    {
        return !inString;
    }

    @Override
    public void accept(char currentChar)
    {
        if (inEscape)
        {
            inEscape = false;
        }
        else
        {
            if (useEscapes && currentChar == escapeChar)
            {
                inEscape = true;
            }
            else if (currentChar == delimChar)
            {
                inString = !inString;
            }
        }
    }

    @Override
    public void clear()
    {
        inString = false;
        inEscape = false;
    }
}
