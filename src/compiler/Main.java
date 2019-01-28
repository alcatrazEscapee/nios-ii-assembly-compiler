/*
 * Part of AssemblyCompiler by Alex O'Neill
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler;

import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;

public class Main
{
    public static void main(String... args)
    {
        if (args.length < 1 || (!args[0].equals("compile") && !args[0].equals("compilef")))
        {
            System.out.println("Missing argument [compile|compilef]");
            return;
        }
        if (args.length < 2)
        {
            System.out.println("Missing argument [input]");
            return;
        }
        if (args.length < 3 && args[0].equals("compilef"))
        {
            System.out.println("Missing argument [output]");
            return;
        }
        try
        {
            String input = Helpers.loadFile(args[1]);
            String output = Compiler.INSTANCE.compile(input);

            if (args[0].equals("compilef"))
            {
                System.out.println("Saving assembly to file.");
                Helpers.saveFile(args[2], output);
            }
            else
            {
                System.out.println("Compiled assembly:\n" + output);
            }
        }
        catch (InvalidAssemblyException e)
        {
            System.out.println("Invalid Assembly: " + e);
        }
    }
}
