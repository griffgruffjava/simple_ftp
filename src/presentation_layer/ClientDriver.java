package presentation_layer;

import application_layer.FtpClient;

import javax.swing.*;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by t00175569 on 10/11/2016.
 */
public class ClientDriver {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        try {

            int menuOneChoice=0;
            int menuTwoChoice;
            String user;
            String password;
            String response;
            String path;
            String title;

            FtpClient ftpClient = new FtpClient("localhost", "1024");

            while(menuOneChoice!=3){
                System.out.print("Welcome to Simple_FTP " +
                        "\n\nHOME Menu:\n" +
                        "To register <enter 1>\n" +
                        "To login <enter 2>\n" +
                        "To exit <enter 3>\n" +
                        "Enter choice:");
                menuOneChoice = input.nextInt();

                if(menuOneChoice==1){
                    System.out.print("Select username:");
                    user = input.next();
                    System.out.print("Select password:");
                    password = input.next();
                    response = ftpClient.registerUser(user,password);

                    System.out.println(response);
                }

                if(menuOneChoice==2){
                    System.out.print("\n\nYou are now logged on.\n" +
                            "ACTION Menu\n" +
                            "To upload <enter 1>\n" +
                            "To download <enter 2>\n" +
                            "To log off <enter 3>\n" +
                            "Enter choice:");
                    menuTwoChoice = input.nextInt();
                    if(menuTwoChoice == 1){
                        System.out.print("enter file path:");
                        path = input.next();
                        System.out.print("enter title for file:");
                        title = input.next();

                    }
                }
            }
            System.out.println("\n\nThank you for using Simple_FTP, Goodbye!");

//            String response = ftpClient.logOn("cgriffin","password");

//            String response = ftpClient.registerUser("hillary_clinton","emailserver");
//            JOptionPane.showMessageDialog(null, response);
//            ftpClient.registerUser("donald_trump","maga");
//            response = ftpClient.logOn("donald_trump","maga");
//            ftpClient.logOn("hillary_clinton","emailserver");
//            JOptionPane.showMessageDialog(null, response);
//            String response = ftpClient.logOff("donald_trump","maga");


//            String response = ftpClient.uploadFile("donald_trump", "C:\\MyFile.txt", "anotherOne");
//            ftpClient.downloadFile("donald_trump", "anotherOne");
//            String response = ftpClient.downloadFile("hillary_clinton", "first");
//            JOptionPane.showMessageDialog(null, ftpClient.uploadFile("donald_trump", "C:\\MyFile.txt", "anotherOne"));
//            JOptionPane.showMessageDialog(null, response);
            ftpClient.closeSocket();





        }
        catch (Exception e) {
            e.printStackTrace();
        }




    }



}
