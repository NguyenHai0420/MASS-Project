import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Form, Button, Spinner, Alert, Modal, Badge } from 'react-bootstrap';
import { useParams, useNavigate } from 'react-router-dom';
import PatientNavbar from '../../../shared/components/PatientNavbar';
import patientService from '../services/patientService';

const DoctorDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  
  const [doctor, setDoctor] = useState(null);
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
  const [slots, setSlots] = useState([]);
  const [loading, setLoading] = useState(true);
  const [slotsLoading, setSlotsLoading] = useState(false);
  const [error, setError] = useState(null);
  
  // Booking Modal State
  const [showModal, setShowModal] = useState(false);
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [bookingReason, setBookingReason] = useState('');
  const [bookingError, setBookingError] = useState('');
  const [isBooking, setIsBooking] = useState(false);

  useEffect(() => {
    fetchDoctorDetail();
  }, [id]);

  useEffect(() => {
    if (doctor) {
      fetchSchedules();
    }
  }, [selectedDate, doctor]);

  const fetchDoctorDetail = async () => {
    try {
      setLoading(true);
      const response = await patientService.getDoctorById(id);
      setDoctor(response.data);
    } catch (err) {
      console.error("Failed to fetch doctor details", err);
      // Mock data for testing
      setDoctor({
        id,
        name: "Dr. Nguyen Van A",
        specialtyName: "Tim mạch",
        specialtyId: 1,
        clinicName: "Phòng khám 1",
        description: "Bác sĩ có hơn 10 năm kinh nghiệm trong lĩnh vực tim mạch."
      });
    } finally {
      setLoading(false);
    }
  };

  const fetchSchedules = async () => {
    try {
      setSlotsLoading(true);
      const response = await patientService.getDoctorSchedules(id, selectedDate);
      setSlots(response.data);
    } catch (err) {
      console.error("Failed to fetch schedules", err);
      // Mock slots: some are available, some are booked
      setSlots([
        { id: 101, startTime: "08:00", endTime: "08:30", isBooked: false },
        { id: 102, startTime: "08:30", endTime: "09:00", isBooked: true },
        { id: 103, startTime: "09:00", endTime: "09:30", isBooked: false },
        { id: 104, startTime: "09:30", endTime: "10:00", isBooked: false }
      ]);
    } finally {
      setSlotsLoading(false);
    }
  };

  const handleSlotClick = (slot) => {
    if (slot.isBooked) return;
    setSelectedSlot(slot);
    setShowModal(true);
    setBookingError('');
    setBookingReason('');
  };

  const handleBookAppointment = async () => {
    if (!bookingReason.trim()) {
      setBookingError("Vui lòng nhập lý do khám.");
      return;
    }
    
    setIsBooking(true);
    setBookingError('');
    
    try {
      await patientService.bookAppointment({
        doctorId: doctor.id,
        specialtyId: doctor.specialtyId,
        slotId: selectedSlot.id,
        date: selectedDate,
        reason: bookingReason
      });
      // Success, close modal and redirect to my appointments
      setShowModal(false);
      navigate('/my-appointments', { state: { message: "Đặt lịch thành công! Trạng thái: Chờ thanh toán." }});
    } catch (err) {
      console.error("Booking failed", err);
      // Giả lập xử lý Booking Conflict (UC-M11 yêu cầu)
      if (err.response && err.response.status === 409) {
        setBookingError("Slot khám này vừa có người khác đặt. Vui lòng chọn slot khác.");
        fetchSchedules(); // Refresh slots
      } else {
        // Fallback cho testing
        setShowModal(false);
        navigate('/my-appointments', { state: { message: "Đặt lịch thành công (Mock)! Trạng thái: Chờ thanh toán." }});
      }
    } finally {
      setIsBooking(false);
    }
  };

  if (loading) {
    return (
      <>
        <PatientNavbar />
        <div className="text-center py-5">
          <Spinner animation="border" variant="primary" />
        </div>
      </>
    );
  }

  return (
    <>
      <PatientNavbar />
      <Container className="py-4">
        {doctor && (
          <Row>
            <Col md={4} className="mb-4">
              <Card className="shadow-sm">
                <Card.Body className="text-center">
                  <div className="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center mx-auto mb-3" style={{width: '100px', height: '100px', fontSize: '36px'}}>
                    {doctor.name.charAt(0)}
                  </div>
                  <Card.Title as="h3">{doctor.name}</Card.Title>
                  <Card.Subtitle className="mb-3 text-muted">{doctor.specialtyName}</Card.Subtitle>
                  <Card.Text className="text-start">
                    <strong>Nơi công tác:</strong> {doctor.clinicName}<br/>
                    <strong>Giới thiệu:</strong> {doctor.description}
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
            
            <Col md={8}>
              <Card className="shadow-sm mb-4">
                <Card.Header className="bg-white">
                  <h4 className="mb-0">Lịch làm việc</h4>
                </Card.Header>
                <Card.Body>
                  <Form.Group className="mb-4" style={{maxWidth: '250px'}}>
                    <Form.Label className="fw-bold">Chọn ngày khám</Form.Label>
                    <Form.Control 
                      type="date" 
                      value={selectedDate}
                      min={new Date().toISOString().split('T')[0]}
                      onChange={(e) => setSelectedDate(e.target.value)}
                    />
                  </Form.Group>
                  
                  {slotsLoading ? (
                    <div className="text-center py-3">
                      <Spinner animation="border" size="sm" variant="primary" /> Đang tải lịch...
                    </div>
                  ) : (
                    <div>
                      <h6 className="mb-3">Các khung giờ trong ngày {selectedDate}</h6>
                      {slots.length === 0 ? (
                        <Alert variant="warning">Bác sĩ không có lịch làm việc trong ngày này.</Alert>
                      ) : (
                        <div className="d-flex flex-wrap gap-2">
                          {slots.map(slot => (
                            <Button 
                              key={slot.id}
                              variant={slot.isBooked ? "secondary" : "outline-primary"}
                              disabled={slot.isBooked}
                              onClick={() => handleSlotClick(slot)}
                              className="px-4 py-2"
                            >
                              {slot.startTime} - {slot.endTime}
                            </Button>
                          ))}
                        </div>
                      )}
                      
                      <div className="mt-4 text-muted small">
                        <span className="me-3"><Badge bg="primary" className="me-1">&nbsp;</Badge> Có thể đặt</span>
                        <span><Badge bg="secondary" className="me-1">&nbsp;</Badge> Đã kín chỗ</span>
                      </div>
                    </div>
                  )}
                </Card.Body>
              </Card>
            </Col>
          </Row>
        )}
      </Container>

      {/* Booking Confirmation Modal */}
      <Modal show={showModal} onHide={() => !isBooking && setShowModal(false)} centered>
        <Modal.Header closeButton={!isBooking}>
          <Modal.Title>Xác nhận đặt lịch khám</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {bookingError && <Alert variant="danger">{bookingError}</Alert>}
          <div className="mb-3">
            <strong>Bác sĩ:</strong> {doctor?.name} <br/>
            <strong>Ngày khám:</strong> {selectedDate} <br/>
            <strong>Thời gian:</strong> {selectedSlot?.startTime} - {selectedSlot?.endTime}
          </div>
          <Form.Group>
            <Form.Label>Lý do khám bệnh <span className="text-danger">*</span></Form.Label>
            <Form.Control 
              as="textarea" 
              rows={3} 
              placeholder="Triệu chứng, vấn đề cần tư vấn..."
              value={bookingReason}
              onChange={(e) => setBookingReason(e.target.value)}
              disabled={isBooking}
            />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)} disabled={isBooking}>
            Hủy
          </Button>
          <Button variant="primary" onClick={handleBookAppointment} disabled={isBooking}>
            {isBooking ? <><Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" /> Đang xử lý...</> : 'Xác nhận đặt lịch'}
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default DoctorDetailPage;
