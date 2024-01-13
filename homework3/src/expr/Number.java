package expr;

import java.math.BigInteger;

public class Number implements Factor {
    private BigInteger num;

    public Number(BigInteger num) {
        this.num = num;
    }

    public String toString() {
        return this.num.toString();
    }

    public Factor derive() {
        Term term = new Term();
        term.addFactor(new Number(new BigInteger("0")));
        return term;
    }

    @Override
    public Factor clone() {
        return new Number(num);
    }
}
