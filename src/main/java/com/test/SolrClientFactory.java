package com.test;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpClientUtil;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Created by Tushar on 4/11/17.
 */
public class SolrClientFactory {
    private static final Logger logger = LoggerFactory.getLogger(SolrClientFactory.class);

    private static final String ZK_SASL_CLIENT_ENABLED = "zookeeper.sasl.client";

    public static SolrClient createZkClient(SolrConnectionDetails connectionDetails) {

        logger.info("Creating SolrCloud client");
        String zkConnectionString = connectionDetails.getZkString();
        initKerberos(connectionDetails.isKerberosEnabled());
        CloudSolrClient cloudSolrClient = new CloudSolrClient(zkConnectionString);
        cloudSolrClient.setDefaultCollection(connectionDetails.getCollectionName());
        return cloudSolrClient;
    }


    public static SolrClient createHttpClient(SolrConnectionDetails connectionDetails) {
        logger.info("Creating Solr standalone client");
        String httpUrl = connectionDetails.getHttpUrl();
        String collectionName = connectionDetails.getCollectionName();
        String urlString = httpUrl + "/" + collectionName;
        initKerberos(connectionDetails.isKerberosEnabled());
        SolrClient client = new HttpSolrClient(urlString);
        return client;
    }

    private static void initKerberos(boolean kerberosEnabled) {
        if (kerberosEnabled) {
            URL resource = SolrClientFactory.class.getClassLoader().getResource("login.conf");
            if (resource == null) {
                throw new RuntimeException("login.conf not found on classpath");
            }
            String jaasConfigPath = resource.getPath();
            System.setProperty("java.security.auth.login.config", jaasConfigPath);
            HttpClientUtil.setConfigurer(new org.apache.solr.client.solrj.impl.Krb5HttpClientConfigurer());
            logger.info("Setting ZK kerberosEnabled to true");
            System.setProperty(ZK_SASL_CLIENT_ENABLED, String.valueOf(true));
        } else {
            logger.info("Setting ZK kerberosEnabled to false");
            System.setProperty(ZK_SASL_CLIENT_ENABLED, String.valueOf(false));
        }

    }
}
