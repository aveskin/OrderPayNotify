package ru.aveskin.app.service;

import ru.aveskin.app.event.PaymentCreatedEvent;

public interface PaymentService {
    void pay(PaymentCreatedEvent paymentCreatedEvent);
}
