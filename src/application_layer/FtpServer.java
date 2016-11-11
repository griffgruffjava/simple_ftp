package application_layer;

import service_layer.DatagramMessage;
import service_layer.MyServerDatagramSocket;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by t00175569 on 10/11/2016.
 */
public class FtpServer {


    public static void main(String[] args) {

        //creating some hardcoded system users
        List<User> registeredUsers = new ArrayList<>();
        User one = new User("cgriffin", "password");
        User two = new User("dtrump", "maga");
        User three = new User("hclinton", "emailserver");
        registeredUsers.add(one);
        registeredUsers.add(two);
        registeredUsers.add(three);

        List<String> loggedOn = new ArrayList<>();


        int serverPort = 1024;    // default port
        if (args.length == 1)
            serverPort = Integer.parseInt(args[0]);
        try {
            // instantiates a datagram socket for both sending and receiving data
            MyServerDatagramSocket mySocket = new MyServerDatagramSocket(serverPort);
//            System.out.println("Echo server ready.");
            while (true) {  // forever loop
                DatagramMessage request = mySocket.receiveMessageAndSender();
                String response = "";
//                String message = request.getMessage();
                ProtocolMessage protocalMessage = new ProtocolMessage(request.getMessage());

                switch (protocalMessage.getCode()) {
                    case 900:
                        response = registerRequest(protocalMessage, registeredUsers);
                        break;
                    case 1000:
                        response = logOnRequest(protocalMessage, registeredUsers, loggedOn);
                        break;
                    case 1100:
                        response = logOffRequest(protocalMessage, registeredUsers, loggedOn);
                        break;
                    case 1200:
                        response = uploadRequest(protocalMessage, loggedOn);
                        break;
                }
                // Now send the echo to the requestor
                mySocket.sendMessage(request.getAddress(), request.getPort(), response);
            } //end while
        } // end try
        catch (Exception ex) {
            ex.printStackTrace();
        } // end catch
    }

//    public static int grabCode(String message) {
//        try {
//            return Integer.parseInt(message.substring(0, 3));
//        }catch (Exception e) {
//            e.printStackTrace();
//            return -1;
//        }
//    }

    public static String registerRequest(ProtocolMessage protocolMessage, List registered) {
        User sentUser = new User(protocolMessage.getDeckOne(), protocolMessage.getDeckTwo());
        if(userNameNotUsed(sentUser.getUserName(), registered)){
            try {
                createDir(sentUser.getUserName());
                registered.add(sentUser);
                return "910-SUCCESS-" + sentUser.getUserName() + " is now registered";
            }catch (Exception e){
                e.printStackTrace();
            }
            return "920-ERROR- cannot register user";
        }

        return "930-ERROR-" + sentUser.getUserName() + " username is already registered";
    }

    public static String logOnRequest(ProtocolMessage protocolMessage, List registered, List active) {

        User sentUser = new User(protocolMessage.getDeckOne(), protocolMessage.getDeckTwo());

        if(registeredAndVerified(sentUser, registered)) {
            if (!active.contains((sentUser.getUserName())))
                active.add(sentUser.getUserName());
            return "1010-SUCCESS-" + sentUser.getUserName() + " is logged on";
        }

        return "1020-ERROR-" + sentUser.getUserName() + " does not match";

    }

    public static String logOffRequest(ProtocolMessage protocolMessage, List registered, List active){

        User sentUser = new User(protocolMessage.getDeckOne(), protocolMessage.getDeckTwo());

        if(active.contains(sentUser.getUserName())) {
            if(registeredAndVerified(sentUser, registered)) {
                active.remove(sentUser.getUserName());
                return "1110-SUCCESS-" + sentUser.getUserName() + " is now logged off";
            }
            return "1120-ERROR- details are incorrect";
        }
        return "1130-ERROR- this user is not logged in";

    }

    public static String uploadRequest(ProtocolMessage protocolMessage, List active){

        if(active.contains(protocolMessage.getDeckOne())){
            try{
                String load = protocolMessage.getDeckTwo();
                String saveAs;
                String content;
                int splitIndex = load.indexOf(":");
                saveAs = load.substring(0,splitIndex);
                content = load.substring(splitIndex+1,load.length());
                String pathToSave = "C:\\"+protocolMessage.getDeckOne()+"\\"+ saveAs + ".txt";
//                String pathToSave = "C:\\testFile.txt";
                File file = new File(pathToSave);

                //http://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(content);
                bw.close();

                return "1210-SUCCESS-" + saveAs + " has been uploaded to server";

            } catch (IOException e) {
                e.printStackTrace();
                return "1220-ERROR- server failure; file cannot be saved";
            }

        }
        return "1230-ERROR-" + protocolMessage.getDeckOne() + " is not logged on; file cannot be saved";
    }


    public static boolean registeredAndVerified(User sentUser, List registered){

        for (Object u : registered) {
            User temp = (User) u;
            boolean credentialsAreGood = temp.getUserName().equals(sentUser.getUserName()) && temp.getPassword().equals(sentUser.getPassword());
            if( credentialsAreGood) {
                return true;
            }
        }
        return false;
    }

    public static boolean userNameNotUsed(String username, List registered){
        for (Object u : registered) {
            User temp = (User) u;
            if (temp.getUserName().equals(username)) {
                return false;
            }
        }
        return true;
    }

    public static boolean createDir(String dir) {
        String path = "C:\\"+dir;
        try {
            new File(path).mkdir();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }








}




