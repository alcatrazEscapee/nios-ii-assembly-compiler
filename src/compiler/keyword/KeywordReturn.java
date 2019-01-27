package compiler.keyword;

import compiler.component.ComponentStatic;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;

public class KeywordReturn implements IKeyword
{
    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, "return");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        StringBuilder source = Helpers.nextLine(inputBuilder);
        IComponent parent = compiler.getComponent(IComponent.Type.CURRENT);
        if (parent == null)
        {
            throw new InvalidAssemblyException("Unexpected return outside of function");
        }
        System.out.println("SAUCY " + source);
        if (source.length() == 0)
        {
            // Add a default return
            String functionName = parent.getFlag();
            String result = IComponent.format("br", functionName + "_ret\n");
            parent.add(new ComponentStatic(result, "return"));
        }
    }
}
