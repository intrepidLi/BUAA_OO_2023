import java.util.ArrayList;
import java.util.HashMap;

public class PurchasingDepartment {
    private ArrayList<Book> shoppingBooks; // 加购图书清单，需要每个整理日更新
    private ArrayList<Integer> shoppingBookNums; // 加购图书的数量
    private HashMap<Book, Integer> returningList; // 校际还书清单，需要每天更新，输出
    private HashMap<Book, Integer> returningInList; // 校际还书清单，输入
    private HashMap<Book, Integer> tempStoreBooks; // 外校还回来的书临时暂存
    // 图书管理处所有的书
    private ArrayList<Request> interSchoolRequests; // 校际借书清单, 需要每天更新, 作为输入方
    private ArrayList<Request> shoppingRequests; // 需要二次遍历的购书清单
    private ArrayList<InterRequest> outInterBooks; // 校际借书清单，需要每天更新，作为输出方
    private static int[] monthDay = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public ArrayList<Book> getShoppingBooks() {
        return shoppingBooks;
    }

    public HashMap<Book, Integer> getReturningList() {
        return returningList;
    }

    public ArrayList<Request> getInterSchoolRequests() {
        return interSchoolRequests;
    }

    public ArrayList<InterRequest> getOutInterBooks() {
        return outInterBooks;
    }

    public PurchasingDepartment() {
        this.shoppingBooks = new ArrayList<>();
        this.shoppingBookNums = new ArrayList<>();
        this.interSchoolRequests = new ArrayList<>();
        this.returningInList = new HashMap<>();
        this.returningList = new HashMap<>();
        this.outInterBooks = new ArrayList<>();
        this.shoppingRequests = new ArrayList<>();
        this.tempStoreBooks = new HashMap<>();
    }

    public void interAddRequest(Request request) {
        interSchoolRequests.add(request); // 校际借阅清单加入新请求
    }

    public void mergeReturnBooks() {
        returningInList.forEach((key, value) -> tempStoreBooks.merge(key, value,
                Integer::sum));
    }

    public HashMap<Book, Integer> getTempStoreBooks() {
        return tempStoreBooks;
    }

    public ArrayList<Integer> getShoppingBookNums() {
        return shoppingBookNums;
    }

    public void shoppingAdd(Book book) {
        for (int i = 0; i < shoppingBooks.size(); i++) {
            if (shoppingBooks.get(i).equals(book)) {
                shoppingBookNums.set(i,
                        shoppingBookNums.get(i) + 1);
                return;
            }
        }
        // book.setSchoolName(schoolName);
        shoppingBooks.add(book);
        shoppingBookNums.add(1);
    }

    public HashMap<Book, Integer> getReturningInList() {
        return returningInList;
    }

    /*public void shoppingAdd(Book book) {
        if (shoppingList.containsKey(book)) {
            int oldNum = shoppingList.get(book);
            int newNum = oldNum + 1;
            shoppingList.put(book, newNum);
        } else {
            shoppingList.put(book, 1);
        }
    }

    public void returningAdd(Book book) {
        if (returningList.containsKey(book)) {
            int oldNum = returningList.get(book);
            int newNum = oldNum + 1;
            returningList.put(book, newNum);
        } else {
            returningList.put(book, 1);
        }
    }*/

    public void mapAdd(HashMap<Book, Integer> map, Book book) {
        if (map.containsKey(book)) {
            int oldNum = map.get(book);
            int newNum = oldNum + 1;
            map.put(book, newNum);
        } else {
            map.put(book, 1);
        }
    }

    public ArrayList<Request> getShoppingRequests() {
        return shoppingRequests;
    }

    public void outInterAdd(InterRequest interRequest) {
        outInterBooks.add(interRequest);
    }

    public void shoppingRequestsAdd(Request request) {
        shoppingRequests.add(request);
    }
}
