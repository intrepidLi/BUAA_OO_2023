import com.oocourse.elevator2.TimableOutput;

public class MainClass {

    public static void main(String[] args) throws Exception {
        // 先初始化时间戳
        TimableOutput.initStartTimestamp();
        // Thread.sleep(1000);
        // 设置控制器，实例化电梯
        Controller controller = new Controller();
        Thread inputThread = new InputThread(controller);
        inputThread.start();
    }
}
