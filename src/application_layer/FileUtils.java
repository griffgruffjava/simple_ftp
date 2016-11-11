package application_layer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by t00175569 on 11/11/2016.
 */
public class FileUtils {

    public static String getFileContent(String path){


        String content = "";
        try {

            Path path1 = Paths.get(path);
            content = new String(Files.readAllBytes(path1));


        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;


    }


    public static boolean writeContentToFile(ProtocolMessage protocolMessage){

        try{
            String load = protocolMessage.getDeckTwo();
            String saveAs = getSaveAs(load);
            String content = getContent(load);
            String pathToSave = "C:\\ftp_server\\"+protocolMessage.getDeckOne()+"\\"+ saveAs + ".txt";
            File file = new File(pathToSave);

            //http://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean writeContentToClient(ProtocolMessage protocolMessage){
        try{
            String load = protocolMessage.getDeckTwo();
            String saveAs = getSaveAs(load);
            String content = getContent(load);
            String pathToSave = "C:\\ftp_downloads\\"+ saveAs;
            File file = new File(pathToSave);

            //http://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean createDir(String parentDir, String subDir) {
        String path = "C:\\"+parentDir+"\\"+subDir;
        try {
            new File(path).mkdirs();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getSaveAs(String load) {
        int splitIndex = load.indexOf(":");
        return load.substring(0,splitIndex);
    }

    public static String getContent(String load){
        int splitIndex = load.indexOf(":");
        return load.substring(splitIndex+1,load.length());
    }
}
