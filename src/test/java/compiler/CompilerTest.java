/*
 * Part of AssemblyCompiler
 * Copyright (c) 2019 - 2019 Alex O'Neill
 * See the project LICENCE.md for more information
 */

package compiler;


import java.util.stream.IntStream;
import java.util.stream.Stream;

import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompilerTest
{
    @TestFactory
    Stream<DynamicTest> testMatches()
    {
        return IntStream.rangeClosed(1, 26).mapToObj(x -> "test" + x).map(x -> DynamicTest.dynamicTest(x, () -> {
            String inputFile = Helpers.loadResource("sources/" + x + ".s");
            String outputFile = Helpers.loadResource("results/" + x + ".s");
            assertEquals(outputFile, AssemblyCompiler.INSTANCE.compile(inputFile));
        }));
    }

    @TestFactory
    Stream<DynamicTest> testExceptions()
    {
        return IntStream.rangeClosed(1, 5).mapToObj(x -> "exc" + x).map(x -> DynamicTest.dynamicTest(x, () -> {
            String inputFile = Helpers.loadResource("fails/" + x + ".s");
            assertThrows(InvalidAssemblyException.class, () -> AssemblyCompiler.INSTANCE.compile(inputFile));
        }));
    }
}