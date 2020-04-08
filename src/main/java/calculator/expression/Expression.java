package calculator.expression;

import calculator.Error;
import calculator.Value;
import calculator.parsing.Token;

import java.util.ArrayList;
import java.util.List;

import static calculator.Constant.getConstantValue;
import static calculator.expression.Operator.*;
import static calculator.parsing.Token.isConstant;

public abstract class Expression {

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
            else if(((i > 0 && tokens.get(i-1).tokenType().equals(Token.TokenType.OPERATOR)) || i == 0) && op == MINUS) {
                if(i > tokens.size() - 2) return new Error(Error.ErrorCode.SYNTAX_ERROR, tokens.get(i-1).value() + tokenI.value());
                tokens.set(i, new Token(Token.TokenType.NUMBER, "-" + tokens.get(i+1)));
                tokens.remove(i+1);
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

        if(parenthesesStart > -1 && parenthesesEnd > -1 && (leastImportant == NONE ||
                (leastImportantInd > parenthesesStart && leastImportantInd < parenthesesEnd))) {
            tokens.remove(parenthesesEnd);
            tokens.remove(parenthesesStart);
            leastImportantInd--;
        }

        //now it is possible that the tokenlist is empty, i.e. the expression was only '()'
        if(tokens.size() == 0) {
            return new Error(Error.ErrorCode.SYNTAX_ERROR, "()");
        }


        Expression lhs = null, rhs = null;
        if(leastImportant != NONE) {
            //create the lhs and rhs of the expression (now we assume that every operator has a lhs and rhs)
            lhs = tokens2Expression(new ArrayList<>(tokens.subList(0, leastImportantInd)), ans);
            rhs = tokens2Expression(new ArrayList<>(tokens.subList(leastImportantInd+1, tokens.size())), ans);
        }

        switch(leastImportant) {
            case NONE: {
                if(isConstant(tokens.get(0).value().charAt(0))) {
                    Value constant = getConstantValue(tokens.get(0).value(), ans);
                    if(constant != null) return constant;
                    return new Error(Error.ErrorCode.UNKNOWN_CONSTANT, tokens.get(0).value());
                }
                try {
                    return new Value(Double.parseDouble(tokens.get(0).value()));
                } catch(NumberFormatException e) {
                    return new Error(Error.ErrorCode.NUMBER_FORMAT_ERROR, tokens.get(0).value());
                }
            }
            case PLUS: return new Addition(lhs, rhs);
            case MINUS: return new Subtraction(lhs, rhs);
            case TIMES: return new Multiplication(lhs, rhs);
            case DIVIDE: return new Division(lhs, rhs);
            case MOD: return new Modulo(lhs, rhs);
            case POW: return new Power(lhs, rhs);


            default: {
                return new Error(Error.ErrorCode.INTERNAL_ERROR, tokens);
            }
        }
    }


    public abstract Value eval();
}
