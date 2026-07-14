import { BrowserRouter, Route, Routes, Navigate } from "react-router-dom";
import Login from "./features/auth/pages/Login";
import Register from "./features/auth/pages/Register";
import ForgotPassword from "./features/auth/pages/ForgotPassword";
import VerifyOtp from "./features/auth/pages/VerifyOtp";
import ResetPassword from "./features/auth/pages/ResetPassword";
import PrivateRoute from "./app/routes/PrivateRoute";
import { AuthProvider } from "./app/providers/AuthProvider";
import "bootstrap/dist/css/bootstrap.min.css";

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* Public routes */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/forgot-password" element={<ForgotPassword />} />
          <Route path="/verify-otp" element={<VerifyOtp />} />
          <Route path="/reset-password" element={<ResetPassword />} />

          {/* Protected routes example */}
          <Route element={<PrivateRoute allowedRoles={["ROLE_PATIENT", "ROLE_DOCTOR", "ROLE_RECEPTIONIST", "ROLE_ADMIN"]} />}>
            <Route path="/dashboard" element={<div>Welcome to Dashboard</div>} />
          </Route>
          
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
