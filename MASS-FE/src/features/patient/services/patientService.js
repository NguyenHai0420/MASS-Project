import axiosClient from '../../../shared/services/axiosClient';

const patientService = {
  // Specialties
  getSpecialties: () => {
    return axiosClient.get('/api/specialties');
  },

  // Doctors
  getDoctors: (specialtyId = null, name = null) => {
    let url = '/api/doctors';
    const params = new URLSearchParams();
    if (specialtyId) params.append('specialtyId', specialtyId);
    if (name) params.append('name', name);
    if (params.toString()) url += `?${params.toString()}`;
    return axiosClient.get(url);
  },

  getDoctorById: (id) => {
    return axiosClient.get(`/api/doctors/${id}`);
  },

  // Schedules
  getDoctorSchedules: (doctorId, date) => {
    return axiosClient.get(`/api/appointments/available-slots`, {
      params: { doctorProfileId: doctorId, date }
    });
  },


  bookAppointment: (data) => {

    return axiosClient.post('/api/appointments', data);
  },

  getMyAppointments: () => {
    return axiosClient.get('/api/appointments/my-appointments');
  },

  cancelAppointment: (id) => {
    return axiosClient.put(`/api/appointments/${id}/cancel`);
  },

  rescheduleAppointment: (id, newSlotData) => {
    return axiosClient.put(`/api/appointments/${id}/reschedule`, newSlotData);
  }
};

export default patientService;
