import { Container } from "react-bootstrap";

const Footer = () => {
  return (
    <footer className="bg-dark text-white text-center py-4 mt-auto">
      <Container>
        <p className="mb-0">&copy; {new Date().getFullYear()} MASS Clinic. All rights reserved.</p>
        <small className="text-muted">Medical Appointment Scheduling System</small>
      </Container>
    </footer>
  );
};

export default Footer;
