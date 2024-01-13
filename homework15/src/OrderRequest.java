public class OrderRequest {
    private Book book;
    private Student student;
    private boolean hasDelete;

    public boolean isHasDelete() {
        return hasDelete;
    }

    public void setHasDelete(boolean hasDelete) {
        this.hasDelete = hasDelete;
    }

    public Book getBook() {
        return book;
    }

    public Student getStudent() {
        return student;
    }

    public OrderRequest(Book book, Student student) {
        this.book = book;
        this.student = student;
        this.hasDelete = false;
    }

    @Override
    public String toString() {
        return student.toString() + " borrowed " +
                book.toString() + " from ordering librarian";
    }
}
