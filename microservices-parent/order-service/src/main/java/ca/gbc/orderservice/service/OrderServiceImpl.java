package ca.gbc.orderservice.service;

import ca.gbc.orderservice.client.InventoryClient;
import ca.gbc.orderservice.dto.OrderRequest;
import ca.gbc.orderservice.event.OrderPlacedEvent;
import ca.gbc.orderservice.model.Order;
import ca.gbc.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j // for logging
@Service //annotation for order of service
@RequiredArgsConstructor //forces implementation of constructor for required dependencies (repo)
@Transactional //can do rollback if need to - e.g. if communication fails, don't make incorrect asumption to charge client account
public class OrderServiceImpl implements  OrderService{

    private final OrderRepository orderRepository; //an injection

    private final InventoryClient inventoryClient;

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;



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

            //send message to kafka
//            OrderPlacedEvent orderPlacedEvent= new OrderPlacedEvent();
//            orderPlacedEvent.setOrderNumber(order.getOrderNumber());
//            orderPlacedEvent.setEmail(orderRequest.userDetails().email());
//            orderPlacedEvent.setEmail(orderRequest.userDetails().firstName());
//            orderPlacedEvent.setEmail(orderRequest.userDetails().lastName());

            OrderPlacedEvent orderPlacedEvent =
                    new OrderPlacedEvent(order.getOrderNumber(), orderRequest.userDetails().email());
            log.info("Start - sending orderplacedevent {} too kafka topic order--placed", orderPlacedEvent);
            kafkaTemplate.send("order-placed", orderPlacedEvent);
            log.info("Sent orderplacedevent {} too kafka topic order--placed", orderPlacedEvent);
//            log.info("Schema {}", orderPlacedEvent.getSchema());


        }else{
            //will see this in log files
            throw new RuntimeException("Product with skuCode " + orderRequest.skuCode() +"not available");
        }


    }

}
