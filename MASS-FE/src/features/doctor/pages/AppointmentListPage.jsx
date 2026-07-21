import { useState, useEffect } from "react";
import { Container, Table, Badge, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../../../shared/components/DashboardLayout";
import doctorService from "../services/doctorService";

// ========================
// UC-M23 — Appointment List
// Doctor xem toàn bộ danh sách cuộc hẹn của mình
// ========================

function getStatusBadge(status) {
    switch (status) {
        case "CONFIRMED": return "primary";
        case "PENDING": return "warning";
        case "COMPLETED": return "success";
        case "CANCELLED": return "danger";
        default: return "secondary";
    }
}

export default function AppointmentListPage() {
    const [appointments, setAppointments] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const data = await doctorService.getMyAppointments();
            setAppointments(data);
        } catch (error) {
            console.error("Lỗi tải danh sách hẹn:", error);
        }
    };

    // Chuyển đến trang nhập kết quả khám
    const handleRecord = (appointmentId) => {
        navigate(`/doctor/medical-record/${appointmentId}`);
    };

    return (
        <DashboardLayout>
            <Container fluid>
                <h4 className="mb-3">📝 Danh sách cuộc hẹn</h4>

                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Bệnh nhân</th>
                            <th>Ngày hẹn</th>
                            <th>Giờ hẹn</th>
                            <th>Lý do khám</th>
                            <th>Trạng thái</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        {appointments.map((a, index) => (
                            <tr key={a.id}>
                                <td>{index + 1}</td>
                                <td>{a.patientName}</td>
                                <td>{a.scheduleDate}</td>
                                <td>{a.scheduleStartTime?.substring(0, 5)} - {a.scheduleEndTime?.substring(0, 5)}</td>
                                <td>{a.reason}</td>
                                <td>
                                    <Badge bg={getStatusBadge(a.status)}>
                                        {a.status}
                                    </Badge>
                                </td>
                                <td>
                                    {/* Chỉ cho nhập kết quả nếu cuộc hẹn đã CONFIRMED */}
                                    {a.status === "CONFIRMED" && (
                                        <Button
                                            variant="success"
                                            size="sm"
                                            onClick={() => handleRecord(a.id)}
                                        >
                                            Nhập kết quả
                                        </Button>
                                    )}
                                    {a.status === "COMPLETED" && (
                                        <Button
                                            variant="outline-secondary"
                                            size="sm"
                                            onClick={() => handleRecord(a.id)}
                                        >
                                            Xem kết quả
                                        </Button>
                                    )}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            </Container>
        </DashboardLayout>
    );
}
