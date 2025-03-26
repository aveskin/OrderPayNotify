package ru.aveskin.ordermicroservice.order.model;

public enum OrderStatus {
    PENDING,      // Заказ ожидает обработки
    PAID,         // Заказ оплачен
    CANCELED,     // Заказ отменен
    PAYMENT_FAILED,// Оплата не прошла
}
