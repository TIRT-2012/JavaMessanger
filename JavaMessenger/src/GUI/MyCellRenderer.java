/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

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

        // This is the only method defined by ListCellRenderer.
        // We just reconfigure the JLabel each time we're called.
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
                label.setBackground(Color.green);
            } else {
                if (index % 2 == 0) {
                    Color cr = new Color(238, 210, 238);
                    label.setBackground(cr);
                } else {
                    label.setBackground(Color.lightGray);
                }
            }
            return label;
        }
    }
