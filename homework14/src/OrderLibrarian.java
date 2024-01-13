import java.util.ArrayList;
import java.util.Iterator;

public class OrderLibrarian {
    private ArrayList<String> requests;
    private ArrayList<Book> orderBooks;
    private ArrayList<Student> orderStudents;

    public ArrayList<OrderRequest> getOrderRequests() {
        return orderRequests;
    }

    private ArrayList<OrderRequest> orderRequests;

    public ArrayList<String> getRequests() {
        return requests;
    }

    public ArrayList<Book> getOrderBooks() {
        return orderBooks;
    }

    public ArrayList<Student> getOrderStudents() {
        return orderStudents;
    }

    public OrderLibrarian() {
        requests = new ArrayList<>();
        orderBooks = new ArrayList<>();
        orderStudents = new ArrayList<>();
        orderRequests = new ArrayList<>();
    }

    public void addOrder(Book book, Student student) {
        orderBooks.add(book);
        orderStudents.add(student);
        String str = student.getId() + " borrowed " +
                book.toString() + " from ordering librarian";
        requests.add(str);
        orderRequests.add(new OrderRequest(book, student));
    }

    public void removeBorder(Student student) {
        /*Iterator<Student> it = orderStudents.iterator();
        int i = 0;
        while (it.hasNext()) {
            Student student1 = it.next();
            if (student1.equals(student)) {
                if (orderBooks.get(i).getType().equals("B")) {
                    orderBooks.remove(i);
                    requests.remove(i);
                    it.remove();
                }
            }
            i++;
        }*/
        Iterator<OrderRequest> it1 = orderRequests.iterator();
        while (it1.hasNext()) {
            OrderRequest orderRequest = it1.next();
            if (orderRequest.getStudent().equals(student)) {
                if (orderRequest.getBook().getType().equals("B")) {
                    it1.remove();
                }
            }
        }
    }
}
