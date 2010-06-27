package net.backupbits.server;

import java.util.*;

import net.backupbits.common.*;
import net.backupbits.server.comm.*;


/**
 * The Server class represents the principal server logic, and
 * it starts the server software.
 *
 * <p>
 * @author Chuck_Slate
 * @version 1.0
 * @since 1.0
 *
 */
public class Server {

    private static ServerProperties props = null;
    private static int workerThreadId = 1;
    // The list of active players

    /**
     * Gets configuration from the props file, creates the channel between server and client, and
     * spawns a new worker object for each new connection.
     *
     * @param args The command line arguments
     *
     */
    public static void main(String args[]) {
        try {

            // "Splash screen"
            System.out.println("\n~=~=~=~=~=~=~=~=~=" +
                    "\nBackup Bits Server" +
                    "\n~=~=~=~=~=~=~=~=~=\n");


            // Mention time/date of system startup
            final Date startDate = new Date();
            System.out.println("Server started: " + startDate);

            // Ensure that we always have a configuration file name to parse
            String configFileName = null;
            if (args.length > 0) {
                configFileName = args[0];
            }

            // Get configuration values from the configuraiton file - note that ServerProperties will deal with it
            // if the configFileName is null
            props = ServerProperties.getRef(configFileName);
            Instruction myInstruction = new Instruction();
            System.out.println("\nServer version: " + myInstruction.getServerVersion());

            // Find out which version of the JRE is running
            String jreVersion = System.getProperty("java.version");
            System.out.println("Loading with JRE version " + jreVersion);


            // Get a server channel so we can receive connections from clients
            ServerChannel sc = new ServerChannel(props);
            // With each new client connection, spawn a child listener to handle it
            while (true) {
                // Spawn a client connection to handle each new client
                ClientConnection clientConnectionHG = sc.acceptConnection();
                // Create a new Player object, passing it client connection, thread count, and debug state
                Thread t = new WorkerThread(clientConnectionHG, workerThreadId, props);
                t.start();
                workerThreadId++;
            }
        } catch (Exception e) {
            System.err.println("Fatal Error - Terminating server.");
            e.printStackTrace();
            System.exit(1);
        }
    } // end main

    public static void stop() {
        // Mention time/date of system shut down
        final Date stopDateComplete = new Date();
        System.out.println("Server shut down completed " + stopDateComplete);

        System.exit(0);
    }
}  // end Game class






