/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import compiler.util.CompileFlag;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;
import compiler.util.Logger;
import compiler.util.pattern.IPattern;
import compiler.util.pattern.Patterns;

public final class AssemblyInterface
{
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final IPattern PATTERN = Patterns.END_SPACE.andThen(Patterns.TRIM_SPACES_DOUBLE_QUOTE).andThen(Patterns.IGNORE_DOUBLE_QUOTE).andThen(Patterns.TRIM_DOUBLE_QUOTE);
    private static final Logger LOG = new Logger("Assembly Compiler");
    private static final String VERSION = "1.5";

    public static void main(String... args)
    {
        if (args.length > 0)
        {
            executeCommand(Helpers.reduceCollection(Arrays.asList(args), x -> x + " "));
        }
        else
        {
            String input;
            LOG.log("command.message.init", VERSION);
            LOG.raw(">>> ");
            while (!(input = SCANNER.nextLine()).equals("exit"))
            {
                executeCommand(input);
                LOG.raw(">>> ");
            }
        }
    }

    public static Logger getLog()
    {
        return LOG;
    }

    private static void executeCommand(String input)
    {
        StringBuilder source = new StringBuilder(input);
        String commandID = PATTERN.apply(source).getString();
        if (source.length() == 0)
        {
            LOG.log("command.error.no_arguments");
            return;
        }
        source.deleteCharAt(0);
        switch (commandID)
        {
            case "compile":
            case "compilef":
                executeCompile(commandID, source);
                break;
            case "help":
                LOG.log("command.message.help");
                break;
            default:
                LOG.log("command.error.unknown_command", commandID);
        }
    }

    private static void executeCompile(String commandArg, StringBuilder source)
    {
        String arg1 = PATTERN.apply(source).getString(), arg2 = "";
        if ("".equals(arg1))
        {
            LOG.log("command.error.missing_argument", "input");
            return;
        }
        if (commandArg.equals("compilef"))
        {
            if (source.length() > 0)
            {
                source.deleteCharAt(0);
            }
            arg2 = PATTERN.apply(source).getString();
            if ("".equals(arg2))
            {
                LOG.log("command.error.missing_argument", "output");
                return;
            }
        }
        try
        {
            Set<CompileFlag> extraArgs = new HashSet<>();
            while (source.length() > 0)
            {
                source.deleteCharAt(0);
                String extraArg = PATTERN.apply(source).getString();
                CompileFlag flag = CompileFlag.get(extraArg);
                if (!"".equals(extraArg) && flag != null)
                {
                    extraArgs.add(flag);
                }
                else
                {
                    LOG.log("command.error.unknown_argument", extraArg);
                }
            }

            String input = Helpers.loadFile(arg1);
            String output = AssemblyCompiler.INSTANCE.compile(input, extraArgs);

            if (commandArg.equals("compilef"))
            {
                Helpers.saveFile(arg2, output);
                LOG.log("command.message.assembly_saved", arg2);
            }
            else
            {
                LOG.log("command.message.assembly_view", output);
            }
        }
        catch (InvalidAssemblyException e)
        {
            LOG.log("command.message.compile_failed");
        }
    }
}
