import { useState, useEffect } from "react";
import { Container, Table, Badge, Row, Col, Card } from "react-bootstrap";
import DashboardLayout from "../../../shared/components/DashboardLayout";
import doctorService from "../services/doctorService";

// ========================
// UC-M23 — Appointment Dashboard
// Trang chủ của Doctor
// Hiển thị các cuộc hẹn hôm nay
// ========================



// Hàm trả về màu badge theo trạng thái
function getStatusBadge(status) {
    switch (status) {
        case "CONFIRMED": return "primary";
        case "PENDING": return "warning";
        case "COMPLETED": return "success";
        case "CANCELLED": return "danger";
        default: return "secondary";
    }
}

export default function DoctorDashboardPage() {
    const [appointments, setAppointments] = useState([]);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const data = await doctorService.getMyAppointments();
            // Lọc các cuộc hẹn trong ngày hôm nay (YYYY-MM-DD)
            const todayStr = new Date().toLocaleDateString('en-CA');
            const todayAppointments = data.filter(a => a.scheduleDate === todayStr);
            setAppointments(todayAppointments);
        } catch (error) {
            console.error("Lỗi tải lịch hẹn:", error);
        }
    };

    // Thống kê nhanh
    const total = appointments.length;
    const pending = appointments.filter((a) => a.status === "PENDING").length;
    const completed = appointments.filter((a) => a.status === "COMPLETED").length;

    return (
        <DashboardLayout>
            <Container fluid>
                <h4 className="mb-4">📋 Dashboard Bác sĩ — Hôm nay</h4>

                {/* Thống kê nhanh */}
                <Row className="g-3 mb-4">
                    <Col md={4}>
                        <Card className="text-center border-primary">
                            <Card.Body>
                                <h3 className="text-primary">{total}</h3>
                                <Card.Text>Tổng cuộc hẹn</Card.Text>
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={4}>
                        <Card className="text-center border-warning">
                            <Card.Body>
                                <h3 className="text-warning">{pending}</h3>
                                <Card.Text>Chờ khám</Card.Text>
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={4}>
                        <Card className="text-center border-success">
                            <Card.Body>
                                <h3 className="text-success">{completed}</h3>
                                <Card.Text>Đã khám</Card.Text>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>

                {/* Danh sách hẹn hôm nay */}
                <h5>Danh sách hẹn hôm nay</h5>
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th>Bệnh nhân</th>
                            <th>Giờ hẹn</th>
                            <th>Lý do khám</th>
                            <th>Trạng thái</th>
                        </tr>
                    </thead>
                    <tbody>
                        {appointments.map((a) => (
                            <tr key={a.id}>
                                <td>{a.queueNumber}</td>
                                <td>{a.patientName}</td>
                                <td>{a.scheduleStartTime?.substring(0, 5)} - {a.scheduleEndTime?.substring(0, 5)}</td>
                                <td>{a.reason}</td>
                                <td>
                                    <Badge bg={getStatusBadge(a.status)}>
                                        {a.status}
                                    </Badge>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            </Container>
        </DashboardLayout>
    );
}
