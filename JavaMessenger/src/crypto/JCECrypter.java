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
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Klasa odpowiedzialna za szyfrowanie danych w programie. 
 * Udostępnia ona możliwość szyfrowania danych zapisanych w strumieniach
 * przy pomocy szyfrów symetrycznych i asymetrycznego Algorytmu RSA.
 */
public class JCECrypter {
   private final int rsaKeySize = 1024;
   private int symetricKeySize = 256;
   private static final String decryptedFile = "out.mp3";
   private static final String encryptedFile = "encrypted.enc";
   private static final String testFile = "test.mp3";
   
   private static final String testString = "Hello World!";

   private String cryptographyAlgorithm = "AES";
   
   /*
    * Tryby pracy szyfratora - działają większością szyfrów blokowych
    * <pusty> - ECB (domyślny)
    * /CBC/PKCS5Padding - CBC
    * /OFB/PKCS5Padding - OFB
    * /CFB/PKCS5Padding - CFB
    * /PCBC/PKCS5Padding - PCBC
    */
   //private static final String cryptographyMode = "";
   private String cryptographyMode = "";
   
   public static void main(String[] args) {
       JCECrypter cryptor = new JCECrypter("AES", 256);
        try {
            //c.testFileCrypting();
            cryptor.testStringCrypting();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
   }
   
   public JCECrypter(String algorithm, int symetricKeySize){
       Security.addProvider(new BouncyCastleProvider());
       
       this.cryptographyAlgorithm = algorithm;
       this.symetricKeySize = symetricKeySize;
   }
   
   public JCECrypter(){
       Security.addProvider(new BouncyCastleProvider());
   }

   public void setCryptographyAlgorithm(String cryptographyAlgorithm) {
        this.cryptographyAlgorithm = cryptographyAlgorithm;
   }

   public void setSymetricKeySize(int symetricKeySize) {
        this.symetricKeySize = symetricKeySize;
   }
   
   public void testStringCrypting() throws Exception{
       System.out.println("Szyfrowana wiadomość: " + testString);
       
       KeyPair RSAKey = this.generateRSAKey();
       SerialPublicKey publicKey = new SerialPublicKey(RSAKey.getPublic());
       
       ByteArrayInputStream in = new ByteArrayInputStream(testString.getBytes());
       ByteArrayOutputStream out = new ByteArrayOutputStream();
       this.crypt(RSAKey.getPublic(), in, out);
       
       System.out.println("Zaszyfrowana wiadomość: " + out.toString());
       
       ByteArrayInputStream in2 = new ByteArrayInputStream(out.toByteArray());
       ByteArrayOutputStream out2 = new ByteArrayOutputStream(); 
       this.decrypt(RSAKey.getPrivate(), in2, out2);
       
       System.out.println("Zdeszyfrowana wiadomość: " + out2.toString());
       
   }
   
   public void testFileCrypting() throws Exception{
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
   
    public SerialCryptedMessage cryptOut(PublicKey publicKey, ByteArrayInputStream in, ByteArrayOutputStream out) throws Exception{
        crypt(publicKey, in, out);
        return new SerialCryptedMessage(out.toByteArray());
    }
    
   public void crypt(PublicKey publicKey, InputStream in, OutputStream out) throws Exception{
       SecretKey symetricKey = this.generateSymetricKey();
       
       Cipher cipher = Cipher.getInstance("RSA"); 
       cipher.init(Cipher.WRAP_MODE, publicKey); 
       byte[] wrappedKey = cipher.wrap(symetricKey); 
       
       out.write(wrappedKey.length); 
       out.write(wrappedKey); 
       
       cipher = Cipher.getInstance(cryptographyAlgorithm + cryptographyMode); 
       cipher.init(Cipher.ENCRYPT_MODE, symetricKey);
       
       if(!cryptographyMode.equals("")){
            byte[] iv = cipher.getIV();

            out.write(iv.length);
            out.write(iv);
       }
       
       work(in, out, cipher); 
       in.close(); 
       out.close(); 
   }
   
   public void decrypt(PrivateKey privateKey, InputStream in, OutputStream out) throws Exception{
     int length = in.read(); 
     byte[] wrappedKey = new byte[length]; 
     in.read(wrappedKey, 0, length); 
       
     Cipher cipher = Cipher.getInstance("RSA"); 
     cipher.init(Cipher.UNWRAP_MODE, privateKey); 
     Key key = cipher.unwrap(wrappedKey, cryptographyAlgorithm, Cipher.SECRET_KEY);
     
     IvParameterSpec ips = null;
     
     if(!cryptographyMode.equals("")){
        int length2 = in.read();
        byte[] my_iv = new byte[length2]; 
        in.read(my_iv, 0, length2);
        ips = new IvParameterSpec(my_iv);
     }
     
     cipher = Cipher.getInstance(cryptographyAlgorithm + cryptographyMode); 
     
     if(!cryptographyMode.equals(""))
        cipher.init(Cipher.DECRYPT_MODE, key, ips); 
     else
        cipher.init(Cipher.DECRYPT_MODE, key);    
       
     work(in, out, cipher); 
     in.close(); 
     out.close();
   }
   
   public SecretKey generateSymetricKey() throws Exception{
     KeyGenerator keygen = KeyGenerator.getInstance(cryptographyAlgorithm); 
     SecureRandom random = new SecureRandom(); 
     keygen.init(symetricKeySize, random); 
     SecretKey key = keygen.generateKey();
     
     return key;
   }
   
   public KeyPair generateRSAKey() throws Exception{
      KeyPairGenerator pairgen = KeyPairGenerator.getInstance("RSA"); 
      SecureRandom random = new SecureRandom(); 
      pairgen.initialize(rsaKeySize, random); 
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
    
   private static void work(InputStream in, OutputStream out, Cipher cipher) throws IOException, GeneralSecurityException 
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
