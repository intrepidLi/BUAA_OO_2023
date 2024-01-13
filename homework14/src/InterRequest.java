public class InterRequest { // 用于记录校际运输的请求
    private String startSchool;
    private String endSchool;
    private Book book;

    public String getStartSchool() {
        return startSchool;
    }

    public String getEndSchool() {
        return endSchool;
    }

    public Book getBook() {
        return book;
    }

    public InterRequest(String startSchool, String endSchool, Book book) {
        this.startSchool = startSchool;
        this.endSchool = endSchool;
        this.book = book;
    }
}
