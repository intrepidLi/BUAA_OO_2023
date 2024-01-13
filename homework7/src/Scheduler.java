import java.util.ArrayList;
import java.util.HashSet;

public class Scheduler {
    private int eleID; // 电梯的ID
    private int currentFloor;
    private int currentDirection; // 电梯运行方向（1为向上， -1为向下， 2为向上静止， -2为向下静止）
    private RequestPool requestPool;
    private HashSet<MyRequest> currentRequests;
    private HashSet<MyRequest> allRequests;
    private ArrayList<MyRequest> pickUp;
    private ArrayList<MyRequest> putDown;
    private int capacity;
    private boolean isEnd;
    private ArrayList<Floor> reachableFloor; // 能到达的楼层
    private ArrayList<Integer> reachableFloorNum;
    private int lowestFloor; // 能到达的最低楼层
    private int highestFloor; // 能到达的最高楼层
    private int reachableNum;

    public void setMaintain(boolean maintain) {
        isMaintain = maintain;
    }

    private boolean isMaintain;

    public Scheduler(RequestPool requestPool, int eleID, ArrayList<Floor> reachableFloor,
                     int capacity, ArrayList<Integer> reachableFloorNum) {
        this.requestPool = requestPool;
        this.isEnd = false;
        this.eleID = eleID;
        this.pickUp = new ArrayList<>();
        this.putDown = new ArrayList<>();
        this.isMaintain = false;
        this.reachableFloor = reachableFloor;
        this.capacity = capacity;
        this.reachableNum = reachableFloor.size();
        this.lowestFloor = reachableFloor.get(0).getId();
        this.highestFloor = reachableFloor.get(reachableNum - 1).getId();
        this.reachableFloorNum = reachableFloorNum;
    }

    // 获取请求池以及电梯当前信息并进行调度计算
    public void ask(Elevator elevator) {
        // TimableOutput.println("Elevator" + this.eleID + " is asking...");
        this.currentRequests = elevator.getCurrentPerson();
        this.currentFloor = elevator.getCurrentFloor();
        this.currentDirection = elevator.getCurrentDirection();
        // this.capacity = elevator.getCapacity();

        pickUp.clear();
        putDown.clear();

        // 获取请求池
        allRequests = requestPool.getAllRequest(currentRequests);

        // 判断电梯是否进入或退出空等
        isEmptyWait(); // 继续往上走

        // 判断电梯是否转向并转向
        needTurn();

        // 判断电梯是否应该载入人
        if (!isMaintain) {
            // TimableOutput.println("Elevator" + eleID + "testPickUp");
            needPickUp();
        }

        // 判断是否应该从电梯下人
        needPutDown();
    }

    private void isEmptyWait() {
        if (allRequests.size() != 0) {
            if (currentDirection == 2) {
                currentDirection = -1; // 若是向上静止，则向下走
            } else if (currentDirection == -2) {
                currentDirection = 1; // 若是向下静止，则向上走
            }
        }
        else if (currentRequests.size() == 0) {
            if (currentDirection == 1) {
                currentDirection = 2; // 若是向上走，改为向上静止
            } else if (currentDirection == -1) {
                currentDirection = -2; // 若是向下走，改为向下静止
            }
        }
    }

    private void needTurn() { // 电梯是否需要转向
        if (Math.abs(currentDirection) == 1 && currentRequests.size() == 0) {
            if (currentDirection == 1) { // 如果电梯正在向上走
                boolean turnFlag = true;
                for (MyRequest request : allRequests) {
                    if (eleID >= 1 && eleID <= 6) { // 如果请求中有出发楼层在现在楼层之上，则电梯继续向上
                        if (request.getFromFloor() > currentFloor) { // 电梯能够到达请求的出发层
                            turnFlag = false; // 如果请求中有出发层在此层，且要往上走的话，也不转向
                        } else if (request.getFromFloor() == currentFloor
                                && request.getFromFloor() < request.getToFloor()) {
                            turnFlag = false;
                        }
                    }
                    else {
                        if (request.getFromFloor() > currentFloor &&
                            accessAble(request)) {
                            turnFlag = false;
                        }
                        else if (request.getFromFloor() == currentFloor
                            && request.getFromFloor() < request.getToFloor()
                            && accessAble(request)) {
                            turnFlag = false;
                        }
                    }
                }
                if (turnFlag) { // 如果不转向，那么电梯转向
                    currentDirection = -1;
                }
            } else { // 如果电梯此时正在往下走
                boolean turnFlag = true;
                for (MyRequest request : allRequests) {
                    if (eleID >= 1 && eleID <= 6) {
                        if (request.getFromFloor() < currentFloor) {
                            // 如果请求中有出发楼层在现在楼层之下，则电梯继续向下
                            turnFlag = false;
                        } else if (request.getFromFloor() == currentFloor &&
                                request.getFromFloor() > request.getToFloor()) {
                            turnFlag = false; // 如果请求中有出发层在此层，且要往下走的话，也不转向
                        }
                    }
                    else {
                        turnFlag = isTurnFlag(request);
                    }
                }
                if (turnFlag) { // 如果不转向，那么电梯转向
                    currentDirection = 1;
                }
            }
        }
        if ((currentFloor == lowestFloor && currentDirection == -1) ||
                (currentFloor == 1 && currentDirection == -1)) {
            currentDirection = 1; // 向下走到1楼转向
        }
        if ((currentFloor == highestFloor && currentDirection == 1) ||
                (currentFloor == 11 && currentDirection == 1)) {
            currentDirection = -1; // 向上走到11楼转向 1是向上走, -1是向下走
        }
    }

