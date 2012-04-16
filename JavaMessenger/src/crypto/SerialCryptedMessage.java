/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.io.Serializable;

/**
 *
 * @author Trebronus
 */
public class SerialCryptedMessage implements Serializable{
    private byte[] message;
    
    public SerialCryptedMessage(byte[] message){
        this.message = message;
    }
    
    public byte[] getByteArray(){
        return this.message;
    }
    
}
