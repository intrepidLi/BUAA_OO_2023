import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.MaintainRequest;
import com.oocourse.elevator3.Request;
import com.oocourse.elevator3.ElevatorInput;

import java.io.IOException;

public class InputThread extends Thread {
    private Controller controller;

    public static int getCnt() {
        return cnt;
    }

    public static void setCnt(int cnt) {
        InputThread.cnt = cnt;
    }

    private static int cnt;

    public  InputThread(Controller controller) {
        this.controller = controller;
        cnt = 0;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            // when request == null
            // it means there are no more lines in stdin
            // 注意此处的变化，考虑最后一个Maintain指令
            // 注意不加 Requestpool.isEmpty
            if (request == null && cnt == 0) {
                break;
            } else if (request != null) {
                // TimableOutput.println("Get " + request);
                // 得到一个新请求， 加入到请求池中
                // 乘客请求
                if (request instanceof PersonRequest) {
                    controller.addRequest(new MyRequest(request));
                }
                // 新增电梯请求
                else if (request instanceof ElevatorRequest) {
                    controller.addElevatorRequest((ElevatorRequest) request);
                }
                // 维护电梯请求
                else { // MaintainRequest
                    controller.addMaintainRequest((MaintainRequest) request);
                    setCnt(cnt + 1);
                }
                // a new valid request
                // System.out.println(request);
            }
        }

        controller.close();
        
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
