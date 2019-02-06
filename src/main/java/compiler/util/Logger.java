/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Logger
{
    private static final Gson GSON = new GsonBuilder().create();
    private final String name;
    private Map<String, String> keys;

    public Logger(String name)
    {
        this.name = "[" + name + "] ";
        String input = Helpers.loadResource("assets/lang.json");
        keys = GSON.fromJson(input, new TypeToken<Map<String, String>>() {}.getType());
    }

    public String format(String key, Object... args)
    {
        return String.format(keys.getOrDefault(key, key), args);
    }

    public void log(String message, Object... args)
    {
        log(format(message, args));
    }

    public void log(String message, Throwable t)
    {
        log(format(message, t.getClass().getSimpleName() + ": " + t.getMessage()));
    }

    public void log(Throwable t)
    {
        log(t.toString());
    }

    public void raw(String message)
    {
        logRaw(message);
    }

    private void log(String message)
    {
        System.out.println(name + message);
    }

    private void logRaw(String message)
    {
        System.out.print(message);
    }
}
