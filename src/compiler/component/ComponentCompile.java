package compiler.component;

import compiler.Compiler;

public class ComponentCompile extends AbstractComponent
{
    @Override
    public String getType()
    {
        return "compile";
    }

    @Override
    public String compile()
    {
        StringBuilder output = new StringBuilder();

        output.append(IComponent.format(".equ", "LAST_RAM_WORD, 0x007FFFFC\n"));

        for (IComponent cmp : components)
        {
            output.append(cmp.compile());
        }

        output.append(IComponent.format(".global", "_start\n"));
        output.append(IComponent.format(".org", "0x00000000\n"));
        output.append("\t.text\n\n");
        return output.toString();
    }

    @Override
    public void add(IComponent sub)
    {
        // Unpack the define statement into name + value:
        String[] parts = sub.getType().split("@");
        Compiler.INSTANCE.registerConstant(parts[0], parts[1]);
        super.add(sub);
    }
}
