package compiler;


import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompilerTest
{
    @Test
    void test1()
    {
        testMatch("test1");
    }

    @Test
    void test2()
    {
        testMatch("test2");
    }

    @Test
    void test3()
    {
        testMatch("test3");
    }

    @Test
    void test4()
    {
        testMatch("test4");
    }

    @Test
    void test5()
    {
        testMatch("test5");
    }

    @Test
    void test6()
    {
        testMatch("test6");
    }

    @Test
    void test7()
    {
        testMatch("test7");
    }

    @Test
    void test8()
    {
        testMatch("test8");
    }

    @Test
    void test9()
    {
        testMatch("test9");
    }

    @Test
    void test10()
    {
        testMatch("test10");
    }

    @Test
    void test11()
    {
        testMatch("test11");
    }

    @Test
    void test12()
    {
        testMatch("test12");
    }

    @Test
    void test13()
    {
        testMatch("test13");
    }

    @Test
    void test14()
    {
        testMatch("test14");
    }

    @Test
    void test15()
    {
        testMatch("test15");
    }

    @Test
    void testException1()
    {
        assertThrows(InvalidAssemblyException.class, () -> testException("exc1"));
    }

    @Test
    void testException2()
    {
        assertThrows(InvalidAssemblyException.class, () -> testException("exc2"));
    }

    @Test
    void testException3()
    {
        assertThrows(InvalidAssemblyException.class, () -> testException("exc3"));
    }

    private void testMatch(String fileName)
    {
        String inputFile = Helpers.loadFile("assets/" + fileName + ".sc");
        String outputFile = Helpers.loadFile("assets/" + fileName + ".s");
        assertEquals(outputFile, Compiler.INSTANCE.compile(inputFile));
    }

    private void testException(String fileName)
    {
        String inputFile = Helpers.loadFile("assets/" + fileName + ".sc");
        Compiler.INSTANCE.compile(inputFile);
    }
}