package com.kramphub.example.app.conf;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DatastoreConfig {

    private GcpProperties gcpProperties;

    @Autowired
    public DatastoreConfig(GcpProperties gcpProperties) {
        this.gcpProperties = gcpProperties;
    }

    @Bean
    public Datastore datastore() {
        log.info("Instantiating datastore configuration");
        return DatastoreOptions.newBuilder().setNamespace(gcpProperties.getDatastore().getNamespace()).build().getService();
    }
}
