import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    // 스레드 수 체크
    static int cnt = 0;

    public static void main(String[] args) {
        // 초기화
        DataList dataList = new DataList();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Runnable putIn = new PutIn(dataList);
        Thread putInThread = new Thread(putIn);
        Runnable analysis = new Analysis(dataList);

        // 데이터 삽입
        putInThread.start();

        // 인터벌 생성
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 분석 및 저장
        while(dataList.getSize() != 0){
            if(cnt < 5) {
                executorService.execute(analysis);
            }
        }

        // 종료
        executorService.shutdown();
        System.out.println("프로그램 종료");

    }

    // 스레드 수 체크 메소드
    public void upCnt(){
        cnt++;
    }
    public void downCnt(){
        cnt--;
    }
}

