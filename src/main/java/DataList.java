import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class DataList {

    // 큐선언
    private Queue<String> DataList = new LinkedBlockingQueue<>();

    public Queue<String> getDataList() {
        return DataList;
    }

    public synchronized String getDataPeek(){
        return DataList.peek();
    }

    public synchronized String getData(){
        String data = null;
        if(this.getDataPeek() == null){
            try {
                System.out.println("wait...");
                wait();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            data = DataList.poll();
        }
        return data;
    }

    public synchronized void setData(String data){
        if(data != null){
            DataList.offer(data);
            if(this.getDataPeek() == null){
                try {
                    System.out.println("notifyAll...");
                    notifyAll();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized int getSize(){
        return DataList.size();
    }
}
