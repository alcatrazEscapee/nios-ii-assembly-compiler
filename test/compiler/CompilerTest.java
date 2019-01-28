/*
 * Part of AssemblyCompiler by Alex O'Neill
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler;


import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompilerTest
{
    @TestFactory
    Stream<DynamicTest> testMatches()
    {
        return IntStream.rangeClosed(1, 22).mapToObj(x -> "test" + x).map(x -> DynamicTest.dynamicTest(x, () -> {
            String inputFile = Helpers.loadFile("assets/" + x + ".sc");
            String outputFile = Helpers.loadFile("assets/" + x + ".s");
            assertEquals(outputFile, Compiler.INSTANCE.compile(inputFile));
        }));
    }

    @TestFactory
    Stream<DynamicTest> testExceptions()
    {
        return IntStream.rangeClosed(1, 4).mapToObj(x -> "exc" + x).map(x -> DynamicTest.dynamicTest(x, () -> {
            String inputFile = Helpers.loadFile("assets/" + x + ".sc");
            InvalidAssemblyException t = assertThrows(InvalidAssemblyException.class, () -> Compiler.INSTANCE.compile(inputFile));
        }));
    }
}