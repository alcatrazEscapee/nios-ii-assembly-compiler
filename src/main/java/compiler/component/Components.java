/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.component;

import static compiler.component.IComponent.Flag.TYPE;

/**
 * This is a bunch of wrapper functions for basic component creation
 * It simplifies some component declarations that are used repeatedly
 */
public final class Components
{
    public static IComponent label(String label)
    {
        return new ComponentLabel("%s:\n", label).setFlag(TYPE, "label");
    }

    public static IComponent br(String label)
    {
        return new ComponentLabel(IComponent.format("br", "%s\n"), label).setFlag(TYPE, "break");
    }
}
