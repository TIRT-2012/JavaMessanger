package DAOs;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Klasa odpowiedzialna za zarządzanie wątkami reprezentującymi zadania 
 * bazodanowe. 
 */
public class DBThreadManager {
    
    private HashMap threads;
    
    /**
     * Konstruktor zarządcy wątków- tworzy mapę aktualnie obsługiwanych wątków.
     */
    public DBThreadManager(){
        threads=new HashMap();
    }
    
    /**
     * 
     * @param t
     * @return
     * Dodaje wątek do zarządcy i zwraca jego id w celu późniejszego odwołania 
     * się do jego wyniku.
     */
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
    
    /**
     * 
     * @param id
     * @return
     * Zwraca wynik wykonania wątku o podanym id, jeżeli wątek nie zakończył 
     * pracy to zawiesza wątek pytający do czasu otrzymania notyfikacji.
     */
    public Object getResult(long id){
        DBThread t=(DBThread) threads.get(id);
        return t.getResult();
    }
    
}
