import { useEffect, useMemo, useState } from 'react';
import AppointmentHeroSection from '../components/AppointmentHeroSection';
import AppointmentListPosting from './AppointmentListPosting';
import {
  appointmentStatuses,
  mockAppointments,
  paymentStatuses,
} from '../services/appointments';
import '../../../styles/appointment.css';

const AppointmentListPage = () => {
  const [keyword, setKeyword] = useState('');
  const [debouncedKeyword, setDebouncedKeyword] = useState('');
  const [selectedStatus, setSelectedStatus] = useState('ALL');
  const [selectedPaymentStatus, setSelectedPaymentStatus] = useState('ALL');
  const [selectedSpecialty, setSelectedSpecialty] = useState('ALL');

  const specialties = useMemo(
    () => [...new Set(mockAppointments.map((appointment) => appointment.specialty))],
    [],
  );

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      setDebouncedKeyword(keyword.trim().toLowerCase());
    }, 400);

    return () => clearTimeout(timeoutId);
  }, [keyword]);

  const visibleAppointments = useMemo(() => {
    return mockAppointments.filter((appointment) => {
      const matchesKeyword =
        !debouncedKeyword ||
        appointment.id.toLowerCase().includes(debouncedKeyword) ||
        appointment.patientName.toLowerCase().includes(debouncedKeyword) ||
        appointment.patientPhone.toLowerCase().includes(debouncedKeyword) ||
        appointment.doctorName.toLowerCase().includes(debouncedKeyword) ||
        appointment.specialty.toLowerCase().includes(debouncedKeyword);

      const matchesStatus =
        selectedStatus === 'ALL' || appointment.status === selectedStatus;

      const matchesPaymentStatus =
        selectedPaymentStatus === 'ALL' ||
        appointment.paymentStatus === selectedPaymentStatus;

      const matchesSpecialty =
        selectedSpecialty === 'ALL' || appointment.specialty === selectedSpecialty;

      return matchesKeyword && matchesStatus && matchesPaymentStatus && matchesSpecialty;
    });
  }, [debouncedKeyword, selectedPaymentStatus, selectedSpecialty, selectedStatus]);

  const handleReset = () => {
    setKeyword('');
    setSelectedStatus('ALL');
    setSelectedPaymentStatus('ALL');
    setSelectedSpecialty('ALL');
  };

  return (
    <main className="appointment-page">
      <AppointmentHeroSection
        keyword={keyword}
        setKeyword={setKeyword}
        selectedStatus={selectedStatus}
        setSelectedStatus={setSelectedStatus}
        selectedPaymentStatus={selectedPaymentStatus}
        setSelectedPaymentStatus={setSelectedPaymentStatus}
        selectedSpecialty={selectedSpecialty}
        setSelectedSpecialty={setSelectedSpecialty}
        statuses={appointmentStatuses}
        paymentStatuses={paymentStatuses}
        specialties={specialties}
        onReset={handleReset}
      />
      <AppointmentListPosting appointments={visibleAppointments} />
    </main>
  );
};

export default AppointmentListPage;
