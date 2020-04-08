package calculator.expression;

import calculator.Value;

import static calculator.Value.pow;

public class Power extends Expression {

    private Expression lhs, rhs;


    public Power(Expression lhs, Expression rhs) {
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
        return pow(lhs.eval(), rhs.eval());
    }


    @Override
    public String toString() {
        return "(" + lhs.toString() + " ^ " + rhs.toString() + ")";
    }
}
