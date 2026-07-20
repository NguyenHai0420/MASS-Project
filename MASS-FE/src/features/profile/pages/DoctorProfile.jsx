import React from "react";
import { Container, Row, Col, Card } from "react-bootstrap";
import BaseProfileForm from "../components/BaseProfileForm";

const DoctorProfile = () => {
  return (
    <Container className="py-5">
      <Row>
        {/* Left Column: Base Profile Form */}
        <Col md={7}>
          <BaseProfileForm />
        </Col>

        {/* Right Column: Doctor Specific Info (Specialty & Schedule) */}
        <Col md={5}>
          <Card className="shadow-sm border-0 bg-light mb-4">
            <Card.Header className="bg-white border-0 pt-4 pb-0">
              <h4 className="fw-bold mb-0">Professional Info</h4>
            </Card.Header>
            <Card.Body className="p-4 text-center text-muted">
              <i className="bi bi-mortarboard mb-2" style={{ fontSize: "2rem" }}></i>
              <p>Specialty, Experience, and Degree details will be managed here.</p>
              {/* Phong can build the professional info form here */}
            </Card.Body>
          </Card>

          <Card className="shadow-sm border-0 bg-light">
            <Card.Header className="bg-white border-0 pt-4 pb-0">
              <h4 className="fw-bold mb-0">Work Schedule</h4>
            </Card.Header>
            <Card.Body className="p-4 text-center text-muted">
              <i className="bi bi-clock-history mb-2" style={{ fontSize: "2rem" }}></i>
              <p>Manage your weekly availability and slots.</p>
              {/* Phong can build the schedule manager here */}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default DoctorProfile;
