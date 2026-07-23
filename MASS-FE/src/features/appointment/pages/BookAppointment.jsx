import React, { useState, useEffect } from "react";
import { Container, Card, Form, Button, Spinner, Alert } from "react-bootstrap";
import { useLocation, useNavigate } from 'react-router-dom';
import patientService from "../../patient/services/patientService";
import paymentService from "../../payment/services/payment.service";

const BookAppointment = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const { doctor, selectedDate, selectedSlot, isReschedule, oldAppointmentId } = location.state || {};

  const [bookingReason, setBookingReason] = useState('');
  const [isBooking, setIsBooking] = useState(false);
  const [bookingError, setBookingError] = useState(null);

  const [isBooked, setIsBooked] = useState(false);
  const [paymentData, setPaymentData] = useState(null);
  const [bookedAppointmentId, setBookedAppointmentId] = useState(null);

  useEffect(() => {
    let intervalId;
    if (isBooked && paymentData && bookedAppointmentId) {
      intervalId = setInterval(async () => {
        try {
          const res = await paymentService.checkStatus(bookedAppointmentId);
          if (res.data === 'WAITING_CHECK_IN' || res.data === 'WAITING_FOR_TURN' || res.data === 'COMPLETED') {
            clearInterval(intervalId);
            navigate('/my-appointments', { state: { message: 'Thanh toán và đặt lịch thành công!' } });
          }
        } catch (err) {
          console.error("Error polling payment status", err);
        }
      }, 3000);
    }
    return () => {
      if (intervalId) clearInterval(intervalId);
    };
  }, [isBooked, paymentData, bookedAppointmentId, navigate]);

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
        const res = await patientService.bookAppointment({
          doctorId: doctor.id,
          date: selectedDate,
          startTime: selectedSlot.startTime,
          reason: bookingReason
        });
        const appointmentId = res.data.id || res.data.appointmentId;
        const paymentRes = await paymentService.createPaymentLink(appointmentId);
        setPaymentData(paymentRes.data);
        setBookedAppointmentId(appointmentId);
        setIsBooked(true);
      }
    } catch (err) {
      console.error("Booking/Reschedule failed", err);
      setBookingError(err.response?.data?.message || err.message || 'Có lỗi xảy ra khi đặt lịch.');
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
          {isBooked ? (
            <div className="text-center">
              <h5 className="text-success mb-3">Đặt lịch thành công! Vui lòng thanh toán.</h5>
              <div className="mb-4 text-start p-3 bg-light rounded border">
                <p className="mb-1"><strong>Bác sĩ:</strong> {doctor?.name}</p>
                <p className="mb-1"><strong>Ngày khám:</strong> {selectedDate}</p>
                <p className="mb-1"><strong>Thời gian:</strong> {selectedSlot?.startTime} - {selectedSlot?.endTime}</p>
                <p className="mb-0"><strong>Lý do:</strong> {bookingReason}</p>
              </div>
              {paymentData?.qrCode && (
                <div className="appt-qr-wrapper mx-auto mb-3" style={{ width: 'fit-content' }}>
                  <img
                    src={`https://api.qrserver.com/v1/create-qr-code/?data=${encodeURIComponent(paymentData.qrCode)}&size=220x220`}
                    alt="QR Code thanh toán"
                    className="appt-qr-img img-fluid shadow-sm rounded"
                  />
                  <div className="mt-2 fw-semibold text-primary">
                    Quét QR để thanh toán qua PayOS
                  </div>
                  {paymentData?.amount && (
                    <div className="mt-2 text-danger fw-bold fs-5">
                      {new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(paymentData.amount)}
                    </div>
                  )}
                </div>
              )}
              <div className="text-muted small mt-3">
                <Spinner animation="grow" size="sm" className="me-2" variant="primary" />
                Hệ thống đang tự động kiểm tra trạng thái thanh toán...
              </div>
              <Button variant="outline-secondary" className="mt-4" onClick={() => navigate('/my-appointments')}>
                Xem danh sách lịch hẹn
              </Button>
            </div>
          ) : (
            <>
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
            </>
          )}
        </Card.Body>
      </Card>
    </Container>
  );
};

export default BookAppointment;
