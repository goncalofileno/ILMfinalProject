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
  Navigate,
  useLocation,
} from "react-router-dom";
import "./index.css";
import { Alert } from "react-bootstrap";
import ResetPasswordPage from "./pages/ResetPasswordPage";
import EditProfilePage from "./pages/EditProfilePage";
import MailWebSocket from "./utilities/websockets/MailWebSocket";
import ResourcesPage from "./pages/ResourcesPage";
import ProjectProfilePageInfo from "./pages/ProjectProfilePageInfo";
import ProjectLogsPage from "./pages/ProjectLogsPage";
import ProjectCreationPage1 from "./pages/ProjectCreationPage1";

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
        <Route
          path="/profile/:systemUsername"
          element={<Navigate to="projects" />}
        />
        <Route
          path="/profile/:systemUsername/:section"
          element={<ProfilePage />}
        />
        <Route
          path="/project/:systemProjectName"
          element={<Navigate to="info" />}
        />
        <Route
          path="/project/:systemProjectName/info"
          element={<ProjectProfilePageInfo />}
        />
        <Route
          path="/project/:systemProjectName/logs"
          element={<ProjectLogsPage />}
        />
        <Route path="/editProfile" element={<EditProfilePage />} />
        <Route path="/mail/inbox" element={<InboxMailPage />} />
        <Route path="/mail/sent" element={<SentMailPage />} />
        <Route path="/resources" element={<ResourcesPage />} />
        <Route path="/myprojects" element={<MyProjectsPage />} />
        <Route path="/create-project/info" element={<ProjectCreationPage1 />} />
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
