package compiler.keyword;

import compiler.util.IComponentManagerStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeywordCallTest
{
    private IKeyword keyword;
    private IComponentManagerStub stub;

    @BeforeEach
    void setUp()
    {
        keyword = new KeywordCall();
        stub = new IComponentManagerStub();
    }

    @Test
    void matches()
    {
        assertTrue(keyword.matches("call", new StringBuilder(" someFunction")));
        assertFalse(keyword.matches("call", new StringBuilder("otherFunction")));
    }

    @Test
    void apply()
    {
        keyword.apply("call", new StringBuilder(" someFunction;"), stub);
        assertEquals("    call            someFunction\n", stub.compile());
    }
}