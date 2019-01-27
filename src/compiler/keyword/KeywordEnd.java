package compiler.keyword;

import java.util.Stack;

import compiler.component.IComponent;
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
        // You can put semicolons at the end of an 'end' if you want to
        if (inputBuilder.length() > 0 && inputBuilder.charAt(0) == ';')
        {
            inputBuilder.deleteCharAt(0);
        }

        IComponent parent = compiler.getComponent(IComponent.Type.CURRENT);
        if (parent == null)
        {
            throw new InvalidAssemblyException("Unexpected 'end' when parsing " + inputBuilder);
        }

        Stack<IComponent> controlStack = compiler.getControlStack();
        if (!controlStack.isEmpty())
        {
            parent.add(controlStack.pop());
        }
        else
        {
            compiler.addComponent(parent);
            compiler.addComponent(IComponent.Type.CURRENT, null);
        }
    }
}
