package ca.gbc.productservice.service;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.model.Product;
import ca.gbc.productservice.repository.ProductRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import org.springframework.data.mongodb.core.query.Query;
import java.util.List;
import java.util.Queue;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService
{
    private final ProductRepository productRepository; //add constructor parameter?


    @Override
    public ProductResponse createProduct(ProductRequest productRequest) throws InterruptedException {
        //can deploy logging in diff modes
        //Thread.sleep(5000);

        log.debug("Creating a new product {}", productRequest.name());
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();//allows the app to set attr

        //persist a product
        productRepository.save(product);

        log.info("Product {} is saved", product.getId());

                return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }

    @Override
    public List<ProductResponse> getAllProducts() throws InterruptedException {

        log.debug("Returning a list of products");
        List<Product> products=productRepository.findAll();


        //return products.stream().map(product -> mapToProductResponse(product)).toList() //collection can be stream-one record at a time but comming in as a collection
        return products.stream().map(this::mapToProductResponse).toList();
    }
    private  ProductResponse mapToProductResponse(Product product){
        return new ProductResponse(product.getId(),  product.getName(), product.getDescription(), product.getPrice());
    }

    private final MongoTemplate mongoTemplate;

    @Override
    public String updateProduct(String id, ProductRequest productRequest) {

        log.debug("Updating a product with id {}", id);

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Product product = mongoTemplate.findOne(query, Product.class);

        if(product !=null){
            product.setDescription(productRequest.description());
            product.setPrice(productRequest.price());
            product.setName(productRequest.name());
            return productRepository.save(product).getId();
        }
        return id;

    }

    @Override
    public void deleteProduct(String id) {
        log.debug("Delete product with id {}", id);
        productRepository.deleteById(id);

    }
}
