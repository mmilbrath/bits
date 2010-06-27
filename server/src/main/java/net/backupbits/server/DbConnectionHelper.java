package net.backupbits.server;


/**
 * @author Chuck Slate
 * @author January, 2006
 * 
 */



import java.sql.*;
import java.util.Properties;
//import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;




public class DbConnectionHelper
{

  String debugValue;
  boolean debug = false;
  Connection conn = null;
  Properties props;
 // final String propertiesFileName = "/ted.properties";

  public Connection getConnection()
  {
 
    try
    {
      // Reading properties file
/*      props = new Properties();
      props.load(new FileInputStream(propertiesFileName));

      dbhostname = (String)props.get("database.hostname");
      dbname = (String)props.get("database.name");
      dbusername = (String)props.get("database.username");
      dbpassword = (String)props.get("database.password");
      debugValue = (String)props.get("debug");
*/
    	debugValue = "true";

    }
    catch (Exception e)
    {
      System.out.println("Error reading properties file: " + e.getMessage());
      System.out.println("Without this file, we have no credentials to access the database!");
    }

    try
    {

      // Open the db connection using SQLite
      Class.forName("org.sqlite.JDBC");
      conn = DriverManager.getConnection("jdbc:sqlite:bits_server.db");


    } // end try

    catch (Exception e)
    {
        System.out.println("DbConnectionHelper.java - Error connecting to database: " + e.getMessage());
    } // end catch

    return conn;

  }  // getConnection()

}  // end class

