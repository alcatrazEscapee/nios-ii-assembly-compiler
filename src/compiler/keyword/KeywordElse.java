package compiler.keyword;

import java.util.Stack;

import compiler.component.ComponentStatic;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.util.InvalidAssemblyException;

public class KeywordElse extends AbstractKeyword
{
    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, "else");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        // Optional colon
        if (inputBuilder.length() > 0 && inputBuilder.charAt(0) == ':')
        {
            inputBuilder.deleteCharAt(0);
        }

        IComponent parent = compiler.getCurrent();
        Stack<IComponent> controlStack = compiler.getControlStack();
        if (parent == null)
        {
            throw new InvalidAssemblyException("Unexpected 'else' outside of a function " + inputBuilder);
        }
        if (controlStack.isEmpty() || !controlStack.peek().getType().startsWith("_if"))
        {
            throw new InvalidAssemblyException("Unknown element on control stack, expected '_if'");
        }
        IComponent componentIf = controlStack.pop();
        String labelIf = componentIf.getType();
        String labelElse = "_else" + labelIf.substring(3);

        controlStack.add(new ComponentStatic(labelElse + ":\n", "", labelElse));
        parent.add(new ComponentStatic(IComponent.format("br", labelElse + "\n")));
        parent.add(componentIf);
    }
}
