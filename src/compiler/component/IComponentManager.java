package compiler.component;

public interface IComponentManager
{
    /**
     * Adds a component directly to the manager
     * Should only be called with specific type components, or upon reaching an 'end' keyword
     *
     * @param component the component to add
     */
    void addComponent(IComponent component);

    /**
     * Gets the current component. Used to add instructions to a function via IComponent#add
     * @return the current component
     */
    IComponent getCurrent();

    /**
     * Sets the current component - i.e. a main or function
     * At some point, it must call IComponentManager#add
     * @param component a component to add
     */
    void setCurrent(IComponent component);
}
