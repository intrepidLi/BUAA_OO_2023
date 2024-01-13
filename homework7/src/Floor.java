import java.util.concurrent.Semaphore;

public class Floor {
    private Semaphore inService = new Semaphore(4); // 服务中有4个信号量
    private Semaphore personInService = new Semaphore(2); // 只接人有2个信号量

    public void acquireInService() {
        try {
            inService.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void releaseInService() {
        // TimableOutput.println("Floor " + id + " inService has released!");
        inService.release();
    }

    public void acquirePersonInService() {
        try {
            personInService.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void releasePersonInService() {
        // TimableOutput.println("Floor " + id + " personInService has released!");
        personInService.release();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public Floor(int id) {
        this.id = id;
    }

}
