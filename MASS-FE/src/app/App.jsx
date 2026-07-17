import react from "react";
import Login from "../features/auth/pages/LoginPage";
import Register from "../features/auth/pages/RegisterPage";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { Navigate } from "react-router-dom";
import { AuthProvider } from "./providers/AuthProvider";
import ForgotPassword from "../features/auth/pages/ForgotPasswordPage";
import VerifyOtp from "../features/auth/pages/VerifyOtpPage";
// import ResetPassword from "../features/auth/pages/ResetPasswordPage";
// import Profile from "../features/profile/pages/ProfilePage";

import HomePage from "../features/patient/pages/HomePage";
import DoctorListPage from "../features/patient/pages/DoctorListPage";
import DoctorDetailPage from "../features/patient/pages/DoctorDetailPage";
import MyAppointmentsPage from "../features/patient/pages/MyAppointmentsPage";

function App() {
  // Logic

  // UI
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
        <Route path="/" element={<Navigate to="/home" />} />


        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/verify-otp" element={<VerifyOtp />} />
        {/* <Route path="/reset-password" element={<ResetPassword />} />
        <Route path="/profile" element={<Profile />} /> */}

        {/* Patient Module Routes */}
        <Route path="/home" element={<HomePage />} />
        <Route path="/doctors" element={<DoctorListPage />} />
        <Route path="/doctors/:id" element={<DoctorDetailPage />} />
        <Route path="/my-appointments" element={<MyAppointmentsPage />} />

      </Routes>
    </BrowserRouter>
    </AuthProvider>
  );
}

export default App;