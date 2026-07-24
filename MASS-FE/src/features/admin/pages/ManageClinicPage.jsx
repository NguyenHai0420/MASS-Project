import { useState, useEffect } from "react";
import { Container, Card, Form, Button } from "react-bootstrap";
import { useForm } from "react-hook-form";
import toast, { Toaster } from "react-hot-toast";
import DashboardLayout from "../../../shared/components/DashboardLayout";
import adminService from "../services/adminService";

export default function ManageClinicPage() {
    const [clinic, setClinic] = useState(null);
    const [loading, setLoading] = useState(true);
    const { register, handleSubmit, formState: { errors, isSubmitting, isDirty }, reset } = useForm();

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        setLoading(true);
        try {
            const data = await adminService.getClinicInfo();
            const clinicData = Array.isArray(data) ? data[0] : data;
            if (clinicData) {
                setClinic(clinicData);
                reset({
                    name: clinicData.name || "",
                    address: clinicData.address || "",
                    phone: clinicData.phone || "",
                    email: clinicData.email || "",
                    workingHours: clinicData.workingHours || ""
                });
            }
        } catch (error) {
            console.error(error);
            toast.error("Không thể tải thông tin phòng khám!");
        } finally {
            setLoading(false);
        }
    };

    const handleResetForm = () => {
        if (clinic) {
            reset({
                name: clinic.name || "",
                address: clinic.address || "",
                phone: clinic.phone || "",
                email: clinic.email || "",
                workingHours: clinic.workingHours || ""
            });
            toast.success("Đã khôi phục thông tin ban đầu!");
        }
    };

    const onSubmit = async (data) => {
        try {
            const payload = {
                name: data.name,
                address: data.address,
                phone: data.phone || "",
                email: data.email || "",
                workingHours: data.workingHours || ""
            };

            let result;
            if (clinic?.id) {
                result = await adminService.updateClinicInfo(clinic.id, payload);
                toast.success("Cập nhật thông tin thành công!");
            } else {
                result = await adminService.createClinicInfo(payload);
                toast.success("Tạo thông tin phòng khám thành công!");
            }

            if (result) {
                setClinic(result);
                reset({
                    name: result.name || "",
                    address: result.address || "",
                    phone: result.phone || "",
                    email: result.email || "",
                    workingHours: result.workingHours || ""
                });
            }
        } catch (error) {
            console.error(error);
            const errorMsg = error.response?.data?.message || "Lưu thất bại!";
            toast.error(errorMsg);
        }
    };

    return (
        <DashboardLayout>
            <Container fluid className="py-2">
                <Toaster />
                <div className="mb-4">
                    <h4 className="fw-bold mb-1">🏥 Quản lý Thông tin Phòng khám</h4>
                    <p className="text-muted mb-0" style={{ fontSize: 14 }}>
                        Cập nhật hồ sơ công khai của phòng khám
                    </p>
                </div>

                <Card className="border-0 shadow-sm">
                    <Card.Body className="p-4">
                        <Form onSubmit={handleSubmit(onSubmit)}>
                            <Form.Group className="mb-3">
                                <Form.Label className="fw-bold">Tên phòng khám</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Nhập tên phòng khám"
                                    {...register("name", { required: "Tên phòng khám không được để trống" })}
                                />
                                {errors.name && (
                                    <p className="text-danger mt-1" style={{ fontSize: 13 }}>{errors.name.message}</p>
                                )}
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label className="fw-bold">Địa chỉ</Form.Label>
                                <Form.Control
                                    as="textarea"
                                    rows={2}
                                    placeholder="Nhập địa chỉ phòng khám"
                                    {...register("address", { required: "Địa chỉ không được để trống" })}
                                />
                                {errors.address && (
                                    <p className="text-danger mt-1" style={{ fontSize: 13 }}>{errors.address.message}</p>
                                )}
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label className="fw-bold">Số điện thoại liên hệ</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Nhập số điện thoại liên hệ"
                                    {...register("phone")}
                                />
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label className="fw-bold">Email</Form.Label>
                                <Form.Control
                                    type="email"
                                    placeholder="Nhập email phòng khám"
                                    {...register("email")}
                                />
                            </Form.Group>

                            <Form.Group className="mb-4">
                                <Form.Label className="fw-bold">Giờ làm việc</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Ví dụ: Thứ 2 - Thu 7: 8:00 - 17:30"
                                    {...register("workingHours")}
                                />
                            </Form.Group>

                            <div className="d-flex gap-2">
                                <Button variant="primary" type="submit" disabled={isSubmitting || loading}>
                                    {isSubmitting ? "Đang lưu..." : "Lưu thay đổi"}
                                </Button>
                                {isDirty && (
                                    <Button variant="outline-secondary" type="button" onClick={handleResetForm}>
                                        Hủy thay đổi
                                    </Button>
                                )}
                            </div>
                        </Form>
                    </Card.Body>
                </Card>
            </Container>
        </DashboardLayout>
    );
}
