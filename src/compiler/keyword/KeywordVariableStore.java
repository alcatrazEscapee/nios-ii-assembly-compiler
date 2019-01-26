package compiler.keyword;

import compiler.component.ComponentStatic;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;

public class KeywordVariableStore extends AbstractKeyword
{
    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        return keyword.endsWith("=") || keyword.equals("&");
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        StringBuilder source = Helpers.nextLine(inputBuilder);
        IComponent parent = compiler.getComponent("current");
        boolean byteFlag = false, ioFlag = false;

        if (keyword.equals("&"))
        {
            String lhs = getArg(source, "=", "[");
            if (!REGISTERS.contains(lhs))
            {
                throw new InvalidAssemblyException("Unable to do variable store with pointer to not a register " + lhs);
            }
            if (source.length() == 0)
            {
                throw new InvalidAssemblyException("Unable to do variable store without assignment " + lhs + source);
            }

            String offset = "0";
            if (source.charAt(0) == '[')
            {
                // Delete leading '['
                source.deleteCharAt(0);
                offset = getArg(source, "]");
                // Delete ending ']'
                source.deleteCharAt(0);
            }

            // Delete '='
            System.out.println("source " + source);
            source.deleteCharAt(0);
            String rhs = getArg(source);

            // Casting flags
            if (rhs.length() >= 4 && rhs.startsWith("(io)"))
            {
                ioFlag = true;
                rhs = rhs.substring(4);
            }
            if (rhs.length() >= 6 && rhs.startsWith("(byte)"))
            {
                byteFlag = true;
                rhs = rhs.substring(6);
            }
            if (rhs.length() >= 8 && rhs.startsWith("(byteio)"))
            {
                byteFlag = ioFlag = true;
                rhs = rhs.substring(8);
            }
            if (!REGISTERS.contains(rhs))
            {
                throw new InvalidAssemblyException("Unable to do variable store from not a register " + rhs + " " + source);
            }
            String cmd = makeStore(byteFlag, ioFlag);
            String result = IComponent.format(cmd, String.format("%s, %s(%s)\n", rhs, offset, lhs));
            parent.add(new ComponentStatic(result));

        }
        else
        {
            String varName = keyword.substring(0, keyword.length() - 1).replace(" ", "");
            String rhs = getArg(source, ALL);

            // Casting flags
            if (rhs.length() >= 4 && rhs.startsWith("(io)"))
            {
                ioFlag = true;
                rhs = rhs.substring(4);
            }
            if (rhs.length() >= 6 && rhs.startsWith("(byte)"))
            {
                byteFlag = true;
                rhs = rhs.substring(6);
            }
            if (rhs.length() >= 8 && rhs.startsWith("(byteio)"))
            {
                byteFlag = ioFlag = true;
                rhs = rhs.substring(8);
            }
            System.out.println("Got var name " + varName + " and lhs " + rhs);

            // variable = rX
            String cmd = makeStore(byteFlag, ioFlag);
            parent.add(new ComponentStatic(IComponent.format(cmd, rhs + ", " + varName + "(r0)\n")));
        }
    }

    private String makeStore(boolean byteFlag, boolean ioFlag)
    {
        return "st" + (byteFlag ? "b" : "w") + (ioFlag ? "io" : "");
    }
}
