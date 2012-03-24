/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic.ContactsListController;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Piotr
 */
public class MyCellRenderer extends DefaultListCellRenderer {

        public MyCellRenderer()
        {
            setOpaque(true);
            
        }
        public Component getListCellRendererComponent(JList list,Object value, // value to display
                int index, // cell index
                boolean isSelected, // is the cell selected
                boolean cellHasFocus) // the list and the cell have the focus
        {
            JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (isSelected) {
                label.setBackground(Color.white);
            } else {
                if (index % 2 == 0) {
                    Color cr2 = new Color(238, 210, 238);
                    label.setBackground(cr2);
                    
                    System.out.println(value.toString());
                    label.setText(value.toString());
                } else {
                    label.setBackground(Color.lightGray);
                }
            }
            return label;
        }
    }
