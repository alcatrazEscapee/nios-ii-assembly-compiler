/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.conditionals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import compiler.component.Components;
import compiler.component.IComponent;

public abstract class ConditionalLogical extends AbstractConditional
{
    final IConditional lhs, rhs;
    private final String op;
    private final String name;

    ConditionalLogical(String name, IConditional lhs, IConditional rhs, String op)
    {
        this.name = name;
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public static class Or extends ConditionalLogical
    {
        Or(String name, IConditional lhs, IConditional rhs, String op)
        {
            super(name, lhs, rhs, op);
        }

        @Override
        public List<IComponent> build()
        {
            List<IComponent> components = new ArrayList<>(lhs.build());
            components.add(Components.label(lhs.getName() + "_f"));
            components.addAll(rhs.build());
            Collections.addAll(components,
                    Components.label(rhs.getName() + "_t"),
                    Components.label(lhs.getName() + "_t"),
                    Components.br(getName() + "_t"),
                    Components.label(rhs.getName() + "_f"),
                    Components.br(getName() + "_f")
            );
            return components;
        }
    }

    public static class And extends ConditionalLogical
    {
        And(String name, IConditional lhs, IConditional rhs, String op)
        {
            super(name, lhs, rhs, op);
        }

        @Override
        public List<IComponent> build()
        {
            List<IComponent> components = new ArrayList<>(lhs.build());
            components.add(Components.label(lhs.getName() + "_t"));
            components.addAll(rhs.build());
            Collections.addAll(components,
                    Components.label(rhs.getName() + "_t"),
                    Components.br(getName() + "_t"),
                    Components.label(lhs.getName() + "_f"),
                    Components.label(rhs.getName() + "_f"),
                    Components.br(getName() + "_f")
            );
            return components;
        }
    }
}
