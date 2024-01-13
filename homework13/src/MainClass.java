import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Date;
import java.util.Scanner;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    private static int n;
    private static int m;
    private static Date lastDate;
    private static String lastTime;
    private static Date startDate;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();
        HashMap<String, Student> students = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            startDate = sdf.parse("2023-01-01");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        n = scanner.nextInt();
        scanner.nextLine();
        // System.out.println("n: " + n);
        for (int i = 0; i < n; i++) {
            String str = scanner.nextLine();
            // System.out.println("str: " + str);
            proInput1(str, library);
        }
        m = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < m; i++) {
            String str = scanner.nextLine();
            proInput2(str, library, i, students);
        }
        // finish(library);
    }

    private static void proInput1(String str, Library library) {
        String[] str1 = str.split(" ");
        int copyNum = Integer.parseInt(str1[1]);
        Book book = new Book(str1[0], copyNum);
        library.addBook(book, copyNum);
    }

    private static void proInput2(String str, Library library,
                                  int flag, HashMap<String, Student> students) {
        String[] str1 = str.split(" ");
        String time = str1[0];
        String studentId = str1[1];
        String operation = str1[2];
        // System.out.println("operation is " + operation);
        String bookstr = str1[3];
        // SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd]");
        Book book = new Book(bookstr);
        Student student = new Student(studentId);
        if (students.containsKey(studentId)) {
            student = students.get(studentId);
        }
        //try {
        // 使用parse方法将字符串转换为Date对象
        // Date date = sdf.parse(time);
        if (!time.equals(lastTime) && flag != 0) {
            int days1 = rangeDays(lastTime);
            int days2 = rangeDays(time);
            // System.out.println("days1 is " + days1);
            // System.out.println("days2 is " + days2);
            if ((days1 / 3) != (days2 / 3)) {
                int days3 = (days1 / 3 + 1) * 3;
                // long time3 = days3 * (24 * 60 * 60 * 1000) + startDate.getTime();
                String newTime = daysToTime(days3);
                // System.out.println("newTime is " + newTime);
                library.arrangeLib(newTime);
            }
        }

        switch (operation) {
            case "borrowed":
                library.borrowBook(book, student, time);
                students.put(studentId, student);
                break;
            case "smeared":
                library.smearBook(book, student);
                break;
            case "lost":
                // System.out.println("Fuck!!! Lost!!!");
                library.loseBook(book, student, time);
                break;
            case "returned":
                library.returnBook(book, student, time);
                break;
            default:
                break;
        }
        lastTime = time;
        //} catch (ParseException e) {
        //e.printStackTrace();
        //}
    }

    private static String dateToString(long time) {
        Date nowDate = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        return String.format("%d-%02d-%02d", year, month, day);
    }

    private static void finish(Library library) {
        int days1 = rangeDays(lastTime);
        if (days1 % 3 != 0) {
            int days3 = (days1 / 3 + 1) * 3;
            String newTime = daysToTime(days3);
            // System.out.println("newTime is " + newTime);
            library.arrangeLib(newTime);
        }
    }

    private static int[] monthDay = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static int[] monthDaySum = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365};

    private static int rangeDays(String time) {
        int day = 0;
        int month = 0;
        // System.out.println("time is " + time);
        String pattern = "\\[(\\d+)\\-(\\d+)\\-(\\d+)\\]";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(time);
        // String[] yearMonthDay = time.split("-");
        if (m.matches()) {
            month = Integer.parseInt(m.group(2));
            // System.out.println(month);
            day = Integer.parseInt(m.group(3));
            // System.out.println(day);
        }
        int days = monthDaySum[month - 1];
        return days + day - 1;
    }

    private static String daysToTime(int days) {
        String resTime = "";
        for (int i = 0; i < 12; i++) {
            if (days == monthDaySum[i]) {
                resTime = String.format("2023-%02d-01", i + 1);
                break;
            } else if (days > monthDaySum[i] && days < monthDaySum[i + 1]) {
                resTime = String.format("2023-%02d-%02d", i + 1, days - monthDaySum[i] + 1);
                break;
            }
        }
        return resTime;
    }
    /*
1
C-0221 1
4
[2023-01-11] 21231948 borrowed C-0221
[2023-01-15] 21923485 borrowed C-0221
[2023-01-19] 21231948 returned C-0221
[2023-02-07] 21923485 returned C-0221

4
B-0021 1
C-0010 1
C-0001 1
B-0041 1
12
[2023-01-10] 21234598 borrowed B-0021
[2023-01-10] 21234598 borrowed B-0041
[2023-01-10] 21234598 borrowed C-0001
[2023-01-10] 21234598 borrowed C-0010
[2023-01-14] 21932348 borrowed B-0041
[2023-01-15] 21231948 borrowed C-0001
[2023-01-15] 21231948 borrowed B-0041
[2023-01-15] 21231948 borrowed B-0021
[2023-01-15] 21231948 borrowed C-0010
[2023-01-18] 21234598 returned B-0021
[2023-01-18] 21234598 returned C-0001
[2023-01-18] 21932348 returned B-0041

4
B-0021 1
C-0010 1
C-0001 1
B-0041 1
11
[2023-01-10] 21234598 borrowed B-0021
[2023-01-11] 21234598 lost B-0021
[2023-01-12] 21234598 borrowed B-0041
[2023-01-13] 21234598 returned B-0041
[2023-01-13] 21234598 borrowed C-0010
[2023-01-13] 21234598 borrowed C-0001
[2023-01-15] 21234598 smeared C-0010
[2023-01-15] 21234598 returned C-0010
[2023-01-15] 21234598 returned C-0001
[2023-01-18] 21932348 borrowed B-0041
[2023-01-18] 21932348 borrowed B-0021


        *
    * */

    /*
4
B-9427 1
C-5596 1
C-3026 1
C-2296 1
8
[2023-01-18] 10746332 borrowed B-9427
[2023-01-18] 10746332 borrowed C-5596
[2023-01-18] 10746332 borrowed C-3026
[2023-01-18] 10746332 borrowed C-2296
[2023-01-19] 21932348 borrowed B-9427
[2023-01-19] 21932348 borrowed C-5596
[2023-01-19] 21932348 borrowed C-3026
[2023-01-20] 21932348 borrowed C-2296

    * */

}
