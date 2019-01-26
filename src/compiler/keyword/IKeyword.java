package compiler.keyword;

import compiler.component.IComponentManager;

public interface IKeyword
{
    static boolean matchKeyword(String keyword, StringBuilder inputBuilder, String keywordMatcher)
    {
        if (keyword.equals(keywordMatcher))
        {
            if (inputBuilder.length() == 0)
            {
                return true;
            }
            char c = inputBuilder.charAt(0);
            return c != '_' && !Character.isLetterOrDigit(c);
        }
        return false;
    }

    boolean matches(String keyword, StringBuilder inputBuilder);

    void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler);

    default void reset() {}
}
