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
import ResetPasswordPage from "./pages/ResetPasswordPage";
import EditProfilePage from "./pages/EditProfilePage";
import MailWebSocket from "./utilities/websockets/MailWebSocket";
import ResourcesPage from "./pages/ResourcesPage";
import ProjectProfilePageInfo from "./pages/ProjectProfilePageInfo";
import ProjectLogsPage from "./pages/ProjectLogsPage";
import ProjectCreationPage1 from "./pages/ProjectCreationPage1";
import ProjectCreationPage2 from "./pages/ProjectCreationPage2";
import ProjectChatPage from "./pages/ProjectChatPage";
import ProjectChatWebSocket from "./utilities/websockets/ProjectChatWebSocket";
import ProjectCreationPage3 from "./pages/ProjectCreationPage3";
import ProjectMembersPage from "./pages/ProjectMembersPage";
import EditProjectPage from "./pages/EditProjectPage";
import ProjectPlanPage from "./pages/ProjectPlanPage";
import ProjectProfileResourcesPage from "./pages/ProjectProfileResourcesPage";
import PrivateRoute from "./utilities/PrivateRoute";
import PublicRoute from "./utilities/PublicRoute";
import { AuthProvider } from "./utilities/AuthContext";
import StatisticsPdfPage from "./pages/StatisticsPdfPage";
import I18nLoader, { useLanguage } from "./I18nLoader";

const AppWithWebSocket = () => {
  const location = useLocation();
  const isInbox = location.pathname === "/mail/inbox";
  const isProjectChat = location.pathname.match(/\/project\/[^/]+\/chat/);

  return (
    <>
      <MailWebSocket isInbox={isInbox} />
      {isProjectChat && (
        <ProjectChatWebSocket projectId={isProjectChat[0].split("/")[2]} />
      )}
      <Routes>
        <Route path="/" element={<PublicRoute component={App} />} />
        <Route path="/register" element={<Register />} />
        <Route path="/create-profile/:token" element={<CreateProfilePage />} />
        <Route
          path="/projects"
          element={<PrivateRoute component={ProjectsPage} />}
        />
        <Route path="/reset-password/:token" element={<ResetPasswordPage />} />
        <Route
          path="/profile/:systemUsername"
          element={<PrivateRoute component={ProfilePage} />}
        />
        <Route
          path="/profile/:systemUsername/:section"
          element={<PrivateRoute component={ProfilePage} />}
        />
        <Route
          path="/project/:systemProjectName"
          element={<PrivateRoute component={() => <Navigate to="info" />} />}
        />
        <Route
          path="/project/:systemProjectName/info"
          element={<PrivateRoute component={ProjectProfilePageInfo} />}
        />
        <Route
          path="/project/:systemProjectName/logs"
          element={<PrivateRoute component={ProjectLogsPage} />}
        />
        <Route
          path="/project/:systemProjectName/chat"
          element={<PrivateRoute component={ProjectChatPage} />}
        />
        <Route
          path="/project/:systemProjectName/resources"
          element={<PrivateRoute component={ProjectProfileResourcesPage} />}
        />
        <Route
          path="/project/:systemProjectName/members"
          element={<PrivateRoute component={ProjectMembersPage} />}
        />
        <Route
          path="/editProfile"
          element={<PrivateRoute component={EditProfilePage} />}
        />
        <Route
          path="/mail/inbox"
          element={<PrivateRoute component={InboxMailPage} />}
        />
        <Route
          path="/mail/sent"
          element={<PrivateRoute component={SentMailPage} />}
        />
        <Route
          path="/resources"
          element={<PrivateRoute component={ResourcesPage} />}
        />
        <Route
          path="/myprojects"
          element={<PrivateRoute component={MyProjectsPage} />}
        />
        <Route
          path="/create-project"
          element={<PrivateRoute component={() => <Navigate to="info" />} />}
        />
        <Route
          path="/create-project/info"
          element={<PrivateRoute component={ProjectCreationPage1} />}
        />
        <Route
          path="/create-project/:systemProjectName/members"
          element={<PrivateRoute component={ProjectCreationPage2} />}
        />
        <Route
          path="/create-project/:systemProjectName/resources"
          element={<PrivateRoute component={ProjectCreationPage3} />}
        />
        <Route
          path="/editProject/:systemProjectName"
          element={<PrivateRoute component={EditProjectPage} />}
        />
        <Route
          path="/project/:systemProjectName/plan/:taskSystemTitle?"
          element={<PrivateRoute component={ProjectPlanPage} />}
        />
        <Route
          path="/statistics"
          element={<PrivateRoute component={StatisticsPdfPage} />}
        />
      </Routes>
    </>
  );
};

const root = ReactDOM.createRoot(document.getElementById("root"));

root.render(
  <React.StrictMode>
    <Router>
      <AuthProvider>
        <I18nLoader>
          <AppWithWebSocket />
        </I18nLoader>
      </AuthProvider>
    </Router>
  </React.StrictMode>
);

reportWebVitals();
