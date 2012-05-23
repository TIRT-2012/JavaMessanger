package DAOs;

import java.lang.reflect.Method;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.persistence.EntityManager;

/**
 * Klasa reprezentujaca pojedynczy watek bazodanowy. Służy do asynchronicznego 
 * wykonywania zaputań. Korzysta z mechanizmów refleksi w celu zwiększenia 
 * uniwersalności.
 */
public class DBThread extends Thread {

    final static int MAX_TRY = 5;
    private Object result;
    private Object object;
    private String function;
    private Class[] parametersClass;
    private Object[] parameters;
    private Lock resultLock;
    private Condition resultAvailable;
    private final static String UNAVAILABLE = "UNAVAILABLE";

    /**
     * 
     * @param object
     * @param function
     * @param parametersClass
     * @param parameters
     * 
     * Obiekt Object reprezentuje klasę na której bedzie wykonana funkcja o 
     * nazwie function przyjmująca parametry o klasach z tablicy parameterClass,
     * tablica parameters jest tablica instancji tych obiektów.
     */
    public DBThread(Object object, String function, Class[] parametersClass, Object[] parameters) {
        this.object = object;
        this.function = function;
        this.parametersClass = parametersClass;
        this.parameters = parameters;
        result = UNAVAILABLE;
        resultLock = new ReentrantLock();
        resultAvailable = resultLock.newCondition();
    }
    
    @Override
    public void run() {
        boolean repeat = false;
        int errorCount = 0;
        resultLock.lock();
        try {
            do {
                try {
                    if (object instanceof EntityManager) {
                        ((EntityManager) object).getTransaction().begin();
                    }
                    Method method = object.getClass().getMethod(function, parametersClass);
                    result = method.invoke(object, parameters);
                    if (object instanceof EntityManager) {
                        ((EntityManager) object).getTransaction().commit();
                    }
                    repeat = false;
                } catch (Exception e) {
                    if (object instanceof EntityManager && ((EntityManager) object).getTransaction().isActive()) {
                        ((EntityManager) object).getTransaction().rollback();
                    }
                    repeat = true;
                    errorCount++;
                }
            } while (repeat && errorCount < MAX_TRY);
            resultAvailable.signalAll();
        } finally {
            resultLock.unlock();
        }
    }

    /**
     * 
     * @return
     * Funkcja zwraca wynik wykonania funkcji wykonanej na encjach lub null w 
     * przypadku funkcji dodającej, usuwającej lub zmieniającej rekordy.
     */
    public Object getResult() {
        resultLock.lock();
        try {
            while (result != null && result.equals(UNAVAILABLE)) {
                resultAvailable.await();
            }
        } catch (InterruptedException e) {
            System.out.println("Exception thrown: " + e.toString());
        } finally {
            resultLock.unlock();
        }
        return result;
    }
}
