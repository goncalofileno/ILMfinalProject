import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import ReactDOM from "react-dom/client";
import App from "./pages/App";
import Register from "./pages/Register";
import CreateProfilePage from "./pages/CreateProfilePage";
import ProjectsPage from "./pages/ProjectsPage";
import reportWebVitals from "./reportWebVitals";
import { BrowserRouter as Router } from "react-router-dom";
import { Route, Routes } from "react-router-dom";
import "./index.css";
import { Alert } from "react-bootstrap";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
   <React.StrictMode>
      <Alert variant="danger" className="alerts-message" style={{visibility: "hidden"}}>
         This is a danger alert—check it out!
      </Alert>
      <Router>
         <Routes>
            <Route index element={<App />} />
            <Route path="/register" element={<Register />} />
            <Route path="/create-profile/:token" element={<CreateProfilePage />} />
            <Route path="/projects" element={<ProjectsPage />} />
         </Routes>
      </Router>
   </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
