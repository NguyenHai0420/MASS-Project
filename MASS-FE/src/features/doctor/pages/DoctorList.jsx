import React from "react";
import { Container } from "react-bootstrap";

const DoctorList = () => {
  return (
    <Container className="py-5 text-center">
      <h2 className="fw-bold mb-4">Our Doctors</h2>
      <div className="p-5 bg-light rounded-4 border border-dashed">
        <i className="bi bi-person-lines-fill text-muted mb-3 d-block" style={{ fontSize: "3rem" }}></i>
        <h4 className="text-muted">Under Construction</h4>
        <p className="text-muted">This page is reserved for the Doctor Listing module.</p>
        <p className="text-secondary small">(Uyên đẩy danh sách bac sĩ)</p>
      </div>
    </Container>
  );
};

export default DoctorList;
