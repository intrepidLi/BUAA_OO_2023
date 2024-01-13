package expr;

import expr.Expr;
import expr.Factor;

public class Trifun implements Factor {
    private String triType; // sin or cos
    private Expr expr;

    public String getTriType() {
        return triType;
    }

    public void setTriType(String triType) {
        this.triType = triType;
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    public Trifun(String s) {
        this.triType = s;
    }

    public void addExpr(Expr expression) {
        this.expr = expression;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(expr.toString());
        sb.append(" ");
        sb.append(triType);
        return sb.toString();
    }
}