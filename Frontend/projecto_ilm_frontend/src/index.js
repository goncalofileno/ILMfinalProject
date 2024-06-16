import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import ReactDOM from "react-dom/client";
import App from "./pages/App";
import Register from "./pages/Register";
import CreateProfilePage from "./pages/CreateProfilePage";
import ProjectsPage from "./pages/ProjectsPage";
import ProfilePage from "./pages/ProfilePage";
import InboxMailPage from "./pages/InboxMailPage";
import SentMailPage from "./pages/SentMailPage";
import MyProjectsPage from "./pages/MyProjectsPage";
import reportWebVitals from "./reportWebVitals";
import {
  BrowserRouter as Router,
  Route,
  Routes,
  useLocation,
} from "react-router-dom";
import "./index.css";
import { Alert } from "react-bootstrap";
import ResetPasswordPage from "./pages/ResetPasswordPage";
import EditProfilePage from "./pages/EditProfilePage";
import MailWebSocket from "./utilities/websockets/MailWebSocket";
import ResourcesPage from "./pages/ResourcesPage";

const AppWithWebSocket = () => {
  const location = useLocation();
  const isInbox = location.pathname === "/mail/inbox";
  return (
    <>
      <MailWebSocket isInbox={isInbox} />
      <Routes>
        <Route index element={<App />} />
        <Route path="/register" element={<Register />} />
        <Route path="/create-profile/:token" element={<CreateProfilePage />} />
        <Route path="/projects" element={<ProjectsPage />} />
        <Route path="/reset-password/:token" element={<ResetPasswordPage />} />
        <Route path="/profile/:systemUsername" element={<ProfilePage />} />
        <Route path="/editProfile" element={<EditProfilePage />} />
        <Route path="/mail/inbox" element={<InboxMailPage />} />
        <Route path="/mail/sent" element={<SentMailPage />} />
        <Route path="/resources" element={<ResourcesPage />} />
        <Route path="/myprojects" element={<MyProjectsPage />} />
      </Routes>
    </>
  );
};

const root = ReactDOM.createRoot(document.getElementById("root"));

root.render(
  <React.StrictMode>
    <Router>
      <AppWithWebSocket />
    </Router>
  </React.StrictMode>
);

reportWebVitals();
