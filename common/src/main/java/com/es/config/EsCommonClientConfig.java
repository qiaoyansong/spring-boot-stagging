package com.es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/27 11:55 上午
 * description：
 */
@Configuration
@ConfigurationProperties(prefix = "es")
public class EsCommonClientConfig {

    private String hostName;

    private String port;

    @Bean(name = "commonRestHighLevelClient")
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
            RestClient.builder(
                new HttpHost(hostName, Integer.parseInt(port), "https")));
    }
}
