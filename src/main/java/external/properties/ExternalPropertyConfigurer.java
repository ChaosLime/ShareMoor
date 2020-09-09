//https://github.com/eugenp/tutorials/tree/9b6d69172c4333b64722db810266650d376fbb94/spring-boot-modules/spring-boot-environment/src/main/java/com/baeldung/properties
package external.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class ExternalPropertyConfigurer {

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer properties = new PropertySourcesPlaceholderConfigurer();
        properties.setLocation(new FileSystemResource("../conf.yaml"));
        properties.setIgnoreResourceNotFound(false);
        return properties;
    }
}