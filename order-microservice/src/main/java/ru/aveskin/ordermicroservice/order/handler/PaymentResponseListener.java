package ru.aveskin.ordermicroservice.order.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.aveskin.ordermicroservice.order.dto.OrderPaymentEventResponse;
import ru.aveskin.ordermicroservice.order.model.Order;
import ru.aveskin.ordermicroservice.order.model.OrderStatus;
import ru.aveskin.ordermicroservice.order.repository.OrderRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentResponseListener {
    private final OrderRepository orderRepository;

    @KafkaListener(topics = "payment-response-events-topic", groupId = "order-group")
    public void handlePaymentResponse(OrderPaymentEventResponse response) {
        log.info("Получен ответ об оплате для заказа {}: {}", response.getOrderId(), response.getStatus());

        Order order = orderRepository.findById(response.getOrderId())
                .orElseThrow(() -> new RuntimeException("Заказ не найден: " + response.getOrderId()));

        if ("SUCCESS".equals(response.getStatus())) {
            order.setStatus(OrderStatus.PAID);
        } else {
            order.setStatus(OrderStatus.PAYMENT_FAILED);
        }

        orderRepository.save(order);
        log.info("Статус заказа {} обновлён на {}", order.getId(), order.getStatus());
    }
}
