/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.literal;

import compiler.component.IComponentManager;

public class IntResult
{
    private final boolean validLiteral;
    private final int value;

    public IntResult(String input, IComponentManager compiler)
    {
        // Match constants
        String var = compiler.getConstant(input);
        if (var.equals(""))
        {
            var = input;
        }

        // Match character literals
        if (input.startsWith("\'") && input.endsWith("\'"))
        {
            this.validLiteral = true;
            this.value = (int) input.charAt(2) == '\\' ? input.charAt(3) : input.charAt(2);
            return;
        }

        // Match integer literals
        int value;
        try
        {
            value = Integer.decode(var);
        }
        catch (NumberFormatException e)
        {
            // Invalid Literal
            this.value = 0;
            this.validLiteral = false;
            return;
        }

        this.value = value;
        this.validLiteral = true;
    }

    public boolean validLiteral()
    {
        return validLiteral;
    }

    public int getValue()
    {
        return value;
    }
}
