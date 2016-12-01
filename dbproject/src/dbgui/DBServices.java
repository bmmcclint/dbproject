///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package dbgui;
//
//import java.awt.FlowLayout;
//import java.awt.GridLayout;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.sql.Connection;
//import javax.swing.DefaultComboBoxModel;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.WindowConstants;
//
///**
// *
// * @author Brandon McClinton
// */
//public class DBServices extends JFrame{
//    private JFrame frame;
//    private JLabel headLabel;
//    private JLabel statusLabel;
//    private JPanel panel;
//    private JTable table;
//    private JComboBox qcombo;
//    private JScrollPane qpane;
//    private JButton show;
//    private JButton qinfo;
//    
//    Connection conn = null;
//    
//    public DBServices(Connection conn) {
//        this.conn = conn;
//        initGUI();
//    } 
//    
//    private void initGUI() {
//        try {
//            this.frame = new JFrame("Services");
//            this.frame.setSize(600, 300);
//            this.frame.setLayout(new GridLayout(3, 1));
//            this.frame.addWindowListener(new WindowAdapter() {
//                public void windowClose(WindowEvent we) {
//                    
//                }
//            });
//            
//            {
//                this.headLabel = new JLabel();
//                this.headLabel.setText("");
//                this.headLabel.setAlignmentX(JLabel.CENTER);
//                this.frame.add(this.headLabel);
//            }
//            {
//                this.statusLabel = new JLabel();
//                this.statusLabel.setText("");
//                this.statusLabel.setAlignmentX(JLabel.CENTER);
//                this.frame.add(this.statusLabel);
//            }
//            {
//                this.panel = new JPanel();
//                this.panel.setLayout(new FlowLayout());
//                this.frame.add(this.panel);
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        
//        this.frame.setLocationRelativeTo(null);
//        this.frame.setVisible(true);
//        this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//    }
//}

