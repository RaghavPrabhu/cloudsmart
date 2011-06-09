/**
 * 
 */
package org.apache.commons.httpclient.contrib.ssl;

import java.security.KeyStore;
       import java.security.KeyStoreException;
        import java.security.NoSuchAlgorithmException;
        import java.security.cert.CertificateException;
        import java.security.cert.X509Certificate;

        import javax.net.ssl.TrustManager;
       import javax.net.ssl.TrustManagerFactory;
        import javax.net.ssl.X509TrustManager;


        // modified by Mike Berger
        public class EasyX509TrustManager implements  X509TrustManager {
           private X509TrustManager standardTrustManager = null;

            /**
             * Constructor for EasyX509TrustManager.
             */
            public EasyX509TrustManager(KeyStore keystore)
                   throws NoSuchAlgorithmException, KeyStoreException {
                super ();
                TrustManagerFactory factory = TrustManagerFactory
                        .getInstance("SunX509");
                factory.init(keystore);
                TrustManager[] trustmanagers = factory.getTrustManagers();
                if (trustmanagers.length == 0) {
                    throw new NoSuchAlgorithmException(
                            "SunX509 trust manager not supported");
                }
                this .standardTrustManager = (X509TrustManager) trustmanagers[0];
           }

            public void checkClientTrusted(X509Certificate[] certificates,
                    String type) throws CertificateException {
                this .standardTrustManager
                        .checkClientTrusted(certificates, type);
            }

            public void checkServerTrusted(X509Certificate[] certificates,
                    String type) throws CertificateException {

                if ((certificates != null) && (certificates.length == 1)) {
                    X509Certificate certificate = certificates[0];
                    certificate.checkValidity();
                } else {
                    this .standardTrustManager.checkServerTrusted(certificates,
                            type);
                }
            }

            public X509Certificate[] getAcceptedIssuers() {
                return this.standardTrustManager.getAcceptedIssuers();
            }

        }
