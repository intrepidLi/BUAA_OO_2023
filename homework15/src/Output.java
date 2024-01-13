public class Output {
    //询问输出
    public static void outputQuery(String time, Student student, Book book) {
        System.out.printf("%s %s queried %s-%s from self-service machine%n", time,
                student.toString(), book.getType(), book.getSerial());
        Output.outputSequence("Student", "Library", time);
        System.out.printf("%s self-service machine provided information of %s-%s%n", time,
                book.getType(), book.getSerial());
        Output.outputSequence("Library", "Student", time);
    }

    // 借书成功输出
    public static void outputSucBorrowBook(String time, Student student,
                                           Book book, String faculty) {
        System.out.printf("%s %s lent %s to %s%n"
                , time, faculty, book.toString(), student.toString());
        outputDesign("NotBorrow", "Borrowed", book, time);
        Output.outputSequence("Library", "Student", time);
        System.out.printf("%s %s borrowed %s from %s%n"
                , time, student.toString(), book.toString(), faculty);
    }

    // 还书成功输出
    public static void outputSucReturnBook(String time, Student student,
                                           Book book, String faculty) {
        System.out.printf("%s %s returned %s to %s%n",
                time, student.toString(), book.toString(), faculty);
        System.out.printf("%s %s collected %s from %s%n",
                time, faculty, book.toString(), student.toString());
        outputDesign("Borrowed", "Collected", book, time);
    }

    // 惩罚并收取罚金输出
    public static void outputPunishAndFine(String time, Student student,
                                           Book book, String faculty) {
        System.out.printf("%s %s got punished by %s%n",
                time, student.toString(), faculty);
        System.out.printf("%s %s received %s's fine%n",
                time, faculty, student.toString());
    }

    // 第四类校内预定时输出
    public static void outputOrderLibrarian(String time, Student student, Book book) {
        System.out.printf("%s %s ordered %s from ordering librarian%n", time, student, book);
        System.out.printf("%s ordering librarian recorded %s's order of %s%n", time, student, book);
        Output.outputSequence("Library", "Student", time);
    }

    // 后勤处修补书籍输出
    public static void outputRepairBook(String time, Book book, String schoolName) {
        System.out.printf("%s %s got repaired by logistics division in %s%n",
                time, book.toString(), schoolName);
        outputDesign("Collected", "Fixed", book, time);
    }

    public static void outputTransport(String time, Book book, String schoolName) {
        System.out.printf("%s %s got transported by purchasing department in %s%n", time,
                book.toString(), schoolName);
        // outputDesign("Borrowed", "Transport", book, time);
    }

    public static void outputReceive(String time, Book book, String schoolName) {
        System.out.printf("%s %s got received by purchasing department in %s%n", time,
                book.toString(), schoolName);
        // outputDesign("Transport", "Collected", book, time);
    }

    // 整理员整理图书
    public static void outputArrange(String time) {
        System.out.printf("%s arranging librarian arranged all the books%n", time);
    }

    public static void outputPurchase(String time, Book book, String schoolName, String faculty) {
        System.out.printf("%s %s got purchased by %s in %s%n", time
            , book.toString(), faculty, schoolName);
    }

    public static void outputRefusedLent(String time, Book book, Student student, String faculty) {
        System.out.printf("%s %s refused lending %s to %s%n",
                time, faculty, book.toString(), student.toString());
        outputDesign("NotBorrow", "NotBorrow", book, time);
        outputSequence("Library", "Student", time);
    }

    public static void outputDesign(String state1, String state2, Book book, String time) {
        System.out.printf("(State) %s %s-%s transfers from %s to %s%n",
                time, book.getType(), book.getSerial(), state1, state2);
    }

    public static void outputSequence(String sender, String receiver, String time) {
        System.out.printf("(Sequence) %s %s sends a message to %s%n",
                time, sender, receiver);
    }
}
