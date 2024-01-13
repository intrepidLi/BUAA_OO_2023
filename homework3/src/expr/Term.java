package expr;

import java.util.Iterator;
import java.util.ArrayList;

public class Term implements Factor {
    private final ArrayList<Factor> factors;

    public Term() {
        this.factors = new ArrayList<>();
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public String toString() {
        Iterator<Factor> iter = factors.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString());
        if (iter.hasNext()) {
            sb.append(" ");
            sb.append(iter.next().toString());
            sb.append(" *");
            while (iter.hasNext()) {
                sb.append(" ");
                sb.append(iter.next().toString());
                sb.append(" *");
            }
        }
        return sb.toString();
    }

    /*public static Term mergeTerm(Term term1, Term term2) {
        if (term1 == null) {
            return term2;
        }
        if (term2 == null) {
            return term1;
        }
        Term term = new Term();
        term1.factors.forEach(term::addFactor);
        term2.factors.forEach(term::addFactor);
        return term;
    }

    public Factor derive() {
        Expr expr = new Expr();
        for (int i = 0; i < factors.size(); i++) {
            Term t = (Term) factors.get(i).derive();
            for (int j = 0; j < factors.size(); j++) {
                if (i != j) {
                    t.addFactor(factors.get(j).clone());
                }
            }
            expr.addTerm(t);
        }
        return expr;
    }

    @Override
    public Factor clone() {
        Term term = new Term();
        for (int i = 0; i < factors.size(); i++) {
            term.addFactor(factors.get(i));
        }
        return term;
    }*/
}
