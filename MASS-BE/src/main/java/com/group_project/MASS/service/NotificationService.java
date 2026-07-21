package com.group_project.MASS.service;

import com.group_project.MASS.model.Appointment;
import com.group_project.MASS.model.Payment;

public interface NotificationService {
    void createPaymentSuccessNotification(Appointment appointment, Payment payment);
}
