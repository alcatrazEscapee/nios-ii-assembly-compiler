package compiler.keyword;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import compiler.component.ComponentStatic;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;
import compiler.util.RegisterExpressions;

public class KeywordWhile extends AbstractKeyword
{
    private Map<String, Integer> counter = new HashMap<>();

    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return IKeyword.matchKeyword(keyword, inputBuilder, "while");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        StringBuilder source = Helpers.nextLine(inputBuilder, ':', false);
        IComponent parent = compiler.getComponent(IComponent.Type.CURRENT);
        Stack<IComponent> controlStack = compiler.getControlStack();

        String lhs = getArg(source, COMPARATORS);
        if (!REGISTERS.contains(lhs))
        {
            if (lhs.equals("true"))
            {
                // get the counter for this function
                String functionName = parent.getFlag();
                int value = counter.getOrDefault(functionName, 1);

                String label = "_while" + value;
                String result = IComponent.format("br", label) + "\n";
                parent.add(new ComponentStatic(label + ":\n", label));
                controlStack.add(new ComponentStatic(result));

                // Increment the counter in the map
                counter.put(functionName, value + 1);
                return;
            }
            throw new InvalidAssemblyException("Unable to do an while statement with LHS not a register: " + lhs + source);
        }

        String op = getOp(source, COMPARATORS);
        if (op.equals(""))
        {
            throw new InvalidAssemblyException("Unknown comparison operator " + source);
        }

        String rhs = getArg(source, ":");
        if (!REGISTERS.contains(rhs))
        {
            throw new InvalidAssemblyException("Unable to do an while statement with RHS not a register: " + rhs + source);
        }

        // This is almost identical to the if statement logic, except the component placement is reversed (label first, break after)
        // get the counter for this function
        String functionName = parent.getFlag();
        int value = counter.getOrDefault(functionName, 1);

        String label = functionName + "_while" + value;
        String result = RegisterExpressions.ofComp(lhs, rhs, op, label);
        parent.add(new ComponentStatic(label + ":\n", label));
        controlStack.add(new ComponentStatic(result));

        // Increment the counter in the map
        counter.put(functionName, value + 1);
    }

    @Override
    public void reset()
    {
        this.counter.clear();
    }
}
