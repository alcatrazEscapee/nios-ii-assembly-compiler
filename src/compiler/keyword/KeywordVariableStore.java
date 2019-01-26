package compiler.keyword;

import compiler.component.ComponentStatic;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.util.Helpers;

public class KeywordVariableStore extends AbstractKeyword
{
    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return keyword.endsWith("=");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        StringBuilder source = Helpers.nextLine(inputBuilder);
        String varName = keyword.substring(0, keyword.length() - 1).replace(" ", "");
        System.out.println("Got var name " + varName + " and source " + source);

        // variable = rX
        // todo: more handling for indexes, offsets, etc.
        IComponent parent = compiler.getComponent("current");
        parent.add(new ComponentStatic(IComponent.format("stw", source + ", " + varName + "(r0)\n")));
    }
}
