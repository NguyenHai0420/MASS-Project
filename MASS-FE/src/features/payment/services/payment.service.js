import axiosClient from '@/shared/services/axiosClient';

const paymentService = {
  // Tạo link thanh toán PayOS cho lịch hẹn
  createPaymentLink: async (appointmentId) => {
    return axiosClient.post(`/api/payments/appointments/${appointmentId}/payment-link`);
  },
};

export default paymentService;
