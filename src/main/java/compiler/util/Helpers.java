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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import compiler.AssemblyInterface;

/**
 * Generic helper class with useful methods
 */
public final class Helpers
{
    public static final Set<String> REGISTERS = new HashSet<>(Arrays.asList("r0", "r1", "r2", "r3", "r4", "r5", "r6", "r7", "r8", "r9", "r10", "r11", "r12", "r13", "r14", "r15", "r16", "r17", "r18", "r19", "r20", "r21", "r22", "r23", "et", "bt", "gp", "sp", "fp", "ea", "sstatus", "ra", "status", "estatus", "bstatus", "ienable", "ipending"));
    public static final char[] DELIMITERS = {'<', '>', '?', '+', '-', '*', '/', '=', '&', '|', '^', '[', ']', '!', ':'};
    public static final String[] OPERATORS = {"?>>", "?<=", "?>=", ">=", "<=", "?<", "?>", "==", "!=", "<<", ">>", "?^", "?|", "?&", "?/", ">", "<", "+", "-", "*", "/", "=", "&", "|", "^"};
    public static final String[] COMPARATORS = {"?<=", "?>=", "?<", "?>", "<=", ">=", "!=", "==", "<", ">"};

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

    public static void nextChar(StringBuilder result, StringBuilder source)
    {
        char c = source.charAt(0);
        source.deleteCharAt(0);
        if (result.length() > 0 || c != ' ')
        {
            result.append(c);
        }
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

    private Helpers() {}
}
