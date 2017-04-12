package com.test;

import com.sun.security.auth.module.Krb5LoginModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tushar on 4/11/17.
 */
public class ClasspathKrb5LoginModule extends Krb5LoginModule {

    private static final Logger logger = LoggerFactory.getLogger(ClasspathKrb5LoginModule.class);

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        logger.info("Initializing CustomKerbModule");
        String keyTabName = (String) options.get("keyTabName");
        URL resource = ClasspathKrb5LoginModule.class.getClassLoader().getResource(keyTabName);
        if (resource == null) {
            throw new RuntimeException("Cannot find " + keyTabName + " in classpath");
        }

        String keytabPath = resource.getPath();
        Map<String, Object> updatedOptions = new HashMap<>();
        updatedOptions.putAll(options);

        logger.info("keytab path = " + keytabPath);
        updatedOptions.put("keyTab", keytabPath);
        super.initialize(subject, callbackHandler, sharedState, updatedOptions);
    }
}
