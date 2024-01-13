import expr.Expo;
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
    private BigInteger maxxIndex;
    private BigInteger maxyIndex;
    private BigInteger maxzIndex;

    public BigInteger getMaxxIndex() {
        return maxxIndex;
    }

    public void setMaxxIndex(BigInteger maxxIndex) {
        this.maxxIndex = maxxIndex;
    }

    public BigInteger getMaxyIndex() {
        return maxyIndex;
    }

    public void setMaxyIndex(BigInteger maxyIndex) {
        this.maxyIndex = maxyIndex;
    }

    public BigInteger getMaxzIndex() {
        return maxzIndex;
    }

    public void setMaxzIndex(BigInteger maxzIndex) {
        this.maxzIndex = maxzIndex;
    }

    public Item(String s) {
        expression = new HashMap<>();
        if (s.equals("x")) {
            expression.put(new Expo(new BigInteger("1"), new BigInteger("0"),
                    new BigInteger("0")), new BigInteger("1"));
            maxxIndex = new BigInteger("1");
            maxyIndex = new BigInteger("0");
            maxzIndex = new BigInteger("0");
        }
        else if (s.equals("y")) {
            expression.put(new Expo(new BigInteger("0"), new BigInteger("1"),
                    new BigInteger("0")), new BigInteger("1"));
            maxxIndex = new BigInteger("0");
            maxyIndex = new BigInteger("1");
            maxzIndex = new BigInteger("0");
        }
        else if (s.equals("z")) {
            expression.put(new Expo(new BigInteger("0"), new BigInteger("0"),
                    new BigInteger("1")), new BigInteger("1"));
            maxxIndex = new BigInteger("0");
            maxyIndex = new BigInteger("0");
            maxzIndex = new BigInteger("1");
        }
        else if (!s.equals("")) {
            expression.put(new Expo(new BigInteger("0"), new BigInteger("0"),
                    new BigInteger("0")), new BigInteger(s));
            maxxIndex = new BigInteger("0");
            maxyIndex = new BigInteger("0");
            maxzIndex = new BigInteger("0");
        }
        else {
            maxxIndex = new BigInteger("0");
            maxyIndex = new BigInteger("0");
            maxzIndex = new BigInteger("0");
        }
    }

    public Item sub(Item otherItem) {
        Item thisItem = new Item("");
        BigInteger thisMaxxIndex;
        BigInteger thisMaxyIndex;
        BigInteger thisMaxzIndex;
        if (this.maxxIndex.compareTo(otherItem.getMaxxIndex()) < 0) {
            thisMaxxIndex = otherItem.getMaxxIndex();
        }
        else {
            thisMaxxIndex = this.maxxIndex;
        }
        if (this.maxyIndex.compareTo(otherItem.getMaxyIndex()) < 0) {
            thisMaxyIndex = otherItem.getMaxyIndex();
        }
        else {
            thisMaxyIndex = this.maxyIndex;
        }
        if (this.maxzIndex.compareTo(otherItem.getMaxzIndex()) < 0) {
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
        Item thisItem = new Item("0");
        BigInteger thisMaxxIndex;
        BigInteger thisMaxyIndex;
        BigInteger thisMaxzIndex;
        thisMaxxIndex = this.maxxIndex.add(otherItem.getMaxxIndex());
        thisMaxyIndex = this.maxyIndex.add(otherItem.getMaxyIndex());
        thisMaxzIndex = this.maxzIndex.add(otherItem.getMaxzIndex());
        Set<Expo> thisKeySet = this.expression.keySet();
        Set<Expo> otherKeySet = otherItem.expression.keySet();
        for (Expo e : thisKeySet) {
            for (Expo e2 : otherKeySet) {
                Item item = new Item("");
                if (this.expression.get(e) != null && otherItem.getExpression().get(e2) != null) {
                    item.getExpression().put(new Expo(e.getExpoX().add(e2.getExpoX()),
                                    e.getExpoY().add(e2.getExpoY()),
                                    e.getExpoZ().add(e2.getExpoZ())),
                            this.expression.get(e).multiply(otherItem.getExpression().get(e2)));
                    item.setMaxxIndex(e.getExpoX().add(e2.getExpoX()));
                    item.setMaxyIndex(e.getExpoY().add(e2.getExpoY()));
                    item.setMaxzIndex(e.getExpoZ().add(e2.getExpoZ()));
                    thisItem = thisItem.add(item);
                }
            }
        }
        thisItem.setMaxxIndex(thisMaxxIndex);
        thisItem.setMaxyIndex(thisMaxyIndex);
        thisItem.setMaxzIndex(thisMaxzIndex);
        return thisItem;
    }

    public Item add(Item otherItem) {
        Item thisItem = new Item("");
        BigInteger thisMaxxIndex;
        BigInteger thisMaxyIndex;
        BigInteger thisMaxzIndex;
        if (this.maxxIndex.compareTo(otherItem.getMaxxIndex()) < 0) {
            thisMaxxIndex = otherItem.getMaxxIndex();
        }
        else {
            thisMaxxIndex = this.maxxIndex;
        }
        if (this.maxyIndex.compareTo(otherItem.getMaxyIndex()) < 0) {
            thisMaxyIndex = otherItem.getMaxyIndex();
        }
        else {
            thisMaxyIndex = this.maxyIndex;
        }
        if (this.maxzIndex.compareTo(otherItem.getMaxzIndex()) < 0) {
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
        }
        thisItem.setMaxxIndex(thisMaxxIndex);
        thisItem.setMaxyIndex(thisMaxyIndex);
        thisItem.setMaxzIndex(thisMaxzIndex);
        return thisItem;
    }

    public Item pow(Item otherItem) {
        Item thisItem = new Item("1");
        Item item0 = new Item("");
        item0.setExpression(this.getExpression());
        item0.setMaxxIndex(this.getMaxxIndex());
        item0.setMaxyIndex(this.getMaxyIndex());
        item0.setMaxzIndex(this.getMaxzIndex());
        BigInteger i = new BigInteger("0");
        Expo expo0 = new Expo(new BigInteger("0"), new BigInteger("0"), new BigInteger("0"));
        BigInteger sum = otherItem.getExpression().get(expo0);
        //System.out.println(sum);
        /*while (sum.compareTo(new BigInteger("0")) > 0) {
            if (sum.and(new BigInteger("1")).equals(new BigInteger("1"))) {
                thisItem = thisItem.mul(item0);
                //System.out.println(thisItem);
            }
            item0 = item0.mul(item0);
            sum = sum.shiftRight(1);
        }*/
        for (; i.compareTo(otherItem.getExpression().get(expo0)) < 0;
             i = i.add(new BigInteger("1"))) {
            thisItem = thisItem.mul(this);
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
                if (o.getExpoX().equals(new BigInteger("0")) &&
                        o.getExpoY().equals(new BigInteger("0")) &&
                        o.getExpoZ().equals(new BigInteger("0"))) {
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
                if (o.getExpoX().equals(new BigInteger("0")) &&
                        o.getExpoY().equals(new BigInteger("0")) &&
                        o.getExpoZ().equals(new BigInteger("0"))) {
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
        sb.append("x");
        sb.append("**");
        sb.append(o.getExpoX().toString());
        sb.append("*");
        sb.append("y");
        sb.append("**");
        sb.append(o.getExpoY().toString());
        sb.append("*");
        sb.append("z");
        sb.append("**");
        sb.append(o.getExpoZ().toString());
    }

}
