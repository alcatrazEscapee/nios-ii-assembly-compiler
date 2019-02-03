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
import compiler.util.Helpers;
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
        test("labelA:\n" +
                        "\tbr              labelA\n",
                new ComponentLabel(IComponent.format("beq", "r0, r0, %s\n"), "labelB").setFlag(TYPE, "break_conditional"),
                Components.label("labelA"),
                Components.label("labelB"),
                Components.br("labelA")
        );
    }

    @Test
    void accept2()
    {
        // Unreachable Statement
        test("labelA:\n" +
                        "\tbr              labelA\n",
                new ComponentLabel("%s:\n", "labelA").setFlag(TYPE, "label"),
                Components.br("labelA"),
                new ComponentStatic(IComponent.format("add", "r0, r0, r0\n")),
                Components.br("labelB")
        );
    }

    @Test
    void accept3()
    {
        // Consecutive Break - Label
        test("labelA:\n" +
                        "\tbeq             r0, r0, labelA\n",
                Components.label("labelB"),
                Components.br("labelA"),
                Components.label("labelA"),
                new ComponentLabel(IComponent.format("beq", "r0, r0, %s\n"), "labelB").setFlag(TYPE, "break_conditional")
        );
    }

    @Test
    void accept4()
    {
        // Consecutive Label - Break
        test("labelB:\n" +
                        "\tbeq             r0, r0, labelB\n" +
                        "\tadd             r0, r0, r0\n" +
                        "\tbr              labelB\n",
                Components.label("labelB"),
                new ComponentLabel(IComponent.format("beq", "r0, r0, %s\n"), "labelA").setFlag(TYPE, "break_conditional"),
                new ComponentStatic(IComponent.format("add", "r0, r0, r0\n")),
                Components.label("labelA"),
                Components.br("labelB")
        );
    }

    @Test
    void accept5()
    {
        // Unused Labels
        test("",
                Components.label("labelA"),
                Components.label("labelB"),
                Components.br("labelC")
        );
    }

    private void test(String exp, IComponent... components)
    {
        List<IComponent> list = new ArrayList<>(Arrays.asList(components));
        Optimizer.accept(list, "simplify_names");
        assertEquals(exp, Helpers.reduceCollection(list, IComponent::compile));
    }
}