import react from "react";
import AppointmentListPage from '../features/appointment/pages/AppointmentListPage';
import { BrowserRouter, Route, Routes, Navigate } from 'react-router-dom';
import Login from '../features/auth/pages/Login';
import Register from '../features/auth/pages/Register';
import ForgotPassword from '../features/auth/pages/ForgotPassword';
import VerifyOtp from '../features/auth/pages/VerifyOtp';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Redirect root đến appointments */}
        <Route path="/" element={<Navigate to="/appointments" />} />

        {/* Module Receptionist – phần của bạn */}
        <Route path="/appointments" element={<AppointmentListPage />} />

        {/* Auth routes */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/verify-otp" element={<VerifyOtp />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;