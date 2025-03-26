package ru.aveskin.ordermicroservice.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaymentEventRequest {
    private Long orderId;
    private String userId;
    private BigDecimal amount;

}
