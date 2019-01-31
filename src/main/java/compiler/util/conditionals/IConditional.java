/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.conditionals;

import java.util.List;

import compiler.component.IComponent;

public interface IConditional
{
    List<IComponent> compile();

    String getName();
}
