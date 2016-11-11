package application_layer;

import service_layer.DatagramMessage;
import service_layer.MyServerDatagramSocket;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static application_layer.FileUtils.createDir;

/**
 * Created by t00175569 on 10/11/2016.
 */
public class FtpServer {


    public static void main(String[] args) {


        //creating some hardcoded system users
        List<User> registeredUsers = new ArrayList<>();
//        User one = new User("cgriffin", "password");
//        User two = new User("donald_trump", "maga");
//        User three = new User("hclinton", "emailserver");
//        registeredUsers.add(one);
//        registeredUsers.add(two);
//        registeredUsers.add(three);

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
                    case 1900:
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
                    case 1300:
                        ProtocolMessage pm = downloadRequest(protocalMessage, loggedOn);
                        response = pm.toString();
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



    public static String registerRequest(ProtocolMessage protocolMessage, List registered) {
        User sentUser = new User(protocolMessage.getDeckOne(), protocolMessage.getDeckTwo());
        if(userNameNotUsed(sentUser.getUserName(), registered)){
            try {
                FileUtils.createDir("ftp_server",sentUser.getUserName());
                registered.add(sentUser);
                return "1910-SUCCESS-" + sentUser.getUserName() + " is now registered";
            }catch (Exception e){
                e.printStackTrace();
            }
            return "1920-ERROR- cannot register user";
        }

        return "1930-ERROR-" + sentUser.getUserName() + " username is already registered";
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
            if(FileUtils.writeContentToFile(protocolMessage)){
                return "1210-SUCCESS-"+ FileUtils.getSaveAs(protocolMessage.getDeckTwo()) + " has been saved to server";
            }else{
                return "1220-ERROR-"+ FileUtils.getSaveAs(protocolMessage.getDeckTwo()) + " cannot be saved to server";
            }


        }
        return "1230-ERROR-" + protocolMessage.getDeckOne() + " is not logged on; file cannot be saved to server";
    }

    public static ProtocolMessage downloadRequest(ProtocolMessage protocolMessage, List active){
        ProtocolMessage response = new ProtocolMessage();
        if(active.contains(protocolMessage.getDeckOne())){
            String path = "C:\\ftp_server\\" + protocolMessage.getDeckOne() + "\\" + protocolMessage.getDeckTwo();
            String serverFile = FileUtils.getFileContent(path);
            response.setCode(1315);
            response.setCall("PROCESS");
            response.setDeckOne(protocolMessage.getDeckOne());
            response.setDeckTwo(protocolMessage.getDeckTwo()+":"+serverFile);
            return response;

        }
        response.setCode(1330);
        response.setCall("ERROR");
        response.setDeckOne(protocolMessage.getDeckOne());
        response.setDeckTwo(" is not logged on; no files can be retrieved");
        return response;

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










}




