package compiler.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
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
            System.out.println("Error reading file" + e);
            return "";
        }
    }

    public static List<String> getLinesUnformatted(String input)
    {
        // Standardize newlines
        input = input.replaceAll("\r\n", "\n").replaceAll("\r", "\n").replaceAll("\\n+", "\n");
        // Standardize spaces
        input = input.replaceAll("[ \t]+", " ");
        System.out.println("FORMATTED:\n" + input);
        // Trim leading and ending spaces
        return Arrays.stream(input.split("\n")).map(String::trim).collect(Collectors.toList());
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
}
