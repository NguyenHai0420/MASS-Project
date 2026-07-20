import axiosClient from "../../../shared/services/axiosClient";

const profileService = {
  getProfile: () => {
    return axiosClient.get("/api/v1/users/profile");
  },
  updateProfile: (profileData) => {
    return axiosClient.put("/api/v1/users/profile", profileData);
  }
};

export default profileService;
