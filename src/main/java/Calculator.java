import calculator.Value;
import calculator.Error;
import calculator.expression.Expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static calculator.expression.Expression.tokens2Expression;
import static calculator.parsing.Token.tokenize;

public class Calculator {

    private static final ArrayList<String> exitAliasList = new ArrayList<>(Arrays.asList("exit", "quit", "stop", "bye", "goodbye", "fuck off"));
    private static final ArrayList<String> helpAliasList = new ArrayList<>(Arrays.asList("help", "info", "wtf", "what", "what the fuck", "nani"));

    private static Value previous = new Value(0);

    private static Boolean isExplicit = false;
    private static Boolean onlyResult = false;
    private static Boolean debug = false;

    public static void main(String[] args) {
        if(args.length > 0) {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < args.length; i++) {
                switch(args[i]) {
                    case "-explicit": isExplicit = true; break;
                    case "-no-equals": onlyResult = true; break;
                    case "-debug" : debug = true; break;
                    default: sb.append(args[i]);
                }
            }
            if(sb.length() > 0) {
                handleLine(sb.toString());
                System.exit(0);
            }
        }

        Scanner s = new Scanner(System.in);
        String nextLine;
        while(s.hasNextLine()) {
            nextLine = s.nextLine();
            handleLine(nextLine);
        }
    }

    private static void handleLine(String nextLine) {
        if(indicatesExit(nextLine)) System.exit(0);
        if(indicatesHelp(nextLine)) {
            printHelp();
            return;
        }
        Expression e = tokens2Expression(tokenize(nextLine), previous);
        if(isExplicit) System.out.println(e);
        Value v = e.eval();
        if(!(v instanceof Error)) previous = v;
        if(!onlyResult) System.out.print(" = ");
        System.out.println(v);
    }

    private static Boolean indicatesExit(String s) {
        return exitAliasList.contains(s);
    }

    private static Boolean indicatesHelp(String s) {
        return helpAliasList.contains(s);
    }

    private static void printHelp() {
        System.out.println("Calculator by Richard Westerhof, 2020\n" +
                "This calculator can evaluate expressions using operators +, -, *, /, %, ^, (, ), and constant names such as 'pi' or 'e'\n" +
                "The constant name 'ans' is special, as it is equal to the value of the previous expression, or 0 if no previous calculation was done\n" +
                "If the previous calculation resulted in an error, the value of 'ans' will not be changed\n" +
                "Decimal values are specified using a dot '.', use of commas is NOT supported in any way!\n" +
                "A multiplication sign '*' before an opening parenthesis '(', constant 'pi' or function name 'sin' may be omitted, i.e. 2(1+1) is equivalent to 2*(1+1)\n" +
                "Whitespace is not necessary, unless it is used to separate two names, i.e. 'sin pi' requires a space, but 'sin3.14' does not\n" +
                "List of commands with their aliases:\n" +
                exitAliasList + ": exits the program\n" +
                helpAliasList + ": shows this help text\n" +
                "List of supported flags:\n" +
                "-explicit: shows the interpreted expression before evaluating, useful for checking order of application, desugaring etc.\n" +
                "-no-equals: omits the equals sign '=' before the result, useful for easy parsing of output\n" +
                "-debug: enables debugging info at several steps during the parsing and evaluation of expressions");
    }
}
