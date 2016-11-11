package application_layer;

import service_layer.MyClientDatagramSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by t00175569 on 10/11/2016.
 */
public class FtpClient {
    private MyClientDatagramSocket clientSocket;
    private InetAddress serverHost;
    private int serverPort;

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

    public String uploadFile(String username, String path) throws SocketException, IOException {
        String protocolMsg = "1200-UPLOAD-" + username + "-" + path;
        clientSocket.sendMessage(serverHost, serverPort, protocolMsg);
        return clientSocket.receiveMessage();
    }

    public void closeSocket() throws SocketException {
        clientSocket.close();
    }
}
