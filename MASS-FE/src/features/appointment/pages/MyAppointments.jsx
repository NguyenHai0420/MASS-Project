import React, { useState, useEffect } from 'react';
import { Container, Card, Badge, Button, Spinner, Alert, Modal, Table } from 'react-bootstrap';
import { useLocation, useNavigate } from 'react-router-dom';
import patientService from '../../patient/services/patientService';

const STATUS_MAPPING = {
  PENDING_PAYMENT: { text: "Chờ thanh toán", bg: "warning" },
  PAID: { text: "Đã thanh toán", bg: "success" },
  CANCELED: { text: "Đã hủy", bg: "danger" }
};

const MyAppointments = () => {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const location = useLocation();
  const navigate = useNavigate();
  const [alertMsg, setAlertMsg] = useState(location.state?.message || '');

  const [showCancelModal, setShowCancelModal] = useState(false);
  const [selectedAppt, setSelectedAppt] = useState(null);
  const [isProcessing, setIsProcessing] = useState(false);

  useEffect(() => {
    fetchAppointments();
  }, []);

  const fetchAppointments = async () => {
    setLoading(true);
    try {
      const response = await patientService.getMyAppointments();
      setAppointments(response.data);
    } catch (err) {
      console.error("Failed to fetch appointments", err);
      setError("Không thể tải danh sách lịch khám. Vui lòng thử lại sau.");
      setAppointments([]);
    } finally {
      setLoading(false);
    }
  };

  const handleCancelClick = (appt) => {
    setSelectedAppt(appt);
    setShowCancelModal(true);
  };

  const confirmCancel = async () => {
    setIsProcessing(true);
    try {
      await patientService.cancelAppointment(selectedAppt.id);
      setAlertMsg("Hủy lịch thành công!");
      fetchAppointments();
    } catch (err) {
      console.error("Cancel failed", err);
      setAlertMsg("Hủy lịch thất bại. Vui lòng thử lại sau.");
    } finally {
      setIsProcessing(false);
      setShowCancelModal(false);
    }
  };


  const handleReschedule = (appt) => {
    if (appt.doctorId) {
      navigate(`/doctors/${appt.doctorId}`, { state: { isReschedule: true, oldAppointmentId: appt.id } });
    } else {
      // Fallback if doctorId is somehow missing
      navigate('/doctors');
    }
  };

  return (
    <>
      <Container className="py-4">
        <h2 className="mb-4">Lịch khám của tôi</h2>

        {alertMsg && <Alert variant="success" onClose={() => setAlertMsg('')} dismissible>{alertMsg}</Alert>}
        {error && <Alert variant="danger">{error}</Alert>}

        {loading ? (
          <div className="text-center py-5">
            <Spinner animation="border" variant="primary" />
          </div>
        ) : (
          <Card className="shadow-sm">
            <Card.Body>
              {appointments.length === 0 ? (
                <Alert variant="info" className="mb-0">Bạn chưa có lịch khám nào.</Alert>
              ) : (
                <div className="table-responsive">
                  <Table hover className="align-middle">
                    <thead className="table-light">
                      <tr>
                        <th>Ngày khám</th>
                        <th>Giờ</th>
                        <th>Bác sĩ</th>
                        <th>Chuyên khoa</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                      </tr>
                    </thead>
                    <tbody>
                      {appointments.map(appt => {
                        const statusInfo = STATUS_MAPPING[appt.status] || { text: appt.status, bg: "secondary" };
                        return (
                          <tr key={appt.id}>
                            <td><strong>{appt.date}</strong></td>
                            <td>{appt.time}</td>
                            <td>{appt.doctorName}</td>
                            <td>{appt.specialty}</td>
                            <td>
                              <Badge bg={statusInfo.bg}>{statusInfo.text}</Badge>
                            </td>
                            <td>
                              {appt.status !== 'CANCELED' && (
                                <div className="d-flex gap-2">
                                  <Button variant="outline-primary" size="sm" onClick={() => handleReschedule(appt)}>Dời lịch</Button>
                                  <Button variant="outline-danger" size="sm" onClick={() => handleCancelClick(appt)}>Hủy</Button>
                                </div>
                              )}
                            </td>
                          </tr>
                        );
                      })}
                    </tbody>
                  </Table>
                </div>
              )}
            </Card.Body>
          </Card>
        )}
      </Container>

      {/* Cancel Confirmation Modal */}
      <Modal show={showCancelModal} onHide={() => !isProcessing && setShowCancelModal(false)} centered>
        <Modal.Header closeButton={!isProcessing}>
          <Modal.Title>Xác nhận hủy lịch</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Bạn có chắc chắn muốn hủy lịch khám với <strong>{selectedAppt?.doctorName}</strong> vào lúc <strong>{selectedAppt?.time} ngày {selectedAppt?.date}</strong> không?
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowCancelModal(false)} disabled={isProcessing}>
            Không
          </Button>
          <Button variant="danger" onClick={confirmCancel} disabled={isProcessing}>
            {isProcessing ? <Spinner size="sm" animation="border" /> : 'Xác nhận hủy'}
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default MyAppointments;
