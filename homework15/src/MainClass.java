import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Objects;

public class MainClass {
    private static int t; // 学校的个数
    private static int n;
    private static int m;
    private static ArrayList<Library> libraries = new ArrayList<>();
    private static String lastTime;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        t = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < t; i++) {
            String str0 = scanner.nextLine();
            // System.out.println(str0);
            String[] str1 = str0.split(" ");
            String schoolName = str1[0];
            n = Integer.parseInt(str1[1]);
            Library library = new Library(schoolName);
            libraries.add(library);
            for (int j = 0; j < n; j++) {
                // scanner.nextLine();
                String str = scanner.nextLine();
                // System.out.println("str: " + str);
                // System.out.println(str);
                proInput1(str, library);
            }
        }

        HashMap<String, Student> students = new HashMap<>();

        m = scanner.nextInt();
        scanner.nextLine();
        System.out.println("[2023-01-01] arranging " +
                "librarian arranged all the books");
        for (int i = 0; i < m; i++) {
            String str = scanner.nextLine();
            proInput2(str, i, students);
        }
        for (Library library1 : libraries) {
            library1.handleRest(libraries, students);
        }
        for (Library library1 : libraries) {
            library1.outputOutBooks(lastTime);
        }
        // finish(library);
    }

    private static void proInput1(String str, Library library) {
        String[] str1 = str.split(" ");
        int copyNum = Integer.parseInt(str1[1]);
        String yesOrNot = str1[2];
        boolean interSchool;
        if (yesOrNot.equals("Y")) {
            interSchool = true;
        } else {
            interSchool = false;
        }
        Book book = new Book(str1[0], copyNum, interSchool, library.getSchoolName());
        library.addBook(book, copyNum);
    }

    private static void proInput2(String str,
                                  int flag, HashMap<String, Student> students) {
        String[] str1 = str.split(" ");
        String time = str1[0];
        String studentInfo = str1[1];
        String[] str2 = studentInfo.split("-");
        String schoolName = str2[0];
        String studentId = str2[1];
        Student student = new Student(studentId, schoolName);
        if (students.containsKey(studentInfo)) {
            student = students.get(studentInfo);
        }
        if (flag == 0) {
            if (!time.equals("[2023-01-01]")) {
                int days1 = 0;
                int days2 = rangeDays(time);
                while ((days1 / 3) != (days2 / 3)) {
                    int days3 = (days1 / 3 + 1) * 3;
                    String newTime = "[" + daysToTime(days3) + "]";
                    Output.outputArrange(newTime);
                    days1 = days3;
                }
            }
        }
        if (!time.equals(lastTime) && flag != 0) { // flag != 0 保证lastTime有值
            int days1 = rangeDays(lastTime);
            int days2 = rangeDays(time);
            int days4 = days1 + 1; // 下一天
            doPreOperation(days4, students);
            // 找整理日时
            if ((days1 / 3) != (days2 / 3)) {
                int days3 = (days1 / 3 + 1) * 3;
                // long time3 = days3 * (24 * 60 * 60 * 1000) + startDate.getTime();
                String newTime = "[" + daysToTime(days3) + "]";
                // System.out.println("newTime is " + newTime);
                for (Library library1 : libraries) {
                    library1.arrangeLib(newTime);
                }
                Output.outputArrange(newTime);
                for (Library library1 : libraries) {
                    library1.orderDeliver(newTime);
                }
                days1 = days3;
                while ((days1 / 3) != (days2 / 3)) {
                    days3 = (days1 / 3 + 1) * 3;
                    newTime = "[" + daysToTime(days3) + "]";
                    Output.outputArrange(newTime);
                    days1 = days3;
                }
            }
        }
        String operation = str1[2];
        Library library = findLibrary(schoolName);
        Book book = new Book(str1[3]);
        doOperation(book, student, time, studentInfo, library,
                operation, students);
        lastTime = time;
    }

    private static void doOperation(Book book, Student student,
                                  String time, String studentInfo, Library library,
                                  String operation, HashMap<String, Student> students) {
        switch (operation) {
            case "borrowed":
                library.borrowBook(book, student, time);
                students.put(studentInfo, student);
                break;
            case "smeared":
                library.smearBook(book, student);
                break;
            case "lost":
                library.loseBook(book, student, time);
                break;
            case "returned":
                library.returnBook(book, student, time, libraries);
                break;
            default:
                break;
        }
    }

    private static void doPreOperation(int days4, HashMap<String, Student> students) {
        String newTime1 = daysToTime(days4);
        // 不是整理日，闭馆后输出所有校内预定
        for (Library library1 : libraries) {
            library1.handleRest(libraries, students);
            // library1.clearOneDay();
        }
        for (Library library1 : libraries) {
            library1.outputOutBooks(lastTime);
        }
        String finalTime = "[" + newTime1 + "]";
        // 开馆前的输出
        for (Library library1 : libraries) {
            // 校际运进
            library1.outputInBooks(finalTime);
        }

        for (Library library1 : libraries) {
            // 发放校际借阅
            library1.interOrderDeliver(students, finalTime);
            library1.allClear();
        }
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

    private static int[] monthDaySum = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365};

    public static int rangeDays(String time) {
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

    public static Library findLibrary(String schoolName) {
        for (Library library : libraries) {
            if (Objects.equals(library.getSchoolName(), schoolName)) {
                return library;
            }
        }
        return null;
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
