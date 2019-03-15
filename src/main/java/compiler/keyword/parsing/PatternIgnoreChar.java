/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword.parsing;

public class PatternIgnoreChar implements IPattern
{
    public static final PatternIgnoreChar IGNORE_MINUS = new PatternIgnoreChar('-', 0);

    private final char charMatch;
    private final int posMatch;
    private char charAt;
    private int posAt;

    private PatternIgnoreChar(char charMatch, int posMatch)
    {
        this.charMatch = charMatch;
        this.posMatch = posMatch;
        clear();
    }

    @Override
    public boolean ends(char nextChar)
    {
        return (posAt != posMatch - 1 || nextChar != charMatch) && (posAt != posMatch || charAt != charMatch);
    }

    @Override
    public void accept(char currentChar)
    {
        posAt++;
        if (posAt == posMatch)
        {
            charAt = currentChar;
        }
    }

    @Override
    public void clear()
    {
        posAt = -1;
    }
}
