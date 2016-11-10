package application_layer;

import service_layer.DatagramMessage;
import service_layer.MyServerDatagramSocket;

/**
 * Created by t00175569 on 10/11/2016.
 */
public class FtpServer {
    public static void main(String[] args) {

        int serverPort = 7;    // default port
        if (args.length == 1)
            serverPort = Integer.parseInt(args[0]);
        try {
            // instantiates a datagram socket for both sending
            // and receiving data
            MyServerDatagramSocket mySocket = new MyServerDatagramSocket(serverPort);
//            System.out.println("Echo server ready.");
            while (true) {  // forever loop
                DatagramMessage request = mySocket.receiveMessageAndSender();
                System.out.println("Request received");
                String message = request.getMessage();
                System.out.println("message received: " + message);
                // Now send the echo to the requestor
                mySocket.sendMessage(request.getAddress(),
                        request.getPort(), message);
            } //end while
        } // end try
        catch (Exception ex) {
            ex.printStackTrace();
        } // end catch
    }
}
