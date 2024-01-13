import com.oocourse.elevator3.TimableOutput;

import java.util.ArrayList;
import java.util.HashSet;

public class Elevator extends Thread {
    private final int transFloor;
    private static final int DOOR_OPEN_TIME = 200;

    @Override
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id; // 电梯id
    private final int capacity; // 电梯容量
    private Scheduler scheduler;
    private int currentFloor; // 电梯现在所在楼层

    public int getCapacity() {
        return capacity;
    }

    private int currentDirection; // 电梯运行方向（1为向上， -1为向下， 2为向上静止， -2为向下静止）
    private HashSet<MyRequest> currentPerson; // 电梯中的人数及请求
    private boolean doorOpen; // 电梯门状态

    public boolean isEnd() {
        return isEnd;
    }

    private boolean isEnd; // 是否已经结束
    private long doorOpenTime; // 电梯门开的时间
    private RequestPool requestPool; // 请求池

    public ArrayList<Floor> getReachableFloor() {
        return reachableFloor;
    }

    public int getLowestFloor() {
        return lowestFloor;
    }

    public int getHighestFloor() {
        return highestFloor;
    }

    public int getReachableNum() {
        return reachableNum;
    }

    private ArrayList<Floor> reachableFloor; // 电梯可到达的楼层
    private ArrayList<Floor> floors; // 总楼层

    public ArrayList<Integer> getReachableFloorNum() {
        return reachableFloorNum;
    }

    private ArrayList<Integer> reachableFloorNum;
    private int lowestFloor; // 电梯可到达的最低楼层
    private int highestFloor; // 电梯可到达的最高楼层
    private int reachableNum; // 电梯可到达的楼层数量
    private boolean acquiredPersonIn;

    public boolean isMaintain() {
        return maintain;
    }

    private boolean maintain;

