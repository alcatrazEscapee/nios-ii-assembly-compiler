package compiler.component;

public interface INamedComponent extends IComponent
{
    @Override
    default Type getType()
    {
        return Type.SUB;
    }

    String getLabel();
}
