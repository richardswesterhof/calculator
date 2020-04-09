package calculator.parsing;

import calculator.Error;

import java.util.ArrayList;

import static calculator.expression.Expression.knownFunctions;
import static calculator.expression.Operator.MINUS;
import static java.lang.Character.isWhitespace;

public class Token {

    public enum TokenType {
        NUMBER, OPERATOR, OPEN_PARENTHESES, CLOSE_PARENTHESES, CONSTANT, FUNCTION, NONE;
    }

    //the string representation of the token
    private String value;
    private TokenType tokenType;


    public Token(TokenType t, String s) {
        value = s;
        tokenType = t;
    }

    public static ArrayList<Token> tokenize(String s) {
        ArrayList<Token> tokens = new ArrayList<>();
        if(s.length() > 0) {
            TokenType tt;
            for(int i = 0; i < s.length(); i++) {
                if(isWhitespace(s.charAt(i))) continue;
                tt = getTokenType(s.charAt(i));
                StringBuilder token = new StringBuilder();
                while(i < s.length() && getTokenType(s.charAt(i)).equals(tt)) {
                    if(tt.equals(TokenType.OPERATOR) && !fitsAsNext(s.charAt(i), token.toString())) {
                        break;
                    }
                    token.append(s.charAt(i));
                    i++;
                    if(tt.equals(TokenType.OPEN_PARENTHESES) || tt.equals(TokenType.CLOSE_PARENTHESES)) {
                        break;
                    }
                }
                i--;
                tokens.add(new Token(tt, token.toString()));
            }
            //now our list of tokens is complete, however it may still contain syntactic sugar
            //e.g. 2(1+1) instead of 2*(1+1) etc.
            desugar(tokens);
        }
//        System.out.println("Tokens: " + tokens);
        return tokens;
    }

    private static TokenType getTokenType(char c) {
        if(isNumber(c)) return TokenType.NUMBER;
        if(isOperator(c)) return TokenType.OPERATOR;
        if(c == '(') return TokenType.OPEN_PARENTHESES;
        if(c == ')') return TokenType.CLOSE_PARENTHESES;
        if(isConstant(c)) return TokenType.CONSTANT;
        return TokenType.NONE;
    }

    public String value() {
        return value;
    }

    public TokenType tokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tt) {
        this.tokenType = tt;
    }

    public static ArrayList<Token> desugar(ArrayList<Token> sugared) {
        for(int i = 0; i < sugared.size(); i++) {
            Token sugaredIm1 = i > 0 ? sugared.get(i - 1) : null;

            //make difference between constant and function
            if(sugared.get(i).tokenType == TokenType.CONSTANT && isKnownFunction(sugared.get(i).value()))
                sugared.get(i).setTokenType(TokenType.FUNCTION);

            //desugar -x into (-1*x) if there is no lhs to the minus sign '-'
            if(((i > 0 && i < sugared.size() - 1 && sugaredIm1.tokenType() != TokenType.CONSTANT
                    && sugaredIm1.tokenType() != TokenType.NUMBER ) || i == 0)
                    && sugared.get(i).value().equals("-")) {
                sugared.add(i+2, new Token(TokenType.CLOSE_PARENTHESES, ")"));
                sugared.add(i+1, new Token(TokenType.OPERATOR, "*"));
                sugared.add(i+1, new Token(TokenType.NUMBER, "-1"));
                sugared.add(i, new Token(TokenType.OPEN_PARENTHESES, "("));
                sugared.remove(i+1);
                i += 3;
            }

            //add multiplication sign '*' between number/constant and parenthesis
            if(i > 0 && sugared.get(i).tokenType == TokenType.OPEN_PARENTHESES &&
                    (sugaredIm1.tokenType == TokenType.NUMBER ||
                    sugaredIm1.tokenType == TokenType.CONSTANT ||
                    sugaredIm1.tokenType == TokenType.CLOSE_PARENTHESES)
            ) {
                sugared.add(i, new Token(TokenType.OPERATOR, "*"));
            }
            //add multiplication sign '*' between number/constant and constant/function
            //e.g. 2sin(0.5pi) becomes 2*sin(0.5*pi)
            else if(i > 0 && (sugared.get(i).tokenType == TokenType.CONSTANT ||
                    sugared.get(i).tokenType == TokenType.FUNCTION) &&
                    (sugaredIm1.tokenType == TokenType.NUMBER || sugaredIm1.tokenType == TokenType.CONSTANT)) {
                sugared.add(i, new Token(TokenType.OPERATOR, "*"));
            }
        }
        return sugared;
    }

    public static Boolean fitsAsNext(char c, String s) {
        //TODO: update this method if there are operators with more than one character!!!
        return s.length() == 0;
    }

    public static Boolean isNumber(char c) {
        return (c >= '0' && c <= '9') || c == '.';
    }

    public static Boolean isOperator(char c) {
        switch(c) {
            case '+':
            case '-':
            case '*':
            case '/':
            case '%':
            case '^': return true;
            default: return false;
        }
    }

    public static Boolean isConstant(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public static Boolean isKnownFunction(String s) {
        return knownFunctions.contains(s);
    }

    @Override
    public String toString() {
        return value;
    }
}
