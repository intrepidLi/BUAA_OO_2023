import com.oocourse.elevator1.PersonRequest;

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

    public RequestPool(HashMap<Integer, Elevator> elevators) {
        this.elevators = elevators;
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

    public synchronized HashSet<PersonRequest> getAllRequest() {
        boolean eleExist = false; // 判断某个电梯中是否还有人
        synchronized (elevators) {
            for (int i : elevators.keySet()) {
                if (elevators.get(i).getCurrentPerson().size() != 0)
                {
                    eleExist = true;
                }
            }
        }
        if (!eleExist && personRequests.isEmpty() && !isEnd) {
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
