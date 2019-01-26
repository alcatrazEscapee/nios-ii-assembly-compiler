package compiler.component;

public class ComponentStatic implements IComponent
{
    private final String result;
    private final String registerWrite;
    private final String type;

    public ComponentStatic(String result)
    {
        this(result, "", "sub");
    }

    public ComponentStatic(String result, String registerWrite)
    {
        this(result, registerWrite, "sub");
    }

    public ComponentStatic(String result, String registerWrite, String type)
    {
        this.type = type;
        this.result = result;
        this.registerWrite = registerWrite;
    }

    @Override
    public String getType()
    {
        return type;
    }

    @Override
    public String compile()
    {
        return result;
    }

    @Override
    public String getWriteRegister()
    {
        return registerWrite;
    }
}
