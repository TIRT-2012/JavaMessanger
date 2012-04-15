/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.io.Serializable;
import java.security.PublicKey;

/**
 *
 * @author Trebronus
 */
public class SerialPublicKey implements Serializable{
    private PublicKey key;
    
    public SerialPublicKey(PublicKey key){
        this.key = key;
    }
    
    public PublicKey getPublicKey(){
        return key;
    }
}
