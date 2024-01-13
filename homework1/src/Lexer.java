public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;
    private int flag;
    private boolean isOperator;

    public Lexer(String input) {
        flag = 0;
        this.input = deleteMulAddProcess(preProcess(input));
        //System.out.println(preProcess(input));
        //System.out.println(this.input);
        this.isOperator = true;
        this.next();
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }

        return sb.toString();
    }

    private void nextSub(char c) {
        if (!isOperator) {
            curToken = "-1";
            flag = 1;
            isOperator = true;
        }
        else {
            pos += 1;
            curToken = String.valueOf(c);
            isOperator = false;
        }
    }

    private void nextAdd(char c) {
        if (!isOperator) {
            curToken = "1";
            flag = 1;
            isOperator = true;
        }
        else {
            pos += 1;
            curToken = String.valueOf(c);
            isOperator = false;
        }
    }

    private void nextVar(char c) {
        pos += 1;
        curToken = String.valueOf(c);
        isOperator = true;
    }

    private void nextPowMul(String s) {
        curToken = s;
        isOperator = false;
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }
        if (flag == 1) {
            pos += 1;
            flag = 0;
            nextPowMul("*");
            return;
        }
        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = this.getNumber()/* TODO */;
            isOperator = true;
        }
        else if (c == '+') { //我的错误答案:String.valueOf(c).equals("(")"()+*".indexOf(c) != -1
            nextAdd(c);
        }
        else if (c == '-') {
            nextSub(c);
        }
        else if (c == '*') {
            pos += 1;
            if (input.charAt(pos) == '*') {
                pos += 1;
                nextPowMul("**");
            }
            else {
                nextPowMul("*");
            }
        }
        else if (c == '(' | c == ')') {
            pos += 1;
            curToken = String.valueOf(c);
            if (c == '(') {
                isOperator = false;
            }
            else {
                isOperator = true;
            }
        }
        else if (c == 'x' | c == 'y' | c == 'z') {
            nextVar(c);
        }
    }

    //预处理：开头为加号或减号时填一个前导0，出现连续的加号和减号时化成一个处理
    private String preProcess(String input) {
        StringBuilder sb = new StringBuilder();
        int length = input.length();
        if (input.charAt(0) == '+' | input.charAt(0) == '-') {
            sb.append("0");
        }
        for (int i = 0; i < length; i++) {
            int opra = 1; // 出现过错误
            char c = input.charAt(i);
            if (input.charAt(i) == '+' | input.charAt(i) == '-') {
                for (; i < length && (input.charAt(i) == '+' | input.charAt(i) == '-'); i++) {
                    if (input.charAt(i) == '-') {
                        opra *= -1;
                    }
                }
                if (opra == 1) {
                    sb.append("+");
                }
                else {
                    sb.append("-");
                }
                i -= 1;
            }
            else {
                sb.append(input.charAt(i));
            }
        }
        return  sb.toString();
    }

    private String deleteMulAddProcess(String str) { //将**和*后的+号删掉
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (str.charAt(i) == '*' && str.charAt(i + 1) == '*' && str.charAt(i + 2) == '+') {
                sb.append("**");
                i += 2;
            }
            else if (str.charAt(i) == '*' && str.charAt(i + 1) == '+') {
                sb.append("*");
                i += 1;
            }
            else {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    public String peek() {
        return this.curToken;
    }
}
