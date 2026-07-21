import React, { useCallback, useEffect, useState } from 'react';
import AppointmentHeroSection from '../components/AppointmentHeroSection';
import AppointmentListPosting from './AppointmentListPosting';
import WalkInModal from '../components/WalkInModal';
import PaymentModal from '@/features/payment/components/PaymentModal';
import AppointmentDetailModal from '../components/AppointmentDetailModal';
import appointmentService from '../services/appointment.service';
import useAppointmentFilter from '../hooks/useAppointmentFilter';
import '@/styles/appointment.css';

const AppointmentListPage = () => {
  // Filter state – dùng useReducer theo pattern giáo viên
  const { filters, dispatch } = useAppointmentFilter();

  // Keyword search – debounced riêng (không cần Redux)
  const [keyword, setKeyword] = useState('');

  // Data state
  const [appointments, setAppointments] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [totalElements, setTotalElements] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  // Modal state
  const [showWalkIn, setShowWalkIn] = useState(false);
  const [showPayment, setShowPayment] = useState(false);
  const [showDetail, setShowDetail] = useState(false);
  const [selectedAppointment, setSelectedAppointment] = useState(null);

  const [specialties, setSpecialties] = useState([]);

  // Fetch specialties
  useEffect(() => {
    appointmentService.getAllSpecialties()
      .then(res => setSpecialties(res.data || []))
      .catch(console.error);
  }, []);

  // Fetch appointments từ API
  const fetchAppointments = useCallback(async () => {
    setIsLoading(true);
    try {
      const params = {
        page: filters.page,
        size: filters.size,
      };

      // Chỉ truyền param nếu khác ALL / rỗng
      if (filters.date) params.date = filters.date;
      if (filters.specialtyId !== 'ALL') params.specialtyId = filters.specialtyId;
      if (filters.status !== 'ALL') params.status = filters.status;

      const res = await appointmentService.getAll(params);
      const data = res.data;

      setAppointments(data.content || []);
      setTotalElements(data.totalElements || 0);
      setTotalPages(data.totalPages || 0);
    } catch (error) {
      console.error('Lỗi tải danh sách lịch hẹn:', error);
      setAppointments([]);
    } finally {
      setIsLoading(false);
    }
  }, [filters]);

  // Gọi API khi filter thay đổi
  useEffect(() => {
    fetchAppointments();
  }, [fetchAppointments]);

  // Client-side filter theo keyword (tìm theo tên/SĐT/email)
  const visibleAppointments = keyword.trim()
    ? appointments.filter((a) => {
      const q = keyword.trim().toLowerCase();
      return (
        a.patientName?.toLowerCase().includes(q) ||
        a.patientPhone?.toLowerCase().includes(q) ||
        a.patientEmail?.toLowerCase().includes(q) ||
        a.doctorName?.toLowerCase().includes(q) ||
        String(a.appointmentId).includes(q)
      );
    })
    : appointments;

  // --- Modal handlers ---

  const handleViewDetail = (appointment) => {
    setSelectedAppointment(appointment);
    setShowDetail(true);
  };

  const handlePayment = (appointment) => {
    setSelectedAppointment(appointment);
    setShowPayment(true);
  };

  const handleCheckIn = async (appointment) => {
    try {
      await appointmentService.checkIn(appointment.appointmentId);
      fetchAppointments(); // Reload list
    } catch (err) {
      console.error('Check-in thất bại:', err);
    }
  };

  const handleCancel = (appointment) => {
    setSelectedAppointment(appointment);
    setShowDetail(true); // Mở detail modal để nhập lý do hủy
  };

  const handleWalkInSuccess = (newAppointment) => {
    console.log('Walk-in tạo thành công:', newAppointment);
    fetchAppointments(); // Reload list
    setShowWalkIn(false);
  };

  const handlePageChange = (newPage) => {
    dispatch({ type: 'SET_PAGE', payload: newPage });
  };

  return (
    <main className="appt-page">
      {/* Hero section với search + filter */}
      <AppointmentHeroSection
        keyword={keyword}
        setKeyword={setKeyword}
        dispatch={dispatch}
        filters={filters}
        specialties={specialties}
      />

      {/* Danh sách lịch hẹn */}
      <AppointmentListPosting
        appointments={visibleAppointments}
        isLoading={isLoading}
        totalElements={totalElements}
        currentPage={filters.page}
        totalPages={totalPages}
        onPageChange={handlePageChange}
        onWalkInClick={() => setShowWalkIn(true)}
        onViewDetail={handleViewDetail}
        onCheckIn={handleCheckIn}
        onPayment={handlePayment}
        onCancel={handleCancel}
      />

      {/* Walk-in Modal */}
      <WalkInModal
        show={showWalkIn}
        onHide={() => setShowWalkIn(false)}
        onSuccess={handleWalkInSuccess}
      />

      {/* Payment Modal */}
      <PaymentModal
        show={showPayment}
        onHide={() => { setShowPayment(false); setSelectedAppointment(null); }}
        appointment={selectedAppointment}
        onPaymentSuccess={fetchAppointments}
      />

      {/* Detail Modal */}
      <AppointmentDetailModal
        show={showDetail}
        onHide={() => { setShowDetail(false); setSelectedAppointment(null); }}
        appointmentId={selectedAppointment?.appointmentId}
        onCheckIn={fetchAppointments}
        onCancel={fetchAppointments}
      />
    </main>
  );
};

export default AppointmentListPage;
