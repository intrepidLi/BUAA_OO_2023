import expr.Expr;
import java.util.ArrayList;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) throws CloneNotSupportedException {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        String newInput = input.replaceAll(" ","").replaceAll("\t", "");
        Lexer lexer = new Lexer(newInput);
        Parser parser = new Parser(lexer);

        Expr expr = parser.parseExpr();
        //System.out.println(expr);
        Item simplifyExpression = simplify(expr);
        String finalStr = simple(simplifyExpression);
        System.out.println(finalStr);

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
                replaceAll("y\\*\\*1\\*", "y\\*").//z的1x次方直接去除，不进行优化
                replaceAll("x\\*\\*0", "1").
                replaceAll("y\\*\\*0", "1").
                replaceAll("z\\*\\*0", "1").
                replaceAll("\\+1\\*", "\\+").replaceAll("\\*1\\*", "\\*").
                replaceAll("-1\\*", "-").replaceAll("\\*1\\*", "\\*");
        return simpleStr;
    }
}
