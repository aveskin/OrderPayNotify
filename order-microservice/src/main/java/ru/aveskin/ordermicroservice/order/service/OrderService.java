package ru.aveskin.ordermicroservice.order.service;

import ru.aveskin.ordermicroservice.order.dto.OrderRequest;
import ru.aveskin.ordermicroservice.order.dto.OrderResponse;
import ru.aveskin.ordermicroservice.order.model.Order;

import java.util.List;

public interface OrderService {
    Order getOrder(Long orderId);

    List<Order> getAllOrders();

    void deleteOrder(Long orderId);

    OrderResponse createOrder(OrderRequest request);

    void payOrder(Long orderId);

    void cancelOrder(Long orderId);
}
