package calculator;

public enum Constant {
    PI(Math.PI),
    E(Math.E),
    ANS(0);

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
