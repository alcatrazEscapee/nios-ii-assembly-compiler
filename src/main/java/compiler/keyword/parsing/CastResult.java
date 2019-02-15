/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword.parsing;

import compiler.util.InvalidAssemblyException;

public final class CastResult
{
    private final String result;
    private final boolean ioFlag, byteFlag, unsignedFlag;

    public CastResult(String input)
    {
        boolean ioFlag = false, byteFlag = false, unsignedFlag = false;
        if (input.startsWith("("))
        {
            int index = input.indexOf(')');
            if (index == -1)
            {
                throw new InvalidAssemblyException("error.message.expected_keyword", input, ")");
            }
            this.result = input.substring(index + 1);
            String[] flags = input.substring(1, index).split(",");
            for (String flag : flags)
            {
                switch (flag)
                {
                    case "u":
                    case "unsigned":
                        unsignedFlag = true;
                        break;
                    case "io":
                    case "input":
                    case "output":
                        ioFlag = true;
                        break;
                    case "byteio": // Here for legacy reasons
                        ioFlag = true;
                    case "byte":
                    case "b":
                        byteFlag = true;
                }
            }

        }
        else
        {
            this.result = input;
        }
        this.byteFlag = byteFlag;
        this.ioFlag = ioFlag;
        this.unsignedFlag = unsignedFlag;
    }

    public String getResult()
    {
        return result;
    }

    public String makeStore()
    {
        return "st" + (byteFlag ? "b" : "w") + (unsignedFlag ? "u" : "") + (ioFlag ? "io" : "");
    }

    public String makeLoad()
    {
        return "ld" + (byteFlag ? "b" : "w") + (unsignedFlag ? "u" : "") + (ioFlag ? "io" : "");
    }
}
