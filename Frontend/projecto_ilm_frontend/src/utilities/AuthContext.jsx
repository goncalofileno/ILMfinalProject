import React, { createContext, useContext, useState, useEffect } from "react";
import Cookies from "js-cookie";
import { validateSession } from "./services";
import { useLocation } from "react-router-dom";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const location = useLocation();

  useEffect(() => {
    const checkSession = async () => {
      const sessionId = Cookies.get("session-id");
      if (sessionId) {
        const valid = await validateSession(sessionId);
        if (valid) {
          setIsAuthenticated(true);
        } else {
          setIsAuthenticated(false);
          Cookies.remove("session-id");
          Cookies.remove("user-systemUsername");
          Cookies.remove("user-userType");
        }
      } else {
        setIsAuthenticated(false);
      }
      setIsLoading(false);
    };
    checkSession();
  }, [location.pathname]);

  return (
    <AuthContext.Provider value={{ isAuthenticated, setIsAuthenticated, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
