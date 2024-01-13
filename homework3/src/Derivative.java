import java.math.BigInteger;
import java.util.HashMap;
import java.util.Set;

public class Derivative {
    private Item item;

    public Derivative(Item item) {
        this.item = item;
    }

    public static String getFun(String input) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();// 记录导函数
        int length = input.length();
        int index;
        int pos = 0;
        int cnpt = 0;
        String c;
        for (index = 0; index < length; index++) {
            StringBuilder sb1 = new StringBuilder();
            c = String.valueOf(input.charAt(index));
            if (c.equals("d")) {
                sb2.append(c);
                index++;
                sb2.append(input.charAt(index));
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
                    } else if (input.charAt(pos) == ')') {
                        cnpt--;
                        if (cnpt == 0) {
                            break;
                        }
                    }
                    sb1.append(input.charAt(pos));
                }
                pos--;
                String str = sb1.toString();
                index = ++pos;
                sb.append(str);
                break;
            }
        }
        return sb2.toString() + sb.toString();
    }

    public static Item diffFun(String diffName, Expo expo) {
        if (diffName.equals("dx")) {
            return difnamex(diffName, expo);
        }
        else if (diffName.equals("dy")) {
            return difnamey(diffName, expo);
        }
        else {
            return difnamez(diffName, expo);
        }
    }

    private static Item difnamex(String diffName, Expo expo) {
        Item resItem = new Item("");
        if (expo.getExpoX() != 0) {
            HashMap<Item, Integer> newSinMap = new HashMap<>();
            newSinMap.putAll(expo.getSinMap());
            HashMap<Item, Integer> newCosMap = new HashMap<>();
            newCosMap.putAll(expo.getCosMap());
            Expo newexpo = new Expo(expo.getExpoX() - 1, expo.getExpoY(), expo.getExpoZ(),
                    newSinMap, newCosMap);
            resItem.getExpression().put(newexpo,
                    new BigInteger(String.valueOf(expo.getExpoX()))); // 先把xyz指数求导后放入
        }
        Set<Item> sinKeyset = expo.getSinMap().keySet(); // 遍历单项式中的sin
        for (Item i : sinKeyset) {
            if (expo.getSinMap().get(i) != 0 &&
                    i.toString().contains("x")) {
                Item newItem = i.diff(diffName);
                Item oldItem = sinDiff(expo, i);
                Item thisItem = oldItem.mul(newItem);
                resItem = resItem.add(thisItem);
            }
        }

        Set<Item> cosKeyset = expo.getCosMap().keySet(); // 遍历单项式中的cos
        for (Item i : cosKeyset) {
            if (expo.getCosMap().get(i) != 0 &&
                    i.toString().contains("x")) {
                Item oldItem = cosDiff(expo, i);
                Item newItem = i.diff(diffName);
                Item thisItem = oldItem.mul(newItem);
                resItem = resItem.add(thisItem);
            }
        }
        return resItem;
    }

    private static Item difnamey(String diffName, Expo expo) {
        Item resItem = new Item("");
        if (expo.getExpoY() != 0) {
            HashMap<Item, Integer> newSinMap = new HashMap<>();
            newSinMap.putAll(expo.getSinMap());
            HashMap<Item, Integer> newCosMap = new HashMap<>();
            newCosMap.putAll(expo.getCosMap());
            Expo newexpo = new Expo(expo.getExpoX(), expo.getExpoY() - 1, expo.getExpoZ(),
                    newSinMap, newCosMap);
            resItem.getExpression().put(newexpo,
                    new BigInteger(String.valueOf(expo.getExpoY()))); // 先把xyz指数求导后放入
        }
        Set<Item> sinKeyset = expo.getSinMap().keySet(); // 遍历单项式中的sin
        for (Item i : sinKeyset) {
            if (expo.getSinMap().get(i) != 0 &&
                    i.toString().contains("y")) {
                Item newItem = i.diff(diffName);
                Item oldItem = sinDiff(expo, i);
                Item thisItem = oldItem.mul(newItem);
                resItem = resItem.add(thisItem);
            }
        }

        Set<Item> cosKeyset = expo.getCosMap().keySet(); // 遍历单项式中的cos
        for (Item i : cosKeyset) {
            if (expo.getCosMap().get(i) != 0 &&
                    i.toString().contains("y")) {
                Item oldItem = cosDiff(expo, i);
                Item newItem = i.diff(diffName);
                Item thisItem = oldItem.mul(newItem);
                resItem = resItem.add(thisItem);
            }
        }
        return resItem;
    }

    private static Item difnamez(String diffName, Expo expo) {
        Item resItem = new Item("");
        if (expo.getExpoZ() != 0) {
            HashMap<Item, Integer> newSinMap = new HashMap<>();
            newSinMap.putAll(expo.getSinMap());
            HashMap<Item, Integer> newCosMap = new HashMap<>();
            newCosMap.putAll(expo.getCosMap());
            Expo newexpo = new Expo(expo.getExpoX(), expo.getExpoY(), expo.getExpoZ() - 1,
                    newSinMap, newCosMap);
            resItem.getExpression().put(newexpo,
                    new BigInteger(String.valueOf(expo.getExpoZ()))); // 先把xyz指数求导后放入
        }
        Set<Item> sinKeyset = expo.getSinMap().keySet(); // 遍历单项式中的sin
        for (Item i : sinKeyset) {
            if (expo.getSinMap().get(i) != 0 &&
                    i.toString().contains("z")) {
                Item newItem = i.diff(diffName);
                Item oldItem = sinDiff(expo, i);
                Item thisItem = oldItem.mul(newItem);
                resItem = resItem.add(thisItem);
            }
        }

        Set<Item> cosKeyset = expo.getCosMap().keySet(); // 遍历单项式中的cos
        for (Item i : cosKeyset) {
            if (expo.getCosMap().get(i) != 0 &&
                    i.toString().contains("z")) {
                Item oldItem = cosDiff(expo, i);
                Item newItem = i.diff(diffName);
                Item thisItem = oldItem.mul(newItem);
                resItem = resItem.add(thisItem);
            }
        }
        return resItem;
    }

    private static Item sinDiff(Expo expo, Item i) {
        HashMap<Item, Integer> newSinmap1 = new HashMap<>();
        HashMap<Item, Integer> newCosmap1 = new HashMap<>();
        newSinmap1.putAll(expo.getSinMap());
        newCosmap1.putAll(expo.getCosMap());
        newSinmap1.put(i, expo.getSinMap().get(i) - 1);
        if (newCosmap1.containsKey(i)) {
            newCosmap1.put(i, newCosmap1.get(i) + 1);
        }
        else {
            newCosmap1.put(i, 1);
        }
        Expo oldExpo = new Expo(expo.getExpoX(), expo.getExpoY(), expo.getExpoZ(),
                newSinmap1, newCosmap1);
        Item oldItem = new Item("");
        oldItem.getExpression().put(oldExpo, new BigInteger(String.
                valueOf(expo.getSinMap().get(i))));
        return oldItem;
    }

    private static Item cosDiff(Expo expo, Item i) {
        HashMap<Item, Integer> newSinmap1 = new HashMap<>();
        HashMap<Item, Integer> newCosmap1 = new HashMap<>();
        newSinmap1.putAll(expo.getSinMap());
        newCosmap1.putAll(expo.getCosMap());
        newCosmap1.put(i, expo.getCosMap().get(i) - 1);
        if (newSinmap1.containsKey(i)) {
            newSinmap1.put(i, newSinmap1.get(i) + 1);
        }
        else {
            newSinmap1.put(i, 1);
        }
        Expo oldExpo = new Expo(expo.getExpoX(), expo.getExpoY(), expo.getExpoZ(),
                newSinmap1, newCosmap1);
        Item oldItem = new Item("");
        oldItem.getExpression().put(oldExpo, new BigInteger(String.valueOf(
                expo.getCosMap().get(i) * (-1))));
        return oldItem;
    }
}
