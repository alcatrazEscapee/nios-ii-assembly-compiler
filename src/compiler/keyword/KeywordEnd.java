package compiler.keyword;

import compiler.component.IComponentManager;
import compiler.util.InvalidAssemblyException;

public class KeywordEnd implements IKeyword
{
    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, "end");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        if (compiler.getCurrent() != null)
        {
            compiler.addComponent(compiler.getCurrent());
        }
        else
        {
            throw new InvalidAssemblyException("Unexpected 'end' when parsing, not in a function.");
        }
    }
}
