import expr.Factor;
import expr.Powfun;
import expr.Term;
import expr.Expr;
import expr.Variable;
import expr.Number;

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

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            lexer.next();
            Factor expr = parseExpr();
            lexer.next();/* TODO */
            return expr;
        } else { //先考虑变量只有x,y,z的情况:
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
