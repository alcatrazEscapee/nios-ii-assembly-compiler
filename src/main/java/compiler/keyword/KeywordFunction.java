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

public class KeywordFunction extends AbstractKeyword
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
        StringBuilder source = Helpers.nextLine(inputBuilder, ':', false);
        if (source.length() == 0)
        {
            throw new InvalidAssemblyException("Function name must be non empty");
        }
        String name = source.toString();
        if (functionNames.containsKey(name))
        {
            throw new InvalidAssemblyException("Can't have multiple functions with the same name");
        }
        // Ensure a unique function name -> prefix mapping
        String shortName = name.replaceAll("[a-z]", "").toLowerCase();
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
