import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class DataList {

    // 큐선언
    private Queue<String> DataList = new LinkedBlockingQueue<>();

    // 디버그 콘솔 출력 변수
    private boolean DEBUG = false;

    public Queue<String> getDataList() {
        return DataList;
    }

    public String getDataPeek(){
        return DataList.peek();
    }

    public synchronized String getData() throws InterruptedException {
        String data = null;
        if(this.getDataPeek() == null){
            try {
                wait();
                System.out.println("잠깐멈춤");
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            data = DataList.poll();
            System.out.println("getData - data : " + data);
        }
        return data;
    }

    public synchronized void setData(String data) throws InterruptedException  {
        if(data != null){
            DataList.offer(data);
            System.out.println("setData - data : " + data);
        }
    }
}
