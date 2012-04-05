/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author SysOp
 */
public class DBThreadManager {
    
    private HashMap threads;
    
    public DBThreadManager(){
        threads=new HashMap();
    }
    
    public long add(DBThread t){
        t.start();
        long id=getNextId();
        threads.put(id, t);
        return id;
    }
    
    private long getNextId(){
        long id=0;
        for(Iterator it=threads.keySet().iterator(); it.hasNext(); id++){
            long current=Long.valueOf(it.next().toString());
            if(current!=id){
                break;
            }
        }
        return id;
    }
    
    public Object getResult(long id){
        DBThread t=(DBThread) threads.get(id);
        return t.getResult();
    }
    
}
