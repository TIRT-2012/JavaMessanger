/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author PWr
 */
public class Hasher {
    public static String HASH_MD5 = "MD5";
    public static String HASH_SHA256 = "SHA-256";
    public static String HASH_SHA512 = "SHA-512";
    
    public static String generateHash(String inputText, String algorithm){
 
        try {
            MessageDigest md;
            md = MessageDigest.getInstance(algorithm);
            md.update(inputText.getBytes());

            byte byteData[] = md.digest();

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < byteData.length; i++)
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

            return sb.toString();
        
        } catch (NoSuchAlgorithmException ex) {
            return "Cannot generate checksum";
        }
    }
    
    
}
