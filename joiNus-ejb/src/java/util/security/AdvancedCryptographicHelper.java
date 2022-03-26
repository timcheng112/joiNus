package util.security;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;



public class AdvancedCryptographicHelper
{
    private static AdvancedCryptographicHelper advancedCryptographicHelper = null;
    
    private static final String DEFAULT_KEYSTORE_TYPE = "PKCS12";
    private static final String DEFAULT_DOMAIN_CONFIG_DIRECTORY = System.getProperty("user.dir");
    private static final String DEFAULT_KEYSTORE_NAME = "default.p12";
    private static final String DEFAULT_KEYSTORE_PATH = DEFAULT_DOMAIN_CONFIG_DIRECTORY + System.getProperty("file.separator") + DEFAULT_KEYSTORE_NAME;
    private static final String DEFAULT_KEY_ALIAS = "default";
    private static final char[] DEFAULT_KEYSTORE_PASSWORD = new char[]{'c', 'h', 'a', 'n', 'g', 'e', 'i', 't'};

    
    
    public AdvancedCryptographicHelper()
    {
    }
    
    
    
    public static AdvancedCryptographicHelper getInstance()
    {
        if (advancedCryptographicHelper == null)
        {
            advancedCryptographicHelper = new AdvancedCryptographicHelper();
        }

        return advancedCryptographicHelper;
    }
    
    
    
    public void exportDefaultX509CertificateToDefaultKeyStore(X509Certificate x509Certificate, PrivateKey privateKey)
    {
        System.out.println("********** AdvancedCryptographicHelper.exportDefaultX509CertificateToDefaultKeyStore: DEFAULT_KEYSTORE_PATH: " + DEFAULT_KEYSTORE_PATH);
        exportX509CertificateToKeyStore(x509Certificate, privateKey, DEFAULT_KEYSTORE_TYPE, DEFAULT_KEY_ALIAS, DEFAULT_KEYSTORE_PASSWORD, DEFAULT_KEYSTORE_PATH);
    }
    
    
    
    public void exportX509CertificateToKeyStore(X509Certificate x509Certificate, PrivateKey privateKey, String keyStoreType, String keyAlias, char[] keyStorePassword, String keyStorePath)
    {
        try
        {
            X509Certificate[] x509Certificates = new X509Certificate[1];
            x509Certificates[0] = x509Certificate;
            
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setKeyEntry(keyAlias, privateKey, keyStorePassword, x509Certificates);
            FileOutputStream fileOutputStream = new FileOutputStream(keyStorePath);
            keyStore.store(fileOutputStream, keyStorePassword);
            fileOutputStream.close();            
        }
        catch(KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    
    public KeyPair loadDefaultX509CertificateKeyPairFromDefaultKeyStore()
    {
        return loadX509CertificateKeyPairFromKeyStore(DEFAULT_KEYSTORE_TYPE, DEFAULT_KEY_ALIAS, DEFAULT_KEYSTORE_PASSWORD, DEFAULT_KEYSTORE_PATH);
    }
    
    
    
    public KeyPair loadX509CertificateKeyPairFromKeyStore(String keyStoreType, String keyAlias, char[] keyStorePassword, String keyStorePath)
    {
        try
        {
            FileInputStream keyStoreStream = new FileInputStream(keyStorePath);
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(keyStoreStream, keyStorePassword);
            X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(keyAlias);
            PublicKey publicKey = x509Certificate.getPublicKey();
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, keyStorePassword);
            
            return new KeyPair(publicKey, privateKey);
        }
        catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException ex)
        {
            ex.printStackTrace();
            
            return null;
        }
    }
    
    
    
    public X509Certificate loadDefaultX509CertificateFromDefaultKeyStore()
    {
        return loadX509CertificateFromKeyStore(DEFAULT_KEYSTORE_TYPE, DEFAULT_KEY_ALIAS, DEFAULT_KEYSTORE_PASSWORD, DEFAULT_KEYSTORE_PATH);
    }
    
    
    
    public X509Certificate loadX509CertificateFromKeyStore(String keyStoreType, String keyAlias, char[] keyStorePassword, String keyStorePath)
    {
        try
        {
            FileInputStream keyStoreStream = new FileInputStream(keyStorePath);
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(keyStoreStream, keyStorePassword);
            return  (X509Certificate) keyStore.getCertificate(keyAlias);            
        }
        catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException ex)
        {
            ex.printStackTrace();
            
            return null;
        }
    }
}