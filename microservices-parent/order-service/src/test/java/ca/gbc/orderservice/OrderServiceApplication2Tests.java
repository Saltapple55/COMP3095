package ca.gbc.orderservice;

import ca.gbc.orderservice.stub.InventoryClientStub;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;


@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port=0)
class OrderServiceApplication2Tests {
	@ServiceConnection
	static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("order_service")
			.withUsername("admin")
			.withPassword("password");

	@LocalServerPort
	private Integer port;
//	@Test
//	void contextLoads() {
//	}

	static{
		postgreSQLContainer.start();

	}

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost/";
		if (port != null) {
			RestAssured.port = port;
			System.out.println("Using port: " + port);
		} else {
			throw new IllegalStateException("Port is not initialized!");
		}
	}
	@Test
	void shouldSubmitOrder(){
		String submitorderJson= """
    {"skuCode": "SKU001",
    "price":  500,
    "quantity": 10
    }
    
				""";
		//mock a call to a client/inventory service
		InventoryClientStub.stubInventoryCall("SKU001", 10);

		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.body(submitorderJson)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().asString();
				assertThat(responseBodyString, Matchers.is("Order placed successfully"));

	}


}
