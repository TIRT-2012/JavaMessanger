/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic.ContactsListController;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.*;

/**
 * Klasa definiująca wygląd listy kontaktów zalogowanego użytkownika
 */
public class MyJlistCellRenderer extends JPanel implements ListCellRenderer {

    JLabel left, right;

    public MyJlistCellRenderer() {
        setLayout(new GridLayout(1, 2));
        left = new JLabel();
        right = new JLabel();
        left.setOpaque(true);
        right.setOpaque(true);
        add(left);
        add(right);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String leftData = ((String[]) value)[0];
        String rightData = ((String[]) value)[1];
        left.setText(leftData);
        right.setText(rightData);
        if (isSelected) {
            left.setBackground(list.getSelectionBackground());
            left.setForeground(list.getSelectionForeground());
            right.setBackground(list.getSelectionBackground());
            right.setForeground(list.getSelectionForeground());
            left.setBackground(Color.white);
            right.setBackground(Color.white);
        } else {
            left.setBackground(list.getBackground());
            left.setForeground(list.getForeground());
            right.setBackground(list.getBackground());
            right.setForeground(list.getForeground());
            if (index % 2 == 0) {
                Color cr2 = new Color(238, 210, 238);
                left.setBackground(cr2);
                right.setBackground(cr2);
            } else {
                left.setBackground(Color.lightGray);
                right.setBackground(Color.lightGray);
            }
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        return this;
    }
}
