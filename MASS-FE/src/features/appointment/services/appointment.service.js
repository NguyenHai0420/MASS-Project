import axiosClient from '@/shared/services/axiosClient';

const appointmentService = {
  // Lấy danh sách lịch hẹn (có filter + phân trang)
  getAll: async (params = {}) => {
    return axiosClient.get('/api/receptionist/appointments', { params });
  },

  // Lấy chi tiết 1 lịch hẹn
  getById: async (appointmentId) => {
    return axiosClient.get(`/api/receptionist/appointments/${appointmentId}`);
  },

  // Lấy danh sách slot trống (để điền form walk-in)
  getAvailableSchedules: async (params = {}) => {
    // Lưu ý: typo từ backend – "scheduels"
    return axiosClient.get('/api/receptionist/appointments/available-scheduels', { params });
  },

  // Tạo lịch hẹn walk-in tại quầy
  createWalkIn: async (data) => {
    return axiosClient.post('/api/receptionist/appointments/walk-in', data);
  },

  // Check-in bệnh nhân
  checkIn: async (appointmentId) => {
    return axiosClient.patch(`/api/receptionist/appointments/${appointmentId}/check-in`);
  },

  // Cập nhật trạng thái lịch hẹn
  updateStatus: async (appointmentId, appointmentStatus) => {
    return axiosClient.patch(`/api/receptionist/appointments/${appointmentId}/status`, {
      appointmentStatus,
    });
  },

  // Hủy lịch hẹn
  cancel: async (appointmentId, cancelReason) => {
    return axiosClient.patch(`/api/receptionist/appointments/${appointmentId}/cancel`, {
      cancelReason,
    });
  },

  // Cập nhật thông tin lịch hẹn
  update: async (appointmentId, data) => {
    return axiosClient.put(`/api/receptionist/appointments/${appointmentId}`, data);
  },

  getDoctorsBySpecialty: async (specialtyId) => {
    return axiosClient.get('/api/receptionist/appointments/doctors', {
      params: { specialtyId }
    });
  },
};

// Danh sách trạng thái lịch hẹn (đồng bộ với backend enum)
export const appointmentStatuses = [
  { value: 'ALL', label: 'Tất cả trạng thái' },
  { value: 'PENDING_PAYMENT', label: 'Chờ thanh toán' },
  { value: 'WAITING_CHECK_IN', label: 'Chờ check-in' },
  { value: 'WAITING_FOR_TURN', label: 'Chờ đến lượt' },
  { value: 'COMPLETED', label: 'Hoàn thành' },
  { value: 'CANCELLED', label: 'Đã hủy' },
  { value: 'NO_SHOW', label: 'Không đến khám' },
];

// Danh sách chuyên khoa mẫu (sẽ được thay bằng API sau nếu có)
export const defaultSpecialties = [
  { id: 1, name: 'Nội tổng quát' },
  { id: 2, name: 'Tim mạch' },
  { id: 3, name: 'Tai Mũi Họng' },
  { id: 4, name: 'Da liễu' },
  { id: 5, name: 'Nhi khoa' },
  { id: 6, name: 'Chấn thương chỉnh hình' },
];

export default appointmentService;
