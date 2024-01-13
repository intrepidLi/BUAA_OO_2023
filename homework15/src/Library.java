import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Library {
    private HashMap<Book, Integer> books; // 在书架上的书
    private ServiceMachine serviceMachine;
    private String schoolName;
    private BorrowAndReturnLibrarian borrowAndReturnLibrarian;
    private ArrayList<Request> waitHandleRequests;
    private OrderLibrarian orderLibrarian;
    private LogisticsDivision logisticsDivision;
    private PurchasingDepartment purchasingDepartment;
    private HashMap<Book, Integer> availableBooks;

    public String getSchoolName() {
        return schoolName;
    }

    public Library(String schoolName) {
        books = new HashMap<>();
        availableBooks = new HashMap<>();
        serviceMachine = new ServiceMachine();
        borrowAndReturnLibrarian = new BorrowAndReturnLibrarian();
        orderLibrarian = new OrderLibrarian();
        logisticsDivision = new LogisticsDivision();
        purchasingDepartment = new PurchasingDepartment();
        waitHandleRequests = new ArrayList<>();
        this.schoolName = schoolName;
    }

    public void addBook(Book book, int copyNum) {
        books.put(book, copyNum);
    }

    public void clearOneDay() {
        waitHandleRequests.clear();
    }

    public void orderNewBook() {

    }

    public void borrowBook(Book book2, Student student, String time) {
        // 首先 询问自助服务机
        Output.outputQuery(time, student, book2);
        // 有余本在书架上
        // A类书不用管
        Book book = findBook(book2);
        if (books.containsKey(book) && books.get(book) > 0) {
            // B类书找借还管理员
            if (book.getType().equals("B")) {
                if (student.haveBbook()) {
                    borrowAndReturnLibrarian.addBook(book); // 借书失败，书放在借还管理员处
                    Output.outputRefusedLent(time, book, student,
                            "borrowing and returning librarian");
                } else { // 借书成功
                    Output.outputSucBorrowBook(time, student, book,
                            "borrowing and returning librarian");
                    student.borrowBook(book, time);
                    student.removeBorder(orderLibrarian); // 借到B类后将所有B类预定删除
                }
            } else if (book.getType().equals("C")) { // C类书找机器
                if (student.containsBook(book)) {
                    serviceMachine.addBook(book); // 借书失败，书放在机器处
                    Output.outputRefusedLent(time, book, student,
                            "self-service machine");
                } else { // 借书成功
                    Output.outputSucBorrowBook(time, student, book, "self-service machine");
                    student.borrowBook(book, time);
                }
            }
            books.put(book, books.get(book) - 1);// 在架上的该书数量 - 1
        } else { // 要借的书没有余量，找预定管理员
            Request request = new Request(time, student.toString(), book, "borrowed");
            waitHandleRequests.add(request);
        }
    }

    public void smearBook(Book book, Student student) {
        student.smearBook(book);
    }

    public void loseBook(Book book2, Student student, String time) {
        student.loseBook(book2);
        Book book = student.findCertainBook(book2);
        Output.outputPunishAndFine(time, student, book2, "borrowing and returning librarian");
        student.removeBook(book2);
        int oldCopyNum = book.getCopyNum(); // 更新副本的数量
        int newCopyNum = oldCopyNum - 1;
        /*if (newCopyNum < 0) {
            System.out.println("Error!!! Book has lost too much!!!");
        }*/
        book2.setCopyNum(newCopyNum);
    }

    public void returnBook(Book book2, Student student, String time, ArrayList<Library> libraries) {
        // 是本校的图书则本校正常收集，不是本校的图书全部由管理处收集
        Book book = student.findCertainBook(book2);
        boolean flag = (book.getSchoolName().equals(schoolName));
        if (book.getType().equals("B")) {
            if (student.getBooks().get(book) == 0) { // 图书完好
                if (flag) {
                    borrowAndReturnLibrarian.addBook(book);
                }
                if (student.checkBeyondDate(book, time)) {
                    Output.outputPunishAndFine(time, student,
                            book, "borrowing and returning librarian");
                }
                Output.outputSucReturnBook(time, student, book,
                        "borrowing and returning librarian");
            } else if (student.getBooks().get(book) == 1) { // 图书损毁 或 逾期
                Output.outputPunishAndFine(time, student, book,
                        "borrowing and returning librarian");
                Output.outputSucReturnBook(time, student, book,
                        "borrowing and returning librarian");
                if (flag) {
                    logisticsDivision.addBook(book);
                }
                Output.outputRepairBook(time, book, schoolName);
            }
        } else if (book.getType().equals("C")) {
            if (student.getBooks().get(book) == 0) {
                if (flag) {
                    serviceMachine.addBook(book);
                }
                if (student.checkBeyondDate(book, time)) {
                    Output.outputPunishAndFine(time, student,
                            book, "borrowing and returning librarian");
                }
                Output.outputSucReturnBook(time, student, book, "self-service machine");
            } else if (student.getBooks().get(book) == 1) {
                Output.outputPunishAndFine(time, student, book,
                        "borrowing and returning librarian");
                Output.outputSucReturnBook(time, student, book, "self-service machine");
                if (flag) {
                    logisticsDivision.addBook(book);
                }
                Output.outputRepairBook(time, book, schoolName);
            }
        }
        student.removeBook(book);
        if (!flag) {
            purchasingDepartment.mapAdd(purchasingDepartment.getReturningList(), book);
            student.removeInterBook(book);
            // System.out.println("book1 is " + book);
            // 送回图书馆输入书籍添加
            Library returnToLibrary = MainClass.findLibrary(book.getSchoolName());
            assert returnToLibrary != null;
            PurchasingDepartment purchasingDepartment1 = returnToLibrary.getPurchasingDepartment();
            purchasingDepartment1.mapAdd(purchasingDepartment1.getReturningInList(), book);
            // System.out.println("book2 is " + book);
        }
    }

    public void arrangeLib(String time) {
        // Output.outputArrange(time);
        availableBooks.clear();
        logisticsDivision.getBooks().forEach((key, value) -> availableBooks.merge(key, value,
                Integer::sum));
        serviceMachine.getBooks().forEach((key, value) -> availableBooks.merge(key, value,
                Integer::sum));
        borrowAndReturnLibrarian.getBooks().forEach((key, value) -> availableBooks.merge(key, value,
                Integer::sum));
        for (int i = 0; i < purchasingDepartment.getShoppingBooks().size(); i++) {
            Book book = purchasingDepartment.getShoppingBooks().get(i);
            book.setCopyNum(Math.max(3, purchasingDepartment.getShoppingBookNums().get(i)));
            book.setSchoolName(schoolName);
            book.setInterSchool(true);
            Output.outputPurchase(time, book, schoolName, "purchasing department");
            availableBooks.put(book, book.getCopyNum());
        }
        purchasingDepartment.getTempStoreBooks().forEach((key, value) ->
                availableBooks.merge(key, value,
                Integer::sum));
        logisticsDivision.getBooks().clear();
        serviceMachine.getBooks().clear();
        borrowAndReturnLibrarian.getBooks().clear();
        purchasingDepartment.getShoppingBooks().clear();
        purchasingDepartment.getShoppingBookNums().clear();
        purchasingDepartment.getTempStoreBooks().clear();
    }

    public void orderDeliver(String time) { // 发放预定的书
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
                Output.outputSucBorrowBook(time, orderRequest.getStudent(),
                        book1, "ordering librarian");
                orderRequest.getStudent().getOrderList().remove(book1);
                orderRequest.getStudent().borrowBook(book1, time);
                availableBooks.put(book1, availableBooks.get(book1) - 1);
            }
        }
        availableBooks.forEach((key, value) -> books.merge(key, value,
                Integer::sum));
    }

    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public PurchasingDepartment getPurchasingDepartment() {
        return purchasingDepartment;
    }

    public void handleRest(ArrayList<Library> libraries, HashMap<String, Student> students) {
        for (Request request : waitHandleRequests) {
            Book book = request.getBook();
            String time = request.getTime();
            String studentInfo = request.getStudentInfo();
            Student student = new Student(studentInfo);
            if (students.get(studentInfo) != null) {
                student = students.get(studentInfo);
            }
            if (!preCheckValidity(book, student)) {
                continue;
            }
            boolean completeFlag = false;
            for (Library library : libraries) {
                if (this.equals(library)) {
                    continue;
                }
                for (Book book1 : library.getBooks().keySet()) {
                    if (book1.getType().equals(book.getType()) &&
                            book1.getSerial().equals(book.getSerial())
                        && library.getBooks().get(book1) > 0 && book1.isInterSchool()) {
                        completeFlag = true; // 可以走校际借阅流程的书
                        int oldNum = library.getBooks().get(book1);
                        library.getBooks().put(book1, oldNum - 1);
                        request.setBook(book1);
                        purchasingDepartment.interAddRequest(request); // 该图书馆图书管理处得到书
                        // 需要在第二天输入的书，更改相应图书馆的输出书籍清单
                        String startSchool = library.getSchoolName();
                        String endSchool = this.schoolName;
                        InterRequest interRequest = new InterRequest(startSchool, endSchool, book1);
                        library.getPurchasingDepartment().outInterAdd(interRequest);
                        student.validInterAdd(book1);
                    }
                }
            }
            if (!completeFlag) { // 如果校际借阅无法满足条件
                // 检查本校是否有馆藏,有馆藏则校内预约，无管藏则管理处加购
                if (this.getBooks().containsKey(book) && book.getCopyNum() > 0) {
                    // B类书 预定
                    if (book.getType().equals("B")) {
                        if (!student.haveBbook()) {
                            student.addOrder(book, time, orderLibrarian);
                            // Output.outputOrderLibrarian(time, student, book);
                        } // C类书预定
                    } else if (book.getType().equals("C")) {
                        if (!student.containsBook(book)) {
                            student.addOrder(book, time, orderLibrarian);
                            // Output.outputOrderLibrarian(time, student, book);
                        }
                    }
                } else { // 加购清单上添加该本书
                    book.setSchoolName(schoolName);
                    student.addOrder(book, time, orderLibrarian);
                    student.addNewBookOrder(book, time);
                    purchasingDepartment.shoppingRequestsAdd(request);
                }
            }
        }
        checkShopping(students);
        waitHandleRequests.clear(); // 每次处理完所有的等待清单请求后清空
        purchasingDepartment.getShoppingRequests().clear();
    }

    private void checkShopping(HashMap<String, Student> students) {
        for (Request request : purchasingDepartment.getShoppingRequests()) {
            Book book = request.getBook();
            String studentInfo = request.getStudentInfo();
            Student student = new Student(studentInfo);
            if (students.get(studentInfo) != null) {
                student = students.get(studentInfo);
            }
            if (!preCheckValidity(book, student)) {
                student.removeOrder(book);
                student.removeNewBookOrder(book);
                continue;
            }
            if (student.getOrderNewBookList().contains(book)) {
                purchasingDepartment.shoppingAdd(book);
            }
        }
    }

    private boolean preCheckValidity(Book book, Student student) { // 先检查一下该学生手里的书是否满足借书要求
        if (book.getType().equals("B")) {
            return !student.haveBbook() && !student.haveValidInterB();
        } else if (book.getType().equals("C")) {
            return !student.containsBook(book) && !student.interContainBook(book);
        }
        return true;
    }

    public void outputOutBooks(String time) {
        for (InterRequest interRequest : purchasingDepartment.getOutInterBooks()) { // 往外借的书
            Output.outputTransport(time, interRequest.getBook(), schoolName);
            Output.outputDesign("NotBorrow", "Transport", interRequest.getBook(),
                    time);
        }
        // purchasingDepartment.getOutInterBooks().clear();
        // 输出完就清空，一天一更新
        for (Book book : purchasingDepartment.getReturningList().keySet()) {
            for (int i = 0; i < purchasingDepartment.getReturningList().get(book); i++) {
                Output.outputTransport(time, book, schoolName);
                Output.outputDesign("Borrowed", "Transport", book,
                        time);
            }
        }
        // purchasingDepartment.getReturningList().clear();
    }

    public void outputInBooks(String time) {
        for (Request request : purchasingDepartment.getInterSchoolRequests()) {
            Output.outputReceive(time, request.getBook(), schoolName);
            Output.outputDesign("Transport", "Borrowed", request.getBook(),
                    time);
        }
        // purchasingDepartment.getInterSchoolRequests().clear();

        for (Book book : purchasingDepartment.getReturningInList().keySet()) {
            for (int i = 0; i < purchasingDepartment.getReturningInList().get(book); i++) {
                Output.outputReceive(time, book, schoolName);
                Output.outputDesign("Transport", "Collected", book,
                        time);
            }
        }
        // purchasingDepartment.getReturningInList().clear();
    }

    public void interOrderDeliver(HashMap<String, Student> students, String time) {
        for (Request request : purchasingDepartment.getInterSchoolRequests()) {
            Book book = request.getBook();
            Student student = students.get(request.getStudentInfo());
            student.borrowBook(book, time);
            Output.outputSucBorrowBook(time, student, book, "purchasing department");
        }
    }

    public void allClear() {
        purchasingDepartment.getOutInterBooks().clear();
        purchasingDepartment.getReturningList().clear();
        purchasingDepartment.getInterSchoolRequests().clear();
        purchasingDepartment.mergeReturnBooks();
        purchasingDepartment.getReturningInList().clear();
    }

    public Book findBook(Book book) {
        for (Book book1 : books.keySet()) {
            if (book1.equals(book)) {
                return book1;
            }
        }
        return book;
    }
}
