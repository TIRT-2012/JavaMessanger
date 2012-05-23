/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.io.Serializable;

/**
 * Klasa wykorzystywana do przesyłania zaszyfrowanych wiadomości jako serializowany obiekt.
 */
public class SerialCryptedMessage implements Serializable{
    private byte[] message;
    
    /**
     * 
     * @param message przesyłany szyfrogram w postaci tablicy bajtów
     */
    public SerialCryptedMessage(byte[] message){
        this.message = message;
    }
    
    /**
     * 
     * @return szyfrogram zapisany w obiekcie w postaci tablicy bajtów 
     */
    public byte[] getByteArray(){
        return this.message;
    }
}
