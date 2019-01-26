package compiler.component;


public class ComponentCompile implements IComponent
{
    @Override
    public String getType()
    {
        return "compile";
    }

    @Override
    public String compile()
    {
        return IComponent.format(".equ", "LAST_RAM_WORD, 0x007FFFFC\n") +
                IComponent.format(".global", "_start\n") +
                IComponent.format(".org", "0x00000000\n") +
                "\t.text\n\n";
    }
}
