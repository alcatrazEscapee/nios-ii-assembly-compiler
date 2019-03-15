/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.pattern;

import compiler.util.literal.PatternResult;

/**
 * A basic string matching pattern interface.
 * See {@link PatternResult}
 */
@FunctionalInterface
public interface IPattern
{
    PatternResult apply(StringBuilder input);

    default IPattern andThen(IPattern other)
    {
        return input -> IPattern.this.apply(input).andThen(other.apply(input));
    }
}
