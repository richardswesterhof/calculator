package calculator.expression;

import calculator.Value;

import static calculator.Value.sin;

public class Sine extends Expression {

    //argument of the sine function, should always be in radians!!
    private Expression argument;

    public Sine(Expression arg) {
        this.argument = arg;
    }

    public Expression argument() {
        return argument;
    }

    @Override
    public Value eval() {
        return sin(argument.eval());
    }

    @Override
    public String toString() {
        return "sin(" + argument.toString() + ")";
    }
}
