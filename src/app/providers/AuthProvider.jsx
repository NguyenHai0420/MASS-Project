import { createContext, useEffect, useState } from "react";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  // Logic for authentication (e.g., login, logout, check auth status)
  const [user, setUser] = useState(null);

  useEffect(() => {
    // Check if user data exists in localStorage on component mount

    function checkAuthStatus() {
      const userData = localStorage.getItem("user");

      // Reset to context
      if (userData) {
        setUser(JSON.parse(userData)); // Set user state from localStorage
      }
    }

    checkAuthStatus();
  }, []);

  const login = (response) => {
    setUser(response);
    localStorage.setItem("user", JSON.stringify(response)); // Store user data in localStorage
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem("user"); // Clear user data from localStorage
  }; 

  const dataContext = { user };
  const actionContext = { login, logout };
  
  return (
    <AuthContext.Provider value={{ ...dataContext, ...actionContext }}>
      {children}
    </AuthContext.Provider>
  );
};