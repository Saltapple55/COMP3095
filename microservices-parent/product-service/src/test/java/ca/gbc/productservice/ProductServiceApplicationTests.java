package ca.gbc.productservice;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;

//import org.testcontainers.containers.MongoDbContainers;
//teels sbringboot to look for a main configuration clss= springbookapplication
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// https://java.testcontainers.org/

class ProductServiceApplicationTests {
    @ServiceConnection
    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.0.10");
    //don't want to occupy real port with tests-co
//
//    @Test //marks method as a test-
//    void contextLoads() {
//    }
    @LocalServerPort //whatever port selected in springboot test is stored in this
    private Integer port;

    //http/localhost:port//api/product
    @BeforeEach
    //method run before each test method
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        //can look here and see where things are being called
        //how to codify client to invoke endpoint?
    }

    static {
        mongoDBContainer.start();
    }


    @Test
    void createProductTest() {
        String requestBody = """
                { "name": "Samsung TV",
                    "description": "Samsung TV - Model 2024",
                    "price": 2000
                }
                """;
        //BDD - Behavioral Driven Development (given when, do this
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")//base URI is local host but need to add extra URI
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.equalTo("Samsung TV"))
                .body("description", Matchers.equalTo("Samsung TV - Model 2024"))
                .body("price", Matchers.equalTo(2000));
        ;


    }

    //not running in intellij,

    @Test
    void getAllProductsTest() {
        String requestBody = """
                { "name": "Samsung TV",
                    "description": "Samsung TV - Model 2024",
                    "price": 2000
                }
                """;
        //BDD - Behavioral Driven Development (given when, do this
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.equalTo("Samsung TV"))
                .body("description", Matchers.equalTo("Samsung TV - Model 2024"))
                .body("price", Matchers.equalTo(2000));

        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/product")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0))
                .body("[0].name", Matchers.equalTo("Samsung TV"))
                .body("[0].description", Matchers.equalTo("Samsung TV - Model 2024"))
                .body("[0].price", Matchers.equalTo(2000));

    }
}
