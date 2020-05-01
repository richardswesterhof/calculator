package calculator.expression;

import calculator.Value;

import static calculator.Value.multiply;

public class Multiplication extends Expression{

    private Expression lhs, rhs;


    public Multiplication(Expression lhs, Expression rhs) {
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
        return multiply(lhs.eval(), rhs.eval());
    }


    @Override
    public String toString() {
        return "(" + lhs + " * " + rhs + ")";
    }
}
