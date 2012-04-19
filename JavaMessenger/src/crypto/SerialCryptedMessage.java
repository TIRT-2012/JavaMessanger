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
    private byte[] file;
    
    public SerialCryptedMessage(byte[] message){
        this.message = message;
    }
    
    public byte[] getByteArray(){
        return this.message;
    }
    
//    public void setFileByteArray(byte[] file)
//    {
//        this.file = file;
//    }
//    
//    public byte[] getFileByteArray()
//    {
//        return this.file;
//    }
}
