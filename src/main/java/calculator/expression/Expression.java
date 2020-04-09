package calculator.expression;

import calculator.Error;
import calculator.Value;
import calculator.parsing.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static calculator.Constant.getConstantValue;
import static calculator.expression.Operator.*;
import static calculator.parsing.Token.isConstant;
import static calculator.parsing.Token.isKnownFunction;

public abstract class Expression {

    //potential extensions: gcd, lcm, isPrime, rand,
    public static final List<String> knownFunctions = Arrays.asList("sin", "asin", "cos", "acos", "tan", "atan", "abs", "ln", "log", "sqrt", "isPrime");

    public static Expression tokens2Expression(List<Token> tokens, Value ans) {
        if(tokens == null || tokens.size() == 0) {
            return new Value(0);
        }
        Operator leastImportant = NONE;

        int leastImpParenthesesOffset = 0;
        int leastImportantInd = -1;
        int currentParenthesesOffset = 0;

        int parenthesesStart = -1;
        int parenthesesEnd = -1;

        for(int i = 0; i < tokens.size(); i++) {
            Token tokenI = tokens.get(i);
            //System.out.println(tokenI);
            Operator op = toOperator(tokenI.value());

            if(tokenI.tokenType().equals(Token.TokenType.OPEN_PARENTHESES)) {
                if(currentParenthesesOffset == 0) parenthesesStart = i;
                currentParenthesesOffset++;
            }
            else if(tokenI.tokenType().equals(Token.TokenType.CLOSE_PARENTHESES)) {
                currentParenthesesOffset--;
                if(currentParenthesesOffset == 0) parenthesesEnd = i;
            }
            else if(op != NONE && isLessImp(op, currentParenthesesOffset, leastImportant, leastImpParenthesesOffset)) {
                leastImportant = op;
                leastImportantInd = i;
                leastImpParenthesesOffset = currentParenthesesOffset;
            }
        }

        if((parenthesesStart == -1 && parenthesesEnd > -1) || (parenthesesEnd == -1 && parenthesesStart > -1)) {
            return new Error(Error.ErrorCode.MISMATCHED_PARENTHESES, tokens);
        }

        //System.out.println("least important operator in " + tokens + ": " + leastImportant + " at index " + leastImportantInd);

        if(parenthesesStart > -1 && parenthesesEnd > -1 && (leastImportant == NUMBER ||
                (leastImportantInd > parenthesesStart && leastImportantInd < parenthesesEnd))) {
            tokens.remove(parenthesesEnd);
            tokens.remove(parenthesesStart);
            leastImportantInd--;
        }

        //now it is possible that the tokenlist is empty, i.e. the expression was only '()'
        if(tokens.size() == 0) {
            return new Error(Error.ErrorCode.SYNTAX_ERROR, "()");
        }

        switch(leastImportant.operatorType()) {
            //operator with no args
            case NO_ARGS: {
                return parseNoArgs(leastImportant, tokens.get(leastImportantInd), ans);
            }
            //operator with a lhs and rhs expression (+, -, *, /, %, ^)
            case TWO_ARGS: {
                //create the lhs and rhs of the expression
                Expression lhs = tokens2Expression(new ArrayList<>(tokens.subList(0, leastImportantInd)), ans);
                Expression rhs = tokens2Expression(new ArrayList<>(tokens.subList(leastImportantInd+1, tokens.size())), ans);
                return parseTwoArgs(leastImportant, lhs, rhs);
            }
            case VAR_ARGS: {
                ArrayList<Expression> arguments = new ArrayList<>();
                if(isKnownFunction(tokens.get(leastImportantInd).value())) {
                    arguments.add(tokens2Expression(new ArrayList<>(tokens.subList(1, tokens.size())), ans));
                }
                return parseVarArgs(leastImportant, tokens.get(leastImportantInd), arguments, ans);
            }

            default: return new Error(Error.ErrorCode.INTERNAL_ERROR, tokens);
        }
    }

    private static Expression parseNoArgs(Operator op, Token t, Value ans) {
        switch(op) {
            case NUMBER: {
                try {
                    return new Value(Double.parseDouble(t.value()));
                } catch(NumberFormatException e) {
                    return new Error(Error.ErrorCode.NUMBER_FORMAT_ERROR, t.value());
                }
            }
            //a function with no arguments is a constant
            case FUNCTION: {
                if(isConstant(t.value().charAt(0))) {
                    return getConstantValue(t.value(), ans);
                }
                return new Error(Error.ErrorCode.INTERNAL_ERROR, t);
            }
            default: return new Error(Error.ErrorCode.SYNTAX_ERROR, t);
        }
    }

    private static Expression parseTwoArgs(Operator op, Expression lhs, Expression rhs) {
        switch(op) {
            case PLUS: return new Addition(lhs, rhs);
            case MINUS: return new Subtraction(lhs, rhs);
            case TIMES: return new Multiplication(lhs, rhs);
            case DIVIDE: return new Division(lhs, rhs);
            case MOD: return new Modulo(lhs, rhs);
            case POW: return new Power(lhs, rhs);

            default: {
                return new Error(Error.ErrorCode.INTERNAL_ERROR, lhs.toString() + op.toString() + rhs.toString());
            }
        }
    }

    private static Expression parseVarArgs(Operator op, Token opName, ArrayList<Expression> arguments, Value ans) {
        if(arguments.size() == 0) return parseNoArgs(op, opName, ans);
        switch(op) {
            case FUNCTION: {
                return functionToExpression(opName.toString(), arguments);
            }
            default: return new Error(Error.ErrorCode.INTERNAL_ERROR, opName.toString() + arguments.toString());
        }
    }

    private static Expression functionToExpression(String funcName, ArrayList<Expression> arguments) {
        switch(funcName) {
            case "sin": {
                Error error = checkArgCount(funcName, arguments, 1);
                return error == null ? new Sine(arguments.get(0)) : error;
            }
            case "cos": {
                Error error = checkArgCount(funcName, arguments, 1);
                return error == null ? new Cosine(arguments.get(0)) : error;
            }


            case "sqrt": {
                Error error = checkArgCount(funcName, arguments, 1);
                return error == null ? new SquareRoot(arguments.get(0)) : error;
            }



            default: return new Error(Error.ErrorCode.UNIMPLEMENTED_OPERATION, funcName + arguments);
        }
    }

    private static Error checkArgCount(String funcName, ArrayList<Expression> arguments, int n) {
        if(arguments.size() < n) return new Error(Error.ErrorCode.TOO_FEW_ARGUMENTS, funcName + arguments);
        if(arguments.size() > n) return new Error(Error.ErrorCode.TOO_MANY_ARGUMENTS, funcName + arguments);
        return null;
    }


    public abstract Value eval();
}
