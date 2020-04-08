package calculator;

public enum Constant {
    PI(3.14159265359),
    E(2.71828182846);

    private double value;

    Constant(double value) {
        this.value = value;
    }

    public double value() {
        return value;
    }

    public static Value getConstantValue(String s, Value ans) {
        s = s.toLowerCase();
        switch(s) {
            case "ans": return new Value(ans);
            case "pi": return new Value(Constant.PI.value());
            case "e": return new Value(Constant.E.value());
            default: return null;
        }
    }
}
