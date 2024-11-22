package ca.gbc.inventoryservice.repository;

import ca.gbc.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    //spring will parse method name
    boolean existsBySkuCodeAndQuantityGreaterThanEqual(String skuCode, Integer quantity);




}
