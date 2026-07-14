const authService = {
    login: async (payload) => {

        console.log("Login payload:", payload);
        if(payload.email === "patient@gmail.com" && 
            payload.password === "123456") {
            const response = {
                id: 1,
                name: "Patient User",
                email: payload.email,
                accessToken: "fake-jwt-token"
            };

            return response;
        }
        throw new Error("Invalid email or password");

        // Implementation for login
    },
    register: async (payload) => {
        console.log("Register payload:", payload);
        return {
            id: 2,
            name: payload.name,
            email: payload.email,
            accessToken: "fake-jwt-token"
        }; 

     
    },
    logout: async () => {
        console.log("Logout called");
        return true; 
       
    }
}

export default authService;