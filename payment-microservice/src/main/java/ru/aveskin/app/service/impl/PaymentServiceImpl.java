package ru.aveskin.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.aveskin.app.event.PaymentCreatedEvent;
import ru.aveskin.app.event.PaymentTriggeredEvent;
import ru.aveskin.app.service.PaymentService;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final KafkaTemplate<String, PaymentTriggeredEvent> kafkaTemplate;

    @Override
    public void pay(PaymentCreatedEvent paymentCreatedEvent) {
        boolean isSuccess = new Random().nextBoolean(); // Эмуляция успешного/неуспешного платежа
        String status = isSuccess ? "SUCCESS" : "FAILED";

        log.info("Обработка платежа для заказа {}: статус {}", paymentCreatedEvent.getOrderId(), status);

        PaymentTriggeredEvent eventResponse = new PaymentTriggeredEvent(
                paymentCreatedEvent.getPaymentId(), paymentCreatedEvent.getOrderId(), status);

        kafkaTemplate.send("payment-triggered-events-topic", eventResponse);

        log.info("Ответ по платежу отправлен: {}", eventResponse);
    }
}
