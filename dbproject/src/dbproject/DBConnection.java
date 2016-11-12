/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbproject;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
/**
 *
 * @author bmcclint
 */
public class DBConnection {
  private final String dbLocation;
  final String oraThinProtocol = "jdbc:oracle:thin";
  
  public DBConnection (String sID) {
    this.dbLocation = "@windowsplex.mynetgear.com:1521:" + sID;
  }
  
  public DBConnection (String host, String port, String sID) {
    this.dbLocation = "@" + host + ":" + port + ":" + sID;
  }
  
  /**
   *
   * @param username
   * @param password
   * @return
   * @throws java.sql.SQLException
   */
  public Connection getDBConnection (String username, String password) 
          throws SQLException {
    DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
    String url = oraThinProtocol + ":" + dbLocation;
//    System.out.println("[TableInfo:] url = " + url);
    Connection conn = DriverManager.getConnection(url, username, password);
    return conn;
  }
}
