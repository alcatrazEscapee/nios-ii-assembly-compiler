/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.pattern;

import compiler.util.Helpers;

/**
 * A collection of {@link IPattern}s for usage in parsing
 */
public final class Patterns
{
    public static final IPattern END_OF_LINE = new MatchEnd(";", "//");
    public static final IPattern END_DELIMITER = new MatchEnd(Helpers.DELIMITERS);
    public static final IPattern END_SPACE = new MatchEnd(" ");
    public static final IPattern END_R_BRACKET = new MatchEnd("]");
    public static final IPattern END_COLON = new MatchEnd(":");

    public static final IPattern NEXT_REGISTER = new MatchExact(Helpers.REGISTERS);
    public static final IPattern NEXT_OPERATOR = new MatchExact(Helpers.OPERATORS);
    public static final IPattern NEXT_COMPARATOR = new MatchExact(Helpers.COMPARATORS);

    public static final IPattern IGNORE_SINGLE_QUOTE = new IgnoreSequence(true, '\'', '\\');
    public static final IPattern IGNORE_DOUBLE_QUOTE = new IgnoreSequence(true, '\"', '\\');

    public static final IPattern TRIM_SPACE_ALL = new TrimSpaces(null);
    public static final IPattern TRIM_SPACES_SINGLE_QUOTE = new TrimSpaces(IGNORE_SINGLE_QUOTE);
    public static final IPattern TRIM_SPACES_DOUBLE_QUOTE = new TrimSpaces(IGNORE_DOUBLE_QUOTE);

    public static final IPattern TRIM_DOUBLE_QUOTE = new TrimSurrounding('\"');

    public static final IPattern IGNORE_FIRST_MINUS = new IgnoreChar('-', 0);
}
