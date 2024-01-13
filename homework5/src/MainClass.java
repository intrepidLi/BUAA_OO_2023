import com.oocourse.elevator1.TimableOutput;

public class MainClass {
    public static void main(String[] args) {
        // 先初始化时间戳
        TimableOutput.initStartTimestamp();
        // 设置控制器，实例化电梯
        Controller controller = new Controller();
        Thread inputThread = new InputThread(controller);
        inputThread.start();
    }
}
