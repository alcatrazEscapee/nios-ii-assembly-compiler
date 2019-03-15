/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword.parsing;

public interface IPattern
{
    IPattern EMPTY = new IPattern() {};

    default boolean ends(char nextChar) { return true; }

    default void accept(char currentChar) {}

    default void accept(String inputChars)
    {
        for (char c : inputChars.toCharArray())
        {
            accept(c);
        }
    }

    default void after(StringBuilder result) {}

    default void clear() {}

    default IPattern and(IPattern other)
    {
        return new IPattern()
        {
            @Override
            public boolean ends(char nextChar)
            {
                return IPattern.this.ends(nextChar) && other.ends(nextChar);
            }

            @Override
            public void accept(char currentChar)
            {
                IPattern.this.accept(currentChar);
                other.accept(currentChar);
            }

            @Override
            public void after(StringBuilder result)
            {
                IPattern.this.after(result);
                other.after(result);
            }

            @Override
            public void clear()
            {
                IPattern.this.clear();
                other.clear();
            }
        };
    }
}
