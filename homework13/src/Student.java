import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Iterator;

public class Student {
    public Student(String id) {
        this.id = id;
        this.orderList = new ArrayList<>();
        this.books = new HashMap<>();
        this.oneDayList = new HashMap<>();
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
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
    private ArrayList<Book> orderList;
    private HashMap<String, Integer> oneDayList;

    public void borrowBook(Book book) {
        books.put(book, 0);
    }

    public boolean haveBbook() {
        for (Book book : books.keySet()) {
            if (Objects.equals(book.getType(), "B")) {
                return true;
            }
        }
        return false;
    }

    public void addOrder(Book book, String time, OrderLibrarian orderLibrarian) {
        if (oneDayList.get(time) == null) {
            oneDayList.put(time, 1);
        } else if (oneDayList.get(time) > 0) {
            int old = oneDayList.get(time);
            oneDayList.put(time, old + 1);
        }
        if (oneDayList.get(time) <= 3 && !orderList.contains(book)) {
            orderList.add(book);
            orderLibrarian.addOrder(book, this);
            System.out.printf("%s %s ordered %s from ordering librarian%n",
                    time, id, book.toString());
        }
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
    }
}
