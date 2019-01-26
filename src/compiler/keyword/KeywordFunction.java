package compiler.keyword;

import compiler.component.ComponentFunction;
import compiler.component.IComponentManager;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;

public class KeywordFunction extends AbstractKeyword
{
    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, "function");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        StringBuilder source = Helpers.nextLine(inputBuilder, ':', false);
        if (source.length() == 0)
        {
            throw new InvalidAssemblyException("Function name must be non empty");
        }
        compiler.setCurrent(new ComponentFunction(source.toString()));

    }
}
