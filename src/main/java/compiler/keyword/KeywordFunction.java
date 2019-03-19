/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword;

import java.util.HashMap;
import java.util.Map;

import compiler.component.ComponentFunction;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;
import compiler.util.pattern.Patterns;

public class KeywordFunction implements IKeyword
{
    private final Map<String, String> functionNames = new HashMap<>();

    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, "function") || IKeyword.matchKeyword(keyword, inputBuilder, "void function");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        StringBuilder source = Patterns.END_COLON.andThen(Patterns.TRIM_SPACE_ALL).apply(inputBuilder).get();
        if (!Helpers.isValidName(source.toString()))
        {
            throw new InvalidAssemblyException("error.message.invalid_function_name", source);
        }
        String name = source.toString();
        if (functionNames.containsKey(name))
        {
            throw new InvalidAssemblyException("error.message.duplicate_function_name", name);
        }
        // Ensure a unique function name -> prefix mapping
        String shortName = name.replaceAll("[a-z0-9]", "").toLowerCase();
        if (shortName.length() <= 1)
        {
            shortName = name.toLowerCase().substring(0, 3);
        }
        String prefix = shortName;
        int index = 0;
        while (functionNames.containsValue(prefix))
        {
            prefix = shortName + (++index);
        }
        functionNames.put(name, prefix);
        compiler.addComponent(IComponent.Type.CURRENT, new ComponentFunction(name, prefix, keyword.startsWith("void")));
    }

    @Override
    public void reset()
    {
        functionNames.clear();
    }
}
