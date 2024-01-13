package expr;

import java.math.BigInteger;
import java.util.Objects;

public class Cos implements Factor {
    private Factor factor;

    public Cos(Factor factor) {
        this.factor = factor;
    }

    public Sin toSin() {
        return new Sin(factor);
    }

    @Override
    public String toString() {
        return "cos(" + factor.toString() + ")";
    }

   /* @Override
    public Factor derive() {
        Term term = new Term();
        term.addFactor(toSin());
        term.addFactor(new Number(new BigInteger("-1")));
        term = Term.mergeTerm(term, (Term) factor.derive()); // finish
        /* TOD 3
        return term;
    }

    @Override
    public Factor clone() {
        return new Cos(factor.clone());
    }*/
}
