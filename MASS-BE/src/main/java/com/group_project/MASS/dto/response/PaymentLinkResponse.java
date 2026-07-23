package com.group_project.MASS.dto.response;

import com.group_project.MASS.model.PaymentStatus;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentLinkResponse {
    private Long appointmentId;
    private Long paymentId;
    private Long orderCode;
    private String checkoutUrl;
    private String qrCode;
    private PaymentStatus paymentStatus;
    private BigDecimal amount;
}
