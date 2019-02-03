/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            System.out.println("Error reading file! " + e);
            return "";
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
            System.out.println("Error saving file!" + e);
        }
    }

    public static List<String> getLinesUnformatted(String input)
    {
        return Arrays.stream(input
                .replaceAll("\r\n", "\n") // Standardize Line Endings
                .replaceAll("\r", "\n")
                .replaceAll("\\n+", "\n") // Remove Blank Lines
                .replaceAll("[ \t]+", " ") // Standardize Spacing
                .split("\n")).map(String::trim).collect(Collectors.toList());
    }

    public static StringBuilder nextLine(StringBuilder source)
    {
        return nextLine(source, ';', false);
    }

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

    public static void advanceToNextWord(StringBuilder source)
    {
        while (source.length() > 0 && source.charAt(0) == ' ')
        {
            source.deleteCharAt(0);
        }
    }

    public static char nextChar(StringBuilder source)
    {
        char c = source.charAt(0);
        source.deleteCharAt(0);
        return c;
    }

    public static String getFromList(StringBuilder source, String... list)
    {
        return getFromList(source, Arrays.asList(list));
    }

    public static String getFromList(StringBuilder source, Collection<String> list)
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
}
