package calculator.parsing;

import java.util.ArrayList;

import static java.lang.Character.isWhitespace;

public class Token {

    public enum TokenType {
        NUMBER, OPERATOR, OPEN_PARENTHESES, CLOSE_PARENTHESES, CONSTANT, NONE;
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
                    if(tt.equals(TokenType.OPEN_PARENTHESES)) {
                        if(tokens.size() > 0 &&
                                (tokens.get(tokens.size()-1).tokenType == TokenType.NUMBER ||
                                 tokens.get(tokens.size()-1).tokenType == TokenType.CONSTANT ||
                                 tokens.get(tokens.size()-1).tokenType == TokenType.CLOSE_PARENTHESES)) {
                            tokens.add(tokens.size(), new Token(TokenType.OPERATOR, "*"));
                        }
                        break;
                    }
                }
                i--;
                tokens.add(new Token(tt, token.toString()));
            }
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

    @Override
    public String toString() {
        return value;
    }
}
