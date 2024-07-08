import LoginHeader from "../components/headers/LoginHeader";
import "./ResetPasswordPage.css";
import PasswordForm from "../components/inputs/PasswordForm";
import { useState } from "react";
import "../components/modals/Modals.css";
import InputForm from "../components/inputs/InputForm";
import { resetPassword } from "../utilities/services";
import { useNavigate, useParams } from "react-router-dom";
import { Trans, t } from "@lingui/macro";

export default function ResetPasswordPage() {
   const { token } = useParams();
   const [password, setPassword] = useState("");
   const [strength, setStrength] = useState(0);
   const [warningTypePassword, setWarningTypePassword] = useState("");
   const [warningTxtPassword, setWarningTxtPassword] = useState("");
   const [showTolltip, setShowTooltip] = useState(false);
   const [conditionsMet, setConditionsMet] = useState({
      upper: false,
      lower: false,
      number: false,
      special: false,
      length: false,
   });
   const [confirmPassword, setConfirmPassword] = useState("");
   const [warningTypeConfirmPassword, setWarningTypeConfirmPassword] = useState("");
   const [warningTxtConfirmPassword, setWarningTxtConfirmPassword] = useState("");
   const navigate = useNavigate();

   const handleOnBlurPassword = () => {
      if (strength >= 4) {
         setWarningTypePassword("success");
         setWarningTxtPassword(t`Password is strong`);
      } else {
         setWarningTypePassword("incorrect");
         setWarningTxtPassword(t`Password must be strong`);
      }
   };
   const updatePassword = (e) => {
      setPassword(e.target.value);
      setStrength(calculateStrength(e.target.value));

      validatePassword(e.target.value);
   };

   const calculateStrength = (password) => {
      let strength = 0;
      if (/\S/.test(password)) {
         strength++;
         if (password.length >= 6) {
            if (/[a-z]/.test(password)) strength++; // lowercase
            if (/[A-Z]/.test(password)) strength++; // uppercase
            if (/\d/.test(password)) strength++; // digits
         }
         if (strength === 4) {
            if (/\W/.test(password)) strength++; // special characters
         }
      }
      return strength;
   };
   const validatePassword = (value) => {
      const conditions = {
         upper: /[A-Z]/.test(value),
         lower: /[a-z]/.test(value),
         number: /\d/.test(value),
         special: /\W/.test(value),
         length: value.length >= 6,
      };
      setConditionsMet(conditions);
   };

   const handleOnBlurConfirmPassword = () => {
      if (password === confirmPassword) {
         setWarningTypeConfirmPassword("success");
         setWarningTxtConfirmPassword(t`Passwords match`);
      } else {
         setWarningTypeConfirmPassword("incorrect");
         setWarningTxtConfirmPassword(t`Passwords do not match`);
      }
   };

   const handleSubmit = (e) => {
      e.preventDefault();
      if (password === confirmPassword && strength >= 4) {
         resetPassword(password, token).then((response) => {
            if (response.status === 200) {
               navigate("/");
            }
         });
      }
   };

   return (
      <>
         <LoginHeader></LoginHeader>
         <div className="ilm-page" id="reset-pass-page">
            <form className="ilm-form" id="reset-pass-form" onSubmit={handleSubmit}>
               <div className="modal-content">
                  <h3 className="modal-title"><Trans>Reset Password</Trans></h3>
                  <div className="modal-body">
                     <div id="div-password">
                        <PasswordForm
                           label="Password"
                           type="password"
                           value={password}
                           setValue={updatePassword}
                           warningType={warningTypePassword}
                           warningTxt={warningTxtPassword}
                           handleOnBlur={handleOnBlurPassword}
                           showTolltip={showTolltip}
                           setShowTooltip={setShowTooltip}
                           conditionsMet={conditionsMet}
                           onBlurActive={true}
                        ></PasswordForm>
                        <div id="div-password-container">
                           <div id="pass-strength">
                              <div><Trans>Password Strength</Trans></div>
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
                                 {strength === 0 && (t`None`)}
                                 {strength === 1 && (t`Weak`)}
                                 {strength === 2 && (t`Fair`)}
                                 {strength === 3 && (t`Good`)}
                                 {strength === 4 && (t`Strong`)}
                                 {strength === 5 && (t`Very Strong`)}
                              </div>
                           </div>
                        </div>
                        <InputForm
                           label={t`Confirm Password`}
                           type="password"
                           value={confirmPassword}
                           setValue={setConfirmPassword}
                           warningType={warningTypeConfirmPassword}
                           warningTxt={warningTxtConfirmPassword}
                           handleOnBlur={handleOnBlurConfirmPassword}
                           onBlurActive={true}
                        ></InputForm>
                     </div>
                  </div>
                  <div className="modal-buttons">
                     <button type="submit" className="submit-button" id="submit-button-reset">
                     <Trans>Submit</Trans>
                     </button>
                  </div>
               </div>
            </form>
         </div>
      </>
   );
}
