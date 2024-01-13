import expr.Expr;
import expr.Factor;

public class FunctionDiff implements Factor {
    private String diffType;
    private Expr expr;

    public FunctionDiff(String s) {
        this.diffType = s;
    }

    public void addExpr(Expr expression) {
        this.expr = expression;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(expr.toString());
        sb.append(" ");
        sb.append(diffType);
        return sb.toString();
    }
}
