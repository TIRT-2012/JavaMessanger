/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.io.*;
import java.security.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * @author Trebronus
 */
public class JCECrypter {
   private static final int KEYSIZE = 512;
   private static final String decryptedFile = "out.txt";
   private static final String encryptedFile = "encrypted.enc";
   private static final String testFile = "plik.txt";
   
   private static final String cryptographyAlgorith = "AES";
   
   public static void main(String[] args) {
       JCECrypter c = new JCECrypter();
        try {
            c.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
   }
   
   public void run() throws Exception{
       //Wygeneruj klucze
       KeyPair RSAKey = this.generateRSAKey();
       
       //Wczytuje dane do strumienia i je szyfruje
       InputStream in = new FileInputStream(testFile);
       DataOutputStream out = new DataOutputStream(new FileOutputStream(encryptedFile));
       this.crypt(RSAKey.getPublic(), in, out);
       
       //Deszyfruje zaszyfrowane dane i je zapiuje
       DataInputStream in2 = new DataInputStream(new FileInputStream(encryptedFile));
       OutputStream out2 = new FileOutputStream(decryptedFile);
       this.decrypt(RSAKey.getPrivate(), in2, out2);
   }
   
   public void crypt(PublicKey publicKey, InputStream in, DataOutputStream out) throws Exception{
       SecretKey symetricKey = this.generateSymetricKey();
       
       Cipher cipher = Cipher.getInstance("RSA"); 
       cipher.init(Cipher.WRAP_MODE, publicKey); 
       byte[] wrappedKey = cipher.wrap(symetricKey); 

       out.writeInt(wrappedKey.length); 
       out.write(wrappedKey); 
       
       cipher = Cipher.getInstance(cryptographyAlgorith); 
       cipher.init(Cipher.ENCRYPT_MODE, symetricKey); 
       work(in, out, cipher); 
       in.close(); 
       out.close(); 
   }
   
   public void decrypt(PrivateKey privateKey, DataInputStream in, OutputStream out) throws Exception{
     int length = in.readInt(); 
     byte[] wrappedKey = new byte[length]; 
     in.read(wrappedKey, 0, length); 
       
     Cipher cipher = Cipher.getInstance("RSA"); 
     cipher.init(Cipher.UNWRAP_MODE, privateKey); 
     Key key = cipher.unwrap(wrappedKey, cryptographyAlgorith, Cipher.SECRET_KEY);
       
     cipher = Cipher.getInstance(cryptographyAlgorith); 
     cipher.init(Cipher.DECRYPT_MODE, key); 
       
     work(in, out, cipher); 
     in.close(); 
     out.close();
   }
   
   public SecretKey generateSymetricKey() throws Exception{
     KeyGenerator keygen = KeyGenerator.getInstance(cryptographyAlgorith); 
     SecureRandom random = new SecureRandom(); 
     keygen.init(random); 
     SecretKey key = keygen.generateKey();
     
     return key;
   }
   
   public KeyPair generateRSAKey() throws Exception{
      KeyPairGenerator pairgen = KeyPairGenerator.getInstance("RSA"); 
      SecureRandom random = new SecureRandom(); 
      pairgen.initialize(KEYSIZE, random); 
      KeyPair keyPair = pairgen.generateKeyPair();
      
      return keyPair;
   }
   
   public void saveRSAKeysToFiles(KeyPair keyPair, String keyName) throws Exception{
      ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(keyName + "_public.key"));
      out.writeObject(keyPair.getPublic()); 
      out.close();  
      
      out = new ObjectOutputStream(new FileOutputStream(keyName + "_private.key")); 
      out.writeObject(keyPair.getPrivate()); 
      out.close(); 
   }
    
   public static void work(InputStream in, OutputStream out, Cipher cipher) throws IOException, GeneralSecurityException 
   { 
      int blockSize = cipher.getBlockSize(); 
      int outputSize = cipher.getOutputSize(blockSize); 
      byte[] inBytes = new byte[blockSize]; 
      byte[] outBytes = new byte[outputSize]; 
 
      int inLength = 0;
      boolean more = true; 
      while (more) 
      { 
         inLength = in.read(inBytes); 
         if (inLength == blockSize) 
         { 
            int outLength = cipher.update(inBytes, 0, blockSize, outBytes); 
            out.write(outBytes, 0, outLength); 
         } 
         else more = false;          
      } 
      if (inLength > 0) 
         outBytes = cipher.doFinal(inBytes, 0, inLength); 
      else 
         outBytes = cipher.doFinal(); 
      out.write(outBytes); 
   } 
}
