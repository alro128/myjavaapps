package web.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hello")
public class HelloConfiguration {

    private String defaultName;
    private Integer randomLimit;

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public Integer getRandomLimit() {
        return randomLimit;
    }

    public void setRandomLimit(Integer randomLimit) {
        this.randomLimit = randomLimit;
    }

    @Override
    public String toString() {
        return "HelloConfiguration [defaultName=" + defaultName + ", randomLimit=" + randomLimit + "]";
    }

}
