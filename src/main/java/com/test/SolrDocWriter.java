package com.test;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpClientUtil;
import org.apache.solr.client.solrj.impl.Krb5HttpClientConfigurer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Created by Tushar on 4/11/17.
 */
public class SolrDocWriter {

    private static final Logger logger = LoggerFactory.getLogger(SolrDocWriter.class);
    private static final String JAAS_CONFIG_FILE = "login.conf";

    private static SolrInputDocument getSolrDoc(int i) throws UnknownHostException {
        SolrInputDocument doc = new SolrInputDocument();
        InetAddress localHost = InetAddress.getLocalHost();
        String value = UUID.randomUUID().toString();
        doc.addField("id", value);
        doc.addField("type", "data_resource");
        doc.addField("name", "hostname__" + localHost + "__" + value + "-" + i);
        return doc;
    }

    public void run(int numDocs) throws IOException, SolrServerException {

        SolrConnectionDetails connectionDetails = new SolrConnectionDetails();
        connectionDetails.load();

        URL resource = SolrDocWriter.class.getClassLoader().getResource(JAAS_CONFIG_FILE);
        if (resource == null) {
            throw new RuntimeException(JAAS_CONFIG_FILE + " not found");
        }
        String path = resource.getPath();
        System.setProperty("java.security.auth.login.config", path);
        SolrClient client = null;
        try {
            client = SolrClientFactory.createZkClient(connectionDetails);
            HttpClientUtil.setConfigurer(new Krb5HttpClientConfigurer());
            SolrInputDocument doc;
            Collection<SolrInputDocument> list = new ArrayList<>();
            for (int i = 0; i < numDocs; i++) {
                doc = getSolrDoc(i);
                list.add(doc);
            }
            client.add(list);
            client.commit();
            logger.info("Committed batch");
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }
}
