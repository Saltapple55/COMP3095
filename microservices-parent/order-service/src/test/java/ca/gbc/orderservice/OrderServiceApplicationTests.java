package ca.gbc.orderservice;

import ca.gbc.orderservice.stub.InventoryClientStub;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class OrderServiceApplicationTests {

	// PostgreSQLContainer is a Testcontainers class representing a PostgreSQL instance
	@ServiceConnection
	static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("order_service")
			.withUsername("admin")
			.withPassword("password");

	// Inject the dynamically assigned port for the Spring Boot application
	@LocalServerPort
	public Integer port;

	// Start the PostgreSQL container
	static {
		postgreSQLContainer.start();
		System.out.println("PostgreSQL Container started: " + postgreSQLContainer.isRunning());
	}

	// Set up RestAssured to point to the correct server before each test
	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		if (port != null) {
			RestAssured.port = port;
			System.out.println("Using port: " + port);
		} else {
			throw new IllegalStateException("Port is not initialized!");
		}
	}

	@Test
	void shouldSubmitOrder() {
		String submitOrderJson = """
                {
                    "skuCode": "SKU001",
                    "price": 999.99,
                    "quantity": 10
                }
                """;

		InventoryClientStub.stubInventoryCall("SKU001", 10);

		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().asString();

		assertThat(responseBodyString, Matchers.is("Order placed successfully"));
	}

	@Test
	void createOrderTest() {
		String requestBody = """
                {
                    "orderNumber": "ORD123",
                    "skuCode": "SKU001",
                    "price": 999.99,
                    "quantity": 1
                }
                """;

		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.statusCode(201)
				.contentType("application/json")
				.body("id", Matchers.notNullValue())
				.body("orderNumber", Matchers.notNullValue())
				.body("skuCode", Matchers.equalTo("SKU001"))
				.body("price", Matchers.equalTo(999.99F))
				.body("quantity", Matchers.equalTo(1));
	}

	@Test
	void getAllOrdersTest() {
		String requestBody = """
                {
                    "orderNumber": "ORD123",
                    "skuCode": "SKU001",
                    "price": 999.99,
                    "quantity": 1
                }
                """;

		// First, create an order
		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("/api/order")
				.then()
				.statusCode(201)
				.contentType("application/json")
				.body("id", Matchers.notNullValue())
				.body("orderNumber", Matchers.notNullValue())
				.body("skuCode", Matchers.equalTo("SKU001"))
				.body("price", Matchers.equalTo(999.99F))
				.body("quantity", Matchers.equalTo(1));

		// Then, retrieve all orders and check the created order is present
		RestAssured.given()
				.when()
				.get("/api/order")
				.then()
				.log().all()
				.statusCode(200)
				.body("size()", Matchers.greaterThan(0))
				.body("[0].orderNumber", Matchers.notNullValue())
				.body("[0].skuCode", Matchers.equalTo("SKU001"))
				.body("[0].price", Matchers.equalTo(999.99F))
				.body("[0].quantity", Matchers.equalTo(1));
	}
}
