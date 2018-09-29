package com.moxi.mogublog.config.solr;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.convert.SolrJConverter;

/**
 * @author limboy
 * @create 2018-09-29 16:09
 */
@Configuration
public class SolrConfig {
    @Value("${spring.data.solr.host}")
    private String solrHost;

    @Value("${spring.data.solr.core}")
    private String solrCore;

    /**
     * 配置SolrTemplate
     */
    @Bean
    public SolrTemplate solrTemplate() {
        HttpSolrServer solrServer = new HttpSolrServer(solrHost);
        SolrTemplate template = new SolrTemplate(solrServer);
        template.setSolrCore(solrCore);
        template.setSolrConverter(new SolrJConverter());
        return template;
    }
}
