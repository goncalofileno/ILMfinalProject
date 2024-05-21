import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import ReactDOM from "react-dom/client";
import App from "./pages/App";
import Register from "./pages/Register";
import CreateProfile from "./pages/CreateProfile";
import reportWebVitals from "./reportWebVitals";
import { BrowserRouter as Router } from "react-router-dom";
import { Route, Routes } from "react-router-dom";
import "./index.css";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
   <React.StrictMode>
      <Router>
         <Routes>
            <Route index element={<App />} />
            <Route path="/register" element={<Register />} />
            <Route path="/createprofile" element={<CreateProfile />} />
         </Routes>
      </Router>
   </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
