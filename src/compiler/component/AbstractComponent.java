/*
 * Part of AssemblyCompiler by Alex O'Neill
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.component;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractComponent implements IComponent
{
    List<IComponent> components;

    AbstractComponent()
    {
        this.components = new ArrayList<>();
    }

    @Override
    public void add(IComponent sub)
    {
        components.add(sub);
    }
}
