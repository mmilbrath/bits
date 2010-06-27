package net.backupbits.server;

import java.net.SocketException;
// import java.util.*;
import java.util.Date;
import java.io.*;
import java.sql.*;
//import java.io.File;
import java.util.ArrayList;

import net.backupbits.common.*;
import net.backupbits.server.comm.*;


/*************************************
 * This class represents the domain logic, and there is one instance
 *
 *
 * @author Chuck_Slate
 * @version 1.0
 * @since 1.0
 *
 */
public class WorkerThread extends Thread implements Channel {

    // Declare thread-specific variables
    private ClientConnection _clientConnection = null;
    // Declare socket-specific variables
    private ServerProperties _props = null;
    private boolean _debug = false;
    private ObjectInputStream _ois = null;
    private ObjectOutputStream _oos = null;
    private boolean _isActive = true;
    private boolean _isRunning = true;
    private int _workerThreadId = 0;
//    private File _tmpFileStore = new File("./");
//    private String _userName = null;
    private Connection dbConnection = null;
    private PreparedStatement pstmt = null;
    private String sqlInsert = "";
    // Open the db connection



    /**
     * Implicit constructor.  Defined here to make inheritance easier.
     *
     */
    public WorkerThread() {
    }

    /**
     * WorkerThread
     *
     * @param clientConnection The socket from the client connection.
     * @param workerThreadId A unique ID used to represent the client until the client provides
     * the server with the client.
     * @param props The Properties object that contains information from the configuration file.
     */
    public WorkerThread(ClientConnection clientConnection, int workerThreadId, ServerProperties props) {

        // Init variables
        _clientConnection = clientConnection;
        _workerThreadId = workerThreadId;
        _props = props;
        // Get the debug value
        _debug = _props.getDebug();

        final Date connectTime = new Date();
        System.out.println(connectTime + ": WorkerThread " + _workerThreadId + " accepted connection from client at " + _clientConnection.getClientAddress() + ":" + _clientConnection.getClientPort());

        DbConnectionHelper dbHelper = new DbConnectionHelper();
        dbConnection = dbHelper.getConnection();


        // Establish bidirectional communication between server thread and client
        try {

        	// Trying to figure out the pwd
            File pwd = new File(".");
            System.out.println(pwd.getCanonicalFile());

        	_oos = new ObjectOutputStream(_clientConnection.getOOS());
            if (_debug) {
                System.out.println("DEBUG WorkerThread " + _workerThreadId + " Got output object stream from client " + _clientConnection.getClientAddress() + ":" + _clientConnection.getClientPort());
            }
            _ois = new ObjectInputStream(_clientConnection.getOIS());
            if (_debug) {
                System.out.println("DEBUG WorkerThread " + _workerThreadId + " Got input object stream from client " + _clientConnection.getClientAddress() + ":" + _clientConnection.getClientPort());
            }
        } // Error if we can't open the streams
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("WorkerThread failed to open input/output streams. Closing client connection.");
            try {
                this._clientConnection.close();
            } // Error if we can't close the socket
            catch (Exception e1) {
                System.err.println("WorkerThread " + _workerThreadId + " failed to close socket.");
                System.err.println(e1.getMessage());
            }
        }
    } // end constructor

    /**
     * Listens for incoming client requests, and reacts accordingly
     */
    // This method is required to extend Thread, and it fires each time Thread.start() is executed.
    public void run() {
        Instruction action = null;
        String verb = "";

        try {
            if (_debug) {
                System.out.println("DEBUG WorkerThread " + _workerThreadId + " is running? " + _isActive);
            }

            // While the application should continue
            while (_isRunning) {

                // Get the first client command (action/verb) for analysis
                action = this.read();

                // Get Verb from
                verb = (String) action.getVerb();

                // While the player is active - i.e., game is on
                while (_isActive) {

                    // Got a request to quit
                    if (verb.equalsIgnoreCase(Verb.CLIENT_REQUEST_CLOSE_CONNECTION)) {
                        if (_debug) {
                            final Date exitTime = new Date();
                            System.out.println(exitTime + " DEBUG WorkerThread " + _workerThreadId + " got " + Verb.CLIENT_REQUEST_CLOSE_CONNECTION);
                        }
                        // close the server side
                        _isActive = false;
                        _isRunning = false;
                        this.closeConnection();
                        break;
                    } // Got a request to store a file
                    else if (verb.equalsIgnoreCase(Verb.CLIENT_REQUEST_BACKUP_FILE)) {
                        if (_debug) {
                            System.out.println("DEBUG WorkerThread " + _workerThreadId + " got " + Verb.CLIENT_REQUEST_BACKUP_FILE);
                        }

                        // Need method here to check ability to backup (i.e., does user have permissions/space?)
                        // Then respond with reason for rejection, as required

                        // backupAvailability(action);


                        boolean backupSuccess = storeBackupFile(action);
                        if (backupSuccess) {
                            action.setVerb(Verb.SERVER_RESPONSE_BACKUP_SUCCESS);
                            write(action);
                        } else {
                            action.setVerb(Verb.SERVER_RESPONSE_BACKUP_FAILURE);
                            write(action);
                        }
                        action.reset();

                    }
                    // Got a request to restore a file
                    else if (verb.equalsIgnoreCase(Verb.CLIENT_REQUEST_RESTORE_FILE)) {
                        if (_debug) {
                            System.out.println("DEBUG WorkerThread " + _workerThreadId + " got " + Verb.CLIENT_REQUEST_RESTORE_FILE);
                        }

                        boolean restoreSuccess = restoreFile(action);
                        if (restoreSuccess) {
                            action.setVerb(Verb.SERVER_RESPONSE_RESTORE_SUCCESS);
                            write(action);
                        } else {
                            action.setVerb(Verb.SERVER_RESPONSE_RESTORE_FAILURE);
                            write(action);
                        }
                        action.reset();

                    }
                    // Got a request to see meta data for stored files
                    else if (verb.equalsIgnoreCase(Verb.CLIENT_REQUEST_GET_FILELIST)) {
                        if (_debug) {
                            System.out.println("DEBUG WorkerThread " + _workerThreadId + " got " + Verb.CLIENT_REQUEST_GET_FILELIST);
                        }

                        getFileData(action);
                        action.reset();

                    } // If we get this far, we got a bad verb
                    else {
                        if (_debug) {
                            System.out.println("DEBUG WorkerThread " + _workerThreadId + " got " + verb + " request.");
                        }

                        // If they just hit a carriage return, advance them a line on the screen
                        if (verb.length() < 1) {
                            action.reset();
                            action.setVerb(Verb.SERVER_RESPONSE_INVALID_VERB);
                            write(action);
                        } else {
                            this.invalidVerb(action);
                        }
                    }

                    if (_debug) {
                        System.out.println("DEBUG WorkerThread " + _workerThreadId + " done with request...");
                    }
                    // Get the next client command (action/verb) for analysis
                    action = this.read();
                    verb = (String) action.getVerb();

                } // end while _isActive

                // If we make it here, we have been sent a QUIT from client.
                //	if(_isRunning)
                //	{

                //	} // end if _isRunning

            } // end while _isRunning

            // close the game out on the server side
            this.closeConnection();
            if (this._clientConnection.isClosed()) {
                final Date exitTime = new Date();
                System.out.println(exitTime + ": WorkerThread " + _workerThreadId + " closed connection with " + this._clientConnection.getClientAddress());
            } else {
                final Date exitTime = new Date();
                System.err.println(exitTime + ": WorkerThread " + _workerThreadId + " failed to close connection with " + this._clientConnection.getClientAddress());
            }

        } // end try
        catch (Exception e) {
            this._isActive = false;
            this._isRunning = false;
            final Date exitTime = new Date();
            System.out.println(exitTime + ": WorkerThread " + _workerThreadId + " (at " + this._clientConnection.getClientAddress() + ") has stopped running.");
            System.err.println(e.getMessage());
            e.printStackTrace();
            action.reset();
            this.closeConnection();
        }

    } // end run



    private void getFileData(Instruction action) {

    	FileHeader tmpFileData = new FileHeader();
    	ArrayList<FileHeader> _fileList = new ArrayList<FileHeader>();
    	String _userName = action.getUserName();

    	// Get a dataset that includes fileID, fileSize, and upload data and time.
    	// Send that to the client.

        String sqlQuery = "SELECT fileID, origFileName, fileSize, postDateTime FROM server_filelist WHERE postedBy = \"" + _userName + "\"";

        if(_debug)
        {
          System.out.println("DEBUG WorkerThread " + _workerThreadId + " db query statement: <" + sqlQuery + ">");
        }

        try
        {
            DbConnectionHelper dbHelper = new DbConnectionHelper();
            dbConnection = dbHelper.getConnection();
            // Create a result set containing all file data for the given user
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);

            // Cycle through each row of the result set
            while (rs.next())
            {
            	tmpFileData = new FileHeader();
            	tmpFileData.setFileID(rs.getInt("fileID"));
            	tmpFileData.setFileName(rs.getString("origFileName"));
            	tmpFileData.setFileSize(rs.getInt("fileSize"));
            	tmpFileData.setFileTimeStamp(rs.getTimestamp("postDateTime"));

            	_fileList.add(tmpFileData);

                if(_debug)
                {
                    System.out.println("DEBUG WorkerThread " + _workerThreadId + " got file data from ResultSet - ID/Name/Size/Timestamp: " + rs.getInt("fileID") + "/" + rs.getString("origFileName") + "/" + rs.getInt("fileSize") + "/" + rs.getTimestamp("postDateTime"));
                }

            }
            // Close the db connection
            dbConnection.close();

        } catch (SQLException e) {
            System.out.println("WorkerThread " + _workerThreadId + " had error while getting file list: " + e.getMessage());
            if(_debug)
            {
            	e.printStackTrace();
            }
        }

        action.reset();
        action.setVerb(Verb.SERVER_RESPONSE_RETURN_FILELIST);
        action.setFileList(_fileList);
        write(action);

        final Date restoreTime = new Date();
        System.out.println(restoreTime + ": WorkerThread " + _workerThreadId + " sent list of files available for restoration (count = " + _fileList.size() + ") to " + this._clientConnection.getClientAddress());

    }

    /**
     * Closes the socket and removes the WorkerThread object from the list of players
     */
    // Kill a player thread
    private void closeConnection() {
        this._clientConnection.close();
    } // end closeConnection

    protected ObjectOutputStream getOOS() {
        return this._oos;
    }

    /**
     * Sends an error message back to the client because the client has sent an invalid verb (request)
     *
     * @param action Instruction class object where verb = Verb.INVALID_VERB
     *
     */
    /// Client provides unknown verb
    private void invalidVerb(Instruction action) {
        String errorDetails = action.getVerb();
        action.reset();
        action.setVerb(Verb.SERVER_RESPONSE_INVALID_VERB);
        action.setNotes("\n'" + errorDetails + "' is not a valid command sequence.");
        write(action);

    } // end invalidVerb

    /**
     * Gets the next message from the client.
     * @return Instruction Class containing the message.
     */
    public Instruction read() {
        Instruction myInstruction = new Instruction();
        try {
            // Get data from the client
            myInstruction = (Instruction) _ois.readObject();

            if (_debug) {
                System.out.println("DEBUG WorkerThread " + _workerThreadId + " has finished Read() request.");
            }
        } // end try
        catch (SocketException se) {
            System.err.println("\nObjectOutputStream SocketException during Read() method for WorkerThread " + _workerThreadId + " - " + se.getMessage());
            se.printStackTrace();

            // close the connection
            _isActive = false;
            _isRunning = false;
            this.closeConnection();
        } // end SocketException
        catch (Exception e) {
            System.err.println("\nObjectOutputStream non-fatal Exception during Read() method for WorkerThread " + _workerThreadId + " - " + e.getMessage());
            e.printStackTrace();
            // Don't close the connection
        }

        return myInstruction;

    } // end Read

    /**
     * Backs up a file
     * @param action Instruction class containing file name and size
     * @return true if backup is successful, else false
     */
    public boolean storeBackupFile(Instruction action) {

        int fileSize = (int) action.getFileSize();
        int bytesRead;
        int current = 0;
        String sourceFileName = action.getFileName();
        String userName = action.getUserName();

        if (_debug) {
            System.out.println("DEBUG WorkerThread " + this._workerThreadId + " got original file name = " + sourceFileName);
            System.out.println("DEBUG WorkerThread " + this._workerThreadId + " got original file size (bytes) = " + fileSize);
        }

        try {
            if (_debug) {
                System.out.println("DEBUG WorkerThread " + this._workerThreadId + " prepping to read binary stream.");
            }
            byte[] mybytearray = new byte[fileSize];
            InputStream is = _clientConnection.getOIS();
            if (_debug) {
                System.out.println("DEBUG WorkerThread " + this._workerThreadId + " ready to read binary stream.");
                System.out.println("DEBUG WorkerThread " + this._workerThreadId + " mybytearray.length = " + mybytearray.length);
            }
            do {
                bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
                if (bytesRead >= 0) {
                    current += bytesRead;
                }
                if (_debug) {
                    System.out.println("DEBUG WorkerThread " + this._workerThreadId + " Bytes read this loop = " + bytesRead);
                }
            } while (bytesRead > 0);

            if (_debug) {
                System.out.println("DEBUG WorkerThread " + this._workerThreadId + " done reading binary stream.");
                System.out.println("DEBUG WorkerThread " + this._workerThreadId + " mybytearray.length = " + mybytearray.length);
            }

            // All data is now in the byte array
            // Save byte array to db

            // Confirm that the file has a file extension
            int charValue = sourceFileName.lastIndexOf(".");
            if(charValue != -1)
            {
              // If it has a file extension, include that meta data in the database
              String fileExtension = sourceFileName.substring(charValue+1);

              // Note that the origFileName is enclosed with DOUBLE quotes. In this way,
              // the filename can include a single quote, aka, apostrophy.
              sqlInsert = " INSERT INTO server_filelist (fileblob,origFileName,origFileExtension," +
                          " fileSize,postedBy,postDateTime) " +
                          " VALUES (?,\""+ sourceFileName + "\",\"" + fileExtension + "\"," +
                          mybytearray.length + ",'" + userName + "',CURRENT_TIMESTAMP)";
            }
            else // Otherwise, skip it
            {
                sqlInsert = " INSERT INTO server_filelist (fileblob,origFileName," +
                " fileSize,postedBy,postDateTime) " +
                " VALUES (?,\""+ sourceFileName + "\"," +
                mybytearray.length + ",'" + userName + "',CURRENT_TIMESTAMP)";
            }

            if(_debug)
            {
              System.out.println("DEBUG WorkerThread " + this._workerThreadId + " Update filelist table statement: <" + sqlInsert + ">");
            }
            DbConnectionHelper dbHelper = new DbConnectionHelper();
            dbConnection = dbHelper.getConnection();
            // Using a prepared statement to make it a bit more efficient and to write the blob
            pstmt = dbConnection.prepareStatement(sqlInsert);
            // Add the byte array as a blob as the first reference
            pstmt.setObject(1, mybytearray);
            // Execute the statement and get the return value to make sure all went well
            int resultCount = pstmt.executeUpdate();

            if(_debug)
            {
            	System.out.println("DEBUG WorkerThread " + this._workerThreadId + " Result of attempted filelist table update: " + resultCount);
            }

            // Close the db connection.
            dbConnection.close();

            final Date backupTime = new Date();
            System.out.println(backupTime + ": WorkerThread " + _workerThreadId + " backed up file " + sourceFileName + " from " + this._clientConnection.getClientAddress());


        } catch (Exception e) {
            System.out.println(this._workerThreadId + " failed to insert file " + sourceFileName + " into the database.");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Restores the file given the passed in file ID
     * @param action
     * @return
     */
    public boolean restoreFile(Instruction action) {

    	int _fileID = action.getFileID();
    	action.reset();


    	// TODO: This statement could eventually get other meta data and send it along with SERVER_RESPONSE_RESTORE_ACCEPTED
    	// That said, the client could no doubt instead use the info it already has locally
    	String sqlQuery = "SELECT fileblob,origfilename,filesize FROM server_filelist WHERE fileid = " + _fileID;

        if(_debug)
        {
          System.out.println("DEBUG WorkerThread " + this._workerThreadId + " select statement: <" + sqlQuery + ">");
        }

    	try {
            DbConnectionHelper dbHelper = new DbConnectionHelper();
            dbConnection = dbHelper.getConnection();
    	    Statement stmt = dbConnection.createStatement();
    	    ResultSet rs = stmt.executeQuery(sqlQuery);

    	    if (rs.next()) {
    	        // Get the BLOB from the result set
    //	        Blob blob = rs.getBlob("fileblob");
    //	        int blobLength = (int)blob.length();
    	    	int blobLength = rs.getInt("fileSize");
    	        byte [] mybytearray = rs.getBytes("fileBlob");

    	        if(_debug)
    	        {
    	          System.out.println("DEBUG WorkerThread " + this._workerThreadId + " got BLOB from database.");
    	        }

    	        OutputStream os = this._clientConnection.getOOS();
    			os.write(mybytearray, 0, blobLength);
//    			os.write(blob.getBytes(1, blobLength), 0, blobLength);
    			os.flush();

    	        if(_debug)
    	        {
    	          System.out.println("DEBUG WorkerThread " + this._workerThreadId + " wrote BLOB to socket.");
    	        }
                final Date restoreTime = new Date();
                System.out.println(restoreTime + ": WorkerThread " + _workerThreadId + " restored file " + rs.getString("origfilename") + " (file ID = " + _fileID + ") to " + this._clientConnection.getClientAddress());


    	    }

            // Close the db connection.
    	    dbConnection.close();

    	}
    	catch (Exception e)
    	{
            System.out.println(this._workerThreadId + " failed to extract file ID " + _fileID + " from the database.");
            e.printStackTrace();
    		return false;
    	}

    	return true;
    }


    /**
     * Writes the provided Instruction object to the client's object output stream. This is a generic function used by
     * the others to simplify the send process.
     *
     * @param action Instruction class containing object to send
     *
     */
    public synchronized void write(Instruction action) {
        if (_debug) {
            System.out.println("DEBUG WorkerThread " + this._workerThreadId + " writing verb = " + (String) action.getVerb());
        }

        try {
            // Send the response to the client
            _oos.reset(); // Important to always reset - otherwise, the server will reuse cached info from previous write
            _oos.writeObject(action);
            _oos.flush();

        } catch (SocketException se) {
            System.err.println("\nObjectOutputStream SocketException during Write() method for WorkerThread " + _workerThreadId + " - " + se.getMessage());
            se.printStackTrace();

            // close the connection
            _isRunning = false;
            this.closeConnection();
        } // end SocketException
        catch (Exception e) {
            System.err.println("\nObjectOutputStream non-fatal Exception during Write() method for WorkerThread " + _workerThreadId + " - " + e.getMessage());
            e.printStackTrace();
        } // end Exception
    } // end write

    public void close() throws ChannelConnectException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void write(byte[] data, int count) throws ChannelConnectException, ChannelWriteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	public InputStream getInputStream() throws ChannelConnectException,
			ChannelReadException {
		// TODO Auto-generated method stub
		return null;
	}
} // end WorkerThread class




