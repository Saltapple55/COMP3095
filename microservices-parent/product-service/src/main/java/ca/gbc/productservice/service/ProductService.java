package ca.gbc.productservice.service;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.repository.ProductRepository;
//Spring - starts from dependency injection- inversion of control- 2 implementation
//service doesn't contain information, but logic - only need one instance of class
//root annotation
//isntead of creating new object, inject in product
//controller will eventually use this
import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);

    List<ProductResponse> getAllProducts();

    String updateProduct(String id, ProductRequest productRequest);

    void deleteProduct(String id);
}
