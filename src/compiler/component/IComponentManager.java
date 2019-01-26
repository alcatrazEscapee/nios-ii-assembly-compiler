package compiler.component;

public interface IComponentManager
{
    void addComponent(IComponent component);

    IComponent getCurrent();

    void setCurrent(IComponent component);
}
