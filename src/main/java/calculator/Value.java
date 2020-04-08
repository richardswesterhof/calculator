package calculator;

import calculator.expression.Expression;

public class Value extends Expression {

    private double val;

    public Value(double val) {
        this.val = val;
    }

    public Value(Value old) {
        this.val = old.val();
    }

    public double val() {
        return val;
    }


    //methods that add, subtract, multiply, divide, etc.
    public static Value add(Value v1, Value v2) {
        if(v1 instanceof Error) return v1;
        if(v2 instanceof Error) return v2;
        return new Value(v1.val() + v2.val());
    }

    public static Value subtract(Value v1, Value v2) {
        if(v1 instanceof Error) return v1;
        if(v2 instanceof Error) return v2;
        return new Value(v1.val() - v2.val());
    }

    public static Value multiply(Value v1, Value v2) {
        if(v1 instanceof Error) return v1;
        if(v2 instanceof Error) return v2;
        return new Value(v1.val() * v2.val());
    }

    public static Value divide(Value v1, Value v2) {
        if(v1 instanceof Error) return v1;
        if(v2 instanceof Error) return v2;
        return new Value(v1.val() / v2.val());
    }

    public static Value mod(Value v1, Value v2) {
        if(v1 instanceof Error) return v1;
        if(v2 instanceof Error) return v2;
        return new Value(v1.val() % v2.val());
    }

    public static Value pow(Value v1, Value v2) {
        if(v1 instanceof Error) return v1;
        if(v2 instanceof Error) return v2;
        return new Value(Math.pow(v1.val(), v2.val()));
    }



    //unknown input types
    public static Error add(Object o1, Object o2) {
        System.err.println("no way to add " + o1.getClass() + " and " + o2.getClass());
        return new Error(Error.ErrorCode.INCORRECT_OPERATOR, o1 + " + " + o2);
    }


    @Override
    public Value eval() {
        return this;
    }

    @Override
    public String toString() {
        return Double.toString(val);
    }
}
