import java.util.HashMap;
import java.util.Objects;

public class Expo {
    private int coe; // 单项式系数
    private int expoX;
    private int expoY;
    private int maxExpox;
    private int maxExpoy;
    private HashMap<String, Integer> sinMap; // 记录sin函数内的值和指数
    private HashMap<String, Integer> cosMap; // 记录cos函数内的值和指数
    private int maxExpoz;
    private int expoZ;

    public int getExpoX() {
        return expoX;
    }

    public HashMap<String, Integer> getSinMap() {
        return sinMap;
    }

    public void setSinMap(HashMap<String, Integer> sinMap) {
        this.sinMap = sinMap;
    }

    public HashMap<String, Integer> getCosMap() {
        return cosMap;
    }

    public void setCosMap(HashMap<String, Integer> cosMap) {
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

    public int getMaxExpox() {
        return maxExpox;
    }

    public void setMaxExpox(int maxExpox) {
        this.maxExpox = maxExpox;
    }

    public int getMaxExpoy() {
        return maxExpoy;
    }

    public void setMaxExpoy(int maxExpoy) {
        this.maxExpoy = maxExpoy;
    }

    public int getMaxExpoz() {
        return maxExpoz;
    }

    public void setMaxExpoz(int maxExpoz) {
        this.maxExpoz = maxExpoz;
    }

    public int getExpoZ() {
        return expoZ;
    }

    public void setExpoZ(int expoZ) {
        this.expoZ = expoZ;
    }

    public Expo(int expoX, int expoY, int expoZ,
                HashMap<String, Integer> sinMap, HashMap<String, Integer> cosMap) {
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
}