    public Elevator(int id, Scheduler scheduler, RequestPool requestPool, int currentFloor,
                    int capacity, int transFloor, ArrayList<Floor> reachableFloor,
                    ArrayList<Integer> reachableFloorNum, ArrayList<Floor> floors) {
        this.id = id;
        this.currentFloor = currentFloor;
        this.currentDirection = -2; // 初始方向为向下静止
        this.currentPerson = new HashSet<>();
        this.scheduler = scheduler;
        this.isEnd = false;
        this.doorOpen = false;
        this.requestPool = requestPool;
        this.capacity = capacity;
        this.transFloor = transFloor;
        this.maintain = false;
        this.reachableFloor = reachableFloor;
        this.reachableNum = reachableFloor.size();
        this.highestFloor = reachableFloor.get(reachableNum - 1).getId();
        // TimableOutput.println("Elevator " + id + " highestFloor is " + highestFloor);
        this.lowestFloor = reachableFloor.get(0).getId();
        // TimableOutput.println("Elevator " + id + " lowestFloor is " + lowestFloor);
        this.reachableFloorNum = reachableFloorNum;
        // TimableOutput.println("Elevator " + id + " reachableFloorNum is " + lowestFloor);
        this.floors = floors;
        this.acquiredPersonIn = false;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(int currentDirection) {
        this.currentDirection = currentDirection;
    }

    public HashSet<MyRequest> getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(HashSet<MyRequest> currentPerson) {
        this.currentPerson = currentPerson;
    }

    @Override
    public void run() {
        while (true) {
            // TimableOutput.println("In Thread Elevator " + id);
            if (maintain && currentPerson.isEmpty() && !doorOpen) {
                TimableOutput.println(String.format("MAINTAIN_ABLE-%d", id));
                InputThread.setCnt(InputThread.getCnt() - 1);
                requestPool.removeElevators(id);
                setEnd();
                updateLine();
                // TimableOutput.println("Elevator " + id + " is over.");
                return;
            }
            if (endThread()) {
                requestPool.removeElevators(id);
                // TimableOutput.println("Elevator " + id + " is over.");
                return;
            }
            if (maintain) {
                scheduler.setMaintain(true);
            }
            scheduler.ask(this);
            currentDirection = scheduler.getCurrentDirection();
            ArrayList<MyRequest> pickUp;
            ArrayList<MyRequest> putDown;
            pickUp = scheduler.getPickUp();
            putDown = scheduler.getPutDown();

            personIn(pickUp, putDown);
            personOut(putDown);
            close(pickUp, putDown);
            if (maintain && currentPerson.isEmpty() && !doorOpen) {
                TimableOutput.println(String.format("MAINTAIN_ABLE-%d", id));
                InputThread.setCnt(InputThread.getCnt() - 1);
                requestPool.removeElevators(id); // 请求池电梯序列中移除该电梯
                setEnd();
                updateLine();
                // TimableOutput.println("Elevator " + id + " is over.");
                return;
            }
            if (endThread()) {
                // TimableOutput.println("Elevator " + id + " is over.");
                requestPool.removeElevators(id); // 请求池电梯序列中移除该电梯
                return;
            }
            if (!doorOpen) {
                move();
            }
            // TimableOutput.println("Out Thread Elevator " + id);
        }
    }

    private synchronized boolean isFinished() {
        TimableOutput.println("Elevator " + id + " is finishing...");
        if (isEnd && requestPool.isEmpty()) {
            notifyAll();
            return true;
        }
        while (!isEnd && requestPool.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void open() {
        if (doorOpen) {
            return;
        }
        TimableOutput.println(String.format("OPEN-%d-%d",
                currentFloor, id));
        doorOpen = true;
        doorOpenTime = System.currentTimeMillis();
    }

    private synchronized void close(ArrayList<MyRequest> pickUp, ArrayList<MyRequest> putDown) {
        if (!doorOpen) {
            return;
        }
        long curTime = System.currentTimeMillis();
        long waitTime = 2 * DOOR_OPEN_TIME - (curTime - doorOpenTime) + 1;
        try {
            if (waitTime > 0) {
                wait(waitTime);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 关门
        TimableOutput.println(String.format("CLOSE-%d-%d", currentFloor, id));
        Floor curFloor = maintainSpeFloor();
        if (acquiredPersonIn) {
            assert curFloor != null;
            curFloor.releasePersonInService();
            acquiredPersonIn = false;
            // TimableOutput.println("Succeed release personInService Elevator " + id);
        }
        assert curFloor != null;
        curFloor.releaseInService();
        // TimableOutput.println("Succeed release inService Elevator " + id);
        doorOpen = false;
    }

    private synchronized void personIn(ArrayList<MyRequest> pickUp, ArrayList<MyRequest> putDown) {
        /*try {
            personInService.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
        if (!pickUp.isEmpty()) {
            // open();
            synchronized (requestPool) {
                for (MyRequest request : pickUp) {
                    if (currentPerson.size() < capacity) {
                        if (request.getFromFloor() == currentFloor) {
                            if (!doorOpen) {
                                Floor curFloor = speFloor();
                                // TimableOutput.println("Trying acquire inService Elevator " + id);
                                assert curFloor != null;
                                curFloor.acquireInService();
                                // TimableOutput.
                                // println("Finished acquire inService Elevator " + id);
                                if (putDown.size() == 0 && !maintain && pickUp.size() != 0) {
                                    // TimableOutput.
                                    // println("Trying acquire personInService Elevator " + id);
                                    curFloor.acquirePersonInService();
                                    this.acquiredPersonIn = true;
                                    // TimableOutput.
                                    // println("Finished acquire personInService Elevator " + id);
                                }
                                open();
                            }
                            boolean moved;
                            moved = requestPool.removeRequest(request);
                            if (moved) {
                                currentPerson.add(request);
                                TimableOutput.println(String.format("IN-%d-%d-%d",
                                        request.getPersonId(), currentFloor, id));
                            }
                        }
                    }
                }
            }
        }
    }

    private synchronized void personOut(ArrayList<MyRequest> putDown) {
        for (MyRequest request : putDown) {
            if (request.getToFloor() == currentFloor) {
                if (!doorOpen) {
                    Floor curFloor = speFloor();
                    // TimableOutput.println("Trying acquire inService Elevator " + id);
                    assert curFloor != null;
                    curFloor.acquireInService();
                    // TimableOutput.println("Finished acquire inService Elevator " + id);
                    open();
                }
                synchronized (currentPerson) {
                    currentPerson.remove(request);
                }
                synchronized (requestPool) {
                    if (request.getChangeFloors().size() != 0) {
                        int newToFloor = request.getChangeFloors().pop();
                        MyRequest personRequest = new MyRequest(this.currentFloor,
                                newToFloor, request.getPersonId());
                        personRequest.setChangeFloors(request.getChangeFloors()); // 注意换乘楼层的更新
                        // System.out.println("Out request " + request);
                        // System.out.println("New request " + personRequest);
                        requestPool.addRequest(personRequest);
                    }
                    request.setFinished(true);
                    // requestPool.checkAllOfRequests();
                }
                TimableOutput.println(String.format("OUT-%d-%d-%d",
                        request.getPersonId(), currentFloor, id));
            }
            else if (maintain) {
                if (!doorOpen) {
                    Floor curFloor = maintainSpeFloor();
                    // TimableOutput.println("Trying acquire Maintain inService Elevator " + id);
                    assert curFloor != null;
                    curFloor.acquireInService();
                    // TimableOutput.println("Finished acquire maintain inService Elevator " + id);
                    open();
                }
                synchronized (currentPerson) {
                    currentPerson.remove(request);
                }
                synchronized (requestPool) {
                    MyRequest personRequest = new MyRequest(this.currentFloor,
                            request.getToFloor(), request.getPersonId());
                    personRequest.setChangeFloors(request.getChangeFloors());
                    requestPool.removeElevators(id);
                    requestPool.addRequest(personRequest);
                    request.setFinished(true);
                    // requestPool.checkAllOfRequests();
                }
                TimableOutput.println(String.format("OUT-%d-%d-%d",
                        request.getPersonId(), currentFloor, id));
            }
        }
    }

    private void move() {
        try {
            Thread.sleep(transFloor);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (currentDirection == 1) {
            currentFloor++;
            TimableOutput.println(String.format("ARRIVE-%d-%d", currentFloor, id));
        } else if (currentDirection == -1) {
            currentFloor--;
            TimableOutput.println(String.format("ARRIVE-%d-%d", currentFloor, id));
        }
    }

    public void setEnd() {
        this.isEnd = true;
    }

    public void setMaintain() {
        this.maintain = true;
        // scheduler.setMaintain(true);
        // setEnd();
    }

    public boolean isReachable(int floor) {
        return reachableFloor.contains(floor); // 查看电梯是否可以到达某一楼层
    }

    private Floor speFloor() {
        for (int i = 0; i < reachableNum; i++) {
            if (reachableFloor.get(i).getId() == currentFloor) {
                return reachableFloor.get(i);
            }
        }
        TimableOutput.println("toNullFloor Elevator " + id);
        TimableOutput.println("CurrentFloor is " + currentFloor);
        return null;
    }

    private Floor maintainSpeFloor() {
        for (int i = 0; i < 11; i++) {
            if (floors.get(i).getId() == currentFloor) {
                return floors.get(i);
            }
        }
        TimableOutput.println("Maintain toNullFloor Elevator " + id);
        TimableOutput.println("CurrentFloor is " + currentFloor);
        return null;
    }

    private boolean endThread() {
        boolean flag = false;
        // TimableOutput.println("Elevator " + id + " is in EndThreading");
        if (isEnd && currentPerson.isEmpty() && requestPool.isEmpty()) {
            // TimableOutput.println("Elevator " + id + " is in isEmptying");
            // TimableOutput.println("Elevator " + id + " pass EndThreaded");
            if (requestPool.isAllFinished()) {
                flag = true;
            }
        }
        return flag;
    }

    private void updateLine() {
        if (!requestPool.isEmpty()) {
            requestPool.updateLine();
        }
    }
}
