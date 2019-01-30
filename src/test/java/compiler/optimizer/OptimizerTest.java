/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.optimizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import compiler.component.ComponentLabel;
import compiler.component.ComponentStatic;
import compiler.component.Components;
import compiler.component.IComponent;
import compiler.util.Optimizer;
import org.junit.jupiter.api.Test;

import static compiler.component.IComponent.Flag.TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OptimizerTest
{
    @Test
    void accept1()
    {
        // Multiple Consecutive Labels and then Consecutive Break - Label
        List<IComponent> list = components(
                new ComponentLabel(IComponent.format("beq", "r0, r0, %s\n"), "labelB").setFlag(TYPE, "break_conditional"),
                Components.label("labelA"),
                Components.label("labelB"),
                Components.br("labelA")
        );
        Optimizer.accept(list);
        assertEquals("labelA:\n" +
                "\tbr              labelA\n", compile(list));
    }

    @Test
    void accept2()
    {
        // Unreachable Statement
        List<IComponent> list = components(
                new ComponentLabel("%s:\n", "labelA").setFlag(TYPE, "label"),
                Components.br("labelA"),
                new ComponentStatic(IComponent.format("add", "r0, r0, r0\n")),
                Components.br("labelB")
        );
        Optimizer.accept(list);
        assertEquals("labelA:\n" +
                "\tbr              labelA\n", compile(list));
    }

    @Test
    void accept3()
    {
        // Consecutive Break - Label
        List<IComponent> list = components(
                Components.label("labelB"),
                Components.br("labelA"),
                Components.label("labelA"),
                new ComponentLabel(IComponent.format("beq", "r0, r0, %s\n"), "labelB").setFlag(TYPE, "break_conditional")
        );
        Optimizer.accept(list);
        assertEquals("labelA:\n" +
                "\tbeq             r0, r0, labelA\n", compile(list));
    }

    @Test
    void accept4()
    {
        // Consecutive Label - Break
        List<IComponent> list = components(
                Components.label("labelB"),
                new ComponentLabel(IComponent.format("beq", "r0, r0, %s\n"), "labelA").setFlag(TYPE, "break_conditional"),
                new ComponentStatic(IComponent.format("add", "r0, r0, r0\n")),
                Components.label("labelA"),
                Components.br("labelB")
        );
        Optimizer.accept(list);
        assertEquals("labelB:\n" +
                "\tbeq             r0, r0, labelB\n" +
                "\tadd             r0, r0, r0\n" +
                "\tbr              labelB\n", compile(list));
    }

    @Test
    void accept5()
    {
        // Unused Labels
        List<IComponent> list = components(
                Components.label("labelA"),
                Components.label("labelB"),
                Components.br("labelC")
        );
        Optimizer.accept(list);
        assertEquals("", compile(list));
    }

    private List<IComponent> components(IComponent... components)
    {
        return new ArrayList<>(Arrays.asList(components));
    }

    private String compile(List<IComponent> components)
    {
        return components.stream().map(IComponent::compile).reduce((x, y) -> x + y).orElse("");
    }
}