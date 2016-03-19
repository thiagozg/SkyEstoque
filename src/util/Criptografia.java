package util;

import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException;  
import sun.misc.BASE64Encoder;

/*
 * Caso nao encontre o BASE64Encoder:
 * Va ate Project\Properties\Java Build Path\Libraries
 * Selecione o JRE System Library
 * Clique em Edit e selecione Workspace default.
 */
  
public class Criptografia {  
public String criptografarSenha(String senha) throws NoSuchAlgorithmException {  
    
  
    try {  
               MessageDigest digest = MessageDigest.getInstance("MD5");  
               digest.update(senha.getBytes());  
               BASE64Encoder encoder = new BASE64Encoder ();  
               return encoder.encode (digest.digest ());  
          } catch (NoSuchAlgorithmException ns) {  
               ns.printStackTrace ();  
               return senha;  
          }  
}  
    } 
