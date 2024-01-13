import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
    private String time;
    private int year;
    private int month;
    private int day;
    private String studentInfo;
    private String operation;
    private Book book;

    public String getTime() {
        return time;
    }

    public String getStudentInfo() {
        return studentInfo;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public Book getBook() {
        return book;
    }

    public int getYear() {
        return year;
    }

    public String getOperation() {
        return operation;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Request(String str) {
        String[] str1 = str.split(" ");
        this.time = str1[0];
        this.studentInfo = str1[1];
        this.operation = str1[2];
        // System.out.println("operation is " + operation);
        String bookstr = str1[3];
        // SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd]");
        Book book = new Book(bookstr);
        this.book = book;
    }

    public Request(String time, String studentInfo, Book book, String operation) {
        this.time = time;
        this.studentInfo = studentInfo;
        this.book = book;
        this.operation = operation;
        String pattern = "\\[(\\d+)\\-(\\d+)\\-(\\d+)\\]";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(time);
        // String[] yearMonthDay = time.split("-");
        if (m.matches()) {
            this.year = Integer.parseInt(m.group(1));
            this.month = Integer.parseInt(m.group(2));
            // System.out.println(month);
            this.day = Integer.parseInt(m.group(3));
            // System.out.println(day);
        }
    }
}
