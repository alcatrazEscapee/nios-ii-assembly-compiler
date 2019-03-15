/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import compiler.keyword.IKeyword;

public class PatternMatchEnd implements IPattern
{
    public static final PatternMatchEnd DELIMITERS = new PatternMatchEnd(IKeyword.DELIMITERS);
    public static final PatternMatchEnd END_OF_LINE = new PatternMatchEnd(";", "//");

    private final List<String> delimiters;
    private StringBuilder source;

    private PatternMatchEnd(char... delimiters)
    {
        this.delimiters = new ArrayList<>(delimiters.length);
        for (char c : delimiters) this.delimiters.add(String.valueOf(c));
        this.source = new StringBuilder();
    }

    private PatternMatchEnd(String... delimiters)
    {
        this.delimiters = Arrays.asList(delimiters);
        this.source = new StringBuilder();
    }

    @Override
    public boolean ends(char nextChar)
    {
        return delimiters.stream().anyMatch(x -> (source.toString() + nextChar).endsWith(x));
    }

    @Override
    public void accept(char currentChar)
    {
        source.append(currentChar);
    }

    @Override
    public void after(StringBuilder result)
    {
        for (String end : delimiters)
        {
            if (result.toString().endsWith(end))
            {
                result.delete(result.length() - end.length() - 1, result.length());
                return;
            }
        }
    }

    @Override
    public void clear()
    {
        source = new StringBuilder();
    }
}
