package calculator.expression;

public enum Operator {
    PLUS(1, OperatorType.TWO_ARGS),
    MINUS(1, OperatorType.TWO_ARGS),
    TIMES(2, OperatorType.TWO_ARGS),
    DIVIDE(2, OperatorType.TWO_ARGS),
    MOD(2, OperatorType.TWO_ARGS),
    POW(3, OperatorType.TWO_ARGS),
    OPEN_PARENTHESIS(100, OperatorType.NO_ARGS),
    CLOSE_PARENTHESIS(100, OperatorType.NO_ARGS),
    FUNCTION(7, OperatorType.VAR_ARGS),
    NUMBER(8, OperatorType.NO_ARGS),

    NONE(Integer.MAX_VALUE, OperatorType.NO_ARGS);

    public enum OperatorType {
        NO_ARGS,
        TWO_ARGS,
        VAR_ARGS,
    }

    private static final long PARENTHESES_OFFSET = 100;

    private long priority;
    private OperatorType operatorType;

    private Operator(long priority, OperatorType ot) {
        this.priority = priority;
        this.operatorType = ot;
    }

    public static Boolean isLessImp(Operator a, int aParenthesesOffset, Operator b, int bParenthesesOffset) {
        if(a == NONE) return false;
        if(b == NONE) return true;
        //functions have priority from right to left
        if(a == FUNCTION || b == FUNCTION)
            return a.priority + aParenthesesOffset * PARENTHESES_OFFSET <
                   b.priority + bParenthesesOffset * PARENTHESES_OFFSET;
        //everything else has priority from left to right
        return a.priority + aParenthesesOffset * PARENTHESES_OFFSET <=
               b.priority + bParenthesesOffset * PARENTHESES_OFFSET;
    }

    public static Operator toOperator(String s) {
        switch(s) {
            case "+": return PLUS;
            case "-": return MINUS;
            case "*": return TIMES;
            case "/": return DIVIDE;
            case "%": return MOD;
            case "^": return POW;
            case "(": return OPEN_PARENTHESIS;
            case ")": return CLOSE_PARENTHESIS;
            default: return toComplexOperator(s);
        }
    }

    private static Operator toComplexOperator(String s) {
        if(s.length() < 1) return NONE;
        s = s.toLowerCase();
        if(s.charAt(0) >= 'a' && s.charAt(0) <= 'z') return FUNCTION;
        if((s.charAt(0) >= '0' && s.charAt(0) <= '9') || s.charAt(0) == '-') return NUMBER;
        return NONE;
    }

    public OperatorType operatorType() {
        return operatorType;
    }

}
