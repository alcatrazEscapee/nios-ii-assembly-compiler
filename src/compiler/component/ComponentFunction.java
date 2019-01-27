package compiler.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import compiler.keyword.IKeyword;

public class ComponentFunction extends AbstractComponent
{
    private final String functionName;
    private final String functionPrefix;
    private final boolean noReturnValue;

    public ComponentFunction(String functionName, boolean noReturnValue)
    {
        this.functionName = functionName;
        this.functionPrefix = functionName.replaceAll("[a-z]", "").toLowerCase();
        this.noReturnValue = noReturnValue;

    }

    @Override
    public Type getType()
    {
        return Type.FUNCTION;
    }

    @Override
    public String compile()
    {
        StringBuilder output = new StringBuilder();
        boolean returnFlag = false;
        output.append(String.format("\n# ========== %s ==========\n", functionName));
        output.append(functionName).append(":\n");

        // Get the list of all register writes
        List<String> registerWrites = new ArrayList<>();
        for (IComponent cmp : components)
        {
            String flag = cmp.getFlag();
            if (IKeyword.REGISTERS.contains(flag) && (!flag.equals("r2") || noReturnValue))
            {
                if (!registerWrites.contains(flag))
                {
                    registerWrites.add(flag);
                }
            }
            else if (flag.equals("return"))
            {
                returnFlag = true;
            }
        }
        Collections.sort(registerWrites);
        int maxSize = registerWrites.size() * 4;

        // Add the subi / stw commands at the header
        if (!registerWrites.isEmpty())
        {
            int size = maxSize;
            output.append(IComponent.format("subi", "sp, sp, " + maxSize + "\n"));
            for (String write : registerWrites)
            {
                size -= 4;
                output.append(IComponent.format("stw", write + ", " + size + "(sp)\n"));
            }
            output.append("\n");
        }

        // Add the body of the function
        for (IComponent cmp : components)
        {
            output.append(cmp.compile());
        }

        // If necessary, add a return label
        if (returnFlag)
        {
            output.append(functionName).append("_ret:\n");
        }

        // Add the addi / ldw commands at the footer
        if (!registerWrites.isEmpty())
        {
            output.append("\n");
            int size = maxSize;
            for (String write : registerWrites)
            {
                size -= 4;
                output.append(IComponent.format("ldw", write + ", " + size + "(sp)\n"));
            }
            output.append(IComponent.format("addi", "sp, sp, " + maxSize + "\n"));
        }

        // Add the return command
        output.append("\tret\n");
        return output.toString();
    }

    @Override
    public String getFlag()
    {
        return functionPrefix;
    }
}
