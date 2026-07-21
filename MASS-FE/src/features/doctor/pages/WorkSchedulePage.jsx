import { useState, useEffect } from "react";
import { Container, Table, Button, Modal, Form } from "react-bootstrap";
import { useForm } from "react-hook-form";
import toast, { Toaster } from "react-hot-toast";
import DashboardLayout from "../../../shared/components/DashboardLayout";
import doctorService from "../services/doctorService";

// ========================
// UC-M08 — Manage Work Schedule
// Doctor quản lý lịch làm việc của mình
// ========================

export default function WorkSchedulePage() {
    const [schedules, setSchedules] = useState([]);
    const [showModal, setShowModal] = useState(false);

    const { register, handleSubmit, formState: { errors }, reset } = useForm();

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const data = await doctorService.getMySchedules();
            setSchedules(data);
        } catch (error) {
            toast.error("Không thể tải lịch làm việc!");
        }
    };

    const handleShowAdd = () => {
        reset({ date: "", startTime: "", endTime: "" });
        setShowModal(true);
    };

    const handleClose = () => {
        setShowModal(false);
        reset();
    };

    const onSubmit = async (data) => {
        try {
            // Định dạng lại thời gian thành HH:mm:ss cho BE
            const payload = {
                date: data.date,
                startTime: data.startTime + ":00",
                endTime: data.endTime + ":00"
            };

            await doctorService.createSchedule(payload);
            toast.success("Thêm lịch làm việc thành công!");
            handleClose();
            fetchData();
        } catch (error) {
            const errorMsg = error.response?.data?.message || "Có lỗi xảy ra!";
            toast.error(errorMsg);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Bạn có chắc muốn xoá lịch này?")) return;
        try {
            await doctorService.deleteSchedule(id);
            toast.success("Xoá lịch thành công!");
            fetchData();
        } catch (error) {
            toast.error("Xoá thất bại!");
        }
    };

    return (
        <DashboardLayout>
            <Container fluid>
                <Toaster />
                <h4 className="mb-3">📅 Lịch làm việc</h4>

                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Ngày</th>
                            <th>Giờ bắt đầu</th>
                            <th>Giờ kết thúc</th>
                            <th>Trạng thái</th>
                            <th>
                                <Button variant="primary" size="sm" onClick={handleShowAdd}>
                                    + Thêm lịch
                                </Button>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        {schedules.map((item, index) => (
                            <tr key={item.id}>
                                <td>{index + 1}</td>
                                <td>{item.date}</td>
                                <td>{item.startTime}</td>
                                <td>{item.endTime}</td>
                                <td>
                                    {item.available ? (
                                        <span className="badge text-bg-success">Còn trống</span>
                                    ) : (
                                        <span className="badge text-bg-secondary">Đã đặt</span>
                                    )}
                                </td>
                                <td>
                                    <Button
                                        variant="danger"
                                        size="sm"
                                        onClick={() => handleDelete(item.id)}
                                    >
                                        Xoá
                                    </Button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>

                {/* Modal Thêm lịch */}
                <Modal show={showModal} onHide={handleClose} backdrop="static">
                    <Modal.Header closeButton>
                        <Modal.Title>Thêm lịch làm việc</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <form onSubmit={handleSubmit(onSubmit)}>
                            <Form.Group className="mb-3">
                                <Form.Label>Ngày</Form.Label>
                                <Form.Control
                                    type="date"
                                    {...register("date", { required: true })}
                                />
                                {errors.date && (
                                    <p className="text-danger">Vui lòng chọn ngày</p>
                                )}
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label>Giờ bắt đầu</Form.Label>
                                <Form.Control
                                    type="time"
                                    {...register("startTime", { required: true })}
                                />
                                {errors.startTime && (
                                    <p className="text-danger">Vui lòng nhập giờ bắt đầu</p>
                                )}
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label>Giờ kết thúc</Form.Label>
                                <Form.Control
                                    type="time"
                                    {...register("endTime", { required: true })}
                                />
                                {errors.endTime && (
                                    <p className="text-danger">Vui lòng nhập giờ kết thúc</p>
                                )}
                            </Form.Group>

                            <Modal.Footer>
                                <Button variant="secondary" onClick={handleClose}>
                                    Đóng
                                </Button>
                                <Button variant="primary" type="submit">
                                    Lưu
                                </Button>
                            </Modal.Footer>
                        </form>
                    </Modal.Body>
                </Modal>
            </Container>
        </DashboardLayout>
    );
}
