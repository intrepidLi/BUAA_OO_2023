import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.MaintainRequest;
import com.oocourse.elevator2.PersonRequest;

import java.util.HashMap;

public class Controller {
    public RequestPool getRequestPool() {
        return requestPool;
    }

    private RequestPool requestPool;
    private HashMap<Integer, Elevator> elevators; // 电梯编号和电梯

    public Controller() {
        // 创建一个请求池
        elevators = new HashMap<>();
        requestPool = new RequestPool(elevators);
        for (int i = 1; i <= 6; i++) {
            Scheduler scheduler = new Scheduler(requestPool, i);
            Elevator elevator = new Elevator(i, scheduler, requestPool, 1, 6, 400);
            elevators.put(i, elevator);
        }
        for (int i : elevators.keySet()) {
            elevators.get(i).start();
        }
    }

    public void addRequest(PersonRequest request) {
        requestPool.addRequest(request);
    }

    public void addElevator(int id, int currentFloor, int capacity, int transFloor) {
        Scheduler scheduler = new Scheduler(requestPool, id);
        Elevator elevator = new Elevator(id, scheduler, requestPool,
                currentFloor, capacity, transFloor);
        // elevators.put(id, elevator);
        requestPool.addElevators(id, elevator); // 请求池电梯队列新增电梯
        elevator.start();
    }

    public void addElevatorRequest(ElevatorRequest request) {
        addElevator(request.getElevatorId(), request.getFloor(), request.getCapacity(),
                (int)(request.getSpeed() * 1000));
    }

    public void addMaintainRequest(MaintainRequest request) {
        for (int i : elevators.keySet()) {
            if (request.getElevatorId() == i) {
                elevators.get(i).setMaintain();
                // TimableOutput.println("Elevator " + i + " need Maintain");
            }
        }
        requestPool.proMaintainRequest();
        // notifyAll();
    }

    public void close() {
        requestPool.setEnd(true);
        for (int i : elevators.keySet()) {
            elevators.get(i).setEnd();
        }
    }
}
