import java.util.HashMap;
import java.util.Objects;

public class Expo {
    private int coe; // 单项式系数
    private int expoX;
    private int expoY;
    private HashMap<Item, Integer> sinMap; // 记录sin函数内的值和指数
    private HashMap<Item, Integer> cosMap; // 记录cos函数内的值和指数
    private int expoZ;

    public int getExpoX() {
        return expoX;
    }

    public HashMap<Item, Integer> getSinMap() {
        return sinMap;
    }

    public void setSinMap(HashMap<Item, Integer> sinMap) {
        this.sinMap = sinMap;
    }

    public HashMap<Item, Integer> getCosMap() {
        return cosMap;
    }

    public void setCosMap(HashMap<Item, Integer> cosMap) {
        this.cosMap = cosMap;
    }

    public void setExpoX(int expoX) {
        this.expoX = expoX;
    }

    public int getExpoY() {
        return expoY;
    }

    public void setExpoY(int expoY) {
        this.expoY = expoY;
    }

    public int getExpoZ() {
        return expoZ;
    }

    public void setExpoZ(int expoZ) {
        this.expoZ = expoZ;
    }

    public Expo(int expoX, int expoY, int expoZ,
                HashMap<Item, Integer> sinMap, HashMap<Item, Integer> cosMap) {
        this.expoX = expoX;
        this.expoY = expoY;
        this.expoZ = expoZ;
        this.sinMap = sinMap;
        this.cosMap = cosMap;
    }

    public void printExpo() {
        System.out.println("Expo:");
        System.out.println(this.expoX);
        System.out.println(this.expoY);
        System.out.println(this.expoZ);
    }

    public boolean isNull() {
        return this.getExpoX() == 0 &&
                this.getExpoY() == 0 &&
                this.getExpoZ() == 0 &&
                this.getCosMap().size() == 0 &&
                this.getSinMap().size() == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Expo expo = (Expo) o;
        return expoX == expo.expoX && expoY == expo.expoY &&
                expoZ == expo.expoZ && sinMap.equals(expo.sinMap) &&
                cosMap.equals(expo.cosMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expoX, expoY, sinMap, cosMap, expoZ);
    }

    /*public Item clone() {
        Item newItem = new Item("");

    }*/
}
