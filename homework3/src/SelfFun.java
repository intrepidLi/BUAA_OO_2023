import expr.Expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelfFun {
    private HashMap<String, ArrayList<String>> functions;

    public HashMap<String, ArrayList<String>> getFunctions() {
        return functions;
    }

    public void setFunctions(HashMap<String, ArrayList<String>> functions) {
        this.functions = functions;
    }

    public SelfFun() {
        this.functions = new HashMap<>();
    }

    public void proFun(String s) {
        String newstr = s.replaceAll(" ", "").replaceAll("\t", "");
        String basiequal = "(?<funname>[f|g|h])\\((?<para>\\S+)(,\\S+)*\\)=(?<funcon>\\S+)";
        Pattern pattern = Pattern.compile(basiequal);
        Matcher matcher = pattern.matcher(newstr);
        String funname = "";
        String para = "";
        String funcon = "";
        if (matcher.find()) {
            funname = matcher.group("funname");
            para = matcher.group("para");
            funcon = matcher.group("funcon");
        }
        if (funcon.contains("f") | funcon.contains("g") | funcon.contains("h")) {
            funcon = subFun(funcon);
        }
        if (funcon.contains("d")) {
            Lexer lexer = new Lexer(funcon);
            Parser parser = new Parser(lexer);
            Expr expr = parser.parseExpr();
            Item simplifyExpression = MainClass.simplify(expr);
            String finalStr = MainClass.simple(simplifyExpression);
            funcon = MainClass.simPlerr(finalStr);
        }
        String[] parame = para.split(","); // 参数的个数
        int len = parame.length;
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(Arrays.asList(parame).subList(0, len));
        arrayList.add(funcon);
        this.functions.put(funname, arrayList);
    }

    public String subFun(String input) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();// 记录函数实参
        int length = input.length();
        int index;
        int pos = 0;
        int cnpt = 0;
        String c;
        for (index = 0; index < length; index++) {
            StringBuilder sb1 = new StringBuilder();
            c = String.valueOf(input.charAt(index));
            if (c.equals("f") | c.equals("g") | c.equals("h")) {
                index++;
                if (input.charAt(index) == '(') {
                    cnpt++; //括号数量
                    index++;
                }
                while (input.charAt(index) == '(') {
                    cnpt++; //括号数量
                    sb1.append(input.charAt(index));
                    index++;
                }
                for (pos = index; cnpt != 0; pos++) {
                    if (input.charAt(pos) == '(') {
                        cnpt++;
                    }
                    else if (input.charAt(pos) == ')') {
                        cnpt--;
                        if (cnpt == 0) {
                            break;
                        }
                    }
                    sb1.append(input.charAt(pos));
                }
                pos--;
                ArrayList<String> varList = functions.get(c); // 相应的函数表达式
                String varstr = sb1.toString();
                String varstr1 = curcheck(varstr);
                // System.out.println(varstr1);
                String[] vars = varstr1.split(",");
                String newfunc = varList.get(varList.size() - 1);
                String[] formpara = {"k", "l", "m"};
                for (int i = 0; i < varList.size() - 1; i++) {
                    newfunc = newfunc.replaceAll(varList.get(i), formpara[i]);// 先将函数表达式中的x, y, z换掉
                }
                String finalcon = newfunc;
                for (int i = 0; i < varList.size() - 1; i++) {
                    finalcon = finalcon.replaceAll(formpara[i],"(" + vars[i] + ")");// 将函数的实参传进形参
                }
                sb.append("(").append(finalcon).append(")"); // 最后要把()加上
                index = ++pos;
                sb2.append(sb.toString());
                sb.setLength(0);
            }
            else {
                sb2.append(c);
            }
        }
        return sb2.toString();
    }

    private String curcheck(String varstr) {
        String text = varstr;
        int flag = 0;
        for (String s : this.functions.keySet()) {
            if (varstr.contains(s)) {
                flag = 1;
            }
        }
        if (flag == 1) {
            text = this.subFun(text);
        }
        return text;
    }
}
