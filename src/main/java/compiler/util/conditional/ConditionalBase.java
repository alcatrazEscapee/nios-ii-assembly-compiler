/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.conditional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import compiler.component.Components;
import compiler.component.IComponent;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;

import static compiler.keyword.IKeyword.COMPARATORS;
import static compiler.keyword.IKeyword.REGISTERS;

public class ConditionalBase extends AbstractConditional
{
    private final String name;
    private final List<IComponent> components;

    ConditionalBase(String name, StringBuilder source)
    {
        this.name = name;

        String lhs = Helpers.matchFromList(source, REGISTERS);
        if (!REGISTERS.contains(lhs))
        {
            throw new InvalidAssemblyException("error.message.unknown_register", lhs);
        }

        String op = Helpers.matchFromList(source, COMPARATORS);
        if (op.equals(""))
        {
            throw new InvalidAssemblyException("error.message.unknown_comparator", op);
        }

        String rhs = source.toString();
        if (!REGISTERS.contains(rhs))
        {
            throw new InvalidAssemblyException("error.message.unknown_register", rhs);
        }

        this.components = new ArrayList<>();
        Collections.addAll(components,
                Components.brOp(lhs, op, rhs, name + "_t"),
                Components.br(name + "_f")
        );
    }

    @Override
    public List<IComponent> build()
    {
        return components;
    }

    @Override
    public String getName()
    {
        return name;
    }
}
