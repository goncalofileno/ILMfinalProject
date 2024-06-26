import InputForm from "../inputs/InputForm";
import "./LoginForm.css";
import { useNavigate } from "react-router-dom";
import { useMediaQuery } from "react-responsive";
import { useState } from "react";
import { loginUser } from "../../utilities/services";

export default function LoginForm({ setShowAlert, setIsModalActive }) {
  const navigate = useNavigate();
  const isPhone = useMediaQuery({ maxWidth: 576 });
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e) => {
   e.preventDefault();
   loginUser(email, password).then((response) => {
       if (response.status === 200) {
         
           if (response.data && response.data.auxiliarToken) { // Verifique se response.data está definido
               navigate(`/create-profile/${response.data.auxiliarToken}`);
           } else {
               navigate("/projects");
           }
       } else if (response.status === 401) {
           setShowAlert(true);
       } else if (response.status === 404) {
           setShowAlert(true);
       } else {
           setShowAlert(true);
       }
   }).catch((error) => {
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
              Register
            </button>

            <button type="submit" className="submit-button" id="login-button">
              Login
            </button>
          </div>
          <div id="forgot-pass-div" onClick={() => setIsModalActive(true)}>
            Forgot your password
          </div>
          <div id="line-or-div">
            <div className="or-line-register" id="or-left-line">
              <hr />
            </div>
            <div>or</div>
            <div className="or-line-register" id="or-right-line">
              {" "}
              <hr />
            </div>
          </div>
          <div id="sign-up-with">
            <div>Sign up with:</div>
            <div id="sign-up-with-icons">
              <i className="fab fa-google fa-lg"></i>
            </div>
          </div>
        </div>
      </form>
    </div>
  );
}
