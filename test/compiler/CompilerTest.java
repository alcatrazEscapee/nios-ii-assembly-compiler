package compiler;

import compiler.util.Helpers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompilerTest
{
    @Test
    void test1()
    {
        test("test1");
    }

    @Test
    void test2()
    {
        test("test2");
    }

    @Test
    void test3()
    {
        test("test3");
    }

    @Test
    void test4()
    {
        test("test4");
    }

    @Test
    void test5()
    {
        test("test5");
    }

    @Test
    void test6()
    {
        test("test6");
    }

    @Test
    void test7()
    {
        test("test7");
    }

    @Test
    void test8()
    {
        test("test8");
    }

    private void test(String fileName)
    {
        String inputFile = Helpers.loadFile("assets/" + fileName + ".sc");
        String outputFile = Helpers.loadFile("assets/" + fileName + ".s");
        assertEquals(outputFile, Compiler.INSTANCE.compile(inputFile));
    }
}