package com.artifex.mupdfdemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

public class LoadFile {

    String fileName;

    Context context;

    private final String characterEncoding = "UTF-8";
    private final String cipherTransformation = "AES/CBC/NoPadding";
    private final String cipherTransformation2 = "AES/CBC/PKCS5Padding";
    private final String aesEncryptionAlgorithm = "AES";
    private final String iv = "1234567890123456";
    private final String key = "AVRPTShlokapp";

    String sourceFilePath;
    String originalFilePath, folderPath;

    public LoadFile(Context context, String fileName, String srcFilePath) {
        this.fileName = fileName;
        this.context = context;

        createDirectory();

        sourceFilePath = srcFilePath;

        originalFilePath = folderPath + "/" + fileName + ".pdf";
    }


    public String getFilePath() {
        if (decrypt()) {
            return originalFilePath;
        }

        return null;
    }

    public String getSourceFilePath() {
        return sourceFilePath;
    }

    public final boolean decrypt() {
        FileInputStream fisSrc = null;
        FileOutputStream fosFile = null;
        //CipherOutputStream cosDecryptedFile = null;
        CipherInputStream cisDecryptedFile = null;

        boolean isEncrypted = false;

        try {
            File fileToEncrypt = new File(sourceFilePath);

            File fileToSave = new File(originalFilePath);

            if (!fileToSave.exists())
                fileToSave.createNewFile();

            System.out.println("Decrypting...");

            fisSrc = new FileInputStream(fileToEncrypt);

            fosFile = new FileOutputStream(fileToSave);

            cisDecryptedFile = new CipherInputStream(fisSrc, decrypt(""));

            /*byte[] inputBytes = new byte[(int) fileToEncrypt.length()];
            fisSrc.read(inputBytes);

            byte[] outputBytes = decrypt("").doFinal(inputBytes);

            fosEncFile.write(outputBytes);*/

            byte[] buf = new byte[1024];

            int read;
            while ((read = cisDecryptedFile.read(buf)) != -1) {
                fosFile.write(buf, 0, read);
                //fosFile.flush();
            }

            Log.v("Notification", "File decrypt successfully");
            isEncrypted = true;
        } catch (Exception e) {
            System.out.println("Error decryption:: " + e.toString());

        } finally {
            if (fosFile != null) {
                try {
                    fosFile.close();
                    Log.v("Notification", "fosFile CLosed...");
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }

            if (cisDecryptedFile != null) {
                try {
                    cisDecryptedFile.close();
                    Log.v("Notification", "cisDecryptedFile CLosed...");
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }

            if (fisSrc != null) {
                try {
                    fisSrc.close();
                    Log.v("Notification", "fisSrc CLosed...");
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        }
        return isEncrypted;
    }

    public final void encrypt() {
        FileInputStream fisSrc = null;
        FileOutputStream fosFile = null;
        CipherInputStream cisEncFile = null;
        //CipherOutputStream cosEncFile = null;

        try {
            Log.v("Notification", "Origial Path : " + originalFilePath);
            File fileToEncrypt = new File(originalFilePath);

            Log.v("Notification", "Source Path : " + sourceFilePath);
            File fileToSave = new File(sourceFilePath);

            if (!fileToSave.exists())
                fileToSave.createNewFile();

            /*Cipher encipher = Cipher.getInstance("AES");

            SecretKeySpec skey = generateKey();//initiate key

            encipher.init(Cipher.ENCRYPT_MODE, skey);*/

            fisSrc = new FileInputStream(fileToEncrypt);

            fosFile = new FileOutputStream(fileToSave);

            cisEncFile = new CipherInputStream(fisSrc, encrypt(""));

            /*byte[] inputBytes = new byte[(int) fileToEncrypt.length()];
            fisSrc.read(inputBytes);

            byte[] outputBytes = encrypt("").doFinal(inputBytes);

            fosEncFile.write(outputBytes);*/

            byte[] buf = new byte[1024];
            int read;
            while ((read = cisEncFile.read(buf)) != -1) {
                fosFile.write(buf, 0, read);
                //fosFile.flush();
            }

            Log.v("Notification", "File encrypt successfully");
        } catch (Exception e) {
            System.out.println("Error Encryption:: " + e.toString());
            e.printStackTrace();
        } finally {
            if (fosFile != null) {
                try {
                    fosFile.close();
                    Log.v("Notification", "fosFile CLosed...");
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }

            if (cisEncFile != null) {
                try {
                    cisEncFile.close();
                    Log.v("Notification", "cisEncFile CLosed...");
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }

            if (fisSrc != null) {
                try {
                    fisSrc.close();
                    Log.v("Notification", "fisSrc CLosed...");
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

    private Cipher decrypt(byte[] key, byte[] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(cipherTransformation);
        SecretKeySpec secretKeySpecy = new SecretKeySpec(key, aesEncryptionAlgorithm);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);
        //cipherText = cipher.doFinal(cipherText);
        return cipher;
    }

    public Cipher decrypt(String encryptedText) throws KeyException, GeneralSecurityException, GeneralSecurityException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
        //byte[] cipheredBytes = Base64.decode(encryptedText);
        byte[] keyBytes = getKeyBytes(key);
        byte[] ivBytes = getKeyBytes(iv);

        return decrypt(keyBytes, ivBytes);
    }

    private byte[] getKeyBytes(String key) throws UnsupportedEncodingException {
        byte[] keyBytes = new byte[16];
        byte[] parameterKeyBytes = key.getBytes(characterEncoding);
        //System.out.println("Key : " + new String(parameterKeyBytes));
        System.arraycopy(parameterKeyBytes, 0, keyBytes, 0, Math.min(parameterKeyBytes.length, keyBytes.length));
        //System.out.println("Key : " + new String(keyBytes));
        return keyBytes;
    }

    private void createDirectory() {
        File fileDirectory = new File(context.getFilesDir().getPath() + "/files");
        if (!fileDirectory.exists()) {
            fileDirectory.mkdirs();
            Log.v("Notification", "Directory Creted");
        }
        folderPath = fileDirectory.getPath();
    }

    private Cipher encrypt(byte[] plainText, byte[] key, byte[] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(cipherTransformation2);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, aesEncryptionAlgorithm);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        //plainText = cipher.doFinal(plainText);
        return cipher;
    }

    public Cipher encrypt(String plainText) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] plainTextbytes = plainText.getBytes(characterEncoding);
        byte[] keyBytes = getKeyBytes(key);
        byte[] ivBytes = getKeyBytes(iv);
        return encrypt(plainTextbytes, keyBytes, ivBytes);
    }

}
