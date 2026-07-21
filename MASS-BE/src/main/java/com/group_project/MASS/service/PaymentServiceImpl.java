package com.group_project.MASS.service;

import com.group_project.MASS.dto.response.PaymentLinkResponse;
import com.group_project.MASS.model.*;
import com.group_project.MASS.repository.AppointmentRepository;
import com.group_project.MASS.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.webhooks.WebhookData;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService{
    private final PayOS payOS;

    private final AppointmentRepository appointmentRepository;
    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;

    @Value("${app.frontend.payment-success-url}")
    private String successUrl;

    @Value("${app.frontend.payment-cancel-url}")
    private String cancelUrl;

    @Override
    public PaymentLinkResponse createPaymentLink(
            Long appointmentId
    ) {
        if (appointmentId == null) {
            throw new IllegalArgumentException(
                    "Appointment ID không được để trống"
            );
        }

        Appointment appointment = appointmentRepository
                .findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy appointment với ID: "
                                + appointmentId
                ));

        if (appointment.getStatus()
                != AppointmentStatus.PENDING_PAYMENT) {
            throw new IllegalStateException(
                    "Appointment không ở trạng thái chờ thanh toán"
            );
        }

        Payment existingPayment = paymentRepository
                .findByAppointmentId(appointmentId)
                .orElse(null);

        if (existingPayment != null
                && existingPayment.getPaymentStatus()
                == PaymentStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Appointment đã được thanh toán thành công!"
            );
        }

        long orderCode = generateOrderCode();

        /*
         * Thay bằng giá khám thực tế của hệ thống.
         * Đổi giá trị sang BigDecimal vì entity dùng BigDecimal
         */
        BigDecimal amount = BigDecimal.valueOf(200000);

        Payment payment;

        if (existingPayment == null) {
            payment = Payment.builder()
                    .appointment(appointment)
                    .amount(amount)
                    .paymentMethod(PaymentMethod.PAYOS)
                    .paymentStatus(PaymentStatus.PENDING)
                    .orderCode(orderCode)
                    .build();
        } else {
            existingPayment.setAmount(amount);
            existingPayment.setPaymentMethod(PaymentMethod.PAYOS);
            existingPayment.setPaymentStatus(
                    PaymentStatus.PENDING
            );
            existingPayment.setOrderCode(orderCode);

            payment = existingPayment;
        }

        payment = paymentRepository.save(payment);

        String description =
                "MASS " + appointment.getId();

        try {
            CreatePaymentLinkRequest request =
                    CreatePaymentLinkRequest.builder()
                            .orderCode(orderCode)
                            .amount(amount.longValue()) // PayOS SDK cần kiểu long/int
                            .description(description)
                            .returnUrl(
                                    successUrl
                                            + "?appointmentId="
                                            + appointmentId
                            )
                            .cancelUrl(
                                    cancelUrl
                                            + "?appointmentId="
                                            + appointmentId
                            )
                            .build();

            CreatePaymentLinkResponse response =
                    payOS.paymentRequests()
                            .create(request);

            return PaymentLinkResponse.builder()
                    .appointmentId(appointmentId)
                    .paymentId(payment.getId())
                    .orderCode(orderCode)
                    .checkoutUrl(response.getCheckoutUrl())
                    .qrCode(response.getQrCode())
                    .paymentStatus(
                            payment.getPaymentStatus()
                    )
                    .build();

        } catch (Exception exception) {
            payment.setPaymentStatus(
                    PaymentStatus.FAILED
            );

            paymentRepository.save(payment);

            throw new IllegalStateException(
                    "Không thể tạo link thanh toán PayOS: "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    @Override
    public void handlePayOSWebhook(
            String webhookBody
    ) {
        final WebhookData webhookData;

        try {
            /*
             * Bắt buộc xác minh webhook trước khi cập nhật database.
             */
            webhookData = payOS.webhooks()
                    .verify(webhookBody);

        } catch (Exception exception) {
            throw new IllegalArgumentException(
                    "Webhook PayOS không hợp lệ",
                    exception
            );
        }

        Long orderCode = webhookData.getOrderCode();

        Payment payment = paymentRepository
                .findByOrderCode(orderCode)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy payment với orderCode: "
                                + orderCode
                ));

        /*
         * Webhook có thể được PayOS gửi lại nhiều lần.
         * Xử lý idempotent để không gửi notification/email trùng.
         */
        if (payment.getPaymentStatus()
                == PaymentStatus.COMPLETED) {
            return;
        }

        Appointment appointment =
                payment.getAppointment();

        payment.setPaymentStatus(
                PaymentStatus.COMPLETED
        );

        payment.setTransactionId(
                webhookData.getReference()
        );

        payment.setPaymentDate(
                LocalDateTime.now()
        );

        appointment.setStatus(
                AppointmentStatus.WAITING_CHECK_IN
        );

        paymentRepository.save(payment);
        appointmentRepository.save(appointment);

        notificationService
                .createPaymentSuccessNotification(
                        appointment,
                        payment
                );

        emailService.sendPaymentSuccessEmail(
                appointment,
                payment
        );
    }

    private long generateOrderCode() {
        long orderCode;

        do {
            orderCode =
                    System.currentTimeMillis()
                            % 1_000_000_000L;
        } while (
                paymentRepository.existsByOrderCode(
                        orderCode
                )
        );

        return orderCode;
    }
}
