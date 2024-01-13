import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class InputThread extends Thread {
    private Controller controller;

    public  InputThread(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                break;
            } else {
                // a new valid request
                // 得到一个新请求， 加入到请求池中
                controller.addRequest(request);
                // System.out.println(request);
            }
        }

        controller.close();
        
        try {
            elevatorInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
