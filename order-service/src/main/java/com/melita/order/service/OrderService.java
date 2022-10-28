package com.melita.order.service;

import com.melita.order.entities.Bundle;
import com.melita.order.entities.Order;
import com.melita.order.entities.OrderItem;
import com.melita.order.entities.Product;
import com.melita.order.exception.AppException;
import com.melita.order.exception.RecordNotFoundException;
import com.melita.order.repos.OrderItemRepository;
import com.melita.order.repos.OrderRepository;
import lombok.Data;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.melita.order.enums.ErrorCode.*;

@Data
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final OrderItemRepository orderItemRepository;

    private final  MessageService messageService;

    public Order save(Order order) {

        // validate items and set order
        if (order.getItems() != null) order.getItems().stream().forEach(tt -> {
            validate(tt);
            tt.setOrder(order);
        });
        Order ord = orderRepository.save(order);
        messageService.sendMessage(ord);
        return ord;
    }

    /**
     * validate the product exists and combination is fine
     */
    private void validate(OrderItem orderItem) {
        if (orderItem.getProduct() == null) throw new AppException(PRODUCT_REQUIRED);
        if (orderItem.getBundle() == null) throw new AppException(BUNDLED_REQUIRED);

        Product product = productService.getById(orderItem.getProduct().getId());
        Optional<Bundle> item = product.getBundles().stream()
                                    .filter(p -> p.getId() == orderItem.getBundle().getId())
                                    .findFirst();

        if (item == null) throw new AppException(PRODUCT_NOT_SUPPORT_BUNDLE);

        // set bundle price
        orderItem.setPrice(product.getPrice().add(item.get().getPrice()));

        //set total price
        orderItem.setTotal(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(()-> new RecordNotFoundException(ORDER_NOT_FOUND));
    }

   @Transactional
    public Order update(Order o, Long id) {
        Order order = getById(id);
        order.setNumber(o.getNumber());
        order.setFirstName(o.getFirstName());
        order.setLastName(o.getLastName());
        order.setMobile(o.getMobile());
        order.setAddress(o.getAddress());
        order.setInstallationTime(o.getInstallationTime());

        // set new items
        Set<OrderItem> items = o.getItems();
        if (items != null) items.stream().forEach(tt -> {
            validate(tt);
            tt.setOrder(order);
        });

        // delete previous items & set new
        orderItemRepository.deleteAll(order.getItems());
        order.setItems(items);

        orderRepository.save(order);
        return order;
    }

    public void delete(Long id) {
        Order o = getById(id);
        orderRepository.delete(o);
    }
}
