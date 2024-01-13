package expr;

import java.math.BigInteger;

public class Expo {
    private BigInteger expoX;
    private BigInteger expoY;
    private BigInteger maxExpox;
    private BigInteger maxExpoy;

    public BigInteger getMaxExpox() {
        return maxExpox;
    }

    public void setMaxExpox(BigInteger maxExpox) {
        this.maxExpox = maxExpox;
    }

    public BigInteger getMaxExpoy() {
        return maxExpoy;
    }

    public void setMaxExpoy(BigInteger maxExpoy) {
        this.maxExpoy = maxExpoy;
    }

    public BigInteger getMaxExpoz() {
        return maxExpoz;
    }

    public void setMaxExpoz(BigInteger maxExpoz) {
        this.maxExpoz = maxExpoz;
    }

    private BigInteger maxExpoz;

    public BigInteger getExpoX() {
        return expoX;
    }

    public void setExpoX(BigInteger expoX) {
        this.expoX = expoX;
    }

    public BigInteger getExpoY() {
        return expoY;
    }

    public void setExpoY(BigInteger expoY) {
        this.expoY = expoY;
    }

    public BigInteger getExpoZ() {
        return expoZ;
    }

    public void setExpoZ(BigInteger expoZ) {
        this.expoZ = expoZ;
    }

    private BigInteger expoZ;

    public Expo(BigInteger expoX, BigInteger expoY, BigInteger expoZ) {
        this.expoX = expoX;
        this.expoY = expoY;
        this.expoZ = expoZ;
    }

    public void printExpo() {
        System.out.println("Expo:");
        System.out.println(this.expoX);
        System.out.println(this.expoY);
        System.out.println(this.expoZ);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = result * 31 + expoX.intValue();
        result = result * 31 + expoY.intValue();
        result = result * 31 + expoZ.intValue();
        return result;
        //return expoX.hashCode() ^ expoY.hashCode() ^ expoZ.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Expo) && ((Expo) obj).expoX.equals(expoX)
                && ((Expo) obj).expoY.equals(expoY)
                && ((Expo) obj).expoZ.equals(expoZ);
    }
}
