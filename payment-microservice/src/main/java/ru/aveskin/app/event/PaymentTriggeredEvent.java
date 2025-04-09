package ru.aveskin.app.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentTriggeredEvent {
    private String paymentId;
    private Long orderId;
    private String status;

}
