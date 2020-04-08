package calculator;

public class Error extends Value {

    public enum ErrorCode {
        SYNTAX_ERROR,
        NUMBER_FORMAT_ERROR,
        DIVIDE_BY_0,
        INCORRECT_OPERATOR,
        UNIMPLEMENTED_OPERATION,
        UNKNOWN_CONSTANT,
        MISMATCHED_PARENTHESES,
        INTERNAL_ERROR;
    }

    private ErrorCode errorCode;
    private String problematicString;

    public Error(ErrorCode e, String problem) {
        super(-1);
        errorCode = e;
        problematicString = problem;
    }

    public Error(ErrorCode e, Object problem) {
        this(e, problem.toString());
    }

    public String getProblematicString() {
        return problematicString;
    }

    @Override
    public String toString() {
        return errorCode.toString() + "\nOccurred at: \"" + problematicString + "\"";
    }
}
