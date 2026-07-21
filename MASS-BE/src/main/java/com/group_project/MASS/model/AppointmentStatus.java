package com.group_project.MASS.model;

public enum AppointmentStatus {
    PENDING_PAYMENT, // Appointment is created but not yet paid
    WAITING_CHECK_IN, // Appointment is paid but the patient has not checked in yet
    WAITING_FOR_TURN, // Patient has checked in but is waiting for their turn
    CANCELLED, // Appointment is cancelled by the patient or the clinic
    COMPLETED, // Appointment is completed successfully
    NO_SHOW,
    PENDING// Patient did not show up for the appointment
}
