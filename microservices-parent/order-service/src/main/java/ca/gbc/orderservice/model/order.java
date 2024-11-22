package ca.gbc.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

//table representation
//
@Entity
@Table(name="t_orders") //class names single, table plural
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder //streamlines instantiations
public class Order {
    @Id
    //underlying orm will rely on postgres to take care of it
    @GeneratedValue(strategy = GenerationType.IDENTITY) //autoincrement
    private Long id;


    private String orderNumber;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;

}
