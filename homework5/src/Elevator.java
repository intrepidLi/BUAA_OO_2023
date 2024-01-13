import com.oocourse.elevator1.PersonRequest;
import com.oocourse.elevator1.TimableOutput;

import java.util.ArrayList;
import java.util.HashSet;

public class Elevator extends Thread {
    private static final int TRANS_FLOOR = 400;
    private static final int DOOR_OPEN_TIME = 200;
    private int id; // 电梯id
    private final int capacity = 6; // 电梯容量
    private Scheduler scheduler;
    private int currentFloor; // 电梯现在所在楼层
    private int currentDirection; // 电梯运行方向（1为向上， -1为向下， 2为向上静止， -2为向下静止）
    private HashSet<PersonRequest> currentPerson; // 电梯中的人数及请求
    private boolean doorOpen; // 电梯门状态
    private boolean isEnd; // 是否已经结束
    private long doorOpenTime; // 电梯门开的时间
    private RequestPool requestPool; // 请求池

    public Elevator(int id, Scheduler scheduler, RequestPool requestPool) {
        this.id = id;
        this.currentFloor = 1;
        this.currentDirection = -2; // 初始方向为向下静止
        this.currentPerson = new HashSet<>();
        this.scheduler = scheduler;
        this.isEnd = false;
        this.doorOpen = false;
        this.requestPool = requestPool;
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

    public HashSet<PersonRequest> getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(HashSet<PersonRequest> currentPerson) {
        this.currentPerson = currentPerson;
    }

    @Override
    public void run() {
        // TimableOutput.println("Elevator " + id + " is running...");
        while (true) {
            if (isEnd && requestPool.isEmpty() && currentPerson.isEmpty()) {
                return;
            }
            scheduler.ask(this);
            currentDirection = scheduler.getCurrentDirection();
            ArrayList<PersonRequest> pickUp;
            ArrayList<PersonRequest> putDown;
            pickUp = scheduler.getPickUp();
            putDown = scheduler.getPutDown();
            personIn(pickUp);
            personOut(putDown);
            close();
            if (isEnd && requestPool.isEmpty() && currentPerson.isEmpty()) {
                return;
            }
            if (!doorOpen) {
                move();
            }
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

    private synchronized void close() {
        if (!doorOpen) {
            return;
        }
        long curTime = System.currentTimeMillis();
        long waitTime = 2 * DOOR_OPEN_TIME - (curTime - doorOpenTime) + 1;
        try {
            wait(waitTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 关门
        TimableOutput.println(String.format("CLOSE-%d-%d", currentFloor, id));
        doorOpen = false;
    }

    private synchronized void personIn(ArrayList<PersonRequest> pickUp) {
        if (!pickUp.isEmpty()) {
            // open();
            for (PersonRequest request : pickUp) {
                if (currentPerson.size() < capacity) {
                    if (request.getFromFloor() == currentFloor) {
                        if (!doorOpen) {
                            open();
                        }
                        boolean moved;
                        synchronized (requestPool) {
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

    private synchronized void personOut(ArrayList<PersonRequest> putDown) {
        for (PersonRequest request : putDown) {
            if (request.getToFloor() == currentFloor) {
                if (!doorOpen) {
                    open();
                }
                synchronized (currentPerson) {
                    currentPerson.remove(request);
                }
                TimableOutput.println(String.format("OUT-%d-%d-%d",
                        request.getPersonId(), currentFloor, id));
            }
        }
    }

    private void move() {
        try {
            Thread.sleep(TRANS_FLOOR);
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
}
