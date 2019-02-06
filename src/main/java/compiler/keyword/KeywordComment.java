/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword;

import compiler.AssemblyCompiler;
import compiler.component.ComponentStatic;
import compiler.component.IComponent;
import compiler.component.IComponentManager;

public class KeywordComment implements IKeyword
{
    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return keyword.equals("//");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        IComponent parent = compiler.getComponent(IComponent.Type.CURRENT);
        if (parent != null)
        {
            // If inside a current function, then add a comment
            parent.add(new ComponentStatic("\t#" + inputBuilder + "\n"));
        }
        else
        {
            AssemblyCompiler.INSTANCE.warn("error.message.discarded_comment");
        }

        // Clear the rest of the line
        inputBuilder.delete(0, inputBuilder.length());
    }
}
