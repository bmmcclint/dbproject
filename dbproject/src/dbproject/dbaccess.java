package dbproject;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bmcclint
 */
public class dbaccess {

  private final String dblocation;
  final String oraThinProtocol = "jdbc:oracle:thin";
  
  public dbaccess (String sID) {
    this.dblocation = "@localhost:1521:" + sID;
  }
  
  public dbaccess (String host, String port, String sID) {
    this.dblocation = "@" + host + ":" + port + ":" + sID;
  }
  
  public dbaccess() {
    this.dblocation = "@windowsplex.mynetgear.com:1521:xe";
  }
  
  public Connection getDBConnection (String username, String password) 
          throws SQLException {
    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
    String url = oraThinProtocol + ":" + dblocation;
    Connection conn = DriverManager.getConnection(url, username, password);
    return conn;
  }
}
