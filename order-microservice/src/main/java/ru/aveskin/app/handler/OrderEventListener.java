package ru.aveskin.app.handler;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.aveskin.app.event.PaymentTriggeredEvent;
import ru.aveskin.app.model.Order;
import ru.aveskin.app.model.OrderStatus;
import ru.aveskin.app.model.OutboxEvent;
import ru.aveskin.app.model.OutboxStatus;
import ru.aveskin.app.repository.OrderRepository;
import ru.aveskin.app.repository.OutboxRepository;


@Component
@RequiredArgsConstructor
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class OrderEventListener {
    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;


    @KafkaListener(topics = "payment-triggered-events-topic")
    public void handleOrderCreatedEvent(PaymentTriggeredEvent event) {
        log.info("Event received: {}", event.toString());


        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new RuntimeException("Заказ не найден: " + event.getOrderId()));

        OutboxEvent outboxEvent = outboxRepository
                .findByOrderId(order.getId())
                .orElseThrow(() -> new RuntimeException("Outbox event not found"));

        if ("SUCCESS".equals(event.getStatus())) {
            order.setStatus(OrderStatus.PAID);
            outboxEvent.setStatus(OutboxStatus.FINISHED);
        } else {
            order.setStatus(OrderStatus.PAYMENT_FAILED);
            outboxEvent.setStatus(OutboxStatus.FAILED);
        }

        outboxRepository.save(outboxEvent);
        orderRepository.save(order);
        log.info("Статус заказа {} обновлён на {}", order.getId(), order.getStatus());
    }
}
