package com.bracks.mylib.net.https;


import com.blankj.utilcode.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * good programmer.
 *
 * @date : 2018-09-01 下午 05:03
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class SslContextFactory {
    /**
     * 使用协议
     */
    private static final String CLIENT_AGREEMENT = "TLS";
    private static final String CLIENT_TRUST_MANAGER = "X.509";
    private static final String CLIENT_TRUST_KEYSTORE = "BKS";
    private static final String CLIENT_TRUST_KEY = "PKCS12";
    private static final String CLIENT_TRUST_PROVIDER = "BC";
    public static String TRUST_CA_PWD = "Huawei@123";
    public static String SELF_CERT_PWD = "IoM@1234";

    /**
     * 单项认证
     */
    public static SSLSocketFactory getSSLSocketFactoryForOneWay(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance(CLIENT_TRUST_MANAGER, CLIENT_TRUST_PROVIDER);
            KeyStore keyStore = KeyStore.getInstance(CLIENT_TRUST_KEYSTORE);
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                Certificate certificate1 = certificateFactory.generateCertificate(certificate);
                keyStore.setCertificateEntry(certificateAlias, certificate1);
                try {
                    if (certificate != null) {
                        certificate.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            SSLContext sslContext = SSLContext.getInstance(CLIENT_AGREEMENT);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 双向认证
     *
     * @param pkcs12
     * @param bks
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactoryForTwoWay(int pkcs12, int bks) {
        try {
            InputStream certificate = Utils.getApp().getResources().openRawResource(pkcs12);
            KeyStore keyStore = KeyStore.getInstance(CLIENT_TRUST_KEY);
            keyStore.load(certificate, SELF_CERT_PWD.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, SELF_CERT_PWD.toCharArray());

            try {
                certificate.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //初始化keystore
            KeyStore clientKeyStore = KeyStore.getInstance(CLIENT_TRUST_KEYSTORE);
            clientKeyStore.load(Utils.getApp().getResources().openRawResource(bks), TRUST_CA_PWD.toCharArray());

            SSLContext sslContext = SSLContext.getInstance(CLIENT_AGREEMENT);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(clientKeyStore);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, SELF_CERT_PWD.toCharArray());

            sslContext.init(kmf.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
