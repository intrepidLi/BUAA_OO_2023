import expr.Expr;
import expr.SelfFun;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class MainClass {

    public static BigInteger getCnt() {
        return cnt;
    }

    public static void setCnt(BigInteger cnt) {
        MainClass.cnt = cnt;
    }

    private static BigInteger cnt = new BigInteger("0");
    private static BigInteger cnt2 = new BigInteger("0");
    private static BigInteger cnt3 = new BigInteger("0");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SelfFun funcs = new SelfFun();
        int funcnt = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < funcnt; i++) {
            String fun = scanner.nextLine();
            //System.out.println(fun);
            String fun1 = Lexer.deleteMulAddProcess(Lexer.preProcess(fun));
            funcs.proFun(fun1);
        }

        String input = scanner.nextLine();
        //System.out.println("input: " + input);
        String newInput = input.replaceAll(" ","").replaceAll("\t", "");
        String newnewInput = funcs.subFun(newInput);
        // System.out.println(newnewInput);
        Lexer lexer = new Lexer(newnewInput);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        //System.out.println(expr);
        Item simplifyExpression = simplify(expr);
        String finalStr = simple(simplifyExpression);
        String finalStr1 = simPlerr(finalStr);
        System.out.println(finalStr1);
        //System.out.println(cnt); // 加法执行次数
        //System.out.println(cnt2); // 乘方执行次数
        //System.out.println(cnt3); // 乘法执行次数
    }

    private static Item simplify(Expr expr) {
        String[] expressions = expr.toString().split(" ");
        ArrayList<Item> stack = new ArrayList<>();
        int flag = 0;
        for (String expression : expressions) {
            if (flag == 0) {
                stack.add(new Item(expression));
                flag = 1;
                continue;
            }
            if (expression.equals("+") | expression.equals("-") |
                    expression.equals("*") | expression.equals("**")) {
                Item item1 = stack.get(stack.size() - 2);
                Item item2 = stack.get(stack.size() - 1);
                Item item;
                switch (expression) {
                    case "+":
                        item = item1.add(item2);
                        stack.remove(item1);
                        stack.remove(item2);
                        stack.add(item);
                        break;
                    case "-":
                        item = item1.sub(item2);
                        stack.remove(item1);
                        stack.remove(item2);
                        stack.add(item);
                        break;
                    case "*":
                        item = item1.mul(item2);
                        stack.remove(item1);
                        stack.remove(item2);
                        stack.add(item);
                        break;
                    default:
                        item = item1.pow(item2);
                        stack.remove(item1);
                        stack.remove(item2);
                        stack.add(item);
                        break;
                }
            }
            else if (expression.equals("sin") | expression.equals("cos")) {
                sinCos(stack, expression);
            }
            else {
                Item item = new Item(expression);
                stack.add(item);
            }
        }
        return stack.get(0);
    }

    private static String simple(Item item) {
        String str = item.toString();
        String simpleStr = str.replaceAll("x\\*\\*1\\*", "x\\*").
                replaceAll("y\\*\\*1\\*", "y\\*"). //z的1x次方直接去除，不进行优化
                replaceAll("x\\*\\*0", "1").replaceAll("\\)\\*\\*1\\*", "\\)\\*").
                replaceAll("y\\*\\*0", "1").
                replaceAll("z\\*\\*0", "1").
                replaceAll("\\+1\\*", "\\+").replaceAll("\\*1\\*", "\\*").
                replaceAll("-1\\*", "-").replaceAll("\\*1\\*", "\\*");
        return simpleStr;
    }

    private static String simPlerr(String str) {
        StringBuilder sb = new StringBuilder();
        String cont = "";
        String newstr = str;
        if (newstr.length() == 1) {
            sb.append(newstr.charAt(0));
        }
        if (newstr.length() >= 2 && !(newstr.charAt(0) == '1' && newstr.charAt(1) == '*')) {
            sb.append(newstr.charAt(0)).append(newstr.charAt(1));
        }
        newstr = newstr.replaceAll("cos\\(\\(1\\*x\\*1\\)\\)", "cos\\(x\\)").
                        replaceAll("cos\\(\\(1\\*y\\*1\\)\\)", "cos\\(y\\)").
                        replaceAll("cos\\(\\(1\\*z\\*1\\)\\)", "cos\\(z\\)").
                        replaceAll("sin\\(\\(1\\*x\\*1\\)\\)", "sin\\(x\\)").
                        replaceAll("sin\\(\\(1\\*y\\*1\\)\\)", "sin\\(y\\)").
                        replaceAll("sin\\(\\(1\\*z\\*1\\)\\)", "sin\\(z\\)");
        int len = newstr.length();
        int i = 0;
        for (i = 2; i < len; i++) {
            sb.append(newstr.charAt(i));
        }
        return sb.toString();
    }

    private static void sinCos(ArrayList<Item> stack, String s) {
        if (s.equals("cos")) {
            Item item1 = stack.get(stack.size() - 1);
            Item item = item1.cos();
            stack.remove(item1);
            stack.add(item);
        }
        else if (s.equals("sin")) {
            Item item1 = stack.get(stack.size() - 1);
            Item item = item1.sin();
            stack.remove(item1);
            stack.add(item);
        }
    }

    public static void addCnt() {
        cnt = cnt.add(new BigInteger("1"));
    }

    public static void addPow() {
        cnt2 = cnt2.add(new BigInteger("1"));
    }

    public static void addMul() {
        cnt3 = cnt3.add(new BigInteger("1"));
    }
}
