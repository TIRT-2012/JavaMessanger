/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.io.Serializable;
import java.security.PublicKey;

/**
 * Klasa służąca do przesyłania używanego algorytmu szyfrowania i klucza publicznego pozostałym klientom.
 */
public class SerialPublicKey implements Serializable{
    private PublicKey key;
    private String algorithm;
    private int symetricKeySize;
    
    public SerialPublicKey(PublicKey key){
        this.key = key;
    }
    
    public PublicKey getPublicKey(){
        return key;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getSymetricKeySize() {
        return symetricKeySize;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setSymetricKeySize(int symetricKeySize) {
        this.symetricKeySize = symetricKeySize;
    }
}
