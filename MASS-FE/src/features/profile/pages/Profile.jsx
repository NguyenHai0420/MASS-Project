import React, { useContext } from "react";
import { AuthContext } from "../../../app/providers/AuthProvider";
import PatientProfile from "./PatientProfile";
import DoctorProfile from "./DoctorProfile";
import BaseProfileForm from "../components/BaseProfileForm";
import { Container, Row, Col } from "react-bootstrap";

const Profile = () => {
  const { user } = useContext(AuthContext);

  if (!user) {
    return null; // or a loading spinner
  }

  // Load different components based on role
  if (user.role === "ROLE_PATIENT") {
    return <PatientProfile />;
  }

  if (user.role === "ROLE_DOCTOR") {
    return <DoctorProfile />;
  }

  // Fallback for Receptionist or Admin if they ever visit this page
  return (
    <Container className="py-5">
      <Row className="justify-content-center">
        <Col md={8}>
          <BaseProfileForm />
        </Col>
      </Row>
    </Container>
  );
};

export default Profile;
