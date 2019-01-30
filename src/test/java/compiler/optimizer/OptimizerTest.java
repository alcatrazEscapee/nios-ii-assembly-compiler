/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.optimizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import compiler.component.ComponentStatic;
import compiler.component.IComponent;
import org.junit.jupiter.api.Test;

import static compiler.component.IComponent.Flag.LABEL;
import static compiler.component.IComponent.Flag.TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OptimizerTest
{
    @Test
    void accept1()
    {
        // Multiple Consecutive Labels and then Consecutive Break - Label
        List<IComponent> list = components(
                new ComponentStatic(IComponent.format("beq", "r0, r0, labelB\n")).setFlag(TYPE, "break_conditional").setFlag(LABEL, "labelB"),
                new ComponentStatic("labelA:\n").setFlag(TYPE, "label").setFlag(LABEL, "labelA"),
                new ComponentStatic("labelB:\n").setFlag(TYPE, "label").setFlag(LABEL, "labelB")
        );
        Optimizer.accept(list);
        assertEquals("labelA:\n", compile(list));
    }

    @Test
    void accept2()
    {
        // Unreachable Statement
        List<IComponent> list = components(
                new ComponentStatic(IComponent.format("br", "labelA\n")).setFlag(TYPE, "break").setFlag(LABEL, "labelA"),
                new ComponentStatic(IComponent.format("add", "r0, r0, r0\n")),
                new ComponentStatic(IComponent.format("br", "labelB\n")).setFlag(TYPE, "break").setFlag(LABEL, "labelB")
        );
        Optimizer.accept(list);
        assertEquals("\tbr              labelA\n", compile(list));
    }

    @Test
    void accept3()
    {
        // Consecutive Break - Label
        List<IComponent> list = components(
                new ComponentStatic(IComponent.format("br", "labelA\n")).setFlag(TYPE, "break").setFlag(LABEL, "labelA"),
                new ComponentStatic("labelA:\n").setFlag(TYPE, "label").setFlag(LABEL, "labelA"),
                new ComponentStatic(IComponent.format("beq", "r0, r0, labelB\n")).setFlag(TYPE, "break_conditional").setFlag(LABEL, "labelB")
        );
        Optimizer.accept(list);
        assertEquals("labelA:\n" +
                "\tbeq             r0, r0, labelB\n", compile(list));
    }

    @Test
    void accept4()
    {
        // Consecutive Label - Break
        List<IComponent> list = components(
                new ComponentStatic(IComponent.format("beq", "r0, r0, labelA\n")).setFlag(TYPE, "break_conditional").setFlag(LABEL, "labelA"),
                new ComponentStatic(IComponent.format("add", "r0, r0, r0\n")),
                new ComponentStatic("labelA:\n").setFlag(TYPE, "label").setFlag(LABEL, "labelA"),
                new ComponentStatic(IComponent.format("br", "labelB\n")).setFlag(TYPE, "break").setFlag(LABEL, "labelB")
        );
        Optimizer.accept(list);
        assertEquals("\tbeq             r0, r0, labelB\n" +
                "\tadd             r0, r0, r0\n" +
                "\tbr              labelB\n", compile(list));
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