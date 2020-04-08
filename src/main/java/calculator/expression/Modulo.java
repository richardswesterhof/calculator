package calculator.expression;

import calculator.Value;

import static calculator.Value.mod;

public class Modulo extends Expression {

    private Expression lhs, rhs;


    public Modulo(Expression lhs, Expression rhs) {
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
        return mod(lhs.eval(), rhs.eval());
    }


    @Override
    public String toString() {
        return "(" + lhs.toString() + " % " + rhs.toString() + ")";
    }
}
