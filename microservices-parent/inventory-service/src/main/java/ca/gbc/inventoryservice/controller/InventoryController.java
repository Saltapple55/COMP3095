package ca.gbc.inventoryservice.controller;

import ca.gbc.inventoryservice.service.InventoryService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    //before
    private final InventoryService inventoryService;
    /*
    //this is what requiredArgsConstructor does
    @Autowired - Spring has a bin-lives in context, this says wants bin injected in controller
    otherwise, would be     private final InventoryService inventoryService = new InventoryServiceImpl();
    //but with autowired, injects same instance

    @Autowired
    public InventoryController(InventoryService inventoryService){
        this.inventoryService=inventoryService
    }
*/
    //Http://localhost:8084?/api/inventory?skuCode=10232%quantity=3
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity ){
        return inventoryService.isInStock(skuCode, quantity);


    }
}
