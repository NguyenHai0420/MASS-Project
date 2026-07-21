import React, { useState, useEffect } from "react";
import { Container, Row, Col, Card, Table, Badge, Spinner } from "react-bootstrap";
import BaseProfileForm from "../components/BaseProfileForm";
import doctorService from "../../doctor/services/doctorService";

const DoctorProfile = () => {
  const [schedules, setSchedules] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchSchedules();
  }, []);

  const fetchSchedules = async () => {
    try {
      setLoading(true);
      const data = await doctorService.getMySchedules();
      setSchedules(data || []);
    } catch (error) {
      console.error("Lỗi tải lịch làm việc:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container className="py-5">
      <Row>
        {/* Left Column: Base Profile Form */}
        <Col md={7}>
          <BaseProfileForm />
        </Col>

        {/* Right Column: Doctor Specific Info (Specialty & Schedule) */}
        <Col md={5}>
          <Card className="shadow-sm border-0 bg-light mb-4">
            <Card.Header className="bg-white border-0 pt-4 pb-0">
              <h4 className="fw-bold mb-0">Professional Info</h4>
            </Card.Header>
            <Card.Body className="p-4 text-center text-muted">
              <i className="bi bi-mortarboard mb-2" style={{ fontSize: "2rem" }}></i>
              <p>Specialty, Experience, and Degree details will be managed here.</p>
            </Card.Body>
          </Card>

          <Card className="shadow-sm border-0 bg-light">
            <Card.Header className="bg-white border-0 pt-4 pb-0">
              <h4 className="fw-bold mb-0">Work Schedule</h4>
            </Card.Header>
            <Card.Body className="p-3">
              {loading ? (
                <div className="text-center py-3">
                  <Spinner animation="border" size="sm" />
                </div>
              ) : schedules.length > 0 ? (
                <Table striped bordered hover size="sm" responsive className="mb-0 text-center" style={{ fontSize: 13 }}>
                  <thead>
                    <tr>
                      <th>Ngày</th>
                      <th>Khung giờ</th>
                      <th>Trạng thái</th>
                    </tr>
                  </thead>
                  <tbody>
                    {schedules.map((item) => (
                      <tr key={item.id}>
                        <td>{item.date}</td>
                        <td>{item.startTime?.substring(0, 5)} - {item.endTime?.substring(0, 5)}</td>
                        <td>
                          {item.available ? (
                            <Badge bg="success">Còn trống</Badge>
                          ) : (
                            <Badge bg="secondary">Đã đặt</Badge>
                          )}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              ) : (
                <div className="text-center py-3 text-muted">
                  <i className="bi bi-clock-history mb-2" style={{ fontSize: "2rem", display: "block" }}></i>
                  <p className="mb-0" style={{ fontSize: 13 }}>Chưa có lịch làm việc nào được phân công.</p>
                </div>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default DoctorProfile;
