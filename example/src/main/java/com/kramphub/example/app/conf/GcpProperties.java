package com.kramphub.example.app.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("gcp")
@Data
public class GcpProperties {

    private DatastoreConf datastore;

    @Data
    public static class DatastoreConf {
        private String namespace;
        private Integer maxQueryLimit;
    }
}
