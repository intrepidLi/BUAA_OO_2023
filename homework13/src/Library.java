import java.util.HashMap;
import java.util.Iterator;

public class Library {
    private HashMap<Book, Integer> books; // 在书架上的书
    private ServiceMachine serviceMachine;
    private BorrowAndReturnLibrarian borrowAndReturnLibrarian;
    private OrderLibrarian orderLibrarian;
    private LogisticsDivision logisticsDivision;

    public Library() {
        books = new HashMap<>();
        serviceMachine = new ServiceMachine();
        borrowAndReturnLibrarian = new BorrowAndReturnLibrarian();
        orderLibrarian = new OrderLibrarian();
        logisticsDivision = new LogisticsDivision();
    }

    public void addBook(Book book, int copyNum) {
        books.put(book, copyNum);
    }

    public void borrowBook(Book book, Student student, String time) {
        // 首先 询问自助服务机
        System.out.printf("%s %s queried %s from self-service machine%n", time,
                student.getId(), book.toString());
        // 有余本在书架上
        // A类书不用管
        if (books.get(book) > 0) {
            // B类书找借还管理员
            if (book.getType().equals("B")) {
                if (student.haveBbook()) {
                    borrowAndReturnLibrarian.addBook(book); // 借书失败，书放在借还管理员处
                } else { // 借书成功
                    System.out.printf("%s %s borrowed %s from borrowing and returning librarian%n"
                            , time, student.getId(), book.toString());
                    student.borrowBook(book);
                    student.removeBorder(orderLibrarian); // 借到B类后将所有B类预定删除
                }
            } else if (book.getType().equals("C")) { // C类书找机器
                if (student.containsBook(book)) {
                    serviceMachine.addBook(book); // 借书失败，书放在机器处
                } else { // 借书成功
                    System.out.printf("%s %s borrowed %s from self-service machine%n",
                            time, student.getId(), book.toString());
                    student.borrowBook(book);
                }
            }
            books.put(book, books.get(book) - 1);// 在架上的该书数量 - 1
        } else { // 要借的书没有余量，找预定管理员
            // B类书 预定
            if (book.getType().equals("B")) {
                if (!student.haveBbook()) {
                    student.addOrder(book, time, orderLibrarian);
                } // C类书预定
            } else if (book.getType().equals("C")) {
                if (!student.containsBook(book)) {
                    student.addOrder(book, time, orderLibrarian);
                }
            }
        }
    }

    public void smearBook(Book book, Student student) {
        student.smearBook(book);
    }

    public void loseBook(Book book, Student student, String time) {
        student.loseBook(book);
        System.out.printf("%s %s got punished by borrowing and returning librarian%n",
                time, student.getId());
        student.removeBook(book);
    }

    public void returnBook(Book book, Student student, String time) {
        // 图书完好
        if (book.getType().equals("B")) {
            if (student.getBooks().get(book) == 0) {
                borrowAndReturnLibrarian.addBook(book);
                System.out.printf("%s %s returned %s to borrowing and returning librarian%n",
                        time, student.getId(), book.toString());
            } else if (student.getBooks().get(book) == 1) { // 图书损毁
                System.out.printf("%s %s got punished by borrowing and returning librarian%n",
                        time, student.getId());
                System.out.printf("%s %s returned %s to borrowing and returning librarian%n",
                        time, student.getId(), book.toString());
                logisticsDivision.addBook(book);
                System.out.printf("%s %s got repaired by logistics division%n",
                        time, book.toString());
            }
        } else if (book.getType().equals("C")) {
            if (student.getBooks().get(book) == 0) {
                serviceMachine.addBook(book);
                System.out.printf("%s %s returned %s to self-service machine%n",
                        time, student.getId(), book.toString());
            } else if (student.getBooks().get(book) == 1) {
                System.out.printf("%s %s got punished by borrowing and returning librarian%n",
                        time, student.getId());
                System.out.printf("%s %s returned %s to self-service machine%n",
                        time, student.getId(), book.toString());
                logisticsDivision.addBook(book);
                System.out.printf("%s %s got repaired by logistics division%n",
                        time, book.toString());
            }
        }
        student.removeBook(book);
    }

    public void arrangeLib(String time) {
        // System.out.println("Enter arrangeLib!!!");
        HashMap<Book, Integer> availableBooks = new HashMap<>();
        logisticsDivision.getBooks().forEach((key, value) -> availableBooks.merge(key, value,
                Integer::sum));
        serviceMachine.getBooks().forEach((key, value) -> availableBooks.merge(key, value,
                Integer::sum));
        borrowAndReturnLibrarian.getBooks().forEach((key, value) -> availableBooks.merge(key, value,
                Integer::sum));
        logisticsDivision.getBooks().clear();
        serviceMachine.getBooks().clear();
        borrowAndReturnLibrarian.getBooks().clear();
        orderDeliver(availableBooks, time);
        availableBooks.forEach((key, value) -> books.merge(key, value,
                Integer::sum));
    }

    public void orderDeliver(HashMap<Book, Integer> availableBooks, String time) {
        // System.out.println("Enter orderDeliver!!!");
        for (int i = 0; i < orderLibrarian.getOrderRequests().size(); i++) {
            OrderRequest orderRequest1 = orderLibrarian.getOrderRequests().get(i);
            if (orderRequest1.getBook().getType().equals("B")) {
                Student student1 = orderRequest1.getStudent();
                for (int j = i + 1; j < orderLibrarian.getOrderRequests().size(); j++) {
                    OrderRequest orderRequestj = orderLibrarian.getOrderRequests().get(j);
                    if (orderRequestj.getStudent().equals(student1)) {
                        if (orderRequestj.getBook().getType().equals("B")) {
                            orderRequestj.setHasDelete(true);
                        }
                    }
                }
            }
        }
        Iterator<OrderRequest> it = orderLibrarian.getOrderRequests().iterator();
        while (it.hasNext()) {
            OrderRequest orderRequest = it.next();
            Book book1 = orderRequest.getBook();
            if (orderRequest.isHasDelete()) {
                it.remove();
                orderRequest.getStudent().getOrderList().remove(book1);
            } else if (availableBooks.get(book1) == null) {
                continue;
            } else if (availableBooks.get(book1) == 0) {
                availableBooks.remove(book1);
                continue;
            } else if (availableBooks.get(book1) != null && availableBooks.get(book1) > 0) {
                it.remove();
                System.out.println("[" + time + "] " + orderRequest.toString());
                orderRequest.getStudent().getOrderList().remove(book1);
                orderRequest.getStudent().borrowBook(book1);
                availableBooks.put(book1, availableBooks.get(book1) - 1);
            }
        }
    }
}
