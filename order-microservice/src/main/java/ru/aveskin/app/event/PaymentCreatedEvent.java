package ru.aveskin.app.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentCreatedEvent {
    private String paymentId;
    private BigDecimal amount;
    private String createdAt;
    private Long orderId;
    private String userId;

}
