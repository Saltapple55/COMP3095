package ca.gbc.orderservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;


//@FeignClient(value="inventory", url="${inventory.service.url}")
@Slf4j
public interface InventoryClient {
    Logger log = LoggerFactory.getLogger(InventoryClient.class);

    //@RequestMapping(method= RequestMethod.GET, value="/api/inventory")
    @GetExchange("/api/inventory")
    @CircuitBreaker(name="inventory", fallbackMethod = "fallbackMethod")
    @Retry(name="inventory")
    boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);
    //authoering the client-the call is made in service

    default boolean fallbackMethod(@RequestParam String code,@RequestParam Integer quantity, Throwable throwable){
        log.info("inventory not working");
        return false;
    }


}
