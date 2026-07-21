import { BrowserRouter, Route, Routes, Navigate } from "react-router-dom";
import Login from "../features/auth/pages/Login";
import Register from "../features/auth/pages/Register";
import ForgotPassword from "../features/auth/pages/ForgotPassword";
import VerifyOtp from "../features/auth/pages/VerifyOtp";
import ResetPassword from "../features/auth/pages/ResetPassword";
import Profile from "../features/profile/pages/Profile";
import Home from "../features/home/pages/Home";
import BookAppointment from "../features/appointment/pages/BookAppointment";
import DoctorList from "../features/doctor/pages/DoctorList";
import AppointmentListPage from "../features/appointment/pages/AppointmentListPage";
import Navbar from "../shared/components/Navbar";
import Footer from "../shared/components/Footer";
import PrivateRoute from "./routes/PrivateRoute";
import { AuthProvider } from "./providers/AuthProvider";
import "bootstrap/dist/css/bootstrap.min.css";

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
              <Route path="/bookAppointment" element={<BookAppointment />} />
              <Route path="/doctors" element={<DoctorList />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/forgot-password" element={<ForgotPassword />} />
              <Route path="/verify-otp" element={<VerifyOtp />} />
              <Route path="/reset-password" element={<ResetPassword />} />

              {/* Module Receptionist */}
              <Route element={<PrivateRoute allowedRoles={["ROLE_RECEPTIONIST"]} />}>
                <Route path="/appointments" element={<AppointmentListPage />} />
              </Route>

              {/* Protected routes example */}
              <Route element={<PrivateRoute allowedRoles={["ROLE_PATIENT", "ROLE_DOCTOR", "ROLE_RECEPTIONIST", "ROLE_ADMIN"]} />}>
                <Route path="/dashboard" element={<div>Welcome to Dashboard</div>} />
                <Route path="/profile" element={<Profile />} />
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