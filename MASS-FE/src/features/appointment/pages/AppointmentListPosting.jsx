import React from 'react';
import { Container, Row, Col, Spinner } from 'react-bootstrap';
import AppointmentCard from '../components/AppointmentCard';

const AppointmentListPosting = ({
  appointments,
  isLoading,
  totalElements,
  currentPage,
  totalPages,
  onPageChange,
  onWalkInClick,
  onViewDetail,
  onCheckIn,
  onPayment,
  onCancel,
}) => {
  return (
    <Container className="py-5">
      {/* Section header */}
      <div className="appt-section-header">
        <div className="d-flex align-items-center gap-2">
          <h3 className="m-0 fw-bold" style={{ color: '#0f172a' }}>
            Danh sách lịch hẹn
          </h3>
          <span className="appt-count">{totalElements ?? appointments.length} lịch hẹn</span>
        </div>

        <button className="btn text-white appt-add-btn" onClick={onWalkInClick}>
          + Thêm Walk-in
        </button>
      </div>

      {/* Loading state */}
      {isLoading && (
        <div className="text-center py-5">
          <Spinner animation="border" style={{ color: '#1b6ca8', width: 48, height: 48 }} />
          <p className="mt-3 text-muted">Đang tải danh sách lịch hẹn...</p>
        </div>
      )}

      {/* Empty state */}
      {!isLoading && appointments.length === 0 && (
        <div className="appt-empty">
          <div className="appt-empty-icon">📅</div>
          <h5 className="fw-bold">Không tìm thấy lịch hẹn</h5>
          <p className="text-muted">Hãy thử đổi từ khóa hoặc bộ lọc trạng thái.</p>
          <button className="btn text-white appt-add-btn" onClick={onWalkInClick}>
            + Tạo lịch Walk-in
          </button>
        </div>
      )}

      {/* Appointment table */}
      {!isLoading && appointments.length > 0 && (
        <div className="table-responsive">
          <table className="table table-hover align-middle">
            <thead className="table-light">
              <tr>
                <th>ID</th>
                <th>Bệnh nhân</th>
                <th>Bác sĩ</th>
                <th>Ngày</th>
                <th>Giờ</th>
                <th>Trạng thái</th>
                <th className="text-end">Thao tác</th>
              </tr>
            </thead>
            <tbody>
              {appointments.map((appointment) => {
                let badgeClass = 'bg-secondary';
                let statusLabel = 'Không xác định';
                switch (appointment.appointmentStatus) {
                  case 'PENDING_PAYMENT': badgeClass = 'bg-warning text-dark'; statusLabel = 'Chờ thanh toán'; break;
                  case 'WAITING_CHECK_IN': badgeClass = 'bg-primary'; statusLabel = 'Chờ check-in'; break;
                  case 'WAITING_FOR_TURN': badgeClass = 'bg-info text-dark'; statusLabel = 'Chờ đến lượt'; break;
                  case 'COMPLETED': badgeClass = 'bg-success'; statusLabel = 'Hoàn thành'; break;
                  case 'CANCELLED': badgeClass = 'bg-danger'; statusLabel = 'Đã hủy'; break;
                  case 'NO_SHOW': badgeClass = 'bg-dark'; statusLabel = 'Không đến khám'; break;
                }

                return (
                  <tr key={appointment.appointmentId}>
                    <td>#{appointment.appointmentId}</td>
                    <td>
                      <div className="fw-bold">{appointment.patientName}</div>
                      <div className="text-muted small">{appointment.patientPhone}</div>
                    </td>
                    <td>{appointment.doctorName}</td>
                    <td>{appointment.appointmentDate}</td>
                    <td>{appointment.appointmentTime}</td>
                    <td><span className={`badge ${badgeClass}`}>{statusLabel}</span></td>
                    <td className="text-end">
                      <button className="btn btn-sm btn-outline-primary me-2" onClick={() => onViewDetail?.(appointment)}>Chi tiết</button>
                      {appointment.appointmentStatus === 'PENDING_PAYMENT' && (
                        <button className="btn btn-sm btn-success me-2" onClick={() => onPayment?.(appointment)}>Thanh toán</button>
                      )}
                      {appointment.appointmentStatus === 'WAITING_CHECK_IN' && (
                        <button className="btn btn-sm btn-info me-2" onClick={() => onCheckIn?.(appointment)}>Check-in</button>
                      )}
                      {(appointment.appointmentStatus === 'PENDING_PAYMENT' || appointment.appointmentStatus === 'WAITING_CHECK_IN') && (
                        <button className="btn btn-sm btn-outline-danger" onClick={() => onCancel?.(appointment)}>Hủy</button>
                      )}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}

      {/* Pagination */}
      {!isLoading && totalPages > 0 && (
        <div className="appt-pagination mt-4">
          <button
            className="appt-page-btn"
            onClick={() => onPageChange(currentPage - 1)}
            disabled={currentPage === 0}
          >
            ‹
          </button>

          {Array.from({ length: totalPages }, (_, i) => (
            <button
              key={i}
              className={`appt-page-btn ${currentPage === i ? 'active' : ''}`}
              onClick={() => onPageChange(i)}
            >
              {i + 1}
            </button>
          ))}

          <button
            className="appt-page-btn"
            onClick={() => onPageChange(currentPage + 1)}
            disabled={currentPage === totalPages - 1 || totalPages === 0}
          >
            ›
          </button>
        </div>
      )}
    </Container>
  );
};

export default AppointmentListPosting;
