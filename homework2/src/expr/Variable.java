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
}
