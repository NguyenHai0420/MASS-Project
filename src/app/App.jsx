import react from "react";
import Login from "../features/auth/pages/LoginPage";
import Register from "../features/auth/pages/RegisterPage";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { Navigate } from "react-router-dom";
import ForgotPassword from "../features/auth/pages/ForgotPasswordPage";
import VerifyOtp from "../features/auth/pages/VerifyOtpPage";
// import ResetPassword from "../features/auth/pages/ResetPasswordPage";
// import Profile from "../features/profile/pages/ProfilePage";

function App() {
  // Logic

  // UI
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />


        {/* Định nghĩa các route cho ứng dụng */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/verify-otp" element={<VerifyOtp />} />
        {/* <Route path="/reset-password" element={<ResetPassword />} />
        <Route path="/profile" element={<Profile />} /> */}

      </Routes>
    </BrowserRouter>
  );
}

export default App;