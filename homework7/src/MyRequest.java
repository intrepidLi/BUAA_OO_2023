import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

public class MyRequest {
    private int fromFloor;
    private int toFloor;

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    private boolean isFinished;

    public int getFromFloor() {
        return fromFloor;
    }

    public void setFromFloor(int fromFloor) {
        this.fromFloor = fromFloor;
    }

    public int getToFloor() {
        return toFloor;
    }

    public void setToFloor(int toFloor) {
        this.toFloor = toFloor;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public Stack<Integer> getChangeFloors() {
        return changeFloors;
    }

    public void setChangeFloors(Stack<Integer> changeFloors) {
        this.changeFloors = changeFloors;
    }

    private int personId;
    private Stack<Integer> changeFloors;

    public MyRequest(Request request) {
        this.fromFloor = ((PersonRequest) request).getFromFloor();
        this.toFloor = ((PersonRequest) request).getToFloor();
        this.personId = ((PersonRequest) request).getPersonId();
        this.changeFloors = new Stack<>();
        this.isFinished = false;
    }

    public MyRequest(int fromFloor, int toFloor, int personId) {
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.personId = personId;
        this.changeFloors = new Stack<>();
        this.isFinished = false;
    }

    @Override
    public String toString() {
        return String.format("%d-FROM-%d-TO-%d", personId, fromFloor, toFloor);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof PersonRequest)) {
            return false;
        } else {
            return ((PersonRequest)obj).getFromFloor() == this.fromFloor
                    && ((PersonRequest)obj).getToFloor() == this.toFloor
                    && ((PersonRequest)obj).getPersonId() == this.personId;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[]{this.personId, this.fromFloor, this.toFloor});
    }

    public MyRequest preProcess(HashMap<Integer, Elevator> elevators) { // 对读入的request进行预处理
        if (findShortest(elevators) == 1) {
            MyRequest request = new MyRequest(fromFloor, toFloor, personId);
            request.setChangeFloors(this.changeFloors);
            return request;
        }
        else {
            int newToFloor = changeFloors.pop();
            MyRequest request = new MyRequest(fromFloor, newToFloor, personId);
            // TimableOutput.println("Add Request " + request);
            Stack<Integer> newChangeFloors = new Stack<Integer>();
            newChangeFloors.addAll(changeFloors);
            request.setChangeFloors(newChangeFloors); // 更新需要换乘的楼层
            return request;
        }
    }

    public int findShortest(HashMap<Integer, Elevator> elevators) {
        int flag = 0;
        // System.out.println("Elevators size is " + elevators.size());
        for (int i : elevators.keySet()) {
            // System.out.println("This is Elevator " + elevators.get(i).getId());
            if (elevators.get(i).getReachableFloorNum().contains(fromFloor)
                && elevators.get(i).getReachableFloorNum().contains(toFloor)) {
                flag = 1; // 有一部电梯可以直接到达
                // System.out.println("Request by " + this.getPersonId() +
                //         " can meet by Elevator " + elevators.get(i).getId());
                break;
            }
        }
        if (flag == 0) {
            int max = Integer.MAX_VALUE; // 无法到达时距离设为Max
            int[][] adjMatrix = new int[12][12];
            for (int i = 1; i <= 11; i++) {
                for (int j = 1; j <= 11; j++) {
                    if (i == j) {
                        adjMatrix[i][j] = 0; // 自身可以到达
                    }
                    else {
                        adjMatrix[i][j] = max; // 初始时全部赋值为Max，都不可到达
                    }
                }
            }
            for (int i : elevators.keySet()) {
                for (int j = 0; j < elevators.get(i).getReachableNum() - 1; j++) { // 遍历电梯可达楼层
                    for (int k = j + 1; k < elevators.get(i).getReachableNum(); k++) {
                        adjMatrix[elevators.get(i).getReachableFloor().get(j).getId()][
                                elevators.get(i).getReachableFloor().get(k).getId()] = 1;
                        // 电梯从可达楼层中的第j个楼层到从可达楼层中的第k个楼层记为1
                        adjMatrix[elevators.get(i).getReachableFloor().get(k).getId()][
                                elevators.get(i).getReachableFloor().get(j).getId()] = 1;
                        // System.out.println("{" + elevators.get(i).getReachableFloor().
                        // get(j).getId() + " ," +
                        //     elevators.get(i).getReachableFloor().get(k).getId() + " }");
                    }
                }
            }
            // 初始化图完成
            int[] dis = new int[12]; // 记录出发楼层到每个楼层的最短距离
            int[] book = new int[12]; // 记录每层是否被访问过
            int[] path = new int[12];
            // 初始化以上两个数组
            for (int i = 1; i <= 11; i++) {
                if (i == fromFloor) {
                    dis[i] = 0;
                }
                else {
                    dis[i] = max; // 不要写成dis[i] = adjMatrix[fromFloor][i]
                }
                path[i] = -1;
                book[i] = 0;
            }
            dijkstra(dis, book, adjMatrix, path);
            if (dis[toFloor] != Integer.MAX_VALUE) {
                // System.out.println("min TransTimes = " + (dis[toFloor] - 1));
                int i = toFloor;
                while (path[i] != -1) {
                    changeFloors.push(i);
                    // System.out.println("pass " + i);
                    i = path[i];
                }
                // Collections.reverse(changeFloors);
            } else
            {
                System.out.println("Can't get to the destination.");
            }
        }
        return flag;
    }

    private void dijkstra(int[] dis, int[] book, int[][] adjMatrix, int[] path) {
        // book[fromFloor] = 1; // 起始站点已访问
        int min; // min存储源站点到尚未访问过的站点中距离最小的层的距离。
        int v; // v：当前处理的楼层。
        int u = 0; // u: 出发楼层到尚未访问过的楼层中距离最小的楼层。
        for (int i = 1; i <= 11; i++) {
            min = Integer.MAX_VALUE;
            for (int j = 1; j <= 11; j++) {
                if (book[j] == 0 && dis[j] < min) { // 楼层未被访问过，且到j的最短距离小于min
                    min = dis[j];
                    u = j;
                }
            }
            book[u] = 1; // u楼层访问过了

            // 更新与该站点相邻的站点的距离
            for (v = 1; v <= 11; v++) {
                if (adjMatrix[u][v] < Integer.MAX_VALUE) {
                    if (dis[v] > dis[u] + adjMatrix[u][v]) {
                        dis[v] = dis[u] + adjMatrix[u][v];
                        path[v] = u;
                        // cnt++;
                    }
                }
            }
        }
    }
}
