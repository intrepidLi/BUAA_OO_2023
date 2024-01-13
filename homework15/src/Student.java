import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Iterator;

public class Student {
    private String schoolName;
    private ArrayList<Book> validInterList; // 发出的有效的校际借阅清单

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
        Student student = (Student) o;
        return schoolName.equals(student.schoolName) && id.equals(student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schoolName, id);
    }

    public Student(String id, String schoolName) {
        this.id = id;
        this.schoolName = schoolName;
        this.orderList = new ArrayList<>();
        this.books = new HashMap<>();
        this.oneDayList = new HashMap<>();
        this.validInterList = new ArrayList<>();
        this.orderNewBookList = new ArrayList<>();
        this.bookTimes = new HashMap<>();
    }

    public Student(String studentInfo) {
        String[] str1 = studentInfo.split("-");
        this.schoolName = str1[0];
        this.id = str1[1];
        this.orderList = new ArrayList<>();
        this.books = new HashMap<>();
        this.orderNewBookList = new ArrayList<>();
        this.oneDayList = new HashMap<>();
        this.validInterList = new ArrayList<>();
        this.bookTimes = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private HashMap<Book, Integer> books; // 书和书的状态
    // 0: 完好，1：损毁，2：丢失
    private HashMap<Book, Integer> bookTimes; // 开始借书的日期
    private ArrayList<Book> orderList;
    private ArrayList<Book> orderNewBookList;
    private HashMap<String, Integer> oneDayList;

    public void borrowBook(Book book, String time) {
        books.put(book, 0);
        int days = MainClass.rangeDays(time);
        bookTimes.put(book, days);
    }

    public boolean haveBbook() {
        for (Book book : books.keySet()) {
            if (Objects.equals(book.getType(), "B")) {
                return true;
            }
        }
        return false;
    }

    public void removeInterBook(Book book) {
        validInterList.remove(book);
    }

    public ArrayList<Book> getOrderNewBookList() {
        return orderNewBookList;
    }

    public void addOrder(Book book, String time, OrderLibrarian orderLibrarian) {
        if (oneDayList.get(time) == null) {
            orderNewBookList.clear();
            oneDayList.put(time, 1);
        } else if (oneDayList.get(time) > 0) {
            int old = oneDayList.get(time);
            oneDayList.put(time, old + 1);
        }
        if (oneDayList.get(time) <= 3 && !orderList.contains(book)) {
            orderList.add(book);
            orderLibrarian.addOrder(book, this);
            Output.outputOrderLibrarian(time, this, book);
        }
    }

    public void addNewBookOrder(Book book, String time) {
        if (oneDayList.get(time) <= 3 && !orderNewBookList.contains(book)) {
            orderNewBookList.add(book);
        }
    }

    public void removeOrder(Book book) {
        orderList.remove(book);
    }

    public void removeNewBookOrder(Book book) {
        orderNewBookList.remove(book);
    }

    public boolean containsBook(Book book) {
        if (books.containsKey(book)) {
            return true;
        } else {
            return false;
        }
    }

    public void smearBook(Book book) {
        books.put(book, 1); // 表示该书已经损毁
    }

    public void loseBook(Book book) {
        books.put(book, 2); // 表示该书已经丢失
        bookTimes.remove(book);
    }

    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public void setBooks(HashMap<Book, Integer> books) {
        this.books = books;
    }

    public ArrayList<Book> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<Book> orderList) {
        this.orderList = orderList;
    }

    public void removeBorder(OrderLibrarian orderLibrarian) {
        Iterator<Book> it = orderList.iterator();
        while (it.hasNext()) {
            Book book1 = it.next();
            if (book1.getType().equals("B")) {
                it.remove();
            }
        }
        orderLibrarian.removeBorder(this);
    }

    public void removeBook(Book book) {
        books.remove(book);
        bookTimes.remove(book);
    }

    public boolean haveValidInterB() {
        for (Book book : validInterList) {
            if (book.getType().equals("B")) {
                return true;
            }
        }
        return false;
    }

    public boolean interContainBook(Book book) {
        for (Book book1 : validInterList) {
            if (book1.equals(book)) {
                return true;
            }
        }
        return false;
    }

    public void validInterAdd(Book book) {
        validInterList.add(book);
    }

    @Override
    public String toString() {
        return schoolName + "-" + id;
    }

    public Book findCertainBook(Book book) {
        for (Book book1 : books.keySet()) {
            if (book1.getType().equals(book.getType()) &&
                    book1.getSerial().equals(book.getSerial())) {
                return book1;
            }
        }
        return book;
    }

    public boolean checkBeyondDate(Book book, String time) {
        int duringDays = book.getDuringDays();
        int days = MainClass.rangeDays(time);
        if (days - bookTimes.get(book) > duringDays) {
            return true;
        } else {
            return false;
        }
    }

    public void getOrderedBook() {

    }
}
