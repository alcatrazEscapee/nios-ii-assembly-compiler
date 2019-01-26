package compiler.keyword;

import compiler.component.ComponentCompile;
import compiler.component.IComponentManager;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;

public class KeywordCompile implements IKeyword
{
    private static final String COMPILE_KEYWORD = "compile";
    private static final String NIOS_II_DE0 = "nios-ii de0";

    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, COMPILE_KEYWORD);
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        Helpers.advanceToNextWord(inputBuilder);
        // Match the compile flag
        if (inputBuilder.substring(0, 11).equals(NIOS_II_DE0))
        {
            inputBuilder.delete(0, 11);
            compiler.addComponent(new ComponentCompile());
        }
        else
        {
            throw new InvalidAssemblyException("Invalid compile specification");
        }
    }
}
