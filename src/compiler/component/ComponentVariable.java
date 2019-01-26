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
    public String getType()
    {
        return "variable";
    }

    @Override
    public String compile()
    {
        return result;
    }
}
