/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbproject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
/**
 *
 * @author bmcclint
 */
public class TableInfo {
  private final Connection conn;
  private final String host = "";
  private final String port = "1521";
  private final String sID = "";
  
  
  /**
   * 
   * @param host
   * @param port
   * @param sID
   * @param username
   *          This is the username associated with the database
   * @param password
   *          This is the required password to access the database
   * @throws SQLException 
   */
  public TableInfo (String host, String port, String sID, String username, 
          String password) throws SQLException {
    conn = new dbaccess(host, port, sID).getDBConnection(username, 
            password);
  }
  
  public TableInfo(String username, String password) throws SQLException {
      this.conn = new dbaccess(host, port, sID).getDBConnection(username, 
              password);
  }
  
  public TableInfo(Connection conn) throws SQLException {
    this.conn = conn;
  }
  
  /**
   * 
   * Executes a specific SQL statement with no predefined parameters
   * @param str
   * @return
   * @throws SQLException 
   */
  public int runUpdate (String str) throws SQLException {
    Statement stmt = conn.createStatement();
    return stmt.executeUpdate(str);
  }
  
  /**
   * 
   * Executes a specific SQL statement with predefined parameters
   * @param str
   * @return
   * @throws SQLException 
   */
  public ResultSet runSQLQuery (String str) throws SQLException {
    Statement stmt = conn.createStatement();
    return stmt.executeQuery(str);
  }
  
  /**
   * 
   * List the names of tables the user has created
   * @return
   * @throws SQLException 
   */
  public String[] listTableName() throws SQLException {
    String str = "select table_name from user_tables";
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(str);
    ArrayList al = new ArrayList();
    
    while (rs.next()) {
      al.add(rs.getString("table_name"));
    }
    
    String[] tn = new String[1];
    tn = (String[]) al.toArray(tn);
    return tn;
  }
  
  /**
   * 
   * Returns the ResultSet of the of a QL statement run on a table
   * @param tn
   * @return
   * @throws SQLException 
   */
  public ResultSet getTable(String tn) throws SQLException {
    String str = "select * from " + tn;
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(str);
    return rs;
  }
  
  /**
   * 
   * Returns the column titles of a specified table
   * @param rs
   * @return
   * @throws SQLException 
   */
  public String[] getTitles (ResultSet rs) throws SQLException {
    ResultSetMetaData rsmd = rs.getMetaData();
    int col = rsmd.getColumnCount();
    String[] title = new String[col];
    for (int i = 0; i < col; i++) {
      title[i] = rsmd.getColumnLabel(col);
    }
    return title;
  }
  
  /**
   * 
   * Returns the values stored in the columns of a table
   * @param tableName
   * @param colName
   * @return
   * @throws SQLException 
   */
  public String[] getColumn (String tableName, String colName) throws 
          SQLException {
    ResultSet rs = runSQLQuery("select distinct " + colName + " from " + 
            tableName + " order by " + colName);
    ArrayList list = new ArrayList();
    while (rs.next()) {
      Object obj = rs.getObject(colName);
      String str = "null";
      if (obj != null) 
        str = obj.toString();
      list.add(str);
    }
    String[] result = new String[1];
    result = (String[])list.toArray(result);
    return result;
  }
  
  /**
   * 
   * Returns the columns of a table in an order specified by the ResulSet
   * @param tn
   * @param values
   * @param order
   * @return
   * @throws SQLException 
   */
  public ResultSet getOrderedColumn (String tn, String values, String order) 
          throws SQLException {
    String str = "select " + values + " from " + tn + " order by " + order;
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(str);
    return rs;
  } 
  
  /**
   * 
   * Returns a date column in it's SHORT format
   * @param tableName
   * @param colName
   * @return
   * @throws SQLException 
   */
  public String[] getDateColumnInShort (String tableName, String colName) 
          throws SQLException {
    String[] result = getDateColumn(tableName, colName, DateFormat.SHORT);
    return result;
  }
  
  /**
   * 
   * Returns a date column in a chosen format
   * @param tableName
   * @param colName
   * @param form
   * @return
   * @throws SQLException 
   */
  public String[] getDateColumn (String tableName, String colName, int form) 
          throws SQLException {
    ResultSet rs = runSQLQuery("select distinct " + colName + " from " + 
            tableName + " order by " + colName);
    DateFormat df = DateFormat.getDateInstance(form);
    ArrayList dList = new ArrayList();
    while (rs.next()) {
      String dat = df.format(rs.getDate(colName));
      dList.add(dat);
    }
    
    String[] result = new String[1];
    result = (String[])dList.toArray(result);
    return result;
  }
  
  /**
   * 
   * Returns the columns of a table as a vector
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Vector getTitleAsVector (ResultSet rs) throws SQLException {
    ResultSetMetaData rsmd = rs.getMetaData();
    int col = rsmd.getColumnCount();
    Vector title = new Vector();
    for (int i = 0; i < col; i++) {
      title.add(rsmd.getColumnLabel(i+1));
    }
    return title;
  }
  
  /**
   * 
   * Returns the types of columns in the table
   * @param rs
   * @return
   * @throws SQLException 
   */
  public int[] getColumnTypes (ResultSet rs) throws SQLException {
    ResultSetMetaData rsmd = rs.getMetaData();
    int col = rsmd.getColumnCount();
    int[] types = new int[col];
    for (int i = 0; i < col; i++) {
      types[i] = rsmd.getColumnType(i+1);
    }
    return types;
  }
  