    private boolean isTurnFlag(MyRequest request) {
        boolean turnFlag = true;
        if (request.getFromFloor() < currentFloor &&
                accessAble(request)) {
            turnFlag = false;
        }
        else if (request.getFromFloor() == currentFloor
                && request.getFromFloor() > request.getToFloor()
                && accessAble(request)) {
            turnFlag = false;
        }
        return turnFlag;
    }

    private void needPickUp() {
        // 接第一个人和与其相同目的地的
        firstNeedPickUp();

        // 接剩下的人
        restNeedPickUp();
    }

    private void firstNeedPickUp() {
        for (MyRequest request : allRequests) {
            if (request.getFromFloor() == currentFloor) {
                if (currentDirection == 1) {
                    if (request.getToFloor() > currentFloor && accessAble(request)) {
                        if (pickUp.size() == 0) {
                            pickUp.add(request); // 如果所有请求中恰好有向上走并出发层相同的，捎带之
                        } else if (pickUp.size() < capacity
                                && pickUp.get(0).getToFloor() == request.getToFloor()) {
                            pickUp.add(request);
                        }
                    }
                }
                else if (currentDirection == -1) { // 如果电梯可达且方向一致，接上
                    if (request.getToFloor() < currentFloor && accessAble(request)) {
                        if (pickUp.size() == 0) {
                            pickUp.add(request); // 如果所有请求中恰好有向下走并出发层相同的，捎带之
                        } else if (pickUp.size() < capacity
                                && pickUp.get(0).getToFloor() == request.getToFloor()) {
                            pickUp.add(request);
                        }
                    }
                }
            }
        }
    }

    private void restNeedPickUp() {
        for (MyRequest request : allRequests) {
            if (request.getFromFloor() == currentFloor) {
                if (currentDirection == 1) {
                    if (request.getToFloor() > currentFloor && accessAble(request)) {
                        if (!pickUp.contains(request) && pickUp.size() < capacity) {
                            pickUp.add(request); // 如果所有请求中恰好有向上走并出发层相同的，捎带之
                        }
                    }
                } else if (currentDirection == -1) {
                    if (request.getToFloor() < currentFloor && accessAble(request)) {
                        if (!pickUp.contains(request) && pickUp.size() < capacity) {
                            pickUp.add(request); // 如果所有请求中恰好有向下走并出发层相同的，捎带之
                        }
                    }
                }
            }
        }
    }

    private void needPutDown() {
        for (MyRequest request : currentRequests) {
            // TimableOutput.println("fuckPutDown");
            if ((request.getToFloor() == this.currentFloor) || isMaintain) {
                putDown.add(request);
            }
            // else if (isMaintain) {
            //     putDown.add(request);
            //     PersonRequest personRequest = new PersonRequest(this.currentFloor,
            //             request.getToFloor(), request.getPersonId());
            //     requestPool.addRequest(personRequest);
            // }
        }
    }

    public int getCurrentDirection() {
        return currentDirection;
    }

    public ArrayList<MyRequest> getPickUp() {
        return pickUp;
    }

    public ArrayList<MyRequest> getPutDown() {
        return putDown;
    }

    public boolean accessAble(MyRequest request) {
        if ((reachableFloorNum.contains(request.getFromFloor())) &&
                (reachableFloorNum.contains(request.getToFloor()))) {
            return true;
        }
        else {
            return false;
        }
    }
}
