package com.melita.order.repos;

import com.melita.order.entities.OrderItem;
import com.melita.order.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
