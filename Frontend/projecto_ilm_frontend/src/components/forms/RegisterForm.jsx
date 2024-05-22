import "./RegisterForm.css";
import InputForm from "../inputs/InputForm";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { registerUser } from "../../utilities/services";

export default function RegisterForm() {
   const [email, setEmail] = useState("");
   const [password, setPassword] = useState("");
   const [strength, setStrength] = useState(0);
   const [confirmPassword, setConfirmPassword] = useState("");
   const [emailRequirementNotMet, setEmailRequirementNotMet] = useState(false);
   const [passwordRequirementNotMet, setPasswordRequirementNotMet] = useState(false);
   const [confirmPasswordRequirementNotMet, setConfirmPasswordRequirementNotMet] = useState(false);
   const navigate = useNavigate();

   const handleSubmit = (e) => {
      e.preventDefault();
      if (email === "") setEmailRequirementNotMet(true);
      if (password === "") setPasswordRequirementNotMet(true);
      if (confirmPassword === "") setConfirmPasswordRequirementNotMet(true);
      if (strength >= 4) {
         registerUser(email, password).then((response) => {
            if (response.status === 201) {
               navigate("/");
            }
         });
      }
   };

   const handleCancel = () => {
      navigate("/");
   };

   const updateEmail = (e) => {
      setEmail(e.target.value);
      setEmailRequirementNotMet(false);
   };
   const updatePassword = (e) => {
      setPassword(e.target.value);
      setStrength(calculateStrength(e.target.value));
      setPasswordRequirementNotMet(false);
   };
   const updateConfirmPassword = (e) => {
      setConfirmPassword(e.target.value);
      setConfirmPasswordRequirementNotMet(false);
   };

   const calculateStrength = (password) => {
      let strength = 0;
      if (/[a-z]/.test(password)) strength++; // lowercase
      if (/[A-Z]/.test(password)) strength++; // uppercase
      if (/\d/.test(password)) strength++; // digits
      if (/\W/.test(password)) strength++; // special characters
      if (password.length >= 6) strength++; // length
      return strength;
   };
   return (
      <div className="login-register-form-container">
         <form className="ilm-form login-register-form">
            <div className="form-content">
               <div className="form-title" id="register-form-title">
                  Register
               </div>
               <div className="register-inputs">
                  <InputForm
                     label="Email"
                     type="email"
                     value={email}
                     setValue={updateEmail}
                     requirementNotMet={emailRequirementNotMet}
                  ></InputForm>
                  <div>
                     <InputForm
                        label="Password"
                        type="password"
                        value={password}
                        setValue={updatePassword}
                        requirementNotMet={passwordRequirementNotMet}
                     ></InputForm>
                     <div id="pass-strength">
                        <div>Password Strength</div>
                        <meter max="5" value={strength}></meter>
                     </div>
                     <div id="pass-strength-string">
                        <div></div>
                        <div
                           style={{
                              color:
                                 strength === 0
                                    ? "black"
                                    : strength === 1
                                    ? "red"
                                    : strength === 2
                                    ? "orange"
                                    : strength === 3
                                    ? "yellow"
                                    : strength === 4
                                    ? "green"
                                    : strength === 5 && "green",
                           }}
                        >
                           {strength === 0 && "None"}
                           {strength === 1 && "Weak"}
                           {strength === 2 && "Fair"}
                           {strength === 3 && "Good"}
                           {strength === 4 && "Strong"}
                           {strength === 5 && "Very Strong"}
                        </div>
                     </div>
                  </div>
                  <InputForm
                     label="Confirm Password"
                     type="password"
                     value={confirmPassword}
                     setValue={updateConfirmPassword}
                     requirementNotMet={confirmPasswordRequirementNotMet}
                  ></InputForm>
                  <div id="buttons-register-form">
                     <button
                        type="button"
                        className="secondary-button"
                        id="cancel-register-form"
                        onClick={handleCancel}
                     >
                        Login
                     </button>
                     <div></div>
                     <button type="submit" className="submit-button" id="submit-register-form" onClick={handleSubmit}>
                        Register
                     </button>
                  </div>
               </div>
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
         </form>
      </div>
   );
}
