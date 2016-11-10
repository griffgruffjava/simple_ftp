package presentation_layer;

import application_layer.FtpClient;

import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by t00175569 on 10/11/2016.
 */
public class ClientDriver {

    public static void main(String[] args) {

        try {
            FtpClient ftpClient = new FtpClient("localhost", "9");
            ftpClient.logOn("cgriffin","password");
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

}
