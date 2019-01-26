package compiler.component;

public class ComponentStatic implements INamedComponent
{
    private final String result;
    private final String registerWrite;
    private final Type type;
    private final String label;

    public ComponentStatic(String result)
    {
        this(result, "", Type.SUB);
    }

    public ComponentStatic(String result, String registerWrite)
    {
        this(result, registerWrite, Type.SUB);
    }

    public ComponentStatic(String result, String registerWrite, Type type)
    {
        this.type = type;
        this.result = result;
        this.registerWrite = registerWrite;
        this.label = "";
    }

    public ComponentStatic(String result, String registerWrite, String label)
    {
        this.type = Type.SUB;
        this.result = result;
        this.registerWrite = registerWrite;
        this.label = label;
    }

    @Override
    public Type getType()
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

    @Override
    public String getLabel()
    {
        return label;
    }
}
