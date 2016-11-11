package application_layer;

import service_layer.MyClientDatagramSocket;

import java.io.*;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by t00175569 on 10/11/2016.
 */
public class FtpClient {
    private MyClientDatagramSocket clientSocket;
    private InetAddress serverHost;
    private int serverPort;
    private FileUtils fileUtils;

    public FtpClient(String hostName, String portNum) throws SocketException, UnknownHostException {
        this.serverHost = InetAddress.getByName(hostName);
        this.serverPort = Integer.parseInt(portNum);
        this.clientSocket = new MyClientDatagramSocket();
    }

    public String registerUser(String username, String password) throws SocketException, IOException {
        String protocolMsg = "900-REG-" + username + "-" + password;
        clientSocket.sendMessage(serverHost, serverPort, protocolMsg);
        return clientSocket.receiveMessage();
    }

    public String logOn(String username, String password) throws SocketException, IOException {
        String protocolMsg = "1000-LON-" + username + "-" + password;
        clientSocket.sendMessage(serverHost, serverPort, protocolMsg);
        return clientSocket.receiveMessage();
    }

    public String logOff(String username, String password) throws SocketException, IOException {
        String protocolMsg = "1100-LOFF-" + username + "-" + password;
        clientSocket.sendMessage(serverHost, serverPort, protocolMsg);
        return clientSocket.receiveMessage();
    }

    public String uploadFile(String username, String path, String saveAs) throws SocketException, IOException {
        String protocolMsg = "1200-UPLOAD-" + username + "-" + saveAs + ":" +fileUtils.getFileContent(path);
        clientSocket.sendMessage(serverHost, serverPort, protocolMsg);
        return clientSocket.receiveMessage();
    }

    public String downloadFile(String username, String filename) throws SocketException, IOException {
        String protocolMsg = "1300-DOWNLOAD-"+ username + "-" + filename;
        clientSocket.sendMessage(serverHost, serverPort, protocolMsg);
        ProtocolMessage response = new ProtocolMessage(clientSocket.receiveMessage());
        if(response.getCode()==1315){
            FileUtils.createDir("ftp_client",username);
            if (FileUtils.writeContentToClient(response)) {
                response.setDeckTwo("file saved locally");
            }
            else {
                response.setCode(1325);
                response.setCall("ERROR");
                response.setDeckTwo("file cannot be saved locally");
            }
        }
        return response.toString();
    }

    public void closeSocket() throws SocketException {
        clientSocket.close();
    }


}
