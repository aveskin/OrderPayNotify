package ru.aveskin.ordermicroservice.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaymentEventResponse {
    private Long orderId;
    private String status;
}
