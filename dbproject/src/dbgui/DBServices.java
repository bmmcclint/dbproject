/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbgui;

import dbproject.TableInfo;
import dbproject.TableUpdate;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 *
 * @author Brandon McClinton
 */
public class DBServices extends JFrame {

    private JComboBox select;
    private JTextArea dbsdesc;
    private JComboBox options;
    private JLabel selectlabel;
    private JLabel optionlabel;
    private JScrollPane rspane;
    private JTextArea dbsrs;
    private JComboBox valueList;
    private JLabel valueListLabel;

    private ResultSet rs;
    private TableInfo ti;
    private TableUpdate tu;
    private Connection conn;

    private String comp_code = null;
    private String person_code = null;
    private String job_code = null;
    private String jp_code;

    private final String[] optionList = {"Hire Employee", "Job Search",
        "Qualified Person Search", "Sector Opportunities"};

    private final String[] valList = {"A service that allows a company to hire "
        + "a new employee.", "A service that allows a person to search for a"
        + " job.", "A service the helps a company find qualified workers.", ""
        + "A service that lists all available opportunities in each business "
        + "sector"};

    public DBServices(Connection conn, TableUpdate tu) {
        super();
        this.tu = tu;
        this.ti = tu.getTableInfo();
        this.conn = conn;
        initGUI();
    }

    private void initGUI() {
        try {
            {
                this.selectlabel = new JLabel();
                this.getContentPane().add(this.selectlabel);
                this.selectlabel.setText("Select Database Service");
                this.selectlabel.setBounds(7, -5, 50, 20);
            }
            {
                this.optionlabel = new JLabel();
                this.getContentPane().add(this.optionlabel);
                this.optionlabel.setText("Service Description");
                this.optionlabel.setBounds(200, -5, 50, 20);
            }
            {
                this.valueListLabel = new JLabel();
                this.getContentPane().add(this.valueListLabel);
                this.valueListLabel.setText("Available Values");
                this.valueListLabel.setBounds(400, -5, 63, 20);
            }
            {
                this.dbsdesc = new JTextArea();
                this.getContentPane().add(this.dbsdesc);
                this.setBounds(7, 50, 125, 70);
                this.dbsdesc.setVisible(false);
            }
            {
                ComboBoxModel dbsModel = new DefaultComboBoxModel(this.optionList);
                this.select = new JComboBox();
                this.getContentPane().add(this.select);
                this.select.setModel(dbsModel);
                this.select.setBounds(55, -5, 200, 20);
                this.select.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        selectActionPerformed(evt);
                    }
                });
            }
            {
                this.rspane = new JScrollPane();
                this.getContentPane().add(this.rspane);
                this.rspane.setBounds(7, 120, 800, 180);
            }

            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.getContentPane().setLayout(null);
            this.pack();
            this.setSize(900, 550);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectActionPerformed(ActionEvent evt) {
        if (this.select.getSelectedIndex() == 0) {
            this.dbsdesc.setVisible(true);
            hireEmployee();
        } else if (this.select.getSelectedIndex() == 1) {
            this.dbsdesc.setVisible(true);
            jobSearch();
        } else if (this.select.getSelectedIndex() == 2) {
            this.dbsdesc.setVisible(true);
            personSearch();
        } else if (this.select.getSelectedIndex() == 3) {
            this.dbsdesc.setVisible(true);
            sectorOps();
        }
    }

    private void optionsActionPerformed(ActionEvent evt) {

    }

    private void hireEmployee() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = "insert into employment (person_code, job_code, "
                + "start_date end_date, status) values (? , ? , ?, ? , ?)";
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, input);
            pstmt.setString(2, input2);
            pstmt.setString(3, input3);
            pstmt.setString(4, input4);
            pstmt.setString(5, input5);
            rs = pstmt.executeQuery();
            rs.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private void jobSearch() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = "with person_skills as ( \n"
                + "select ks_code \n"
                + "from person_skill \n"
                + "where person_code = ? ) \n"
                + ""
                + "select jp_code, jp_title \n"
                + "from job_profile j \n"
                + "where not exists ( \n"
                + "   (select ks_code \n"
                + "   from jp_skill \n"
                + "   where j.jp_code = jp_code) \n"
                + "     minus \n"
                + "   (select ks_code \n"
                + "   from person_skills))";
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, input);
            rs = pstmt.executeQuery();
            rs.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private void personSearch() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = "with required_skills as ( \n"
                + "   select ks_code \n"
                + "   from jp_skill \n"
                + "   where jp_code = ?) \n"
                + ""
                + "select last_name, first_name, email, person_code \n"
                + "from person p \n"
                + "where not exists ( \n"
                + "   (select ks_code \n"
                + "   from required_skills) \n"
                + "     minus \n"
                + "   (select ks_code \n"
                + "   from person_skill \n"
                + "   where p.person_code = person_code))";
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, input6);
            rs = pstmt.executeQuery();
            rs.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private void sectorOps() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = "with available_jobs ( \n"
                + "  comp_code, comp_name, primary_sector, job_code, jp_code, jp_title) as ( \n"
                + "    (select comp_code, comp_name, primary_sector, job_code, jp_code, jp_title \n"
                + "    from job natural join job_profile natural join company) \n"
                + "      minus \n"
                + "    (select comp_code, comp_name, primary_sector, job_code, jp_code, jp_title \n"
                + "    from job natural join job_profile natural join employment natural join company)),  \n"
                + ""
                + "jobs_among_sectors (comp_code, comp_name, job_code, jp_title, primary_sector) as ( \n"
                + "  select comp_code, comp_name, job_code, jp_title, primary_sector  \n"
                + "  from available_jobs  \n"
                + "  group by comp_code, comp_name, job_code, jp_title, primary_sector)  \n"
                + ""
                + "select primary_sector, comp_code, comp_name, job_code, jp_title \n"
                + "from jobs_among_sectors  \n"
                + "order by primary_sector desc";
        try {
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            rs.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

}
