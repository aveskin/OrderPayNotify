package ru.aveskin.ordermicroservice.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.aveskin.ordermicroservice.order.dto.OrderPaymentEventRequest;
import ru.aveskin.ordermicroservice.order.dto.OrderRequest;
import ru.aveskin.ordermicroservice.order.dto.OrderResponse;
import ru.aveskin.ordermicroservice.order.model.Order;
import ru.aveskin.ordermicroservice.order.model.OrderStatus;
import ru.aveskin.ordermicroservice.order.repository.OrderRepository;
import ru.aveskin.ordermicroservice.order.service.KafkaEventSender;
import ru.aveskin.ordermicroservice.order.service.OrderService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final KafkaEventSender kafkaEventSender;
    private final TransactionTemplate transactionTemplate;

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
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        Order order = new Order();
        order.setUserId(request.userId());
        order.setAmount(request.amount());
        order.setStatus(OrderStatus.PENDING);
        Order createdOrder = orderRepository.save(order);

        return new OrderResponse(createdOrder.getId());
    }

    @Override
    public void payOrder(Long orderId) {
        Optional<Order> orderOp = orderRepository.findOrderById(orderId);
        var order = orderOp.orElseThrow(() -> new RuntimeException(orderId + "не найден"));

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new IllegalStateException("Заказ уже оплачен или отменён");
        }
        transactionTemplate.executeWithoutResult(transactionStatus -> kafkaEventSender
                .sendPaymentRequestEvent(new OrderPaymentEventRequest(orderId,
                        order.getUserId(),
                        order.getAmount())));
    }

    @Override
    public void cancelOrder(Long orderId) {
        Optional<Order> orderOp = orderRepository.findOrderById(orderId);
        var order = orderOp.orElseThrow(() -> new RuntimeException(orderId + "не найден"));
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }
}
