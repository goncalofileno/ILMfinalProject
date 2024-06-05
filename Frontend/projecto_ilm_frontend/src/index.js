import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import ReactDOM from "react-dom/client";
import App from "./pages/App";
import Register from "./pages/Register";
import CreateProfilePage from "./pages/CreateProfilePage";
import ProjectsPage from "./pages/ProjectsPage";
import ProfilePage from "./pages/ProfilePage";
import reportWebVitals from "./reportWebVitals";
import { BrowserRouter as Router } from "react-router-dom";
import { Route, Routes } from "react-router-dom";
import "./index.css";
import { Alert } from "react-bootstrap";
import ResetPasswordPage from "./pages/ResetPasswordPage";

const root = ReactDOM.createRoot(document.getElementById("root"));

root.render(
   <React.StrictMode>
      <Router>
         <Routes>
            <Route index element={<App />} />
            <Route path="/register" element={<Register />} />
            <Route path="/create-profile/:token" element={<CreateProfilePage />} />
            <Route path="/projects" element={<ProjectsPage />} />
            <Route path="/reset-password/:token" element={<ResetPasswordPage />} />
            <Route path="/profile/:systemUsername" element={<ProfilePage />} />
         </Routes>
      </Router>
   </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
