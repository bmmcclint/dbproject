/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbgui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 *
 * @author Brandon McClinton
 */
public class DatePanel extends JPanel{
    private JLabel fromLable;
    private JTextField day1;
    private JTextField year1;
    protected JComboBox month;
    
    private String label;
    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new DatePanel("To: "));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    public DatePanel(String lable) {
        super();
        this.label = label;
        initGUI();
    }

    private void initGUI() {
        try {
            {
                this.fromLable = new JLabel();
                this.add(this.fromLable);
                this.fromLable.setText(label);
                this.fromLable.setPreferredSize(new Dimension(41,26));
            }
            {
                this.day1 = new JTextField();
                this.add(this.day1);
                this.day1.setText("01");
                this.setPreferredSize(new Dimension(32, 23));
            }
            {
                this.year1 = new JTextField();
                this.add(this.year1);
                this.year1.setText("2006");
                this.year1.setPreferredSize(new Dimension(51, 23));
            }
            {
                ComboBoxModel monthModel = new DefaultComboBoxModel(
                    new String[] {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", 
                        "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"});
                this.month = new JComboBox();
                this.add(this.month);
                this.month.setModel(monthModel);
            }
            FlowLayout layout = new FlowLayout();
            this.setPreferredSize(new Dimension(254, 37));
            this.setLayout(layout);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    
    public String getDoM() {
        return this.day1.getText();
    }
    
    public String getMonth() {
        return this.month.getSelectedItem().toString();
    }
    
    public String getYear() {
        return this.year1.getText();
    }
    
    public String getDateString() {
        String day = getDoM();
        if (day.length() == 1) {
            day = "0" + day;
        }
        return day + "-" + getMonth() + "-" + getYear();
    }
}
