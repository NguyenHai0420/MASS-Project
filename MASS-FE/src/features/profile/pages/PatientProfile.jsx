import React from "react";
import { Container, Row, Col, Card } from "react-bootstrap";
import BaseProfileForm from "../components/BaseProfileForm";

const PatientProfile = () => {
  return (
    <Container className="py-5">
      <Row>
        {/* Left Column: Base Profile Form */}
        <Col md={7}>
          <BaseProfileForm />
        </Col>

        {/* Right Column: Patient Specific Info (Appointments & Records) */}
        <Col md={5}>
          <Card className="shadow-sm border-0 bg-light mb-4">
            <Card.Header className="bg-white border-0 pt-4 pb-0">
              <h4 className="fw-bold mb-0">My Appointments</h4>
            </Card.Header>
            <Card.Body className="p-4 text-center text-muted">
              <i className="bi bi-calendar2-x mb-2" style={{ fontSize: "2rem" }}></i>
              <p>No upcoming appointments found.</p>
              {/* Uyên can replace this with a list of appointments later */}
            </Card.Body>
          </Card>

          <Card className="shadow-sm border-0 bg-light">
            <Card.Header className="bg-white border-0 pt-4 pb-0">
              <h4 className="fw-bold mb-0">Medical Records</h4>
            </Card.Header>
            <Card.Body className="p-4 text-center text-muted">
              <i className="bi bi-file-earmark-medical mb-2" style={{ fontSize: "2rem" }}></i>
              <p>Your medical history will appear here.</p>
              {/* Uyên can replace this with medical records later */}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default PatientProfile;
