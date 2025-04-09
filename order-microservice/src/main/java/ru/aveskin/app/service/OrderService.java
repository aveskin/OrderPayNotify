package ru.aveskin.app.service;

import ru.aveskin.app.dto.OrderRequest;
import ru.aveskin.app.dto.OrderResponse;
import ru.aveskin.app.model.Order;

import java.util.List;

public interface OrderService {
    Order getOrder(Long orderId);

    List<Order> getAllOrders();

    void deleteOrder(Long orderId);

    OrderResponse createOrder(OrderRequest request);

    void payOrder(Long orderId);

    void cancelOrder(Long orderId);
}
