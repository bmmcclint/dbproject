/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbproject;

import java.util.Arrays;


/**
 *
 * @author bmcclint
 */
public class dbQueriesTest {
  
    public static void main(String[] args) {
    dbQueries test = new dbQueries();
    String[] list = test.getQueryList();
    System.out.println(Arrays.toString(list));
  }
}
