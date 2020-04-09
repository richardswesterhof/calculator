package calculator.expression;

import calculator.Value;

import static calculator.Value.cos;

public class Cosine extends Expression {

    //argument of the cosine function, should always be in radians!!
    private Expression argument;

    public Cosine(Expression arg) {
        this.argument = arg;
    }

    public Expression argument() {
        return argument;
    }

    @Override
    public Value eval() {
        return cos(argument.eval());
    }

    @Override
    public String toString() {
        return "cos(" + argument + ")";
    }
}
