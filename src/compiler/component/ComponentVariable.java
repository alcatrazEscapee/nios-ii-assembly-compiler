package compiler.component;

public class ComponentVariable implements IComponent
{
    private final String result;
    private final boolean isWordAligned;

    public ComponentVariable(String result, boolean isWordAligned)
    {
        this.result = result;
        this.isWordAligned = isWordAligned;
    }

    public boolean isWordAligned()
    {
        return isWordAligned;
    }

    @Override
    public Type getType()
    {
        return Type.VARIABLE;
    }

    @Override
    public String compile()
    {
        return result;
    }
}
