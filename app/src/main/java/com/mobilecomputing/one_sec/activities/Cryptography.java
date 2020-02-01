package com.mobilecomputing.one_sec.activities;

/*
 * created by Avinash
 */

import com.google.api.client.util.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.*;

public class Cryptography {
    private static Cryptography single_instance = null;

    private KeyPairGenerator keyGen;
    private KeyPair keyPair;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Cipher cipher;
    private byte[] bufferCipher;
    private String algorithm = "RSA";
    private int keySize = 512;

    public Cryptography() {
        //Initialise keypair values
        try {
            Security.addProvider(new BouncyCastleProvider());
            keyGen = KeyPairGenerator.getInstance(algorithm);
            keyGen.initialize(keySize);
            keyPair = keyGen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
            cipher = Cipher.getInstance(algorithm);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //singleton class
    public static Cryptography getInstance()
    {
        if (single_instance == null)
            single_instance = new Cryptography();

        return single_instance;
    }

    //encrypt the message
    public String encrypt(String message){
        try{
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            bufferCipher = cipher.doFinal(message.getBytes());
            return Base64.encodeBase64String(bufferCipher);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //decrypt the message
    public String decrypt(String encryptedMessage){
        try{
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] encryptedBufferCipher = cipher.doFinal(Base64.decodeBase64(encryptedMessage.getBytes()));
            return new String(encryptedBufferCipher, 0, encryptedBufferCipher.length);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}

