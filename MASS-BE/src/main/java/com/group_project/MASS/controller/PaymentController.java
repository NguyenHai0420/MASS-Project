package com.group_project.MASS.controller;

import com.group_project.MASS.dto.response.ApiMessageResponse;
import com.group_project.MASS.dto.response.PaymentLinkResponse;
import com.group_project.MASS.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/appointments/{appointmentId}/payment-link")
    public ResponseEntity<PaymentLinkResponse>
    createPaymentLink(
            @PathVariable("appointmentId") Long appointmentId
    ) {
        return ResponseEntity.ok(
                paymentService.createPaymentLink(
                        appointmentId
                )
        );
    }

    @GetMapping("/appointments/{appointmentId}/status")
    public ResponseEntity<String> checkStatus(@PathVariable("appointmentId") Long appointmentId) {
        return ResponseEntity.ok(paymentService.checkPaymentStatus(appointmentId));
    }

    @PostMapping("/payos/webhook")
    public ResponseEntity<ApiMessageResponse>
    handleWebhook(
            @RequestBody String body
    ) {
        paymentService.handlePayOSWebhook(body);

        return ResponseEntity.ok(
                new ApiMessageResponse(
                        "Webhook xử lý thành công!"
                )
        );
    }
}
