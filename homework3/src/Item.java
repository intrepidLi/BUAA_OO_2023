import java.math.BigInteger;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class Item {

    public HashMap<Expo, BigInteger> getExpression() {
        return expression;
    }

    public void setExpression(HashMap<Expo, BigInteger> expression) {
        this.expression = expression;
    }

    private HashMap<Expo, BigInteger> expression; // Expo为指数,后面为系数
    //private ArrayList<BigInteger> expression;

    public Item(String s) {
        expression = new HashMap<>();
        if (s.equals("x")) {
            expression.put(new Expo(1, 0,
                    0, new HashMap<>(), new HashMap<>()), new BigInteger("1"));
        }
        else if (s.equals("y")) {
            expression.put(new Expo(0, 1,
                    0, new HashMap<>(), new HashMap<>()), new BigInteger("1"));
        }
        else if (s.equals("z")) {
            expression.put(new Expo(0, 0,
                    1, new HashMap<>(), new HashMap<>()), new BigInteger("1"));
        }
        else if (!s.equals("")) {
            expression.put(new Expo(0, 0,
                    0, new HashMap<>(), new HashMap<>()), new BigInteger(s));
        }
    }

    public Item sin() {
        Item hey = new Item("");
        hey.setExpression(this.expression);
        HashMap<Item, Integer> newSin = new HashMap<>();
        newSin.put(hey, 1);
        //System.out.println(newSin.get(hey));
        if (this.toString().equals("0")) {
            return new Item("0");
        }
        else {
            Item thisItem = new Item("");
            Expo expo1 = new Expo(0, 0, 0, newSin, new HashMap<>());
            thisItem.expression.put(expo1, new BigInteger("1"));
            return thisItem;
        }
    }

    public Item cos() {
        Item hey = new Item("");
        hey.setExpression(this.expression);
        HashMap<Item, Integer> newCos = new HashMap<>();
        newCos.put(hey, 1);
        if (this.toString().equals("0")) {
            //System.out.println("cosStr: " + sb.toString());
            return new Item("1");
        }
        else {
            Item thisItem = new Item("");
            Expo expo1 = new Expo(0, 0, 0, new HashMap<>(), newCos);
            thisItem.expression.put(expo1, new BigInteger("1"));
            return thisItem;
        }
    }

    public Item diff(String diffname) {
        Item thisItem = new Item("");
        Set<Expo> thisKeySet = this.expression.keySet();
        for (Expo expo : thisKeySet) {
            if (expo.getExpoX() == 0 && expo.getExpoY() == 0 && expo.getExpoZ() == 0 &&
                expo.getSinMap().size() == 0 && expo.getCosMap().size() == 0) {
                Item tempItem = new Item("");
                tempItem.getExpression().put(expo, new BigInteger("0")); // 数字求导变为0
                thisItem = thisItem.add(tempItem);
            }
            else {
                Item newItem = Derivative.diffFun(diffname, expo);
                for (Expo expo1 : newItem.getExpression().keySet()) {
                    Item tempItem = new Item("");
                    tempItem.getExpression().put(expo1, newItem.getExpression().get(expo1).
                            multiply(this.expression.get(expo)));
                    thisItem = thisItem.add(tempItem);
                }
            }
        }
        return thisItem;
    }

    public Item sub(Item otherItem) {
        Item thisItem = new Item("");
        Set<Expo> thisKeySet = this.expression.keySet();
        Set<Expo> otherKeySet = otherItem.expression.keySet();
        for (Expo expo : thisKeySet) {
            thisItem.getExpression().put(expo, this.expression.get(expo));
        }
        for (Expo expo : otherKeySet) {
            if (this.expression.get(expo) != null) {
                if (this.expression.get(expo).subtract(otherItem.expression.get(expo)).
                        equals(new BigInteger("0"))) {
                    thisItem.getExpression().remove(expo);
                }
                else {
                    thisItem.getExpression().put(expo, this.expression.get(expo).subtract(otherItem.
                            getExpression().get(expo)));
                }
            }
            else {
                thisItem.getExpression().put(expo, otherItem.expression.get(expo).
                        multiply(new BigInteger("-1")));
            }
        }
        return thisItem;
    }

    public Item mul(Item otherItem) {
        Item thisItem = new Item("");
        Set<Expo> thisKeySet = this.expression.keySet();
        Set<Expo> otherKeySet = otherItem.expression.keySet();
        for (Expo e2 : otherKeySet) {
            for (Expo e : thisKeySet) {
                HashMap<Item, Integer> indexSin = new HashMap<>();
                HashMap<Item, Integer> indexCos = new HashMap<>();
                for (Item s : e.getSinMap().keySet()) {
                    if (e2.getSinMap().get(s) != null) {
                        indexSin.put(s, e.getSinMap().get(s) + e2.getSinMap().get(s));
                    }
                    else {
                        indexSin.put(s, e.getSinMap().get(s));
                    }
                }
                for (Item s2 : e2.getSinMap().keySet()) {
                    if (e.getSinMap().get(s2) == null) {
                        indexSin.put(s2, e2.getSinMap().get(s2));
                    }
                }
                for (Item s : e.getCosMap().keySet()) {
                    if (e2.getCosMap().get(s) != null) {
                        indexCos.put(s, e.getCosMap().get(s) + e2.getCosMap().get(s));
                    }
                    else {
                        indexCos.put(s, e.getCosMap().get(s));
                    }
                }
                for (Item s2 : e2.getCosMap().keySet()) {
                    if (e.getCosMap().get(s2) == null) {
                        indexCos.put(s2, e2.getCosMap().get(s2));
                    }
                }
                MainClass.addMul();
                int newExpox = e.getExpoX() + e2.getExpoX();
                int newExpoy = e.getExpoY() + e2.getExpoY();
                int newExpoz = e.getExpoZ() + e2.getExpoZ();
                BigInteger newcoe = this.expression.get(e).
                        multiply(otherItem.getExpression().get(e2));
                Expo newexpo = new Expo(newExpox, newExpoy, newExpoz, indexSin, indexCos);
                if (thisItem.getExpression().get(newexpo) == null) {
                    thisItem.getExpression().put(newexpo, newcoe);
                }
                else {
                    Item item = new Item("");
                    item.getExpression().put(newexpo, newcoe);
                    thisItem = thisItem.add(item);
                }
            }
        }
        return thisItem;
    }

    public Item add(Item otherItem) {
        Item thisItem = new Item("");
        Set<Expo> thisKeySet = this.expression.keySet();
        Set<Expo> otherKeySet = otherItem.getExpression().keySet();
        for (Expo expo : thisKeySet) {
            thisItem.getExpression().put(expo, this.expression.get(expo));
        }
        for (Expo expo : otherKeySet) {
            if (this.expression.get(expo) != null) {
                if (this.expression.get(expo).add(otherItem.expression.get(expo)).
                        equals(new BigInteger("0"))) {
                    thisItem.getExpression().remove(expo);
                }
                else {
                    thisItem.getExpression().put(expo, this.expression.get(expo).add(otherItem.
                            getExpression().get(expo)));
                }
            }
            else {
                thisItem.getExpression().put(expo, otherItem.expression.get(expo));
            }
            MainClass.addCnt();
        }
        return thisItem;
    }

    public Item pow(Item otherItem) {
        Item thisItem = new Item("1");
        BigInteger i = new BigInteger("0");
        Expo expo0 = new Expo(0, 0, 0, new HashMap<>(), new HashMap<>());
        for (; i.compareTo(otherItem.getExpression().get(expo0)) < 0;
             i = i.add(new BigInteger("1"))) {
            thisItem = thisItem.mul(this);
            MainClass.addPow();
        }
        return thisItem;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<Expo> set = expression.keySet();
        //for (Expo o : set) {
        //    if (expression.get(o).compareTo(new BigInteger("0")) < 0) {
        //        continue;
        //    }
        //    if (!expression.get(o).equals(new BigInteger("0"))) {
        //        if ((o.getExpoX() == 0) && (
        //                o.getExpoY() == 0) &&
        //                (o.getExpoZ() == 0) && o.getSinMap().size() == 0 &&
        //                o.getCosMap().size() == 0) {
        //            sb.append(expression.get(o).toString());
        //        }
        //        else {
        //            proMo(sb, o);
        //        }
        //        expression.put(o, new BigInteger("0"));
        //        break;
        //    }
        //}
        for (Expo o : set) {
            if (!expression.get(o).equals(new BigInteger("0"))) {
                if (expression.get(o).toString().charAt(0) != '-') {
                    sb.append("+");
                }
                if ((o.getExpoX() == 0) && (
                        o.getExpoY() == 0) &&
                        (o.getExpoZ() == 0) && o.getSinMap().size() == 0 &&
                        o.getCosMap().size() == 0) {
                    sb.append(expression.get(o).toString());
                }
                else {
                    proMo(sb, o);
                }
            }
        }
        if (sb.toString().length() == 0) {
            return "0";
        }
        else {
            return sb.toString();
        }
    }

    /*public String toString1() {
        StringBuilder sb = new StringBuilder();
        Set<Expo> set = expression.keySet();
        for (Expo o : set) {
            if (!expression.get(o).equals(new BigInteger("0"))) {
                if (expression.get(o).toString().charAt(0) != '-') {
                    sb.append("+");
                }
                if ((o.getExpoX() == 0) && (
                        o.getExpoY() == 0) &&
                        (o.getExpoZ() == 0) && o.getSinMap().size() == 0 &&
                        o.getCosMap().size() == 0) {
                    sb.append(expression.get(o).toString());
                }
                else {
                    proMo(sb, o);
                }
            }
        }
        if (sb.toString().length() == 0) {
            return "0";
        }
        else {
            return sb.toString();
        }
    }*/

    public void proMo(StringBuilder sb, Expo o) {
        sb.append(expression.get(o).toString());
        sb.append("*");
        addx(o, sb);
        sb.append("*");
        addy(o, sb);
        sb.append("*");
        addz(o, sb);
        for (Item s : o.getSinMap().keySet()) {
            if (o.getSinMap().get(s) == 0) { //将三角函数全部乘起来
                sb.append("*").append("1");
                continue;
            }
            sb.append("*");
            if (isNumeric(s)) {
                sb.append("sin(");
                sb.append(s);
                sb.append(")");
            }
            else {
                sb.append("sin((");
                sb.append(s);
                sb.append("))");
            }
            if (o.getSinMap().get(s) != 1) {
                sb.append("**");
                sb.append(o.getSinMap().get(s));
            }
        }
        for (Item s : o.getCosMap().keySet()) {
            if (o.getCosMap().get(s) == 0) {
                sb.append("*").append("1");
                continue;
            }
            sb.append("*");
            if (isNumeric(s)) {
                sb.append("cos(");
                sb.append(s);
                sb.append(")");
            }
            else {
                sb.append("cos((");
                sb.append(s);
                sb.append("))");
            }
            if (o.getCosMap().get(s) != 1) {
                sb.append("**");
                sb.append(o.getCosMap().get(s));
            }
        }
    }

    private void addz(Expo o, StringBuilder sb) {
        if (o.getExpoZ() == 1) {
            sb.append("z");
        }
        else {
            sb.append("z");
            sb.append("**");
            sb.append(o.getExpoZ());
        }
    }

    private void addy(Expo o, StringBuilder sb) {
        if (o.getExpoY() == 1) {
            sb.append("y");
        }
        else {
            sb.append("y");
            sb.append("**");
            sb.append(o.getExpoY());
        }
    }

    private void addx(Expo o, StringBuilder sb) {
        if (o.getExpoX() == 1) {
            sb.append("x");
        }
        else {
            sb.append("x");
            sb.append("**");
            sb.append(o.getExpoX());
        }
    }

    //private boolean isNumeric(String str) {
    //    int len = str.length();
    //    for (int i = 0; i < len; i++) {
    //        if (!Character.isDigit(str.charAt(i))) {
    //            return false;
    //        }
    //    }
    //    return true;
    //}
    private boolean isNumeric(Item str) {
        int flag = 1;
        if (str.getExpression().size() == 1) {
            for (Expo e : str.getExpression().keySet()) {
                if (!e.isNull()) {
                    flag = 0;
                    break;
                }
            }
            if (flag == 0) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return Objects.equals(expression, item.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression);
    }
}
