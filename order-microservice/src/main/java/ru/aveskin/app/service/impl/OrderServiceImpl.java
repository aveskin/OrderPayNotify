package ru.aveskin.app.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aveskin.app.dto.OrderRequest;
import ru.aveskin.app.dto.OrderResponse;
import ru.aveskin.app.event.PaymentCreatedEvent;
import ru.aveskin.app.model.Order;
import ru.aveskin.app.model.OrderStatus;
import ru.aveskin.app.model.OutboxEvent;
import ru.aveskin.app.model.OutboxStatus;
import ru.aveskin.app.repository.OrderRepository;
import ru.aveskin.app.repository.OutboxRepository;
import ru.aveskin.app.service.OrderService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Order getOrder(Long orderId) {
        Optional<Order> orderOp = orderRepository.findOrderById(orderId);
        var order = orderOp.orElseThrow(() -> new RuntimeException(orderId + "не найден"));
        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new RuntimeException("Заказ был отменен");
        }
        return order;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Order order = new Order();
        order.setUserId(request.userId());
        order.setAmount(request.amount());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(Instant.now());
        Order createdOrder = orderRepository.save(order);

        createOutboxEvent(order);
        return new OrderResponse(createdOrder.getId());
    }

    @Override
    public void payOrder(Long orderId) {
//        Optional<Order> orderOp = orderRepository.findOrderById(orderId);
//        var order = orderOp.orElseThrow(() -> new RuntimeException(orderId + "не найден"));
//
//        if (!order.getStatus().equals(OrderStatus.PENDING)) {
//            throw new IllegalStateException("Заказ уже оплачен или отменён");
//        }
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Optional<Order> orderOp = orderRepository.findOrderById(orderId);
        var order = orderOp.orElseThrow(() -> new RuntimeException(orderId + "не найден"));
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    private void createOutboxEvent(Order order) {
        try {
            String paymentId = UUID.randomUUID().toString();
            PaymentCreatedEvent paymentCreatedEvent = new PaymentCreatedEvent(
                    paymentId,
                    order.getAmount(),
                    order.getCreatedAt().toString(),
                    order.getId(),
                    order.getUserId()
            );

            String eventJson = objectMapper.writeValueAsString(paymentCreatedEvent);

            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .orderId(order.getId())
                    .payload(eventJson)
                    .status(OutboxStatus.NEW)
                    .createdAt(Instant.now())
                    .build();

            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации события", e);
        }
    }
}
