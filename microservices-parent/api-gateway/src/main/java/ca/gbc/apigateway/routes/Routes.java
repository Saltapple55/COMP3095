package ca.gbc.apigateway.routes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
@Slf4j
//generally want a log file
public class Routes {
    //how to inject the correct string - maps to the value depending of which application.properties is running
    @Value("${services.product-url}")
    private String productServiceUrl;
    @Value("${services.order-url}")

    private String orderServiceUrl;
    @Value("${services.inventory-url}")

    private String inventoryServiceUrl;

    //mot routing will be in request mapping
   @Bean
    public RouterFunction<ServerResponse> productServiceRoute(){
        log.info("initializing product service route with URL: {}", productServiceUrl);

        return GatewayRouterFunctions.route("product_service")
                .route(RequestPredicates.path("/api/product"), request -> {
                            log.info("Received request for product service: {}", request.uri());
                            return HandlerFunctions.http(productServiceUrl).handle(request);
                        })
                .filter(CircuitBreakerFilterFunctions //fallback mechanism defined below
                        .circuitBreaker("productServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
//
//        return route("product_service")
//                .route(RequestPredicates.path("/api/product"), request ->{
//                    log.info("Recieved request for product");
//                    try{
//                        ServerResponse response = http(productServiceUrl).handle(request);
//                        log.info("Response status: {}", response.statusCode() );
//                        return response;
//                    }
//                    catch (Exception e){
//                        log.error("Error occurred while routing to:{} ", e.getMessage(), e);
//                        return ServerResponse.status(500).body("Error ocurred when making product service");
//                    }
//       }).build();
    }
    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute(){
        log.info("initializing order service route with URL: {}", orderServiceUrl);


        return GatewayRouterFunctions.route("order_service")
                .route(RequestPredicates.path("/api/order"), request -> {
                    log.info("Received request for order service: {}", request.uri());
                    return HandlerFunctions.http(orderServiceUrl).handle(request);
                })
                .filter(CircuitBreakerFilterFunctions //fallback mechanism defined below
                        .circuitBreaker("orderServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
       //no
//        return route("order_service")
//                .route(RequestPredicates.path("/api/order"), request ->{
//                    log.info("Recieved request for order");
//                    try{
//                        ServerResponse response = http(orderServiceUrl).handle(request);
//                        log.info("Response status: {}", response.statusCode() );
//                        return response;
//                    }
//                    catch (Exception e){
//                        log.error("Error occurred while routing to:{} ", e.getMessage(), e);
//                        return ServerResponse.status(500).body("Error ocurred when making order service");
//                    }
//                }).build();
    }
    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoute(){
//        //no
        log.info("initializing order service route with URL: {}", inventoryServiceUrl);


        return GatewayRouterFunctions.route("inventory_service")
                .route(RequestPredicates.path("/api/inventory"), request -> {
                    log.info("Received request for inventory service: {}", request.uri());
                    return HandlerFunctions.http(inventoryServiceUrl).handle(request);
                })
                .filter(CircuitBreakerFilterFunctions //fallback mechanism defined below
                        .circuitBreaker("orderServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
//        return route("inventory_service")
//                .route(RequestPredicates.path("/api/inventory"), request ->{
//                    log.info("Recieved request for inventory");
//                    try{
//                        ServerResponse response = http(inventoryServiceUrl).handle(request);
//                        log.info("Response status: {}", response.statusCode() );
//                        return response;
//                    }
//                    catch (Exception e){
//                        log.error("Error occurred while routing to:{} ", e.getMessage(), e);
//                        return ServerResponse.status(500).body("Error ocurred when making order service");
//                    }
//                }).build();
    }
    @Bean
    public RouterFunction<ServerResponse> productServiceSwaggerRoute(){
       return GatewayRouterFunctions.route("product_service_swagger")
               .route(RequestPredicates.path("/aggregate/product-service/v3/api-docs")
               , HandlerFunctions.http(productServiceUrl))
        .filter(setPath("/api-docs"))
               .build();
    }
    @Bean
    public RouterFunction<ServerResponse> orderServiceSwaggerRoute(){
        return GatewayRouterFunctions.route("order_service_swagger")
                .route(RequestPredicates.path("/aggregate/order-service/v3/api-docs")
                        , HandlerFunctions.http(orderServiceUrl))
                .filter(setPath("/api-docs"))
                .build();
    } @Bean
    public RouterFunction<ServerResponse> inventoryServiceSwaggerRoute(){
        return GatewayRouterFunctions.route("inventory_service_swagger")
                .route(RequestPredicates.path("/aggregate/inventory-service/v3/api-docs")
                        , HandlerFunctions.http(inventoryServiceUrl))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute(){
       return route("fallbackRoute")
               .route(RequestPredicates.path("/fallbackRoute"),
                       request->ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                               .body("Service Temporarily Unavailable, please try again later"))
               .build();

    }

}
