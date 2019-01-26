package compiler.component;

public class ComponentMain extends AbstractComponent
{
    private static final String HEADER = "# Entry point\n" +
            "_start:\n" +
            IComponent.format("movia", "sp, LAST_RAM_WORD\n");
    private static final String FOOTER = "_end:\n" +
            IComponent.format("br", "_end\n");

    @Override
    public String getType()
    {
        return "main";
    }

    @Override
    public String compile()
    {
        StringBuilder output = new StringBuilder();
        output.append(HEADER);
        for (IComponent cmp : components)
        {
            output.append(cmp.compile());
        }
        output.append(FOOTER);
        return output.toString();
    }
}
