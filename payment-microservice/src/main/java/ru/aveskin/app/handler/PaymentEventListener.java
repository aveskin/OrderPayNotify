package ru.aveskin.app.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.aveskin.app.event.PaymentCreatedEvent;
import ru.aveskin.app.service.PaymentService;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentEventListener {
    private final PaymentService paymentService;

    @KafkaListener(topics = "payment-created-events-topic")
    public void handlePaymentCreatedEvent(PaymentCreatedEvent event) {
        log.info("Event received: {}", event.toString());

        paymentService.pay(event);
    }
}
