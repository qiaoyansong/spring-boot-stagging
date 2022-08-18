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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/27 11:55 上午
 * description：es网址
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
        return new RestHighLevelClient(RestClient.builder(
                // 新版ES都走的https协议
                new HttpHost(hostName, Integer.parseInt(port), "http"))
        );
    }

    /**
     * 使用ssl证书创建的客户端，高版本ES 使用https + ssl证书的方式，低版本不需要
     */
    public RestHighLevelClient restHighLevelClientCase2() {
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

            return new RestHighLevelClient(RestClient.builder(
                    // 新版ES都走的https协议
                    new HttpHost(hostName, Integer.parseInt(port), "https"))
                    .setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                        httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                        httpAsyncClientBuilder.disableAuthCaching();
                        httpAsyncClientBuilder.setSSLStrategy(sessionStrategy);
                        return httpAsyncClientBuilder;
                    }));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
