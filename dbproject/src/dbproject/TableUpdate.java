/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbproject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Vector;
/**
 *
 * @author bmcclint
 */
public class TableUpdate {
  private Connection conn;
  private TableInfo ti;
  
  public TableUpdate (String host, String port, String sID, String username, 
          String password) throws SQLException {
    conn = new dbaccess(host, port, sID).getDBConnection(username, password);
    ti = new TableInfo(conn);
  }
  
  public TableUpdate (Connection conn) throws SQLException {
    this.conn = conn;
    ti = new TableInfo(conn);
  }
  
  public TableInfo getTableInfo() {
    return ti;
  }
  
  public int insertRow (Vector dataRow, String tableName, ResultSet rs) throws 
          SQLException {
    int[] cTypes = ti.getColumnTypes(rs);
    String stmt = "insert into " + tableName + " values(";
    for (int i = 0; i < cTypes.length; i++) {
      if (i > 0)
        stmt = stmt + ", ";
      if (cTypes[i] == java.sql.Types.NUMERIC)
        stmt = stmt + dataRow.elementAt(i);
      else 
        stmt = stmt + "'" + dataRow.elementAt(i) + "'";
    }
    stmt = stmt + ")";
    Statement sqlStmt = conn.createStatement();
    return sqlStmt.executeUpdate(stmt);
  }
  
  public int copyTable (String fromTable, String toTable) throws SQLException {
    ResultSet rs = ti.runSQLQuery("select * from " + fromTable);
    int numRow = insertResultSet(rs, toTable);
    return numRow;
  }
  
  public int insertResultSet (ResultSet rs, String toTable) throws SQLException {
    String pstmtStr = TableUpdate.formInsertPStmt(toTable, rs);
    PreparedStatement pstmt = conn.prepareStatement(pstmtStr);
    int[] colType = TableInfo.getColumnType(rs);
    int counter = 0;
    while (rs.next()) {
      for (int i = 0; i < colType.length; i++) {
        switch (colType[i]) {
          case Types.BOOLEAN:
            pstmt.setBoolean(i+1, rs.getBoolean(i+1));
            break;
          case Types.DATE:
          case Types.TIMESTAMP:
            java.sql.Date date = rs.getDate(i+1);
            if (date != null)
              pstmt.setDate(i+1, date);
            break;
          case Types.REAL:
          case Types.DOUBLE:
            pstmt.setDouble(i+1, rs.getDouble(i+1));
            break;
          case Types.FLOAT:
            pstmt.setFloat(i+1, rs.getFloat(i+1));
            break;
          case Types.INTEGER:
          case Types.NUMERIC:
            int num = rs.getInt(i+1);
            pstmt.setInt(i+1, rs.getInt(i+1));
            break;
          case Types.BIGINT:
            pstmt.setLong(i+1, rs.getLong(i+1));
            break;
          case Types.VARCHAR:
          case Types.LONGNVARCHAR:
          case Types.CHAR:
            String str = rs.getString(i+1);
            pstmt.setString(i+1, rs.getString(i+1));
        }
      }
      counter = counter + pstmt.executeUpdate();
    }
    return counter;
  }
  
  public static String formInsertPStmt (String tableName, ResultSet rs) 
          throws SQLException {
    StringBuffer sb = new StringBuffer("insert into " + tableName + " (");
    String[] cols = TableInfo.getColumn(rs);
    for (int i = 0; i < cols.length; i++) {
      sb.append(cols[i]);
      if (i < cols.length - 1)
        sb.append(",");
    }
    sb.append(") values (");
    for (int i = 0; i < cols.length; i++) {
      sb.append("?");
      if (i < cols.length - 1)
        sb.append(",");
    }
    sb.append(")");
    return sb.toString();
  }
  
  public static void main (String[] args) throws SQLException {
    if (args.length < 3) {
      System.out.println("usage: java TableUpdate table-name new-table "
              + "db_username db-password");
      System.exit(1);
    }
    
    dbaccess tc = new dbaccess("test");
    Connection conn = tc.getDBConnection(args[0], args[1]);
    TableUpdate tu = new TableUpdate(conn);
    int rowIns = tu.copyTable(args[2], args[3]);
    System.out.println(rowIns + " rows were inserted.");
  }
}
