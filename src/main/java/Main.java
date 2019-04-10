import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    public static void main(String[] args) {
        final DataList dataList = new DataList();

        // 데이터추출
        Runnable putIn = new PutIn(dataList);
        // 분석
        Runnable analysis = new Analysis(dataList, "Thread");

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Thread putInThread = new Thread(putIn);
        //putInThread.start();

        putIn.run();
        while(true){
            executorService.execute(analysis);
        }
    }
}

