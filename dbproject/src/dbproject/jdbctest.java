/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbproject;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class jdbctest {

	private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String DB_CONNECTION = "jdbc:oracle:thin:@windowsplex.mynetgear.com:1521:xe";
	private static final String DB_USER = "brandon";
	private static final String DB_PASSWORD = "Obeytdojtyl7";
    
    public static void main(String[] args) throws SQLException {
      String query = "select email from person wehre person_code = ?";
      
      try {
        Connection conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, "1008087");
        ResultSet rs = stmt.executeQuery();
         
       while (rs.next()) {
          String email = rs.getString(5);
        }
      }
      catch (SQLException sqle) {
        sqle.printStackTrace();
      }
    }
}
