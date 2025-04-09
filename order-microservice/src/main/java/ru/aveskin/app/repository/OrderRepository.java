package ru.aveskin.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aveskin.app.model.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOrderById(Long id);
}
