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
public class SerialCryptogram implements Serializable{
    private byte[] array;
    
    public SerialCryptogram(byte[] array){
        this.array = array;
    }
    
    public byte[] getBytesArray(){
        return this.array;
    }
}
