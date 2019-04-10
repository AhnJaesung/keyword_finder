import DAO.Dao;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;

import java.text.SimpleDateFormat;
import java.util.*;

public class Analysis implements Runnable{
    private DataList dataList;
    private String threadName;

    public Analysis(DataList dataList, String threadName){
        this.dataList = dataList;
        this.threadName = threadName;
    }

    @Override
    public void run(){
        // 데이터 테스트
        /*data = data.replaceAll("\"", "");
        data = data.replaceAll("'", "\"");
        data = data.replaceAll(System.getProperty("line.separator"), "");
        data = data.replaceAll("(\r\n|\r|\n|\n\r)", "");
        data = data.replaceAll("\n", "");*/
        /*System.out.println(data.substring(3100, 3700));*/
        //System.out.println(data);

        long beforeTime = System.currentTimeMillis();
        boolean checkTime = true;

        /*while (true){*/
            if(dataList.getDataPeek() != null){
                System.out.println(Thread.currentThread().getName());
                //System.out.println(threadName + "구동");
                String data = dataList.getData();
                // 제이슨 변환
                JsonParser parser = new JsonParser();
                JsonObject jsonObject =  parser.parse(data).getAsJsonObject();
                String str = jsonObject.get("text").getAsString();

                // 파라미터
                Map<String, String> paramMap = new HashMap<String, String>();
                String wordList = "";
                String date = "";
                String locX ="";
                String locY = "";

                // kkma 형태소 분석기
                /*String strToExtrtKwrd = str;

                KeywordExtractor ke = new KeywordExtractor();

                KeywordList kl = ke.extractKeyword(strToExtrtKwrd, true);

                for( int j = 0; j < kl.size(); j++ ) {
                    Keyword kwrd = kl.get(j);
                    //System.out.println(kwrd.getString() + kwrd.getCnt());
                    if(kl.size() == j+1){
                        wordList += kwrd.getString();
                    }else{
                        wordList += kwrd.getString() + ", ";
                    }
                }
                System.out.println(wordList);*/


                // KOMORAN 형태소 분석기
                Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
                KomoranResult analyzeResultList = komoran.analyze(str);
                // 전체
                //System.out.println(analyzeResultList.getPlainText());
                // 명사추출(키워드)
                //System.out.println(analyzeResultList.getNouns());
                wordList = analyzeResultList.getNouns().toString().substring(1, analyzeResultList.getNouns().toString().length() - 1);
                // 토큰별 추출
                /*List<Token> tokenList = analyzeResultList.getTokenList();
                for (Token token : tokenList) {
                    System.out.format("(%2d, %2d) %s/%s\n", token.getBeginIndex(), token.getEndIndex(), token.getMorph(), token.getPos());
                }*/

                // 좌표 값 확인
                //System.out.println(jsonObject.get("coordinates") != null);
                if(jsonObject.get("coordinates") != null && jsonObject.get("coordinates").getAsJsonObject().get("coordinates") != null){
                    //System.out.println(jsonObject.get("coordinates").getAsJsonObject().get("coordinates").toString());
                    locX = String.valueOf(jsonObject.get("coordinates").getAsJsonObject().get("coordinates").getAsJsonArray().get(0));
                    locY = String.valueOf(jsonObject.get("coordinates").getAsJsonObject().get("coordinates").getAsJsonArray().get(1));
                }
                paramMap.put("locX", locX);
                paramMap.put("locY", locY);

                paramMap.put("wordList", wordList);

                // 날짜 변환
                String[] dayList = jsonObject.get("created_at").toString().substring(1, jsonObject.get("created_at").toString().length()-1).split(" ");
                String day =  dayList[1] + " " + dayList[2] + " " + dayList[3] + " " + dayList[5];
                SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat fromFormat = new SimpleDateFormat("MMM d HH:mm:ss yyyy", Locale.ENGLISH);
                try {
                    Date fromDate = fromFormat.parse(day);
                    date = toFormat.format(fromDate);
                    //System.out.println(date);
                }catch (Exception e){
                    e.printStackTrace();
                }
                paramMap.put("writeDate", date);

                // keyword 인서트
                Dao dao = new Dao();
                int idxSeq = dao.insertData(paramMap);

                Map<String, String> paramMap2 = new HashMap<String, String>();
                paramMap2.put("idxSeq", Integer.toString(idxSeq));

                // keyword 카운트
                String[] list = wordList.split(",");
                for(String s : list){
                    paramMap2.put("word", s.trim());
                    dao.keywordCnt(paramMap2);
                }
            }else{
                if(checkTime){
                    long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
                    long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
                    System.out.println("시간차이(m) : "+secDiffTime);
                    checkTime = false;
                }
            }
        /*}*/
    }
}