  /**
   * 
   * Return the requested ResultSet from the table
   * @param tn
   * @param colName
   * @param colType
   * @param val
   * @return
   * @throws SQLException
   * @throws ParseException 
   */
  public ResultSet getSelectedResultSet (String tn, String colName, int colType, 
          String val) throws SQLException, ParseException {
    String whereClause;
    if (val.equals("null")) {
      whereClause = colName + " is null";
    }
    else {
      if (colType == Types.DATE) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        Date dat = sdf.parse(val);
        sdf.applyPattern("dd-MMM-yyyy");
        val = "'" + val + "'";
      }
      whereClause = colName + " = " + val;
    }
    
    String sqlStr = "select * from " + tn + " where " + whereClause;
    ResultSet rs = runSQLQuery(sqlStr);
    return rs;
  }
  
  /**
   * 
   * Converts th output of a ResultSet into a 2D array of strings
   * @param rs
   * @return
   * @throws SQLException 
   */
  public String[][] resultSet2DArray (ResultSet rs) throws SQLException {
    ResultSetMetaData rsmd = rs.getMetaData();
    int col = rsmd.getColumnCount();
    ArrayList al = new ArrayList(1);
    String[] row;
    while (rs.next()) {
      row = new String[col];
      for (int i = 0; i < col; i++) {
        Object obj = rs.getObject(i+1);
        if (obj != null) 
          row[i] = obj.toString();
        else 
          row[i] = "";
      }
      al.add(row);
    }
    String[][] tab = new String[al.size()][col];
      for (int i = 0; i < al.size(); i++) {
        tab[i] = (String[])al.get(i);
      }
      return tab;
  }
  
  /**
   * 
   * Converts the output of a ResultSet into an array of strings
   * @param rs
   * @return
   * @throws SQLException 
   */
  public String[] resultSet2Array (ResultSet rs) throws SQLException {
    ArrayList al = new ArrayList(1);
    String row;
    while (rs.next()) {
      row = new String("");
      Object obj = rs.getObject(1);
      if (obj != null) {
        row = obj.toString();
        al.add(row);
      }
    }
    String[] tab = new String[al.size()];
    for (int i = 0; i < al.size(); i++) {
      tab[i] = (String) (al.get(i));
    }
    return tab;
  }
  
  /**
   * 
   * Converts a ResultSet into a Vector object
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Vector resultSet2Vector (ResultSet rs) throws SQLException {
    ResultSetMetaData rsmd = rs.getMetaData();
    int col = rsmd.getColumnCount();
    Vector vec = new Vector();
    Vector row = null;
    while (rs.next()) {
      row = new Vector();
      for (int i = 0; i < col; i++) {
        row.add(rs.getObject(i+1));
      }
      vec.add(row);
    }
    return vec;
  }
  
  /**
   * 
   * Returns an array of strings of the column names
   * @param rs
   * @return
   * @throws SQLException 
   */
  public static String[] getColumn (ResultSet rs) throws SQLException {
    ResultSetMetaData rsmd = rs.getMetaData();
    int colNum = rsmd.getColumnCount();
    String[] col = new String[colNum];
    for (int i = 0; i < colNum; i++) {
      col[i] = rsmd.getColumnName(i+1);
    }
    return col;
  }
  
  /**
   *
   * Returns a string array of the column types
   * @param rs
   * @return
   * @throws SQLException
   */
  public static int[] getColumnType (ResultSet rs) throws SQLException {
    ResultSetMetaData rsmd = rs.getMetaData();
    int colNum = rsmd.getColumnCount();
    int[] colType = new int[colNum];
    for (int i = 0; i < colNum; i++) {
      colType[i] = rsmd.getColumnType(i+1);
    }
    return colType;
  }
  
  /**
   * 
   * Tests the functionality of the above methods
   * @param args
   * @throws SQLException 
   */
//  public static void main (String[] args) throws SQLException {
//    dbaccess tc = new dbaccess();
//    Connection conn = tc.getDBConnection(args[0], args[1]);
//    TableInfo ti = new TableInfo(conn);
//    System.out.println("\n Your queries are listed below.\n");
//    String[] queries = ti.listTableName();
//    for (String query : queries) {
//      System.out.println(query);
//    }
//    if (queries.length == 0) 
//      System.out.println("You don't have any table(s)");
//    else {
//      for (String name : queries) {
//        System.out.println(name);
//      }
//       System.out.println("\n List your first table's contents. \n");
//       ResultSet rs = ti.getTable(queries[1]);
//       String[] titles = ti.getTitles(rs);
//      for (String title : titles) {
//        System.out.println(title);
//      }
//       System.out.println("\n");
//       String[][] table = ti.resultSet2DArray(rs);
//      for (String[] table1 : table) {
//        for (int j = 0; j < table[0].length; j++) {
//          System.out.println(table1[j]);
//        }
//        System.out.println();
//      }
//    }
////    String[] dateS = ti.getDateColumnInShort("employment", "start_date");
////    String[] dateL = ti.getDateColumn("employment", "start_date", DateFormat.MEDIUM);
//  }
}
