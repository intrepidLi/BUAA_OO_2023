import java.math.BigInteger;
import java.util.HashMap;
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
    private int maxxIndex;
    private int maxyIndex;

    public int getMaxxIndex() {
        return maxxIndex;
    }

    public void setMaxxIndex(int maxxIndex) {
        this.maxxIndex = maxxIndex;
    }

    public int getMaxyIndex() {
        return maxyIndex;
    }

    public void setMaxyIndex(int maxyIndex) {
        this.maxyIndex = maxyIndex;
    }

    public int getMaxzIndex() {
        return maxzIndex;
    }

    public void setMaxzIndex(int maxzIndex) {
        this.maxzIndex = maxzIndex;
    }

    private int maxzIndex;

    public Item(String s) {
        expression = new HashMap<>();
        if (s.equals("x")) {
            expression.put(new Expo(1, 0,
                    0, new HashMap<>(), new HashMap<>()), new BigInteger("1"));
            maxxIndex = 1;
            maxyIndex = 0;
            maxzIndex = 0;
        }
        else if (s.equals("y")) {
            expression.put(new Expo(0, 1,
                    0, new HashMap<>(), new HashMap<>()), new BigInteger("1"));
            maxxIndex = 0;
            maxyIndex = 1;
            maxzIndex = 0;
        }
        else if (s.equals("z")) {
            expression.put(new Expo(0, 0,
                    1, new HashMap<>(), new HashMap<>()), new BigInteger("1"));
            maxxIndex = 0;
            maxyIndex = 0;
            maxzIndex = 1;
        }
        else if (!s.equals("")) {
            expression.put(new Expo(0, 0,
                    0, new HashMap<>(), new HashMap<>()), new BigInteger(s));
            maxxIndex = 0;
            maxyIndex = 0;
            maxzIndex = 0;
        }
        else {
            maxxIndex = 0;
            maxyIndex = 0;
            maxzIndex = 0;
        }
    }

    public Item sin() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.toString());
        //System.out.println("sinStr1: " + sb.toString());
        if (sb.toString().equals("0")) {
            //System.out.println("sinStr: " + this);
            return new Item("0");
        }
        else {
            Item thisItem = new Item("");
            HashMap<String, Integer> hm = new HashMap<>();
            hm.put(sb.toString(), 1);
            //System.out.println("sinStr1: " + sb.toString());
            Expo expo1 = new Expo(0, 0, 0, hm, new HashMap<>());
            thisItem.expression.put(expo1, new BigInteger("1"));
            return thisItem;
        }
    }

    public Item cos() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.toString());
        Item thisItem = new Item("");
        if (sb.toString().equals("0")) {
            //System.out.println("cosStr: " + sb.toString());
            return new Item("1");
        }
        else {
            HashMap<String, Integer> hm = new HashMap<>();
            hm.put(sb.toString(), 1);
            //System.out.println("cosStr1: " + sb.toString());
            Expo expo1 = new Expo(0, 0, 0, new HashMap<>(), hm);
            thisItem.expression.put(expo1, new BigInteger("1"));
            return thisItem;
        }
    }

    public Item sub(Item otherItem) {
        Item thisItem = new Item("");
        int thisMaxxIndex;
        int thisMaxyIndex;
        int thisMaxzIndex;
        if (this.maxxIndex < otherItem.getMaxxIndex()) {
            thisMaxxIndex = otherItem.getMaxxIndex();
        }
        else {
            thisMaxxIndex = this.maxxIndex;
        }
        if (this.maxyIndex < otherItem.getMaxyIndex()) {
            thisMaxyIndex = otherItem.getMaxyIndex();
        }
        else {
            thisMaxyIndex = this.maxyIndex;
        }
        if (this.maxzIndex < otherItem.getMaxzIndex()) {
            thisMaxzIndex = otherItem.getMaxzIndex();
        }
        else {
            thisMaxzIndex = this.maxzIndex;
        }
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
        thisItem.setMaxxIndex(thisMaxxIndex);
        thisItem.setMaxyIndex(thisMaxyIndex);
        thisItem.setMaxzIndex(thisMaxzIndex);
        return thisItem;
    }

    public Item mul(Item otherItem) {
        Item thisItem = new Item("");
        //int thisMaxxIndex;
        //int thisMaxyIndex;
        //int thisMaxzIndex;
        //thisMaxxIndex = this.maxxIndex + otherItem.getMaxxIndex();
        //thisMaxyIndex = this.maxyIndex + otherItem.getMaxyIndex();
        //thisMaxzIndex = this.maxzIndex + otherItem.getMaxzIndex();
        //System.out.println("fuck");
        //System.out.println(otherItem.getExpression().keySet().size());
        //System.out.println(this.getExpression().keySet().size());
        Set<Expo> thisKeySet = this.expression.keySet();
        Set<Expo> otherKeySet = otherItem.expression.keySet();
        for (Expo e2 : otherKeySet) {
            for (Expo e : thisKeySet) {
                HashMap<String, Integer> indexSin = new HashMap<>();
                HashMap<String, Integer> indexCos = new HashMap<>();
                for (String s : e.getSinMap().keySet()) {
                    if (e2.getSinMap().get(s) != null) {
                        indexSin.put(s, e.getSinMap().get(s) + e2.getSinMap().get(s));
                    }
                    else {
                        indexSin.put(s, e.getSinMap().get(s));
                    }
                }
                for (String s2 : e2.getSinMap().keySet()) {
                    if (e.getSinMap().get(s2) == null) {
                        indexSin.put(s2, e2.getSinMap().get(s2));
                    }
                }
                for (String s : e.getCosMap().keySet()) {
                    if (e2.getCosMap().get(s) != null) {
                        indexCos.put(s, e.getCosMap().get(s) + e2.getCosMap().get(s));
                    }
                    else {
                        indexCos.put(s, e.getCosMap().get(s));
                    }
                }
                for (String s2 : e2.getCosMap().keySet()) {
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
        //thisItem.setMaxxIndex(thisMaxxIndex);
        //thisItem.setMaxyIndex(thisMaxyIndex);
        //thisItem.setMaxzIndex(thisMaxzIndex);
        return thisItem;
    }

    public Item add(Item otherItem) {
        Item thisItem = new Item("");
        int thisMaxxIndex;
        int thisMaxyIndex;
        int thisMaxzIndex;
        if (this.maxxIndex < otherItem.getMaxxIndex()) {
            thisMaxxIndex = otherItem.getMaxxIndex();
        }
        else {
            thisMaxxIndex = this.maxxIndex;
        }
        if (this.maxyIndex < otherItem.getMaxyIndex()) {
            thisMaxyIndex = otherItem.getMaxyIndex();
        }
        else {
            thisMaxyIndex = this.maxyIndex;
        }
        if (this.maxzIndex < otherItem.getMaxzIndex()) {
            thisMaxzIndex = otherItem.getMaxzIndex();
        }
        else {
            thisMaxzIndex = this.maxzIndex;
        }
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
        thisItem.setMaxxIndex(thisMaxxIndex);
        thisItem.setMaxyIndex(thisMaxyIndex);
        thisItem.setMaxzIndex(thisMaxzIndex);
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
        for (Expo o : set) {
            if (expression.get(o).compareTo(new BigInteger("0")) < 0) {
                continue;
            }
            if (!expression.get(o).equals(new BigInteger("0"))) {
                if ((o.getExpoX() == 0) && (
                        o.getExpoY() == 0) &&
                        (o.getExpoZ() == 0) && o.getSinMap().size() == 0 &&
                        o.getCosMap().size() == 0) {
                    sb.append(expression.get(o).toString());
                }
                else {
                    proMo(sb, o);
                }
                expression.put(o, new BigInteger("0"));
                break;
            }
        }
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

    public void proMo(StringBuilder sb, Expo o) {
        sb.append(expression.get(o).toString());
        sb.append("*");
        addx(o, sb);
        sb.append("*");
        addy(o, sb);
        sb.append("*");
        addz(o, sb);
        for (String s : o.getSinMap().keySet()) {
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
        for (String s : o.getCosMap().keySet()) {
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

    private boolean isNumeric(String str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
