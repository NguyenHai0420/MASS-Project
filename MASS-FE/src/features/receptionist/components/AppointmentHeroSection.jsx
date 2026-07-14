import { Button, Col, Container, Form, Row } from 'react-bootstrap';

const AppointmentHeroSection = ({
  keyword,
  setKeyword,
  selectedStatus,
  setSelectedStatus,
  selectedPaymentStatus,
  setSelectedPaymentStatus,
  selectedSpecialty,
  setSelectedSpecialty,
  statuses,
  paymentStatuses,
  specialties,
  onReset,
}) => {
  return (
    <section className="appointment-hero text-white">
      <Container>
        <div className="appointment-hero-content text-center">
          <span className="appointment-eyebrow">Receptionist Module</span>
          <h1 className="fw-bold mb-3">Appointment List</h1>
          <p className="text-white-50 mb-4">
            Danh sách lịch hẹn bệnh nhân đã đặt, hỗ trợ tìm kiếm, lọc trạng thái và theo dõi thanh toán.
          </p>

          <div className="appointment-search-box mx-auto mb-4">
            <Form.Control
              type="text"
              className="appointment-search-input"
              placeholder="Tìm theo tên bệnh nhân, SĐT, mã lịch hẹn, bác sĩ..."
              value={keyword}
              onChange={(event) => setKeyword(event.target.value)}
            />
            <Button className="appointment-search-btn">Search</Button>
          </div>

          <Row className="g-3 justify-content-center">
            <Col md={3} sm={6}>
              <Form.Select
                value={selectedStatus}
                onChange={(event) => setSelectedStatus(event.target.value)}
              >
                {statuses.map((status) => (
                  <option key={status.value} value={status.value}>
                    {status.label}
                  </option>
                ))}
              </Form.Select>
            </Col>

            <Col md={3} sm={6}>
              <Form.Select
                value={selectedPaymentStatus}
                onChange={(event) => setSelectedPaymentStatus(event.target.value)}
              >
                {paymentStatuses.map((status) => (
                  <option key={status.value} value={status.value}>
                    {status.label}
                  </option>
                ))}
              </Form.Select>
            </Col>

            <Col md={3} sm={6}>
              <Form.Select
                value={selectedSpecialty}
                onChange={(event) => setSelectedSpecialty(event.target.value)}
              >
                <option value="ALL">Tất cả chuyên khoa</option>
                {specialties.map((specialty) => (
                  <option key={specialty} value={specialty}>
                    {specialty}
                  </option>
                ))}
              </Form.Select>
            </Col>

            <Col md="auto" sm={6}>
              <Button variant="light" className="w-100 fw-semibold" onClick={onReset}>
                Reset
              </Button>
            </Col>
          </Row>
        </div>
      </Container>
    </section>
  );
};

export default AppointmentHeroSection;
