package compiler.keyword;

import compiler.util.IComponentManagerStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeywordVariableStoreTest
{
    private IKeyword keyword;
    private IComponentManagerStub stub;

    @BeforeEach
    void setUp()
    {
        keyword = new KeywordVariableStore();
        stub = new IComponentManagerStub();
    }

    @Test
    void matches()
    {
        assertTrue(keyword.matches("*", new StringBuilder("r0 = r1;")));
        assertTrue(keyword.matches("someVar=", new StringBuilder("r1;")));

        assertFalse(keyword.matches("r3=", new StringBuilder("r4;")));
    }

    @Test
    void apply1()
    {
        // VAR = (cast) rX
        keyword.apply("test=", new StringBuilder(" r2;"), stub);
        assertEquals("    stw             r2, test(r0)\n", stub.compile());
    }

    @Test
    void apply2()
    {
        // VAR = (cast) rX
        keyword.apply("A=", new StringBuilder("(byteio) r2;"), stub);
        assertEquals("    stbio           r2, A(r0)\n", stub.compile());
    }

    @Test
    void apply3()
    {
        // *rX = (cast) rY
        keyword.apply("*", new StringBuilder("r3 = (io) r6"), stub);
        assertEquals("    stwio           r6, 0(r3)\n", stub.compile());
    }

    @Test
    void apply4()
    {
        // *rX[OFF] = (cast) rY
        keyword.apply("*", new StringBuilder(" r5[test] = (byte) r2;"), stub);
        assertEquals("    stb             r2, test(r5)\n", stub.compile());
    }
}