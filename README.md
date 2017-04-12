# solr-sentry-test

https://issues.apache.org/jira/browse/SENTRY-1703


In a kerberized cluster with sentry for solr enabled:
1. Create keytab for the principal as client.keytab
2. Update client.jaas to reflect the principal and realm
3. Create/use a collection with write permission for principal
4. Update solr.properties
5. Update launch.sh to reflect your classpath execute
6. Watch solr admin console for logs
