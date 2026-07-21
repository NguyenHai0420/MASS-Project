package com.group_project.MASS.service;

import com.group_project.MASS.dto.response.PaymentLinkResponse;

public interface PaymentService {
    PaymentLinkResponse createPaymentLink(Long appointmentId);

    void handlePayOSWebhook(String webhookBody);
}
