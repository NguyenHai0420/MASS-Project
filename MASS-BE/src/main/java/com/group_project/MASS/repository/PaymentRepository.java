package com.group_project.MASS.repository;

import com.group_project.MASS.model.Payment;
import com.group_project.MASS.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Find a payment by appointment ID
    Optional<Payment> findByAppointmentId(Long appointmentId);

    // Find a payment by order code
    Optional<Payment> findByOrderCode(Long orderCode);

    // Check if a payment exists by appointment ID
    boolean existsByAppointmentId(Long appointmentId);

    boolean existsByOrderCode(Long orderCode);

    // Find all payments by payment status, ordered by creation date in descending order
    List<Payment> findByPaymentStatusOrderByCreatedAtDesc(PaymentStatus paymentStatus);
}
