package kr.co.jsol.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// application.yml 혹은 properties에
// storage.location : ~~~
// storage:
//  location: ~~~ 와 같은 형태로 값이 있어야 함.
@ConfigurationProperties(prefix="storage")
@Configuration
public class StorageConfig {
    private String location;

    public StorageConfig() {
//        this.location = location;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location=location;
    }
}
