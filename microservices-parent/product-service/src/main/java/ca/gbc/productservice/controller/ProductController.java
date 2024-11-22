package ca.gbc.productservice.controller;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.service.ProductService;
import ca.gbc.productservice.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //Controller useful when replying html/xml - @RestController is json is lighter,
@RequestMapping("/api/product")
@RequiredArgsConstructor //for dependency/constructor injection- otherwisewill need to write constructor in code
public class ProductController {
    private final ProductService productService;
    //how  do we know what endpoint//method they want?

    @PostMapping //a way to signal what status code
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest){ //when call method-require in request
        ProductResponse createdproduct =productService.createProduct(productRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/product/"+createdproduct.id());


       // productService.createProduct(productRequest); //bc logic is layered
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdproduct);


    }

    //get request
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }
    @PutMapping("/{productId}") //not creating/get-just update with Put

    //id normally passed by url - //http://localhost:8082/api/product
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable("productId") String productId,
                                            @RequestBody ProductRequest productRequest){
        String updatedId= productService.updateProduct(productId, productRequest);
        HttpHeaders headers=new HttpHeaders();
        headers.add("Location", "/api/product"+updatedId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }
    //passed in same as update
    @DeleteMapping("/{productId}") //not creating/get-just update with Put

    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable("productId") String productId){
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
