import java.util.Objects;

public class Cos {
    private String str;
    private int index;

    public String getStr() {
        return str;
    }

    public Cos(int i, String s) {
        this.index = i;
        this.str = s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cos cos = (Cos) o;
        return index == cos.index && str.equals(cos.str);
    }

    @Override
    public int hashCode() {
        return Objects.hash(str, index);
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
