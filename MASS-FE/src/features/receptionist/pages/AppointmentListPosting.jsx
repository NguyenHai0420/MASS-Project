import { Button, Col, Container, Row } from 'react-bootstrap';
import AppointmentCard from '../components/AppointmentCard';

const AppointmentListPosting = ({ appointments }) => {
  return (
    <Container className="py-5">
      <div className="d-flex justify-content-between align-items-center flex-wrap gap-3 mb-4">
        <div className="d-flex align-items-center gap-2">
          <h3 className="m-0 fw-bold text-dark">Booked Appointments</h3>
          <span className="appointment-count">{appointments.length} appointments</span>
        </div>

        <Button variant="outline-primary" className="rounded-pill fw-semibold px-4">
          + Add Walk-in Appointment
        </Button>
      </div>

      {appointments.length === 0 ? (
        <div className="appointment-empty text-center p-5">
          <h5 className="fw-bold text-dark">Không tìm thấy lịch hẹn</h5>
          <p className="text-secondary">Hãy thử đổi từ khóa hoặc bộ lọc trạng thái.</p>
        </div>
      ) : (
        <Row className="g-4">
          {appointments.map((appointment) => (
            <Col lg={6} key={appointment.id}>
              <AppointmentCard appointment={appointment} />
            </Col>
          ))}
        </Row>
      )}
    </Container>
  );
};

export default AppointmentListPosting;
