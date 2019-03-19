/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util;

public enum CompileFlag
{
    DEBUG_MODE;

    public static CompileFlag get(String input)
    {
        switch (input)
        {
            case "-d":
            case "-debug":
                return DEBUG_MODE;
            default:
                return null;
        }
    }
}
