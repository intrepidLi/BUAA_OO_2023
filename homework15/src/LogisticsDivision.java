import java.util.HashMap;

public class LogisticsDivision {
    private HashMap<Book, Integer> books;

    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public void setBooks(HashMap<Book, Integer> books) {
        this.books = books;
    }

    public LogisticsDivision() {
        this.books = new HashMap<>();
    }

    public void addBook(Book book) {
        if (!books.containsKey(book)) {
            books.put(book, 1);
        } else {
            int oldNum = books.get(book);
            books.put(book, oldNum + 1);
        }
    }
}
