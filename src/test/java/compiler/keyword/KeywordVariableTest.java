/*
 * Part of AssemblyCompiler by Alex O'Neill
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword;

import compiler.util.IComponentManagerStub;
import compiler.util.InvalidAssemblyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeywordVariableTest
{
    private IKeyword keyword;
    private IComponentManagerStub stub;

    @BeforeEach
    void setUp()
    {
        keyword = new KeywordVariable();
        stub = new IComponentManagerStub();
    }

    @Test
    void matches()
    {
        assertTrue(keyword.matches("int", new StringBuilder(" x = 3;")));
        assertTrue(keyword.matches("byte", new StringBuilder(" x = 3;")));
        assertTrue(keyword.matches("string", new StringBuilder(" x = \" test \";")));
        assertTrue(keyword.matches("const", new StringBuilder(" x = 3;")));
        assertTrue(keyword.matches("var", new StringBuilder("[12];")));

        assertFalse(keyword.matches("int", new StringBuilder("eger;")));
        assertFalse(keyword.matches("byte", new StringBuilder("d;")));
        assertFalse(keyword.matches("string", new StringBuilder("s;")));
        assertFalse(keyword.matches("const", new StringBuilder("ant;")));
        assertFalse(keyword.matches("var", new StringBuilder("12];")));
    }

    @Test
    void apply1()
    {
        keyword.apply("int", new StringBuilder(" x = 3;"), stub);
        assertEquals("x:\n    .word           3\n", stub.compile());
    }

    @Test
    void apply2()
    {
        keyword.apply("byte", new StringBuilder(" thing = 0xF;"), stub);
        assertEquals("thing:\n    .byte           0xF\n", stub.compile());
    }

    @Test
    void apply3()
    {
        keyword.apply("string", new StringBuilder(" s = \"some text with spaces\";"), stub);
        assertEquals("s:\n    .asciz          \"some text with spaces\"\n", stub.compile());
    }

    @Test
    void apply4()
    {
        keyword.apply("var", new StringBuilder("[1234] long;"), stub);
        assertEquals("long:\n    .skip           1234\n", stub.compile());
    }

    @Test
    void apply5()
    {
        keyword.apply("int", new StringBuilder(" x;"), stub);
        assertEquals("x:\n    .skip           4\n", stub.compile());
    }

    @Test
    void apply6()
    {
        keyword.apply("byte", new StringBuilder(" x;"), stub);
        assertEquals("x:\n    .skip           1\n", stub.compile());
    }

    @Test
    void apply7()
    {
        keyword.apply("const", new StringBuilder(" x = 0xFFFFFF;"), stub);
        assertEquals("    .equ            x, 0xFFFFFF\n", stub.compile());
    }

    @Test
    void apply8()
    {
        keyword.apply("int", new StringBuilder(" x = 1, 2, 3;"), stub);
        assertEquals("x:\n    .word           1, 2, 3\n", stub.compile());
    }

    @Test
    void apply9()
    {
        keyword.apply("byte", new StringBuilder(" x = 0xF, 0x7, 0xA;"), stub);
        assertEquals("x:\n    .byte           0xF, 0x7, 0xA\n", stub.compile());
    }

    @Test
    void applyException1()
    {
        assertThrows(InvalidAssemblyException.class, () -> keyword.apply("const", new StringBuilder(" x;"), stub));
    }

    @Test
    void applyException2()
    {
        assertThrows(InvalidAssemblyException.class, () -> keyword.apply("var", new StringBuilder("[123] x = 3;"), stub));
    }
}