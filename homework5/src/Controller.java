import com.oocourse.elevator1.PersonRequest;

import java.util.HashMap;

public class Controller {
    private RequestPool requestPool;
    private HashMap<Integer, Elevator> elevators; // 电梯编号和电梯

    public Controller() {
        // 创建一个请求池
        elevators = new HashMap<>();
        requestPool = new RequestPool(elevators);
        for (int i = 1; i <= 6; i++) {
            Scheduler scheduler = new Scheduler(requestPool, i);
            Elevator elevator = new Elevator(i, scheduler, requestPool);
            elevators.put(i, elevator);
        }
        for (int i : elevators.keySet()) {
            elevators.get(i).start();
        }
    }

    public void addRequest(PersonRequest request) {
        requestPool.addRequest(request);
    }

    public void close() {
        requestPool.setEnd(true);
        for (int i : elevators.keySet()) {
            elevators.get(i).setEnd();
        }
    }
}
