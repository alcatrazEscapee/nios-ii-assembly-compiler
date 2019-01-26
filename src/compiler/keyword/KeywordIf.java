package compiler.keyword;

import java.util.Stack;

import compiler.component.ComponentStatic;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.keyword.regex.RegisterExpressions;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;

public class KeywordIf extends AbstractKeyword
{
    private int counter = 0;

    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, "if");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        StringBuilder source = Helpers.nextLine(inputBuilder, ':', false);
        IComponent parent = compiler.getComponent("current");
        Stack<IComponent> controlStack = compiler.getControlStack();

        String lhs = getArg(source, COMPARATORS);
        if (!REGISTERS.contains(lhs))
        {
            throw new InvalidAssemblyException("Unable to do an if statement with LHS not a register: " + lhs + source);
        }

        String op = getOp(source, COMPARATORS);
        if (op.equals(""))
        {
            throw new InvalidAssemblyException("Unknown comparison operator " + source);
        }

        String rhs = getArg(source, ":");
        if (!REGISTERS.contains(rhs))
        {
            throw new InvalidAssemblyException("Unable to do an if statement with RHS not a register: " + rhs + source);
        }

        String label = "_if" + (++counter);
        String result = RegisterExpressions.ofCompInverted(lhs, rhs, op, label);
        parent.add(new ComponentStatic(result));
        controlStack.add(new ComponentStatic(label + ":\n", "", label));
    }

    @Override
    public void reset()
    {
        this.counter = 0;
    }
}
