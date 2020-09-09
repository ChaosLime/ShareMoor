//https://github.com/eugenp/tutorials/tree/9b6d69172c4333b64722db810266650d376fbb94/spring-boot-modules/spring-boot-environment/src/main/java/com/baeldung/properties
package external.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ExternalPropertyFileLoader {

    @Autowired
    ConfProperties prop;

    public static void main(String[] args) {
        new SpringApplicationBuilder(ExternalPropertyFileLoader.class).build()
        .run(args);
    }

}