import InputForm from "../inputs/InputForm";
import "./LoginForm.css";
import { useNavigate } from "react-router-dom";
import { useMediaQuery } from "react-responsive";
import { useState } from "react";
import { loginUser } from "../../utilities/services";
import { useAuth } from "../../utilities/AuthContext";
import { Trans, t } from "@lingui/macro";

export default function LoginForm({ setShowAlert, setIsModalActive }) {
  const navigate = useNavigate();
  const isPhone = useMediaQuery({ maxWidth: 576 });
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const { setIsAuthenticated } = useAuth();

  const handleSubmit = (e) => {
    e.preventDefault();
    loginUser(email, password)
      .then((response) => {
        if (response.status === 200) {
          // Atualizar o estado de autenticação
          setIsAuthenticated(true);
  
          if (response.data && response.data.auxiliarToken) {
            navigate(`/create-profile/${response.data.auxiliarToken}`);
          } else {
            if (response.data.hasProjects) {
              navigate("/myprojects");
            } else {
              navigate("/projects");
            }
          }
        } else {
          setShowAlert(true);
        }
      })
      .catch((error) => {
        console.error("Error during login process", error);
        setShowAlert(true);
      });
  };

  return (
    <div
      className="login-register-form-container"
      style={{ marginTop: isPhone && "6vh" }}
    >
      <form className="ilm-form login-register-form" onSubmit={handleSubmit}>
        <div className="form-content">
          <div className="form-title" id="login-form-title">
            Login
          </div>
          <div id="login-inputs">
            <InputForm
              label="Email"
              type="email"
              value={email}
              setValue={setEmail}
              onBlurActive={false}
            ></InputForm>
            <InputForm
              label="Password"
              type="password"
              value={password}
              setValue={setPassword}
              onBlurActive={false}
            ></InputForm>
          </div>
          <div id="login-buttons">
            <button
              type="button"
              className="secondary-button"
              id="register-button-login"
              onClick={() => navigate("/register")}
            >
              <Trans>Register</Trans>
            </button>

            <button type="submit" className="submit-button" id="login-button">
              Login
            </button>
          </div>
          <div id="forgot-pass-div" onClick={() => setIsModalActive(true)}>
            <Trans>Forgot your password</Trans>
          </div>
        </div>
      </form>
    </div>
  );
}
