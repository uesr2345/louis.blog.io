package com.example.top_louis_blog_meven.utils;

import com.mysql.jdbc.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @ClassName Java WEB
 * @Author Louis
 * @Date 2021/12/23 16:04
 * @Version IDEA 2021.1.2
 * @text DataBase Utils
 **/

public class BaseDao {

      /**
       * The driver class required to connect to the database
       */
      private static String DATABASE_CONNECTION_DRIVER;

      /**
       * The URL required to connect to the database
       */
      private static String DATABASE_CONNECTION_URL;

      /**
       * Root required to connect to the database
       */
      private static String DATABASE_CONNECTION_USERNAME;

      /**
       * The password required to connect to the database
       */
      private static String DATABASE_CONNECTION_PASSWORD;

      /**
       * Hungry man model
       * The singleton mode is implemented by using static internal classes, and the returned object is the same every time
       */
      static class BaseDaoHelper {
            // Final static instance object
            static final BaseDao CONNECTION_OBJECT = new BaseDao();
      }

      /**
       * Create a parameterless construction method
       */
      private BaseDao(){
            // Replace the properties configuration file with an InputStream stream file
            InputStream databasePropStream = BaseDao.class.getClassLoader().getResourceAsStream("database.properties");
            // Create properties instance object
            Properties properties = new Properties();
            try {
                  // The properties instance object calls the load method to load the stream file
                  properties.load(databasePropStream);
                  // Get the value and assign the value to the class property by reading the key of properties
                  DATABASE_CONNECTION_DRIVER = properties.getProperty("DATABASE_CONNECTION_DRIVER");
                  DATABASE_CONNECTION_URL = properties.getProperty("DATABASE_CONNECTION_URL");
                  DATABASE_CONNECTION_USERNAME = properties.getProperty("DATABASE_CONNECTION_USERNAME");
                  DATABASE_CONNECTION_PASSWORD = properties.getProperty("DATABASE_CONNECTION_PASSWORD");
            } catch (IOException e) {
                  e.printStackTrace();
            }
      }

      /**
       * Method for opening connection channel of program control database
       * @return Connection Object
       * @throws SQLException This declaration will cause an exception to be thrown
       */
      public Connection getConnection() throws SQLException {
            try {
                  // Load the driver class connecting to the database through the forname method of class
                  Class.forName(DATABASE_CONNECTION_DRIVER);
            } catch (ClassNotFoundException e) {
                  e.printStackTrace();
            }
            // Call the getconnection method in the drivermanager class, pass the database URL, database account and database password to open up the database connection channel
            // Return database connection object
            return (Connection) DriverManager.getConnection(DATABASE_CONNECTION_URL, DATABASE_CONNECTION_USERNAME, DATABASE_CONNECTION_PASSWORD);
      }

      /**
       * Close channel object connection
       * @throws SQLException This declaration will cause an exception to be thrown
       */
      public synchronized StringBuffer close(java.sql.Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) throws SQLException {
            StringBuffer stringBuffer = new StringBuffer();
            if (resultSet != null) {
                  // Judge whether the resultset is empty. If it is not empty, the object will be closed
                  resultSet.close();
                  stringBuffer.append("\t[INFO] ResultSet成功关闭！！！");
            }
            if (preparedStatement != null) {
                  // Judge whether the Preparedstatement is empty. If it is not empty, the object will be closed
                  preparedStatement.close();
                  stringBuffer.append("\t[INFO] PreparedStatement成功关闭！！！");
            }
            if (connection != null) {
                  // Judge whether the connection is empty. If it is not empty, the object will be closed
                  connection.close();
                  stringBuffer.append("\t[INFO] Connection关闭成功！！！");
            }
            return stringBuffer;
      }

      /**
       * The database performs a query operation and returns a result object
       * @return ResultSet Interface
       * @throws SQLException This declaration will cause an exception to be thrown
       */
      public ResultSet executeQuery(java.sql.Connection connection, PreparedStatement preparedStatement, String sql, Object[] parmas) throws SQLException {
            // Call the getconnection method to get the connection object
            connection = getConnection();
            // Judge whether the connection object is empty
            if (connection != null) {
                  // Get SQL statement
                  preparedStatement = connection.prepareStatement(sql);
                  if (parmas != null) {
                        // Loop traversal sets the parameters above the SQL statement
                        for (int i = 0; i < parmas.length; i++) {
                              // Set the required parameters on SQL accordingly. The first number should be 1 instead of 0
                              preparedStatement.setObject(i + 1, parmas[i]);
                        }
                  }
                  // When the program is here, it means that the value has been successfully set in the database statement. The next step is to execute the SQL statement
                  return preparedStatement.executeQuery();
            }else {
                  return null;
            }
      }

      /**
       * The database executes the add, delete and modify operation method, and the returned object is of type int
       * @return Int Type
       * @throws SQLException This declaration will cause an exception to be thrown
       */
      public int executeUpdate(java.sql.Connection connection,PreparedStatement preparedStatement, String sql, Object[] parmas) throws SQLException {
            // Call the getconnection method to get the connection object
            connection = getConnection();
            // Judge whether the connection object is empty
            if (connection != null) {
                  // Get SQL statement
                  preparedStatement = connection.prepareStatement(sql);
                  if (parmas != null) {
                        // Loop traversal sets the parameters above the SQL statement
                        for (int i = 0; i < parmas.length; i++) {
                              // Set the required parameters on SQL accordingly. The first number should be 1 instead of 0
                              preparedStatement.setObject(i + 1, parmas[i]);
                        }
                  }
                  // When the program is here, it means that the value has been successfully set in the database statement. The next step is to execute the SQL statement
                  return preparedStatement.executeUpdate();
            } else {
                  return 3;
            }
      }

      /**
       * A batch method for adding data to the database, which is used to perform the insertion operation of multi row data
       * @param sql Database operation statement
       * @throws SQLException This declaration will cause an exception to be thrown
       */
      public synchronized void addBatch(String sql, int maxColNum) throws SQLException {
            // Get connection to database
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < maxColNum; i++) {
                  preparedStatement.addBatch();
            }
      }

      /**
       * Method for clearing batch data from database
       * @throws SQLException This declaration will cause an exception to be thrown
       */
      public synchronized void clearBatch() throws SQLException {
            // Get connection to database
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("");
            preparedStatement.clearBatch();
      }
}
