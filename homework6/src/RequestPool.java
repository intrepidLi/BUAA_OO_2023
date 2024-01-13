import com.oocourse.elevator2.PersonRequest;

import java.util.HashMap;
import java.util.HashSet;

public class RequestPool {
    private boolean isEnd;
    private HashMap<Integer, Elevator> elevators;
    // 请求队列
    private HashSet<PersonRequest> personRequests = new HashSet<>();

    public synchronized void addRequest(PersonRequest request) {
        personRequests.add(request);
        // TimableOutput.println("addRequest");
        notifyAll();
        // System.out.println(request);
    }

    public synchronized void proMaintainRequest() {
        notifyAll();
    }

    public RequestPool(HashMap<Integer, Elevator> elevators) {
        this.elevators = elevators;
    }

    public synchronized void addElevators(int id, Elevator elevator) {
        synchronized (elevators) {
            elevators.put(id, elevator);
        }
    }

    public synchronized boolean removeRequest(PersonRequest request) {
        if (personRequests.contains(request)) {
            personRequests.remove(request); // 移除指令
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return personRequests.isEmpty();
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized boolean getEnd() {
        notifyAll();
        return isEnd;
    }

    public synchronized HashSet<PersonRequest> getAllRequest(
            HashSet<PersonRequest> currentRequests) {
        boolean eleExist = false; // 判断某个电梯中是否还有人
        boolean mtExist = false; // 判断某个电梯是否需要维护
        if (currentRequests.size() != 0) {
            eleExist = true;
        }
        synchronized (elevators) {
            for (int i : elevators.keySet()) {
                // if (elevators.get(i).getCurrentPerson().size() != 0)
                // {
                //     eleExist = true;
                // }
                if (elevators.get(i).isMaintain() && !elevators.get(i).isEnd()) {
                    mtExist = true;
                }
            }
        }
        if (!eleExist && personRequests.isEmpty() && !isEnd && !mtExist) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        /*if (personRequests.isEmpty()) {
               return null;
           }*/
        //return personRequests;
        HashSet<PersonRequest> allRequest = new HashSet<>(personRequests);
        return allRequest;
    }
}
