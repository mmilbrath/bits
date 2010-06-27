package net.backupbits.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;
import java.util.Enumeration;

/**
 * The ServerProperties class is a utility class other classes use to obtain
 * configuration information. This class reads from the configuration file
 * and sets default properties.
 * 
 * @author Chuck_Slate
 * @version 1.0
 * @since 1.0
 * 
 */
public class ServerProperties {

    // Name of default configuration file
    private static final String DEFAULT_PROPERTIES_FILENAME = "server.conf";
    // Conf file properties
    private static final String DEBUG = "debug";
    private static final String LISTEN_IP = "listen.ip";
    private static final String LISTEN_PORT = "listen.port";
    // Default values (used only if they cannot be read from the conf file)
    private static final String DEFAULT_DEBUG = "true";
    private static final String DEFAULT_LISTEN_IP = "0.0.0.0";
    private static final String DEFAULT_LISTEN_PORT = "4430";
    // propRef is used to provide a reference to other classes who need access to the content of this
    // singleton class
    private static ServerProperties propRef = null;
    private Properties properties = null;

    /**
     * Defines the default configuration values, attempts to load the passed in configuration file name, and 
     * prints the values actually used.
     * 
     * @param configFileName The name of the file, specified when the app is first launched, to be used
     * for configuration. 
     */
    private ServerProperties(String configFileName) {
        this.properties = new Properties();
        // Adding default values to prop file.  We'll then load the conf file.  When done, any attributes
        // whose values are not explicitly set in the conf file will instead use these defaults 
        this.properties.setProperty(DEBUG, DEFAULT_DEBUG);
        this.properties.setProperty(LISTEN_IP, DEFAULT_LISTEN_IP);
        this.properties.setProperty(LISTEN_PORT, DEFAULT_LISTEN_PORT);
        this.loadProperties(configFileName);
        this.printProperties();
    } // end constructor

    /**
     * Returns true if debugging is enabled.
     * 
     * @return boolean
     */
    public boolean getDebug() {
        String debugValue = properties.getProperty(DEBUG);
        if (debugValue.equalsIgnoreCase("true") || debugValue.equalsIgnoreCase("on") || debugValue.equalsIgnoreCase("yes")) {
            return true;
        }
        return false;
    } // end getPlayerDebug

    /**
     * Used by other classes to get a reference to the ClientProperties object. In other words, 
     * the class in question will declare a ClientProperties object, then set it equal to 
     * ServerProperties.getRef(configFileName);
     * 
     * @param configFileName The name of the file to use for configuration.
     * @return ServerProperties The reference.
     */
    public static synchronized ServerProperties getRef(String configFileName) {
        if (propRef == null) {
            propRef = new ServerProperties(configFileName);
        }
        return propRef;
    } // end getRef

    /**
     * Parses the configuration and returns the IP address.
     * 
     * @return InetAddress The IP address on which the app should listen for incoming client requests.
     */
    public InetAddress getListenIp() {
        InetAddress bindIp = null;
        String tmpServerIP = properties.getProperty(LISTEN_IP);
        // (There has GOT to be a better way to do this... Chuck)
        // Take the IP address string from conf file and tokenize it by decimal point
        // Then convert each token into an int
        // Then create a byte array and convert the ints to bytes when adding them to the array
        // Finally, create an InetAddress using the byte array
        String[] tokenizedIP = tmpServerIP.split("\\.");
        int ip0 = Integer.parseInt(tokenizedIP[0]);
        int ip1 = Integer.parseInt(tokenizedIP[1]);
        int ip2 = Integer.parseInt(tokenizedIP[2]);
        int ip3 = Integer.parseInt(tokenizedIP[3]);

        byte[] serverIpBytes = {(byte) ip0, (byte) ip1, (byte) ip2, (byte) ip3};
        try {
            bindIp = InetAddress.getByAddress(serverIpBytes);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return bindIp;

    } // end getBindIp

    /**
     * Parses the configuration and returns the listen port.
     * 
     * @return int The TCP port on which the app should listen for incoming requests.
     */
    public int getListenPort() {
        return (Integer.parseInt((String) properties.getProperty(LISTEN_PORT)));
    } // end getBindPort

    /**
     * Loads the configuration values from the input file. If the input file name is null, the 
     * app instead attempts to use "./client.conf" as its configuration file. If it can't find either file, 
     * it uses the default values, which are defined in the ClientProperties class.
     *  
     * @param configFileName The name of the configuration file specified at launch time.
     */
    private void loadProperties(String configFileName) {

        // Initially set props file name to default value
        String fileName = ServerProperties.DEFAULT_PROPERTIES_FILENAME;
        // If passed a props file name, use that instead
        if (configFileName != null) {
            fileName = configFileName;
            System.out.println("Got startup directive to use specific configuration file (" + fileName + ")");
        } else {
            System.out.println("No configuration file specified at startup...\nAssuming default configuration file (" + fileName + ")");
        }


        System.out.println("Attempting to read configuration file...");
        File propsFile = new File(fileName);
        // If the specified conf file does not exist, use default configuration values
        if (!propsFile.exists()) {
            System.out.println("WARNING - Could not open configuration file.");
            System.out.println("Using default values instead...");
        } // Configuration file exists, so use it to load props
        else {
            try {
                FileInputStream fis = new FileInputStream(fileName);
                // If this loads, it will overwrite all of the default properties, which were set above.
                // If not, the defaults are used instead
                properties.load(fis);
                fis.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }  // end loadProperties

    /**
     * Displays the current configuration values.
     * 
     */
    public void printProperties() {
        System.out.println("\nConfiguration values:");
        Enumeration<?> en = properties.propertyNames();
        while (en.hasMoreElements()) {
            String tmpProperty = (String) en.nextElement();
            // Get property value from next property in enumeration
            System.out.println(tmpProperty + " = " + (String) properties.get(tmpProperty));
        }
    } // end printProperties

    // This is ensure this singleton class cannot be cloned
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
} // end ClientProperties

