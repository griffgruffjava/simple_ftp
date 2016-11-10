package application_layer;

import service_layer.DatagramMessage;
import service_layer.MyServerDatagramSocket;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by t00175569 on 10/11/2016.
 */
public class FtpServer {



    public static void main(String[] args) {

        Map<String, String> users = new HashMap<>();
        users.put("cgriffin", "password1");

        int serverPort = 7;    // default port
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
                        response = logOnRequest(protocalMessage,users);
                }
                // Now send the echo to the requestor
                mySocket.sendMessage(request.getAddress(),
                        request.getPort(), response);
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

    public static String logOnRequest(ProtocolMessage protocolMessage, Map<String,String> users) {
        System.out.println("FtpServer.logOnRequest");
        return (users.get(protocolMessage.getDeckOne()));


    }
}
