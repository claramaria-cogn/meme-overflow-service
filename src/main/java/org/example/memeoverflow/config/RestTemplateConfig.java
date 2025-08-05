package org.example.memeoverflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.cert.X509Certificate;

/**
 * Configuration for RestTemplate with SSL bypass for external API calls
 * WARNING: This configuration disables SSL verification and should only be used
 * in development
 */
@Configuration
public class RestTemplateConfig {

  private final MemeOverflowProperties properties;

  public RestTemplateConfig(MemeOverflowProperties properties) {
    this.properties = properties;
  }

  @Bean
  public RestTemplate restTemplate() {
    return createSSLIgnoringRestTemplate();
  }

  private RestTemplate createSSLIgnoringRestTemplate() {
    try {
      // Create a custom request factory that bypasses SSL verification
      SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory() {
        @Override
        protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
          super.prepareConnection(connection, httpMethod);

          if (connection instanceof HttpsURLConnection httpsConnection) {

            try {
              // Create a trust manager that accepts all certificates
              TrustManager[] trustAllCerts = new TrustManager[] {
                  new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                      return new X509Certificate[0];
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                      // Trust all client certificates
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                      // Trust all server certificates
                    }
                  }
              };

              // Install the all-trusting trust manager
              SSLContext sslContext = SSLContext.getInstance("TLS");
              sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
              httpsConnection.setSSLSocketFactory(sslContext.getSocketFactory());

              // Create a hostname verifier that accepts all hostnames
              httpsConnection.setHostnameVerifier((hostname, session) -> true);

            } catch (Exception e) {
              System.err.println("Failed to configure SSL for connection: " + e.getMessage());
            }
          }
        }
      };

      // Set timeouts from properties
      int timeoutMillis = properties.getDeepAi().getTimeoutSeconds() * 1000;
      requestFactory.setConnectTimeout(timeoutMillis);
      requestFactory.setReadTimeout(timeoutMillis);

      return new RestTemplate(requestFactory);
    } catch (Exception e) {
      System.err.println("Failed to create SSL-ignoring RestTemplate, using default: " + e.getMessage());
      return new RestTemplate();
    }
  }
}
