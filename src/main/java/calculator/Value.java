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

    public static Value sin(Value v1) {
        if(v1 instanceof Error) return v1;
        return new Value(Math.sin(v1.val()));
    }

    public static Value asin(Value v1) {
        if(v1 instanceof Error) return v1;
        return new Value(Math.asin(v1.val()));
    }

    public static Value cos(Value v1) {
        if(v1 instanceof Error) return v1;
        return new Value(Math.cos(v1.val()));
    }

    public static Value acos(Value v1) {
        if(v1 instanceof Error) return v1;
        return new Value(Math.acos(v1.val()));
    }

    public static Value tan(Value v1) {
        if(v1 instanceof Error) return v1;
        return new Value(Math.tan(v1.val()));
    }

    public static Value atan(Value v1) {
        if(v1 instanceof Error) return v1;
        return new Value(Math.atan(v1.val()));
    }

    public static Value abs(Value v1) {
        if(v1 instanceof Error) return v1;
        return new Value(v1.val() < 0 ? v1.val() * -1 : v1.val());
    }

    public static Value sqrt(Value v1) {
        if(v1 instanceof Error) return v1;
        return new Value(Math.sqrt(v1.val()));
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
