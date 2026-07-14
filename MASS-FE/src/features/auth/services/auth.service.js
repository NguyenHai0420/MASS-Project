import axiosClient from "../../../shared/services/axiosClient";

const authService = {
  login: (payload) => {
    return axiosClient.post("/api/v1/auths/login", payload);
  },
  register: (payload) => {
    return axiosClient.post("/api/v1/auths/register", payload);
  },
  me: () => {
    return axiosClient.get("/api/v1/auths/me");
  },
  logout: () => {
    return axiosClient.post("/api/v1/auths/logout");
  },
  forgotPassword: (email) => {
    return axiosClient.post("/api/v1/auths/forgot-password", { email });
  },
  verifyOtp: (email, otp) => {
    return axiosClient.post("/api/v1/auths/verify-otp", { email, otp });
  },
  resetPassword: (email, otp, newPassword) => {
    return axiosClient.post("/api/v1/auths/reset-password", { email, otp, newPassword });
  }
};

export default authService;
