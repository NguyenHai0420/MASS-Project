import { Badge, Button, Card } from 'react-bootstrap';

const statusConfig = {
  WAITING_CHECK_IN: { label: 'Chờ check-in', variant: 'warning', border: '#f59e0b' },
  WAITING_PAYMENT: { label: 'Chờ thanh toán', variant: 'info', border: '#0ea5e9' },
  CHECKED_IN: { label: 'Đã check-in', variant: 'primary', border: '#4f46e5' },
  COMPLETED: { label: 'Hoàn thành', variant: 'success', border: '#16a34a' },
  CANCELLED: { label: 'Đã hủy', variant: 'secondary', border: '#64748b' },
  NO_SHOW: { label: 'Không đến khám', variant: 'danger', border: '#dc2626' },
};

const paymentConfig = {
  PAID: { label: 'Đã thanh toán', variant: 'success' },
  UNPAID: { label: 'Chưa thanh toán', variant: 'danger' },
  REFUNDED: { label: 'Đã hoàn tiền', variant: 'secondary' },
};

const AppointmentCard = ({ appointment }) => {
  const status = statusConfig[appointment.status] || statusConfig.WAITING_CHECK_IN;
  const payment = paymentConfig[appointment.paymentStatus] || paymentConfig.UNPAID;

  return (
    <Card
      className="appointment-card h-100 shadow-sm"
      style={{ borderLeft: `6px solid ${status.border}` }}
    >
      <Card.Body className="p-4 d-flex flex-column">
        <div className="d-flex justify-content-between align-items-start gap-3 mb-3">
          <div>
            <span className="appointment-code">{appointment.id}</span>
            <h5 className="fw-bold text-dark mt-2 mb-1">{appointment.patientName}</h5>
            <p className="text-secondary small mb-0">{appointment.patientPhone}</p>
          </div>
          <Badge bg={status.variant} className="rounded-pill px-3 py-2">
            {status.label}
          </Badge>
        </div>

        <div className="appointment-meta mb-3">
          <span><strong>Chuyên Khoa: </strong>{appointment.specialty}</span>
          <span><strong>Tên: </strong>{appointment.doctorName}</span>
          <span><strong>Lịch Khám: </strong>{appointment.appointmentDate}</span>
          <span><strong>Thời Gian Cụ Thể: </strong>{appointment.appointmentTime}</span>
        </div>

        <p className="text-secondary small appointment-note flex-grow-1">Yêu cầu khám bệnh: {appointment.note}</p>

        <div className="d-flex flex-wrap gap-2 mb-3">
          <Badge bg={payment.variant} className="rounded-pill px-3 py-2">
            {payment.label}
          </Badge>
          <Badge bg={appointment.bookingType === 'WALK_IN' ? 'dark' : 'light'} text={appointment.bookingType === 'WALK_IN' ? 'light' : 'dark'} className="rounded-pill px-3 py-2 border">
            {appointment.bookingType === 'WALK_IN' ? 'Walk-in' : 'Online Booking'}
          </Badge>
        </div>

        <div className="d-flex gap-2 pt-3 border-top">
          <Button variant="outline-primary" size="sm" className="rounded-pill px-3">
            View Detail
          </Button>
          <Button variant="primary" size="sm" className="rounded-pill px-3">
            Check-in / Payment
          </Button>
        </div>
      </Card.Body>
    </Card>
  );
};

export default AppointmentCard;
