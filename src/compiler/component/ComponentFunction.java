package compiler.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComponentFunction extends AbstractComponent
{
    private final String functionName;
    private final boolean noReturnValue;

    public ComponentFunction(String functionName, boolean noReturnValue)
    {
        this.functionName = functionName;
        this.noReturnValue = noReturnValue;
    }

    @Override
    public String getType()
    {
        return "function";
    }

    @Override
    public String compile()
    {
        StringBuilder output = new StringBuilder();
        output.append(String.format("\n# ========== %s ==========\n", functionName));
        output.append(functionName).append(":\n");

        // Get the list of all register writes
        List<String> registerWrites = new ArrayList<>();
        for (IComponent cmp : components)
        {
            String write = cmp.getWriteRegister();
            if (!write.equals("") && (!write.equals("r2") || noReturnValue))
            {
                if (!registerWrites.contains(write))
                {
                    registerWrites.add(write);
                }
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

        // Add the addi / ldw commands at the header
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
}
