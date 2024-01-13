import java.util.Objects;

public class Book {
    private String type;
    // ??? 序列号是否为 int
    private String serial;
    private int copyNum; // 副本数
    private boolean interSchool; // 可否校际借阅
    private String schoolName;

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

    public boolean isInterSchool() {
        return interSchool;
    }

    public void setInterSchool(boolean interSchool) {
        this.interSchool = interSchool;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Book(String bookstr) {
        String[] str1 = bookstr.split("-");
        this.type = str1[0];
        this.serial = str1[1];
        this.copyNum = 0;
        this.schoolName = null;
    }

    public Book(String bookstr, int copyNum, boolean interSchool, String schoolName) {
        String[] str1 = bookstr.split("-");
        this.type = str1[0];
        this.serial = str1[1];
        this.copyNum = copyNum;
        this.interSchool = interSchool;
        this.schoolName = schoolName;
    }

    public String getSchoolName() {
        return schoolName;
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
        return type.equals(book.type) && serial.equals(book.serial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, serial);
    }

    public void setCopyNum(int copyNum) {
        this.copyNum = copyNum;
    }

    @Override
    public String toString() {
        return schoolName + "-" + type + "-" + serial;
    }
}