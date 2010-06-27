package net.backupbits.server.comm;

import java.net.*;

import net.backupbits.server.ServerProperties;


/**
 * The ServerChannel class is a utility class used to abstract the server-side details of the
 * networking layer from the Server code. That is, the Server class only knows that it has 
 * a channel between itself and its clients. However, thanks to the abstraction,
 * it has no idea if the channel is TCP sockets-based or other.  In this way, the networking layer
 * can be changed in the future without modifications to the Server code itself. Currently, the ServerChannel 
 * class embeds a ServerSocket object to represent the server side of the connection.
 * 
 *
 * @author Chuck_Slate
 * @version 1.0
 * @since 1.0
 * 
 */
public class ServerChannel {

    private static InetAddress serverListenIp;
    private static int serverListenPort;
    private ServerSocket serverSocket;

    /**
     * Constructor
     * @param props ServerProperties object containing bind information
     */
    public ServerChannel(ServerProperties props) {
        try {
            serverListenIp = props.getListenIp();
            serverListenPort = props.getListenPort();

            // Display configuration values
            System.out.println("ServerChannel setting server listen IP = " + serverListenIp);
            System.out.println("ServerChannel setting server listen port = " + serverListenPort);


            // Bind server socket
            System.out.println("ServerChannel attempting to bind server socket...");
            // Explicitly feeding the port and IP from the conf file to the server socket
            // The middle operand (0) effects the "backlog." From what I can tell, it controls how many connections java
            // will queue if a listener is not available before generating an error.  All I know for sure is that the 0 means use the default
            // and that's OK with this guy...
            serverSocket = new ServerSocket(serverListenPort, 0, serverListenIp);
            if (serverSocket.isBound()) {
                System.out.println("ServerChannel server socket successfully bound to " + serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort());
            }

            // Now just wait for incoming client connections
            System.out.println("ServerChannel ready and waiting for client connections...\n");

        } catch (Exception e) {
            System.err.println("ServerChannel - Fatal Error!");
            e.printStackTrace();
            System.exit(1);
        }
    } // end constructor

    /**
     * Accepts client connections
     * @return A ClientConnection object for each new connection
     */
    public ClientConnection acceptConnection() {

        try {
            ClientConnection cc = new ClientConnection(serverSocket.accept());
            return cc;
        } catch (Exception e) {
            System.err.println("ServerChannel acceptConnection - Fatal Error!");
            e.printStackTrace();
            return null;
        }
    } // end acceptConnection
} // end ServerChannel

