import axios from "axios";

const BASE_URL = "http://localhost:8080";

const axiosClient = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true, // Send/receive the httpOnly accessToken cookie on every request
  timeout: 10000, 
});

axiosClient.interceptors.response.use(
  function (response) {
    return response;
  },
  async function (error) {
    if (error.response && error.response.status === 401) {
        console.error("Unauthorized! Session expired or invalid token.");
        localStorage.removeItem("user");

        if (window.location.pathname !== "/login" && window.location.pathname !== "/register" && window.location.pathname !== "/forgot-password") {
          window.location.href = "/login";
        }
    }
    return Promise.reject(error);
  }
);

export default axiosClient;
