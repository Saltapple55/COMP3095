package ca.gbc.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(value="product")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Product { //going to store in mongodb, but
    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price; // collections in mongodb hold documents-aka rows

}
