import java.io.*;
import java.util.*;

public class PutIn implements Runnable{
    private DataList dataList;

    public PutIn(DataList dataList){
        this.dataList = dataList;
    }

    @Override
    public void run() {
        // 크롤링 파일 읽기
        String targetDirName = "C:\\Users\\ajs\\Desktop\\test";
        String moveDirName =  "C:\\Users\\ajs\\Desktop\\test\\move";
        String fileExt = "txt";

        while(true){

            // 특정 폴더내 특정 확장파일 목록 불러오기
            List fileList = getFileNames(targetDirName, fileExt);
            //System.out.println("=============================   filesize : " + fileList.size() + "   ============================");
            if(fileList.size() > 0){
                BufferedReader bReader = null;

                for(int i = 0; i < fileList.size(); i++) {
                    File file = new File(targetDirName+"\\"+fileList.get(i));
                    try {
                        //System.out.println("filepath : "+targetDirName+"\\"+fileList.get(i));
                        System.out.println("=============================   fileName : " + fileList.get(i) + "   ============================");
                        if(file.exists()){
                            bReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"euc-kr"));
                            String txt = "";
                            String data = "";
                            while((txt = bReader.readLine()) != null){
                                data += txt;
                            }
                            dataList.setData(data);

                        }else {
                            //파일없음
                        }
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    } finally {
                        try {
                            if (bReader != null) {
                                bReader.close();
                                System.out.println("================================   Close   ================================");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    try {
                        FileInputStream fin = new FileInputStream(targetDirName+"\\"+fileList.get(i));
                        FileOutputStream fout = new FileOutputStream(moveDirName+"\\"+fileList.get(i));
                        int tmp = 0;
                        while((tmp = fin.read()) != -1){
                            fout.write(tmp);
                        }
                        fin.close();
                        fout.close();
                        System.out.println(file.delete());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }else{
                System.out.println("읽을 파일이 없습니다.");
                break;
            }

        }

    }

    // 파일 목록 불러오기
    public List<String> getFileNames(String targetDirName, String fileExt) {
        List<String> fileNames = new ArrayList();
        File dir = new File(targetDirName);
        fileExt = fileExt.toLowerCase();

        if(dir.isDirectory()) {
            String dirName = dir.getPath();
            String[] filenames = dir.list(null);
            int cntFiles = filenames.length;

            for(int iFile=0; iFile < cntFiles; iFile++){
                String filename = filenames[iFile];
                String fullFileName = dirName + "\\" + filename;
                File file = new File(fullFileName);

                boolean isDirectory = file.isDirectory();
                if(!isDirectory && filename.toLowerCase().endsWith(fileExt)) {
                    //fileNames.add(fullFileName);
                    fileNames.add(filename);
                }
            }
        }
        return fileNames;
    }
}
