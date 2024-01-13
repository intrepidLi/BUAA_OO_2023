import java.util.HashMap;
import java.util.HashSet;

public class RequestPool {
    private boolean isEnd;
    private HashMap<Integer, Elevator> elevators;
    // 请求队列
    private HashSet<MyRequest> personRequests = new HashSet<MyRequest>();
    private HashSet<MyRequest> allOfRequests = new HashSet<>();

    public synchronized void addRequest(MyRequest request) {
        MyRequest request1 = request.preProcess(elevators);
        personRequests.add(request1);
        allOfRequests.add(request1);
        // TimableOutput.println("addRequest");
        notifyAll();
        // System.out.println(request);
    }

    public synchronized void updateLine() {
        HashSet<MyRequest> newRequests = new HashSet<>();
        for (MyRequest request : personRequests) {
            MyRequest request1 = request.preProcess(elevators);
            newRequests.add(request1);
            allOfRequests.remove(request);
            allOfRequests.add(request1);
        }
        personRequests.clear();
        personRequests.addAll(newRequests);
        notifyAll();
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
            updateLine();
        }
    }

    public synchronized void removeElevators(int id) {
        synchronized (elevators) {
            elevators.remove(id);
        }
    }

    public synchronized boolean removeRequest(MyRequest request) {
        if (personRequests.contains(request)) {
            personRequests.remove(request); // 移除指令
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean isEmpty() {
        // TimableOutput.println("Judging isEmptying...");
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

    public synchronized HashSet<MyRequest> getAllRequest(
            HashSet<MyRequest> currentRequests) {
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
            // System.out.println("Fuck, you are waiting!!!");
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
        HashSet<MyRequest> allRequest = new HashSet<>(personRequests);
        return allRequest;
    }

    public synchronized void checkAllOfRequests() {
        for (MyRequest request : allOfRequests) {
            if (request.isFinished()) {
                allOfRequests.remove(request);
            }
        }
    }

    public synchronized boolean isAllFinished() {
        for (MyRequest request : allOfRequests) {
            if (!request.isFinished()) {
                // addRequest(request);
                // request.setFinished(true);
                // notifyAll();
                return false;
            }
        }
        // notifyAll();
        return true;
    }
}
