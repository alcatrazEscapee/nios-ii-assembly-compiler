/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword.parsing;

public final class CastResult
{
    private final String result;
    private final boolean ioFlag, byteFlag;

    public CastResult(String input)
    {
        if (input.length() >= 4 && input.startsWith("(io)"))
        {
            ioFlag = true;
            byteFlag = false;
            result = input.substring(4);
        }
        else if (input.length() >= 6 && input.startsWith("(byte)"))
        {
            ioFlag = false;
            byteFlag = true;
            result = input.substring(6);
        }
        else if (input.length() >= 8 && input.startsWith("(byteio)"))
        {
            byteFlag = ioFlag = true;
            result = input.substring(8);
        }
        else
        {
            byteFlag = ioFlag = false;
            result = input;
        }
    }

    public String getResult()
    {
        return result;
    }

    public String makeStore()
    {
        return "st" + (byteFlag ? "b" : "w") + (ioFlag ? "io" : "");
    }

    public String makeLoad()
    {
        return "ld" + (byteFlag ? "b" : "w") + (ioFlag ? "io" : "");
    }
}
