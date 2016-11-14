/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbproject;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author bmcclint
 */
public class dbaccessTest {

  private static String username = "brandon";
  private static String password = "Obeytdojtyl7";
  
  public static void main (String[] args) throws SQLException {
    dbaccess dbaccesstest = new dbaccess();
    Connection connTest = dbaccesstest.getDBConnection(username, password);
    System.out.println("test passed hopefully");
  }
}