import javax.swing.table.*;
import java.sql.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DBServices extends JFrame {

    static Scanner input = new Scanner(System.in);
    static int choice = 0;
    static int choice2 = 0;
    static JFrame mainFrame;
    static JLabel headerLabel;
    static JLabel statusLabel;
    static JPanel controlPanel;
    static JTable jt;
    Connection conn = null;

    public DBServices(Connection conn) {
        this.conn = conn;
        prepareGUI();
        DBServices.display(conn);
    }

    private void prepareGUI() {

        mainFrame = new JFrame("Services");
        mainFrame.setSize(600, 300);
        mainFrame.setLayout(new GridLayout(3, 1));
        //mainFrame.setLocationRelativeTo(null);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {

            }
        });
        headerLabel = new JLabel("", JLabel.CENTER);
        statusLabel = new JLabel("", JLabel.CENTER);

        statusLabel.setSize(340, 100);

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    static void display(Connection conn) {

        headerLabel.setText("Service Menu");

        final DefaultComboBoxModel queryList = new DefaultComboBoxModel();

        queryList.addElement("Hire Employee (Company)");
        queryList.addElement("Job Finder (Person)");
        queryList.addElement("Qualified Person Finder (Company)");
        queryList.addElement("Business Sector Opportunities");

        final JComboBox queryCombo = new JComboBox(queryList);
        queryCombo.setSelectedIndex(0);

        JScrollPane queryListScrollPane = new JScrollPane(queryCombo);

        JButton showButton = new JButton("Select Service");
        JButton queryInfo = new JButton("Service Description");

        showButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String choice;
                String choice2;
                String choice3;

                // A company hires a new person
                if (queryCombo.getSelectedIndex() == 0) {

                    try {
                        Statement st = conn.createStatement();
                        ResultSet rs = st.executeQuery("(select job_code from "
                                + "job_profile natural join job) minus (select "
                                + "job_code from job_profile natural join job "
                                + "natural join employment)");
                        String[] comp_code = {""};

                        ArrayList<String> rowValues = new ArrayList<>();
                        while (rs.next()) {
                            rowValues.add(rs.getString(1));
                        }
                        comp_code = rowValues.toArray(comp_code);
                        choice = (String) JOptionPane.showInputDialog(null, 
                                "Select the available job", null, 
                                JOptionPane.QUESTION_MESSAGE, null, 
                                comp_code, comp_code[0]);

                        st = conn.createStatement();
                        rs = st.executeQuery("(select person_code from works "
                                + "natural join job natural join job_profile "
                                + "natural join person where end_date != "
                                + "'present')");
                        String[] person_code = {""};

                        rowValues = new ArrayList<>();
                        while (rs.next()) {
                            rowValues.add(rs.getString(1));
                        }
                        person_code = rowValues.toArray(person_code);
                        choice2 = (String) JOptionPane.showInputDialog(null, 
                                "Select unemployed person ID", null, 
                                JOptionPane.QUESTION_MESSAGE, null, person_code, 
                                person_code[0]);

                        String date = (String) JOptionPane.showInputDialog(null, 
                                "Enter start date Ex. 12/15/2016", null, 
                                JOptionPane.QUESTION_MESSAGE);

                        if (!choice.equals("")) {
                            companyHires(conn, choice, choice2, date);
                        }
                    } catch (Exception ec) {
                        ec.printStackTrace();
                    }
                }
                // Helps a person find a job
                if (queryCombo.getSelectedIndex() == 1) {
                    try {
                        Statement st = conn.createStatement();
                        ResultSet rs = st.executeQuery("select person_code from person");
                        String[] person_code = {""};

                        ArrayList<String> rowValues = new ArrayList<>();
                        while (rs.next()) {
                            rowValues.add(rs.getString(1));
                        }
                        person_code = rowValues.toArray(person_code);
                        choice = (String) JOptionPane.showInputDialog(null, "To find jobs you are qualified for, please select your person ID", null, JOptionPane.QUESTION_MESSAGE, null, person_code, person_code[0]);

                        if (!choice.equals("")) {
                            jobFinder(conn, choice);
                        }
                    } catch (Exception ec) {
                        ec.printStackTrace();
                    }
                }
                // Query 3 
                if (queryCombo.getSelectedIndex() == 2) {
                    //query3(conn);              
                }
                // Query 4
                if (queryCombo.getSelectedIndex() == 3) {
                    sectorJobs(conn);

                }
            }
        });

        queryInfo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String choice;
                String choice2;

                // Query 1
                if (queryCombo.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "A service that allows a company to hire a new employee.", "Description", JOptionPane.PLAIN_MESSAGE);
                }
                // Query 2
                if (queryCombo.getSelectedIndex() == 1) {
                    JOptionPane.showMessageDialog(null, "A service that assists a person in finding a job.", "Description", JOptionPane.PLAIN_MESSAGE);
                }
                // Query 3 
                if (queryCombo.getSelectedIndex() == 2) {
                    JOptionPane.showMessageDialog(null, "A service that helps a company find qualified workers.", "Description", JOptionPane.PLAIN_MESSAGE);
                }
                // Query 4
                if (queryCombo.getSelectedIndex() == 3) {
                    JOptionPane.showMessageDialog(null, "A service that displays all available opportunities in each business sector.", "Description", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        controlPanel.add(queryInfo);
        controlPanel.add(queryListScrollPane);
        controlPanel.add(showButton);
        mainFrame.setVisible(true);
    }

    // Company Hires
    public static void companyHires(Connection conn, String choice, String choice2, String date) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        String dateto = "present";
        String query = "insert into works (person_code, job_code, date_from, date_to) values (? , ? , ?, ?)";

        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, choice2);
            ps.setString(2, choice);
            ps.setString(3, date);
            ps.setString(4, dateto);
            rs = ps.executeQuery();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Job Finder
    public static void jobFinder(Connection conn, String choice) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "(select job_code, title from job natural join job_profile where jp_code in (select distinct P.jp_code from job_profile P where not exists ((select ks_code from required_skill S where S.jp_code = P.jp_code) minus (select ks_code from person_skill where person_code = ?))))minus (select job_code, title from job_profile natural join job natural join works)";

        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, choice);
            rs = ps.executeQuery();
            JTable table = new JTable(buildTableModel(rs));
            JOptionPane.showMessageDialog(null, new JScrollPane(table));
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get qualified workers for company
    public static void query3(Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "with salary (comp_name, total_salary) as (select comp_name, sum(pay_rate) from company natural join job natural join works where pay_type = 'salary' and date_to = 'present' group by comp_name), wage (comp_name, total_wage) as (select comp_name, sum(pay_rate) * 1920 from company natural join job natural join works where pay_type = 'wage' and date_to = 'present' group by comp_name), the_total as((select *  from salary) union (select * from wage)) select comp_name, sum(total_salary) as total_cost from  the_total group by comp_name order by total_cost desc";

        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            JTable table = new JTable(buildTableModel(rs));
            JOptionPane.showMessageDialog(null, new JScrollPane(table));
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Job opportunities in business sector 
    public static void sectorJobs(Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "with available_jobs (comp_code, comp_name, primary_sector, job_code, jp_code, title) as ((select comp_code, comp_name, primary_sector, job_code, jp_code, title from job natural join job_profile natural join company) minus (select comp_code, comp_name, primary_sector, job_code, jp_code, title from job natural join job_profile natural join works natural join company)), jobs_among_sectors (comp_code, comp_name, job_code, title, primary_sector) as (select comp_code, comp_name, job_code, title, primary_sector from available_jobs group by comp_code, comp_name, job_code, title, primary_sector) select primary_sector, comp_code, comp_name, job_code, title from jobs_among_sectors order by primary_sector desc";

        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            JTable table = new JTable(buildTableModel(rs));
            JOptionPane.showMessageDialog(null, new JScrollPane(table));
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }
}
