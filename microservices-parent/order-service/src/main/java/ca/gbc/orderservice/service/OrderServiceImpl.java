package ca.gbc.orderservice.service;

import ca.gbc.orderservice.client.InventoryClient;
import ca.gbc.orderservice.dto.OrderRequest;
import ca.gbc.orderservice.model.Order;
import ca.gbc.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j // for logging
@Service //annotation for order of service
@RequiredArgsConstructor //forces implementation of constructor for required dependencies (repo)
@Transactional //can do rollback if need to - e.g. if communication fails, don't make incorrect asumption to charge client account
public class OrderServiceImpl implements  OrderService{

    private final OrderRepository orderRepository; //an injection

    private final InventoryClient inventoryClient;
    @Override
    public void placeOrder(OrderRequest orderRequest){

        //need to check inventory service
        //if you use postman rn, may think it failed, but is working fine

        //var is typeless, prefered for service requests
        var isProductInStock=inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if(isProductInStock) {

            Order order = Order.builder()
                    .orderNumber(UUID.randomUUID().toString())//autogenerations
                    .price(orderRequest.price()) //calls to setters - coming from orderRequest
                    .skuCode(orderRequest.skuCode())
                    .quantity(orderRequest.quantity())
                    .build();
            orderRepository.save(order);

            //process order and persist it to db

        }else{
            //will see this in log files
            throw new RuntimeException("Product with skuCode " + orderRequest.skuCode() +"not available");
        }


    }

}
