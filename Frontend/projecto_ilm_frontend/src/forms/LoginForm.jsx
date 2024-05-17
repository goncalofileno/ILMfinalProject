import InputForm from "../inputs/InputForm";
import "./LoginForm.css";
import { useNavigate } from "react-router-dom";

export default function LoginForm() {
   const navigate = useNavigate();
   return (
      <div className="login-register-form-container">
         <form className="ilm-form login-register-form">
            <div className="form-content">
               <div className="form-title" id="login-form-title">
                  Login
               </div>
               <div id="login-inputs">
                  <InputForm label="Email" type="email"></InputForm>
                  <InputForm label="Password" type="password"></InputForm>
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
               <div id="forgot-pass-div">Forgot your password</div>
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
                     <i class="fab fa-google fa-lg"></i>
                  </div>
               </div>
            </div>
         </form>
      </div>
   );
}
