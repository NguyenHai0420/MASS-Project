import { BrowserRouter, Route, Routes, Navigate, useNavigate } from "react-router-dom";
import { useContext, useEffect } from "react";
import { AuthContext } from "./providers/AuthProvider";
import Login from "../features/auth/pages/Login";
import Register from "../features/auth/pages/Register";
import ForgotPassword from "../features/auth/pages/ForgotPassword";
import VerifyOtp from "../features/auth/pages/VerifyOtp";
import ResetPassword from "../features/auth/pages/ResetPassword";
import Profile from "../features/profile/pages/Profile";
import Home from "../features/home/pages/Home";
import BookAppointment from "../features/appointment/pages/BookAppointment";
import DoctorList from "../features/doctor/pages/DoctorList";
import ReceptionistAppointmentListPage from "../features/appointment/pages/AppointmentListPage";
import MyAppointments from "../features/appointment/pages/MyAppointments";
import DoctorDetail from "../features/doctor/pages/DoctorDetail";
import Navbar from "../shared/components/Navbar";
import Footer from "../shared/components/Footer";
import PrivateRoute from "./routes/PrivateRoute";
import { AuthProvider } from "./providers/AuthProvider";
import AdminDashboardPage from "../features/admin/pages/AdminDashboardPage";
import ManageDoctorsPage from "../features/admin/pages/ManageDoctorsPage";
import ManageSpecialtiesPage from "../features/admin/pages/ManageSpecialtiesPage";
import ManageUsersPage from "../features/admin/pages/ManageUsersPage";
import ManageClinicPage from "../features/admin/pages/ManageClinicPage";
import ManageStatisticsPage from "../features/admin/pages/ManageStatisticsPage";
import DoctorDashboardPage from "../features/doctor/pages/DoctorDashboardPage";
import WorkSchedulePage from "../features/doctor/pages/WorkSchedulePage";
import AppointmentListPage from "../features/doctor/pages/AppointmentListPage";
import MedicalRecordPage from "../features/doctor/pages/MedicalRecordPage";
import "bootstrap/dist/css/bootstrap.min.css";


// Redirect về đúng dashboard theo role sau khi login
function DashboardRedirect() {
  const { user } = useContext(AuthContext);
  const navigate = useNavigate();
  useEffect(() => {
    if (!user) return;
    if (user.role === "ROLE_ADMIN") navigate("/admin/dashboard", { replace: true });
    else if (user.role === "ROLE_DOCTOR") navigate("/doctor/dashboard", { replace: true });
    else navigate("/profile", { replace: true });
  }, [user, navigate]);
  return null;
}

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <div className="d-flex flex-column min-vh-100">
          <Navbar />
          <main className="flex-grow-1">
            <Routes>
              {/* Public routes */}
              <Route path="/" element={<Home />} />
              <Route path="/book-appointment" element={<BookAppointment />} />
              <Route path="/bookAppointment" element={<BookAppointment />} />
              <Route path="/doctors" element={<DoctorList />} />
              <Route path="/doctors/:id" element={<DoctorDetail />} />
              <Route path="/my-appointments" element={<MyAppointments />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/forgot-password" element={<ForgotPassword />} />
              <Route path="/verify-otp" element={<VerifyOtp />} />
              <Route path="/reset-password" element={<ResetPassword />} />

              {/* Module Receptionist */}
              <Route element={<PrivateRoute allowedRoles={["ROLE_RECEPTIONIST"]} />}>
                <Route path="/appointments" element={<ReceptionistAppointmentListPage />} />
                <Route path="/receptionist/dashboard" element={<DashboardRedirect />} />
              </Route>

              {/* Module Patient */}
              <Route element={<PrivateRoute allowedRoles={["ROLE_PATIENT"]} />}>
                <Route path="/my-appointments" element={<MyAppointments />} />
              </Route>

              {/* Protected routes common */}
              <Route element={<PrivateRoute allowedRoles={["ROLE_PATIENT", "ROLE_DOCTOR", "ROLE_RECEPTIONIST", "ROLE_ADMIN"]} />}>
                <Route path="/dashboard" element={<DashboardRedirect />} />
                <Route path="/profile" element={<Profile />} />
              </Route>

              {/* Admin routes */}
              <Route element={<PrivateRoute allowedRoles={["ROLE_ADMIN"]} />}>
                <Route path="/admin/dashboard" element={<AdminDashboardPage />} />
                <Route path="/admin/doctors" element={<ManageDoctorsPage />} />
                <Route path="/admin/specialties" element={<ManageSpecialtiesPage />} />
                <Route path="/admin/users" element={<ManageUsersPage />} />
                <Route path="/admin/clinic" element={<ManageClinicPage />} />
                <Route path="/admin/statistics" element={<ManageStatisticsPage />} />
              </Route>

              {/* Doctor routes */}
              <Route element={<PrivateRoute allowedRoles={["ROLE_DOCTOR"]} />}>
                <Route path="/doctor/dashboard" element={<DoctorDashboardPage />} />
                <Route path="/doctor/schedule" element={<WorkSchedulePage />} />
                <Route path="/doctor/appointments" element={<AppointmentListPage />} />
                <Route path="/doctor/medical-record" element={<MedicalRecordPage />} />
              </Route>
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </main>
          <Footer />
        </div>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;