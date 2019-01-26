package compiler.keyword;

import compiler.component.ComponentStatic;
import compiler.component.IComponentManager;

public class KeywordComment implements IKeyword
{
    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return keyword.equals("//");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        // todo: add comment to output somehow?
        compiler.getCurrent().add(new ComponentStatic("\t#" + inputBuilder + "\n"));
    }
}
