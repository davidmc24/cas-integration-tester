package app;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

class App extends Application<AppConf> {
    public static void main(String... args) throws Exception {
        disableHostValidation();
        new App().run(args);
    }

    /**
     * Only needed for localhost testing with self-signed certs; do not use in production
     */
    private static void disableHostValidation() throws Exception {
        TrustManager[] gullibleTrustManager = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, gullibleTrustManager, new java.security.SecureRandom());
        SSLContext.setDefault(sc);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> hostname.equals("localhost"));
    }

    @Override
    public String getName() {
        return "cas-integration-tester";
    }

    @Override
    public void run(AppConf configuration, Environment environment) throws Exception {
        environment.jersey().register(new RootResource(configuration));
    }
}
