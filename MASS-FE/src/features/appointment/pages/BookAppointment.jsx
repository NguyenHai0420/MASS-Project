import React from "react";
import { Container } from "react-bootstrap";

const BookAppointment = () => {
  return (
    <Container className="py-5 text-center">
      <h2 className="fw-bold mb-4">Book an Appointment</h2>
      <div className="p-5 bg-light rounded-4 border border-dashed">
        <i className="bi bi-tools text-muted mb-3 d-block" style={{ fontSize: "3rem" }}></i>
        <h4 className="text-muted">Under Construction</h4>
        <p className="text-muted">This page is reserved for the Appointment Booking module.</p>
        <p className="text-secondary small">(Uyên đẩy đặt lịch ở đây)</p>
      </div>
    </Container>
  );
};

export default BookAppointment;
