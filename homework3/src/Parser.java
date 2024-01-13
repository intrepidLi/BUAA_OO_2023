import expr.Expr;
import expr.Number;
import expr.Term;
import expr.Trifun;
import expr.Powfun;
import expr.Variable;
import expr.Factor;

import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        expr.addTerm(parseTerm());

        while (lexer.peek().equals("+") | lexer.peek().equals("-")) { /* TODO */
            expr.getOperators().add(lexer.peek());
            lexer.next();
            expr.addTerm(parseTerm());
        }
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        term.addFactor(parsePowfun());

        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parsePowfun());/* TODO */
        }
        return term;
    }

    public Powfun parsePowfun() {
        Powfun powfun = new Powfun();
        powfun.addFactor(parseFactor());

        while (lexer.peek().equals("**")) {
            lexer.next();
            powfun.addFactor(parseFactor());
        }
        return powfun;
    }

    public Trifun parseTrifun(String s) {
        Trifun trifun = new Trifun(s);
        trifun.addExpr(parseExpr());

        while (lexer.peek().equals("sin(") | lexer.peek().equals("cos(")) {
            lexer.next();
            trifun.addExpr(parseExpr());
        }
        return trifun;
    }

    public FunctionDiff parseDiff(String s) {
        FunctionDiff funcdif = new FunctionDiff(s);
        funcdif.addExpr(parseExpr());
        while (lexer.peek().equals("dx(") | lexer.peek().equals("dy(")
                | lexer.peek().equals("dz(")) {
            lexer.next();
            funcdif.addExpr(parseExpr());
        }
        return funcdif;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("dx(") | lexer.peek().equals("dy(") | lexer.peek().equals("dz(")) {
            String difSym = lexer.peek();
            lexer.next();
            Factor expr = parseDiff(difSym.substring(0, 2));
            lexer.next();
            return expr;
        }
        else if (lexer.peek().equals("sin(") | lexer.peek().equals("cos(")) {
            String triSym = lexer.peek();
            lexer.next();
            Factor expr = parseTrifun(triSym.substring(0, 3)); //substring两参数只含开头不含结尾
            lexer.next();
            return expr;
        }
        else if (lexer.peek().equals("(")) {
            lexer.next();
            Factor expr = parseExpr();
            lexer.next();/* TODO */
            return expr;
        }
        else { //先考虑变量只有x,y,z的情况:
            if (lexer.peek().equals("x") | lexer.peek().equals("y") | lexer.peek().equals("z")) {
                String str = lexer.peek();
                lexer.next();
                return new Variable(str);
            }
            else {
                BigInteger num = new BigInteger(lexer.peek());/* TODO */
                lexer.next();
                return new Number(num);
            }
        }
    }
}
