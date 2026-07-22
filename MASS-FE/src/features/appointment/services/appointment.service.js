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

  getById: async (appointmentId) => {
    return axiosClient.get(`/api/receptionist/appointments/${appointmentId}`);
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

  getAllSpecialties: async () => {
    return axiosClient.get('/api/receptionist/appointments/specialties');
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



export default appointmentService;
