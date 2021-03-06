package com.usian.config;


import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig extends ElasticsearchProperties {

    @Bean
    public RestHighLevelClient getRestHighLevelClient() {
        String[] split = getClusterNodes().split(",");
        HttpHost[] hosts = new HttpHost[split.length];
        for (int i = 0; i < hosts.length; i++) {
            String h = split[i];
            hosts[i] = new HttpHost(h.split(":")[0], Integer.parseInt(h.split(":")[1]));
        }
        return new RestHighLevelClient(RestClient.builder(hosts));
    }
}
