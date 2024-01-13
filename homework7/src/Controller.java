import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.MaintainRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class Controller {
    public RequestPool getRequestPool() {
        return requestPool;
    }

    private RequestPool requestPool;
    private HashMap<Integer, Elevator> elevators; // 电梯编号和电梯
    private ArrayList<Floor> floors;

    public Controller() {
        // 创建一个请求池
        elevators = new HashMap<>();
        requestPool = new RequestPool(elevators);
        floors = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            ArrayList<Integer> reachableFloorNum = new ArrayList<>();
            for (int j = 1; j <= 11; j++) {
                Floor floor = new Floor(j);
                floors.add(floor); // 注意1层对应Floors.get(0)
                reachableFloorNum.add(j);
            }
            Scheduler scheduler = new Scheduler(requestPool, i, floors, 6, reachableFloorNum);
            Elevator elevator = new Elevator(i, scheduler, requestPool, 1, 6, 400,
                    floors, reachableFloorNum, floors);
            elevators.put(i, elevator);
        }
        for (int i : elevators.keySet()) {
            elevators.get(i).start();
        }
    }

    public void addRequest(MyRequest request) {
        requestPool.addRequest(request);
    }

    public void addElevator(int id, int currentFloor, int capacity,
                            int transFloor, int accessibility) {
        ArrayList<Floor> reachableFloor = new ArrayList<>();
        ArrayList<Integer> reachableFloorNum = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            int res = accessibility & (1 << (i - 1));
            if (res != 0) {
                reachableFloor.add(floors.get(i - 1)); // 第i层是第i-1个
                reachableFloorNum.add(i);
                // TimableOutput.println("Elevator " + id + " can reach " + i + " floor");
            }
        }
        Scheduler scheduler = new Scheduler(requestPool, id, reachableFloor,
                capacity, reachableFloorNum);
        Elevator elevator = new Elevator(id, scheduler, requestPool,
                currentFloor, capacity, transFloor, reachableFloor, reachableFloorNum, floors);
        // elevators.put(id, elevator);
        requestPool.addElevators(id, elevator); // 请求池电梯队列新增电梯
        elevator.start();
    }

    public void addElevatorRequest(ElevatorRequest request) {
        addElevator(request.getElevatorId(), request.getFloor(), request.getCapacity(),
                (int)(request.getSpeed() * 1000), request.getAccess());
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
        synchronized (elevators) {
            for (int i : elevators.keySet()) {
                elevators.get(i).setEnd();
            }
        }
    }
}
