/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbgui;

import dbproject.TableInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author bmcclint
 */
public class TableSelect extends javax.swing.JFrame {
  private TableInfo ti;
  private JLabel tnLabel;
  private JComboBox tnJCombo;
  private JTable table;
  private JLabel sortCommentLabel;
  private JCheckBox orderCheckBox;
  private JComboBox valueCombo;
  private JLabel valueLabel;
  private JComboBox columnNameCombo;
  private JLabel columnName;
  private JScrollPane JScrollPane1;
  private ResultSet rs;
  private int[] columnTypes;
  private int chosenColumnTypes;
  private TableModel emptyTableModel = new DefaultTableModel(new String[][] {{" ", " "}}, new String[] {""});
  private ComboBoxModel emptyComboBoxModel = new DefaultComboBoxModel(new String[] { "", ""});;
  
  public TableSelect(TableInfo ti) {
    super();
    this.ti = ti;
    initGUI();
  }
  
  private void initGUI() {
    try {
    {
      this.tnLabel = new JLabel();
      this.getContentPane().add(this.tnLabel);
      this.tnLabel.setText("Table Name:");
      this.tnLabel.setBounds(14, 14, 91, 28);
    }
    {
      ComboBoxModel tnJomboModel = new DefaultComboBoxModel(ti.listTableName());
      this.tnJCombo = new JComboBox();
      this.getContentPane().add(this.tnJCombo);
      this.tnJCombo.setModel(tnJomboModel);
      this.tnJCombo.setBounds(126, 14, 266, 28);
      this.tnJCombo.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
          try {
            tnJComboActionPerformed(evt);
          } catch (SQLException ex) {
            Logger.getLogger(TableSelect.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      });
    }
    {
      TableModel tableModel = emptyTableModel;
      this.table = new JTable();
      this.table.setModel(tableModel);
      this.table.setBounds(21, 56, 826, 357);
    }
    {
      this.JScrollPane1 = new JScrollPane(this.table);
      this.getContentPane().add(this.JScrollPane1);
      this.JScrollPane1.setBounds(7, 98, 861, 329);
    }
    {
      this.columnName = new JLabel();
      this.getContentPane().add(this.columnName);
      this.columnName.setText("Column Name:");
      this.columnName.setBounds(14, 56, 112, 28);
    }
    {
      ComboBoxModel columnNameComboModel = emptyComboBoxModel;
      this.columnNameCombo = new JComboBox();
      this.getContentPane().add(this.columnNameCombo);
      this.columnNameCombo.setModel(columnNameComboModel);
      this.columnNameCombo.setBounds(126, 56, 266, 28);
      this.columnNameCombo.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
          try {
            columnNameComboActionPerformed(evt);
          } catch (SQLException ex) {
            Logger.getLogger(TableSelect.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      });
    }
    {
      this.valueLabel = new JLabel();
      this.getContentPane().add(this.valueLabel);
      this.valueLabel.setText("Select a value:");
      this.valueLabel.setBounds(434, 14, 147, 28);
    }
    {
      ComboBoxModel valueComboBoxModel = emptyComboBoxModel;
      this.valueCombo = new JComboBox();
      this.getContentPane().add(this.valueCombo);
      this.valueCombo.setModel(valueComboBoxModel);
      this.valueCombo.setBounds(434, 56, 343, 28);
      this.valueCombo.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
          try {
            valueComboActionPerformed(evt);
          } catch (SQLException ex) {
            Logger.getLogger(TableSelect.class.getName()).log(Level.SEVERE, null, ex);
          } catch (ParseException ex) {
            Logger.getLogger(TableSelect.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      });
    }
    {
      this.orderCheckBox = new JCheckBox("sorted", null, true);
      this.getContentPane().add(this.orderCheckBox);
      this.orderCheckBox.setBounds(791, 56, 70, 28);
    }
    {
      this.sortCommentLabel = new JLabel();
      this.getContentPane().add(this.sortCommentLabel);
      this.sortCommentLabel.setText("unchecking \"sorted\" shows null values");
      this.sortCommentLabel.setBounds(623, 14, 245, 28);
    }
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this.getContentPane().setLayout(null);
    this.pack();
    this.setSize(883, 485);
  } catch (Exception e) {
    e.printStackTrace();
  }
}
  
  private void tnJComboActionPerformed(ActionEvent evt) throws SQLException{
    String chosenTable = (String) this.tnJCombo.getSelectedItem();
    String chosenColumn = (String) this.columnNameCombo.getSelectedItem();
    int choseIndex = this.columnNameCombo.getSelectedIndex();
    String[] columnValue = null;
    try {
        rs = ti.getTable(chosenTable);
        TableModel tableModel = new DefaultTableModel(ti.resultSet2Vector(rs), ti.getTitleAsVector(rs));
        table.setModel(tableModel);
        if (rs != null) {
          columnTypes = ti.getColumnTypes(rs);
          DefaultComboBoxModel comboModel = new DefaultComboBoxModel(ti.getTitles(rs));
          this.columnNameCombo.setModel(comboModel);
          this.valueCombo.setModel(emptyComboBoxModel);
        }
    } catch (Exception sqle) {
      sqle.printStackTrace();
    }
  }
  
  private void columnNameComboActionPerformed(ActionEvent evt) throws SQLException {
    String chosenTable = (String) this.tnJCombo.getSelectedItem();
    String chosenColumn = (String) this.columnNameCombo.getSelectedItem();
    int chosenIndex = this.columnNameCombo.getSelectedIndex();
    String[] columnValue = null;
    try {
      if (this.chosenColumnTypes == Types.DATE) {
        columnValue = ti.getDateColumnInShort(chosenTable, chosenColumn);
      }
      else if (this.orderCheckBox.isSelected()) {
        columnValue = ti.resultSet2Array(ti.getOrderedColumn(chosenTable, chosenColumn, chosenColumn));
      }
      else
        columnValue = ti.getColumn(chosenTable, chosenColumn);
    } catch (Exception sqle) {
      sqle.printStackTrace();
    }
  }
  
  private void valueComboActionPerformed(ActionEvent evt) throws SQLException, ParseException {
    String chosenTable= (String) this.tnJCombo.getSelectedItem();
    String chosenColumn = (String) this.columnNameCombo.getSelectedItem();
    try {
      String value = (String) this.valueCombo.getSelectedItem();
      boolean sorted = this.orderCheckBox.isSelected();
      rs = ti.getSelectedResultSet(chosenTable, chosenColumn, chosenColumnTypes, value);
      TableModel tableModel = new DefaultTableModel(ti.resultSet2Vector(rs), ti.getTitleAsVector(rs));
      table.setModel(tableModel);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
