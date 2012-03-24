/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic.ContactsListController;

import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
/**
 *
 * @author Piotr
 */
public class ContactsLocationListModel implements ListModel {
  private List<Entities.Contacts> collection;
  
  public ContactsLocationListModel(List<Entities.Contacts> collection) {
    this.collection = collection;
  }

  public Object getElementAt(int index) {
    return(collection.get(index));
  }

  public int getSize() {
    return(collection.size());
  }

  public void addListDataListener(ListDataListener l) {}

  public void removeListDataListener(ListDataListener l) {}
}
