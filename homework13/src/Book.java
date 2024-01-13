import java.util.Objects;

public class Book {
    private String type;
    // ??? 序列号是否为 int
    private String serial;
    private int copyNum; // 副本数

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public int getCopyNum() {
        return copyNum;
    }

    public Book(String bookstr) {
        String[] str1 = bookstr.split("-");
        this.type = str1[0];
        this.serial = str1[1];
    }

    public Book(String bookstr, int copyNum) {
        String[] str1 = bookstr.split("-");
        this.type = str1[0];
        this.serial = str1[1];
        this.copyNum = copyNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        return Objects.equals(serial, book.serial) && type.equals(book.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, serial);
    }

    public void setCopyNum(int copyNum) {
        this.copyNum = copyNum;
    }

    public String toString() {
        return type + "-" + serial;
    }
}