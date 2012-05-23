/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic.ContactsListController;

import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Klasa definiująca wygląd tabeli kontaktów zalogowanego użytkownika
 */

public class MyTableCellRenderer extends DefaultTableCellRenderer {

        public MyTableCellRenderer()
        {
            setOpaque(true);
            
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
        {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (isSelected) {
                cell.setBackground(Color.white);
            } else {
                if (row % 2 == 0) {
                    Color cr2 = new Color(238, 210, 238);
                    cell.setBackground(cr2);
                    
              } else {
                    cell.setBackground(Color.lightGray);
                }
            }
            return cell;
        }
    }
