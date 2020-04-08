package calculator.expression;

public enum Operator {
    PLUS(1),
    MINUS(1),
    TIMES(2),
    DIVIDE(2),
    MOD(2),
    POW(3),
    OPEN_PARENTHESES(100),
    CLOSE_PARENTHESES(100),
    FUNCTION(100),

    NONE(Integer.MAX_VALUE);

    private static final int PARENTHESES_OFFSET = 10;

    private int priority;

    private Operator(int priority) {
        this.priority = priority;
    };

    public static Boolean isLessImp(Operator a, int aParenthesesOffset, Operator b, int bParenthesesOffset) {
        return a.priority + aParenthesesOffset * PARENTHESES_OFFSET <= b.priority + bParenthesesOffset * PARENTHESES_OFFSET;
    }

    public static Operator toOperator(String s) {
        switch(s) {
            case "+": return PLUS;
            case "-": return MINUS;
            case "*": return TIMES;
            case "/": return DIVIDE;
            case "%": return MOD;
            case "^": return POW;
            case "(": return OPEN_PARENTHESES;
            case ")": return CLOSE_PARENTHESES;
            default: return NONE;
        }
    }

}
