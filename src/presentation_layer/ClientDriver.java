package presentation_layer;

import application_layer.FtpClient;

import javax.swing.*;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by t00175569 on 10/11/2016.
 */
public class ClientDriver {

    public static void main(String[] args) {

        try {
            FtpClient ftpClient = new FtpClient("localhost", "1024");
            String response = ftpClient.logOn("cgriffin","password");
            JOptionPane.showMessageDialog(null, response);
            response = ftpClient.logOff("cgriffin","password");
            JOptionPane.showMessageDialog(null, response);


        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

}
