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

public abstract class ConditionalBinary implements IConditional
{
    final IConditional inner;
    private final String name;

    ConditionalBinary(String name, IConditional inner)
    {
        this.name = name;
        this.inner = inner;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public static class Not extends ConditionalBinary
    {
        Not(String name, IConditional inner)
        {
            super(name, inner);
        }

        @Override
        public List<IComponent> compile()
        {
            List<IComponent> components = new ArrayList<>(inner.compile());
            Collections.addAll(components,
                    Components.label(inner.getName() + "_t"),
                    Components.br(getName() + "_f"),
                    Components.label(inner.getName() + "_f"),
                    Components.br(getName() + "_t")
            );
            return components;
        }
    }

}
