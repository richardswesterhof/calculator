package calculator.expression;

import calculator.Value;

import static calculator.Value.sqrt;

public class SquareRoot extends Expression {

    private Expression argument;

    public SquareRoot(Expression arg) {
        this.argument = arg;
    }

    public Expression argument() {
        return argument;
    }

    @Override
    public Value eval() {
        return sqrt(argument.eval());
    }

    @Override
    public String toString() {
        return "sqrt(" + argument.toString() + ")";
    }
}
