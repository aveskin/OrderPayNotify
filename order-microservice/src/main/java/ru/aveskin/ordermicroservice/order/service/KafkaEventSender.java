package ru.aveskin.ordermicroservice.order.service;

import ru.aveskin.ordermicroservice.order.dto.OrderPaymentEventRequest;

public interface KafkaEventSender {
    void sendPaymentRequestEvent(OrderPaymentEventRequest orderPaymentEventRequest);
}
