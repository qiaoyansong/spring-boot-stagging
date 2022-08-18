package com.common.es.config;

import lombok.Setter;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestHighLevelClientBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/27 11:55 上午
 * description：es网址 ： https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-compatibility.html
 */
@Setter
@Configuration
@ConfigurationProperties(prefix = "es")
public class EsCommonClientConfig {

    private String hostName;

    private String port;

    private String userName;

    private String password;

    @Bean(name = "commonRestHighLevelClient", destroyMethod = "close")
    public RestHighLevelClient restHighLevelClient() {
        try {
            // ssl证书:
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(userName, password));
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    // 信任所有
                    return true;
                }
            }).build();
            SSLIOSessionStrategy sessionStrategy = new SSLIOSessionStrategy(sslContext, NoopHostnameVerifier.INSTANCE);
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));

            return new RestHighLevelClientBuilder(RestClient.builder(
                    // 新版ES都走的https协议
                    new HttpHost(hostName, Integer.parseInt(port), "https"))
                    .setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                        httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                        httpAsyncClientBuilder.disableAuthCaching();
                        httpAsyncClientBuilder.setSSLStrategy(sessionStrategy);
                        return httpAsyncClientBuilder;
                    })
                    .build())
                    // 启动兼容模式，可以与es 8.0版本通信
//                    .setApiCompatibilityMode(true)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
