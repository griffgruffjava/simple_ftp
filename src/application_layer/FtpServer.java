package application_layer;

import service_layer.DatagramMessage;
import service_layer.MyServerDatagramSocket;

import java.util.*;

/**
 * Created by t00175569 on 10/11/2016.
 */
public class FtpServer {



    public static void main(String[] args) {

        //creating some hardcoded system users
        List<User> registeredUsers = new ArrayList<>();
        User one = new User("a", "a");
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

                switch (protocalMessage.getCode()){
                    case 1000 :
                        response = logOnRequest(protocalMessage, registeredUsers, loggedOn);
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

    public static String logOnRequest(ProtocolMessage protocolMessage, List registered, List active) {

        User sentUser = new User(protocolMessage.getDeckOne(), protocolMessage.getDeckTwo());

        if(registered.equals(sentUser)) {
            if(!active.contains((sentUser.getUserName())))
                active.add(sentUser.getUserName());
            return "1010-ULO-" + sentUser.getUserName() + " is logged on";
        }
        else {
            return "1020-UNF-" + sentUser.getUserName() + " does not match";
        }

    }
}
