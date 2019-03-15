/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword;

import compiler.component.Components;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.keyword.parsing.PatternMatchEnd;
import compiler.keyword.parsing.PatternTrimSpaces;
import compiler.util.Helpers;

public class KeywordCall implements IKeyword
{
    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, "call");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        StringBuilder source = Helpers.matchPattern(inputBuilder, PatternMatchEnd.END_OF_LINE.and(PatternTrimSpaces.ALL));
        IComponent parent = compiler.getComponent(IComponent.Type.CURRENT);
        Helpers.requireNonNull(parent, "error.message.extra_keyword", "call");
        parent.add(Components.call(source.toString()));
    }
}
