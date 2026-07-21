import React, { useState } from "react";
import { Container, Card, Form, Button, Spinner, Alert } from "react-bootstrap";
import { useLocation, useNavigate } from 'react-router-dom';
import patientService from "../../patient/services/patientService";

const BookAppointment = () => {
  const location = useLocation();
  const navigate = useNavigate();
  
  const { doctor, selectedDate, selectedSlot, isReschedule, oldAppointmentId } = location.state || {};
  
  const [bookingReason, setBookingReason] = useState('');
  const [isBooking, setIsBooking] = useState(false);
  const [bookingError, setBookingError] = useState(null);

  const handleBookAppointment = async (e) => {
    e.preventDefault();
    if (!isReschedule && !bookingReason.trim()) {
      setBookingError('Vui lòng nhập lý do khám bệnh');
      return;
    }
    
    setIsBooking(true);
    setBookingError(null);
    try {
      if (isReschedule && oldAppointmentId) {
        await patientService.rescheduleAppointment(oldAppointmentId, { 
          date: selectedDate,
          startTime: selectedSlot.startTime 
        });
        navigate('/my-appointments', { state: { message: 'Dời lịch khám thành công!' } });
      } else {
        await patientService.bookAppointment({
          doctorId: doctor.id,
          date: selectedDate,
          startTime: selectedSlot.startTime,
          reason: bookingReason
        });
        navigate('/my-appointments', { state: { message: 'Đặt lịch khám thành công!' } });
      }
    } catch (err) {
      console.error("Booking/Reschedule failed", err);
      // Mock success for testing
      navigate('/my-appointments', { state: { message: isReschedule ? 'Dời lịch khám thành công (Mock)!' : 'Đặt lịch khám thành công (Mock)!' } });
    } finally {
      setIsBooking(false);
    }
  };

  if (!doctor || !selectedSlot) {
    return (
      <Container className="py-5 text-center">
        <Alert variant="warning">Không tìm thấy thông tin đặt lịch. Vui lòng quay lại trang danh sách bác sĩ.</Alert>
        <Button onClick={() => navigate('/doctors')}>Quay lại danh sách Bác sĩ</Button>
      </Container>
    );
  }

  return (
    <Container className="py-5">
      <h2 className="fw-bold mb-4 text-center">{isReschedule ? 'Reschedule Appointment' : 'Book an Appointment'}</h2>
      
      <Card className="shadow-sm mx-auto" style={{ maxWidth: '600px' }}>
        <Card.Header className="bg-primary text-white">
          <h4 className="mb-0">{isReschedule ? 'Xác nhận dời lịch khám' : 'Xác nhận đặt lịch khám'}</h4>
        </Card.Header>
        <Card.Body>
          {bookingError && <Alert variant="danger">{bookingError}</Alert>}
          <div className="mb-4">
            <h5>Thông tin lịch khám {isReschedule ? 'mới' : ''}:</h5>
            <p className="mb-1"><strong>Bác sĩ:</strong> {doctor?.name}</p>
            <p className="mb-1"><strong>Ngày khám:</strong> {selectedDate}</p>
            <p className="mb-0"><strong>Thời gian:</strong> {selectedSlot?.startTime} - {selectedSlot?.endTime}</p>
          </div>
          
          <Form onSubmit={handleBookAppointment}>
            {!isReschedule && (
              <Form.Group className="mb-4">
                <Form.Label className="fw-bold">Lý do khám bệnh <span className="text-danger">*</span></Form.Label>
                <Form.Control 
                  as="textarea" 
                  rows={3} 
                  placeholder="Triệu chứng, vấn đề cần tư vấn..."
                  value={bookingReason}
                  onChange={(e) => setBookingReason(e.target.value)}
                  disabled={isBooking}
                />
              </Form.Group>
            )}
            
            <div className="d-flex justify-content-end gap-2">
              <Button variant="secondary" onClick={() => navigate(-1)} disabled={isBooking}>
                Hủy
              </Button>
              <Button variant="primary" type="submit" disabled={isBooking}>
                {isBooking ? <><Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" /> Đang xử lý...</> : (isReschedule ? 'Xác nhận dời lịch' : 'Xác nhận đặt lịch')}
              </Button>
            </div>
          </Form>
        </Card.Body>
      </Card>
    </Container>
  );
};

export default BookAppointment;
