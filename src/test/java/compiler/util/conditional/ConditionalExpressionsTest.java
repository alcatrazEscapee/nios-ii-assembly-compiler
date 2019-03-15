/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.util.conditional;

import java.util.Collections;
import java.util.List;

import compiler.component.Components;
import compiler.component.IComponent;
import compiler.util.Helpers;
import compiler.util.Optimizer;
import compiler.util.pattern.Patterns;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConditionalExpressionsTest
{
    @Test
    void parse1()
    {
        test("\tbne             r0, r1, test_a_t\n" +
                "\tbr              test_a_f\n", "(r0 != r1)");
    }

    @Test
    void parse2()
    {
        test("\tblt             r3, r4, test_b_t\n" +
                "\tbr              test_b_f\n" +
                "test_b_t:\n" +
                "\tbr              test_a_f\n" +
                "test_b_f:\n" +
                "\tbr              test_a_t\n", "not (r3 < r4)");
    }

    @Test
    void parse3()
    {
        test("\tbeq             r0, r0, test_b_t\n" +
                "\tbr              test_b_f\n" +
                "test_b_t:\n" +
                "\tbeq             r1, r1, test_c_t\n" +
                "\tbr              test_c_f\n" +
                "test_c_t:\n" +
                "\tbr              test_a_t\n" +
                "test_b_f:\n" +
                "test_c_f:\n" +
                "\tbr              test_a_f\n", "r0 == r0 and r1 == r1");
    }

    @Test
    void parse4()
    {
        test("\tbgt             r1, r2, test_c_t\n" +
                "\tbr              test_c_f\n" +
                "test_c_t:\n" +
                "\tbgt             r2, r3, test_d_t\n" +
                "\tbr              test_d_f\n" +
                "test_d_t:\n" +
                "\tbr              test_b_t\n" +
                "test_c_f:\n" +
                "test_d_f:\n" +
                "\tbr              test_b_f\n" +
                "test_b_f:\n" +
                "\tbgt             r4, r5, test_e_t\n" +
                "\tbr              test_e_f\n" +
                "test_e_t:\n" +
                "test_b_t:\n" +
                "\tbr              test_a_t\n" +
                "test_e_f:\n" +
                "\tbr              test_a_f\n", "(r1 > r2 and r2 > r3) or r4 > r5");
    }

    @Test
    void parse5()
    {
        test("\tble             r1, r2, test_b_t\n" +
                "\tbr              test_b_f\n" +
                "test_b_f:\n" +
                "\tbge             r3, r4, test_d_t\n" +
                "\tbr              test_d_f\n" +
                "test_d_t:\n" +
                "\tbgt             r3, r2, test_f_t\n" +
                "\tbr              test_f_f\n" +
                "test_f_t:\n" +
                "\tbgt             r2, r3, test_g_t\n" +
                "\tbr              test_g_f\n" +
                "test_g_t:\n" +
                "\tbr              test_e_t\n" +
                "test_f_f:\n" +
                "test_g_f:\n" +
                "\tbr              test_e_f\n" +
                "test_e_t:\n" +
                "\tbr              test_c_t\n" +
                "test_d_f:\n" +
                "test_e_f:\n" +
                "\tbr              test_c_f\n" +
                "test_c_t:\n" +
                "test_b_t:\n" +
                "\tbr              test_a_t\n" +
                "test_c_f:\n" +
                "\tbr              test_a_f\n", "(r1 <= r2) or r3 >= r4 and ((r3 > r2) and r2 > r3)");
    }

    @Test
    void parseOptimized1()
    {
        testOptimized("\tbne             r0, r1, test_a_t\n" +
                "\tbr              test_a_f\n" +
                "test_a_t:\n" +
                "\tadd             r0, r0, r0\n" +
                "test_a_f:\n", "(r0 != r1)");
    }

    @Test
    void parseOptimized2()
    {
        testOptimized("\tblt             r3, r4, test_a_f\n" +
                "\tadd             r0, r0, r0\n" +
                "test_a_f:\n", "not (r3 < r4)");
    }

    @Test
    void parseOptimized3()
    {
        testOptimized("\tbeq             r0, r0, test_b_t\n" +
                "\tbr              test_a_f\n" +
                "test_b_t:\n" +
                "\tbeq             r1, r1, test_a_t\n" +
                "\tbr              test_a_f\n" +
                "test_a_t:\n" +
                "\tadd             r0, r0, r0\n" +
                "test_a_f:\n", "r0 == r0 and r1 == r1");
    }

    @Test
    void parseOptimized4()
    {
        testOptimized("\tbgt             r1, r2, test_c_t\n" +
                "\tbr              test_b_f\n" +
                "test_c_t:\n" +
                "\tbgt             r2, r3, test_a_t\n" +
                "test_b_f:\n" +
                "\tbgt             r4, r5, test_a_t\n" +
                "\tbr              test_a_f\n" +
                "test_a_t:\n" +
                "\tadd             r0, r0, r0\n" +
                "test_a_f:\n", "(r1 > r2 and r2 > r3) or r4 > r5");
    }

    @Test
    void parseOptimized5()
    {
        testOptimized("\tble             r1, r2, test_a_t\n" +
                "\tbge             r3, r4, test_d_t\n" +
                "\tbr              test_a_f\n" +
                "test_d_t:\n" +
                "\tbgt             r3, r2, test_f_t\n" +
                "\tbr              test_a_f\n" +
                "test_f_t:\n" +
                "\tbgt             r2, r3, test_a_t\n" +
                "\tbr              test_a_f\n" +
                "test_a_t:\n" +
                "\tadd             r0, r0, r0\n" +
                "test_a_f:\n", "(r1 <= r2) or r3 >= r4 and ((r3 > r2) and r2 > r3)");
    }

    private void test(String exp, String test)
    {
        List<IComponent> components = new IConditional.Builder("test").build(Patterns.END_OF_LINE.andThen(Patterns.TRIM_SPACE_ALL).apply(new StringBuilder(test)).get()).build();
        assertEquals(exp, Helpers.reduceCollection(components, IComponent::compile));
    }

    private void testOptimized(String exp, String test)
    {
        List<IComponent> components = new IConditional.Builder("test").build(Patterns.END_OF_LINE.andThen(Patterns.TRIM_SPACE_ALL).apply(new StringBuilder(test)).get()).build();
        Collections.addAll(components,
                Components.noop(),
                Components.label("test_a_t"),
                Components.noop(),
                Components.label("test_a_f")
        );
        Optimizer.accept(components, "simplify_names", "invert_conditionals");
        assertEquals(exp, Helpers.reduceCollection(components, IComponent::compile));

        // Everything should pass this second test, as there are no outside labels, so everything should be optimized away as unused
        components = new IConditional.Builder("test").build(Patterns.END_OF_LINE.andThen(Patterns.TRIM_SPACE_ALL).apply(new StringBuilder(test)).get()).build();
        Optimizer.accept(components, "simplify_names");
        assertEquals("", Helpers.reduceCollection(components, IComponent::compile));
    }
}