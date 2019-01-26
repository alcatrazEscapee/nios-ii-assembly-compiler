package compiler;

import compiler.util.Helpers;

public class Main
{
    public static void main(String[] args)
    {
        String input = Helpers.loadFile("assets/test1.sc");
        String output = Compiler.INSTANCE.compile(input);
        System.out.println("OUTPUT:\n\n" + output);
    }
}
