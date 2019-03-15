/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import compiler.AssemblyInterface;
import compiler.keyword.parsing.IPattern;

public final class Helpers
{
    public static String loadFile(String fileName)
    {
        Path filePath = Paths.get(fileName);
        try (BufferedReader reader = Files.newBufferedReader(filePath))
        {
            return reader.lines().reduce((x, y) -> x + "\n" + y).orElse("");
        }
        catch (IOException e)
        {
            throw new Error("File '" + fileName + "' not found.");
        }
    }

    public static String loadResource(String fileName)
    {
        InputStream input = Helpers.class.getClassLoader().getResourceAsStream(fileName);
        if (input == null)
        {
            throw new Error("Resource '" + fileName + "' not found.");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input)))
        {
            return reader.lines().reduce((x, y) -> x + "\n" + y).orElse("");
        }
        catch (IOException e)
        {
            throw new Error("Resource '" + fileName + "' not found.");
        }
    }

    public static void saveFile(String fileName, String fileData)
    {
        Path filePath = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath))
        {
            writer.write(fileData);
        }
        catch (IOException e)
        {
            AssemblyInterface.getLog().log("utils.error.save_file_exception", e);
        }
    }

    public static List<String> getLinesUnformatted(String input)
    {
        return Arrays.stream(input
                .replaceAll("\r\n", "\n") // Standardize Line Endings
                .replaceAll("\r", "\n")
                .replaceAll("[ \t]+", " ") // Standardize Spacing
                .split("\n")).map(String::trim).collect(Collectors.toList());
    }

    /**
     * @deprecated use {@link Helpers#matchPattern(StringBuilder, IPattern)} instead
     */
    @Deprecated
    public static StringBuilder nextLine(StringBuilder source)
    {
        return nextLine(source, ';', false);
    }

    /**
     * @deprecated use {@link Helpers#matchPattern(StringBuilder, IPattern)} instead
     */
    @Deprecated
    public static StringBuilder nextLine(StringBuilder source, char delimiter, boolean useWhitespace)
    {
        StringBuilder word = new StringBuilder();
        while (source.length() > 0 && source.charAt(0) != delimiter)
        {
            if (source.charAt(0) != ' ' || useWhitespace)
            {
                word.append(source.charAt(0));
            }
            source.deleteCharAt(0);
        }
        if (source.length() > 0)
        {
            // Remove last delimiter
            source.deleteCharAt(0);
        }
        return word;
    }

    /**
     * @deprecated use {@link Helpers#matchPattern(StringBuilder, IPattern)} instead
     */
    @Deprecated
    public static void advanceToNextWord(StringBuilder source)
    {
        while (source.length() > 0 && source.charAt(0) == ' ')
        {
            source.deleteCharAt(0);
        }
    }

    public static void nextChar(StringBuilder result, StringBuilder source)
    {
        char c = source.charAt(0);
        source.deleteCharAt(0);
        if (result.length() > 0 || c != ' ')
        {
            result.append(c);
        }
    }

    /**
     * @deprecated use {@link Helpers#matchPattern(StringBuilder, IPattern)} instead
     */
    @Deprecated
    public static String matchFromList(StringBuilder source, String... list)
    {
        return matchFromList(source, Arrays.asList(list));
    }

    /**
     * @deprecated use {@link Helpers#matchPattern(StringBuilder, IPattern)} instead
     */
    @Deprecated
    public static String matchFromList(StringBuilder source, Collection<String> list)
    {
        for (String s : list)
        {
            if (source.length() >= s.length() && source.substring(0, s.length()).equals(s))
            {
                source.delete(0, s.length());
                return s;
            }
        }
        return "";
    }

    /**
     * @deprecated use {@link Helpers#matchPattern(StringBuilder, IPattern)} instead
     */
    @Deprecated
    public static String matchUntil(StringBuilder source, char... delimiters)
    {
        StringBuilder arg = new StringBuilder();
        while (source.length() > 0 && !arrayContains(source.charAt(0), delimiters))
        {
            arg.append(source.charAt(0));
            source.deleteCharAt(0);
        }
        return arg.toString();
    }

    public static StringBuilder matchPattern(StringBuilder source, IPattern pattern)
    {
        StringBuilder arg = new StringBuilder();
        pattern.clear();
        while (source.length() > 0 && !pattern.ends(source.charAt(0)))
        {
            char c = source.charAt(0);
            arg.append(c);
            pattern.accept(c);
            source.deleteCharAt(0);
        }
        pattern.after(arg);
        System.out.println("Result after pattern matching: [" + arg + "]");
        return arg;
    }

    private static boolean arrayContains(char c, char... array)
    {
        for (char c1 : array)
        {
            if (c == c1)
            {
                return true;
            }
        }
        return false;
    }

    public static <T> String reduceCollection(Collection<T> items, Function<T, String> mappingFunction)
    {
        return items.stream().map(mappingFunction).reduce((x, y) -> x + y).orElse("");
    }

    public static String alphabetSuffix(int n)
    {
        /*
         Source: https://stackoverflow.com/questions/8710719/generating-an-alphabetic-sequence-in-java
         This generates the sequence a, b, c, ... y, z, aa, ab, ac ... az, ba ...
         */
        char[] buf = new char[(int) Math.floor(Math.log(25 * (n + 1)) / Math.log(26))];
        for (int i = buf.length - 1; i >= 0; i--)
        {
            n--;
            buf[i] = (char) ('a' + n % 26);
            n /= 26;
        }
        return new String(buf);
    }

    public static <T> void requireNonNull(T obj, String message, Object... args)
    {
        if (obj == null)
        {
            throw new InvalidAssemblyException(message, args);
        }
    }

    public static String[] getCommandArgs(String line)
    {
        boolean string = false;
        List<String> args = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        StringBuilder source = new StringBuilder(line).append(' ');
        while (source.length() > 0)
        {
            char c = source.charAt(0);
            source.deleteCharAt(0);
            if (string)
            {
                if (c == '\"' || c == '\'')
                {
                    string = false;
                }
                else
                {
                    current.append(c);
                }
            }
            else if (c == '\"' || c == '\'')
            {
                string = true;
            }
            else if (c == ' ')
            {
                args.add(current.toString());
                current = new StringBuilder();
            }
            else
            {
                current.append(c);
            }
        }
        return args.toArray(new String[0]);
    }

    private Helpers() {}
}
