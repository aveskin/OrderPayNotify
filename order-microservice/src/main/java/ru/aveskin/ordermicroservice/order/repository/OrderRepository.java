package ru.aveskin.ordermicroservice.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aveskin.ordermicroservice.order.model.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOrderById(Long id);
}
