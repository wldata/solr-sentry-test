package com.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Tushar on 4/11/17.
 */
public class SolrConnectionDetails {
    private String zkString;
    private String httpUrl;
    private String collectionName;
    private boolean kerberosEnabled;

    public boolean isKerberosEnabled() {
        return kerberosEnabled;
    }

    public void setKerberosEnabled(boolean kerberosEnabled) {
        this.kerberosEnabled = kerberosEnabled;
    }

    public String getZkString() {
        return zkString;
    }

    public void setZkString(String zkString) {
        this.zkString = zkString;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public void load() throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = SolrConnectionDetails.class.getClassLoader().getResourceAsStream("solr.properties");
        properties.load(inputStream);

        this.collectionName = properties.getProperty("collection");
        this.zkString = properties.getProperty("zkString");
        this.httpUrl = properties.getProperty("httpUrl");
        this.kerberosEnabled = Boolean.valueOf(properties.getProperty("kerberosEnabled"));
    }
}
