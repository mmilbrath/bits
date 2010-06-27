package net.backupbits.server.comm;

import java.net.*;
import java.io.*;

/**
 * The ClientConnection class is a utility class used to abstract the client connection details of the
 * networking layer from the Server code. That is, the Server class only knows that it has a channel
 * between itself and its clients. However, thanks to the abstraction,
 * it has no idea if the channel is TCP sockets-based or other.  In this way, the networking layer
 * can be changed in the future without modifications to the Server code itself. Currently, the ClientConnection 
 * class embeds a Socket object to represent the client side of the connection.
 * 

 * @author Chuck_Slate
 * @version 1.0
 * @since 1.0
 * 
 */
public class ClientConnection {

    /** ClientConnection (currently) implements the connection using a socket */
    private Socket _socket;

    /**
     * Constructor
     *
     */
    public ClientConnection(Socket newSocket) {
        _socket = newSocket;
    }

    /**
     * Get the IP address used by the client for the socket
     * @return Client's IP
     */
    public String getClientAddress() {
        return _socket.getInetAddress().toString();
    }

    /**
     * Get the TCP port number used by the client for the socket
     * @return Client's TCP port
     */
    public int getClientPort() {
        return _socket.getPort();
    }

    /**
     * Get the IP address used by the server for the socket
     * @return Client's IP
     */
    public String getServerAddress() {
        return _socket.getLocalAddress().toString();
    }

    /**
     * Get the TCP port number used by the server for the socket
     * @return Server's TCP port
     */
    public int getServerPort() {
        return _socket.getLocalPort();
    }

    /**
     * Get the OutputStream for the client socket, which the server will use to write instructions
     * to the client
     * @return Output stream
     */
    public OutputStream getOOS() {
        try {
            return _socket.getOutputStream();
        } catch (IOException e) {
            System.err.println("ClientConnection getOOS() error - " + e.getMessage());
            return null;
        }
    }

    /**
     * Get the InputStream for the client socket, which the server will use to read instructions
     * from the client
     * @return Input stream
     */
    public InputStream getOIS() {
        try {
            return _socket.getInputStream();
        } catch (IOException e) {
            System.err.println("ClientConnection getOIS() error - " + e.getMessage());
            return null;
        }
    }

    /**
     * Close the socket
     */
    public void close() {
        try {
            _socket.close();
        } catch (IOException e) {
            System.err.println("ClientConnection close() error - " + e.getMessage());
        }
    }

    /**
     * See if the socket is closed
     * @return true if closed
     */
    public boolean isClosed() {
        return _socket.isClosed();
    }
}
