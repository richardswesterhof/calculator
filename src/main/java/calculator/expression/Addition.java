package calculator.expression;

import calculator.Value;

import static calculator.Value.add;

public class Addition extends Expression {

    private Expression lhs, rhs;


    public Addition(Expression lhs, Expression rhs) {
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
        return add(lhs.eval(), rhs.eval());
    }


    @Override
    public String toString() {
        return "(" + lhs.toString() + " + " + rhs.toString() + ")";
    }
}

