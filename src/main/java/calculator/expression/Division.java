package calculator.expression;

import calculator.Value;

import static calculator.Value.divide;

public class Division extends Expression {

    private Expression lhs, rhs;


    public Division(Expression lhs, Expression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Expression lhs() {
        return lhs;
    }

    public Expression rhs() {
        return rhs;
    }

    @Override
    public Value eval() {
        return divide(lhs.eval(), rhs.eval());
    }


    @Override
    public String toString() {
        return "(" + lhs.toString() + " / " + rhs.toString() + ")";
    }
}
