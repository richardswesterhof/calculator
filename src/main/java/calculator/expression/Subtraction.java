package calculator.expression;

import calculator.Value;

import static calculator.Value.subtract;

public class Subtraction extends Expression {

    private Expression lhs, rhs;


    public Subtraction(Expression lhs, Expression rhs) {
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
        return subtract(lhs.eval(), rhs.eval());
    }


    @Override
    public String toString() {
        return "(" + lhs.toString() + " - " + rhs.toString() + ")";
    }
}
