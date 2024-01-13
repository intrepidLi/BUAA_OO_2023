package expr;

//变量因子
public class Variable implements Factor {
    private final String var;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Variable(String varx) {
        this.var = varx;
    }

    public String getVar() {
        return var;
    }

    public String toString() {
        return this.var;
    }

    /*public Factor derive() {
        Term term = new Term();
        Term one = new Term();
        one.addFactor(new Number(new BigInteger("1")));
        term = Term.mergeTerm(term, one);
        /* TOD 2
        return term;
    }

    @Override
    public Factor clone() {
        return new Variable(var);
    }*/
}
