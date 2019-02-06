/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util;

import compiler.AssemblyInterface;

public class InvalidAssemblyException extends RuntimeException
{
    public InvalidAssemblyException(String key, Object... args)
    {
        super(AssemblyInterface.getLog().format(key, args));
    }
}
