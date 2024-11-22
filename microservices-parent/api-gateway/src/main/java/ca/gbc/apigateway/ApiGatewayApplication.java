package ca.gbc.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiGatewayApplication {
//offer how to route the requetss
    //integrate keycloak, identity management- to authenticate
    //when request not work, normally a 403 bad request
    //docker images- trusted content official best, and also verif
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}
