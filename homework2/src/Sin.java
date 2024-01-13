import java.util.Objects;

public class Sin {
    private String str;
    private int index;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Sin(int i, String s) {
        this.index = i;
        this.str = s;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sin sin = (Sin) o;
        return index == sin.index && str.equals(sin.str);
    }

    @Override
    public int hashCode() {
        return Objects.hash(str, index);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
