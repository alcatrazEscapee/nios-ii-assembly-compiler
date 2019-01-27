package compiler.keyword;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import compiler.component.ComponentStatic;
import compiler.component.ComponentVariable;
import compiler.component.IComponent;
import compiler.component.IComponentManager;
import compiler.util.Helpers;
import compiler.util.InvalidAssemblyException;

public class KeywordVariable extends AbstractKeyword
{
    private static final Set<String> VARIABLE_KEYWORDS = new HashSet<>(Arrays.asList("var", "int", "byte", "string", "const"));

    @Override
    public boolean matches(String keyword, StringBuilder inputBuilder)
    {
        for (String var : VARIABLE_KEYWORDS)
        {
            if (IKeyword.matchKeyword(keyword, inputBuilder, var))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void apply(String keyword, StringBuilder inputBuilder, IComponentManager compiler)
    {
        if (keyword.equals("int") || keyword.equals("byte"))
        {
            StringBuilder source = Helpers.nextLine(inputBuilder);
            // Variables with defined sizes
            String varName = getArg(source, "=");
            if (source.length() == 0)
            {
                // Declaration with no assignment
                int size = keyword.equals("byte") ? 1 : 4;
                boolean aligned = size == 4;

                compiler.addComponent(new ComponentVariable(varName + ":\n" +
                        IComponent.format(".skip", size + "\n"), aligned));
            }
            else if (source.charAt(0) == '=')
            {
                source.deleteCharAt(0);
                String vars = source.toString().replace(",", ", ");
                if (keyword.equals("byte"))
                {
                    compiler.addComponent(new ComponentVariable(varName + ":\n" +
                            IComponent.format(".byte", vars + "\n"), false));
                }
                else
                {
                    compiler.addComponent(new ComponentVariable(varName + ":\n" +
                            IComponent.format(".word", vars + "\n"), true));
                }
            }
            else
            {
                throw new InvalidAssemblyException("Unexpected token " + source.toString());
            }
        }
        else if (keyword.equals("var"))
        {
            StringBuilder source = Helpers.nextLine(inputBuilder);
            if (source.charAt(0) != '[')
            {
                throw new InvalidAssemblyException("Unexpected token, expecting '[' " + source.toString());
            }
            // Remove open bracket
            source.deleteCharAt(0);
            String varSize = getArg(source, "]");

            if (source.charAt(0) != ']')
            {
                throw new InvalidAssemblyException("Unexpected token, expecting ']' " + source);
            }

            // Remove close bracket
            source.deleteCharAt(0);

            String rhs = getArg(source, ALL);
            if (source.length() != 0)
            {
                throw new InvalidAssemblyException("unexpected token, expecting ';' " + source);
            }

            compiler.addComponent(new ComponentVariable(rhs + ":\n" +
                    IComponent.format(".skip", varSize + "\n"), false));
        }
        else if (keyword.equals("string"))
        {
            // need to not ignore spaces!
            StringBuilder source = Helpers.nextLine(inputBuilder, ';', true);
            Helpers.advanceToNextWord(source);
            String varName = getArg(source, "=", " ");
            Helpers.advanceToNextWord(source);
            if (source.length() == 0)
            {
                throw new InvalidAssemblyException("Can't declare a string type variable without assignment");
            }
            if (source.charAt(0) != '=')
            {
                throw new InvalidAssemblyException("Unknown token (expecting '=') at " + source);
            }

            source.deleteCharAt(0);
            Helpers.advanceToNextWord(source);
            if (source.charAt(0) != '\"')
            {
                throw new InvalidAssemblyException("Unknown token (expecting '\"') at " + source);
            }

            source.deleteCharAt(0);
            String varValue = getArg(source, "\"");

            compiler.addComponent(new ComponentVariable(varName + ":\n" +
                    IComponent.format(".asciz", "\"" + varValue + "\"\n"), false));
        }
        else if (keyword.equals("const"))
        {
            StringBuilder source = Helpers.nextLine(inputBuilder);
            String varName = getArg(source, "=");
            if (source.length() == 0 || source.charAt(0) != '=')
            {
                throw new InvalidAssemblyException("Unexpected token at " + varName + source);
            }
            // Remove '='
            source.deleteCharAt(0);

            IComponent cmp = compiler.getComponent(IComponent.Type.COMPILE);
            String result = IComponent.format(".equ", String.format("%s, %s\n", varName, source));

            compiler.addConstant(varName, source.toString());
            cmp.add(new ComponentStatic(result));
        }
    }
}
