//https://github.com/eugenp/tutorials/tree/9b6d69172c4333b64722db810266650d376fbb94/spring-boot-modules/spring-boot-environment/src/main/java/com/baeldung/properties
package external.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfProperties {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;
    
    @Value("${spring.servlet.multipart.max-request-size}")
    private String maxRequestSize;

    /*public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }*/

    
}