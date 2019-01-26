package compiler.keyword;

import compiler.component.ComponentMain;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;

public class KeywordMain implements IKeyword
{
    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, "main");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        Helpers.advanceToNextWord(inputBuilder);
        if (inputBuilder.charAt(0) != ':')
        {
            throw new InvalidAssemblyException("Expected ':' after main declaration");
        }

        inputBuilder.deleteCharAt(0);
        compiler.addComponent(IComponent.Type.CURRENT, new ComponentMain());
    }
}
