/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler.keyword;

import compiler.util.IComponentManagerStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeywordReturnTest
{
    private IKeyword keyword;
    private IComponentManagerStub stub;

    @BeforeEach
    void setUp()
    {
        keyword = new KeywordReturn();
        stub = new IComponentManagerStub();
    }

    @Test
    void matches()
    {
        assertTrue(keyword.matches("return", new StringBuilder(" r3;")));
        assertTrue(keyword.matches("return", new StringBuilder(";")));

        assertFalse(keyword.matches("return", new StringBuilder("stuff")));
    }

    @Test
    void apply1()
    {
        keyword.apply("return", new StringBuilder(";"), stub);
        assertEquals("    br              _ret\n", stub.compile());
    }

    @Test
    void apply2()
    {
        keyword.apply("return", new StringBuilder(" r3;"), stub);
        assertEquals("    mov             r2, r3\n    br              _ret\n", stub.compile());
    }
}