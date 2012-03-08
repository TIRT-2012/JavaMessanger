/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DBSupport;

import java.util.Iterator;
import java.util.List;
import static java.lang.System.*;
/**
 *
 * @author SysOp
 */
public class TryInsert {
    
    
    
    public static void main(String[] a){
        UsersDAO uDAO= new UsersDAO();
        Users tmp;//=uDAO.findById(2L);
//        out.println(tmp.getUserName());
        List<Users> list=uDAO.findAll();
        for(Iterator it=list.iterator(); it.hasNext();){
            tmp=(Users) it.next();
            out.println(tmp.getUserName());
        }
    }
}
