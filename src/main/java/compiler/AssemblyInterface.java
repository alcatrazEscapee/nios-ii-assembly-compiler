/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler;

import java.util.Scanner;

import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;
import compiler.util.Logger;

public final class AssemblyInterface
{
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Logger LOG = new Logger("Assembly Compiler");
    private static final String VERSION = "1.2";

    public static void main(String... args)
    {
        if (args.length > 0)
        {
            executeCommand(args);
        }
        else
        {
            String input;
            LOG.log("command.message.init", VERSION);
            LOG.raw(">>> ");
            while (!(input = SCANNER.nextLine()).equals("exit"))
            {
                executeCommand(Helpers.getCommandArgs(input));
                LOG.raw(">>> ");
            }
        }
    }

    public static Logger getLog()
    {
        return LOG;
    }

    private static void executeCommand(String[] args)
    {
        if (args.length == 0)
        {
            LOG.log("command.error.no_arguments");
        }
        switch (args[0])
        {
            case "compile":
            case "compilef":
                executeCompile(args);
                break;
            case "help":
                LOG.log("command.message.help");
                break;
            default:
                LOG.log("command.error.unknown_command", args[0]);
        }
    }

    private static void executeCompile(String[] args)
    {
        if (args.length < 2)
        {
            LOG.log("command.error.missing_argument", "input");
            return;
        }
        if (args.length < 3 && args[0].equals("compilef"))
        {
            LOG.log("command.error.missing_argument", "output");
            return;
        }
        try
        {
            String input = Helpers.loadFile(args[1]);
            String output = AssemblyCompiler.INSTANCE.compile(input);

            if (args[0].equals("compilef"))
            {
                Helpers.saveFile(args[2], output);
                LOG.log("command.message.assembly_saved", args[2]);
            }
            else
            {
                LOG.log("command.message.assembly_view", output);
            }
        }
        catch (Error e)
        {
            LOG.log(e);
        }
        catch (InvalidAssemblyException e)
        {
            LOG.log("command.message.compile_failed");
        }
    }
}
