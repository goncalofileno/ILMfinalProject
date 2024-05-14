import "./RegisterForm.css";
import InputForm from "../inputs/InputForm";
import { useState } from "react";

export default function RegisterForm() {
   const [password, setPassword] = useState("");
   const [strength, setStrength] = useState(0);

   const updatePassword = (event) => {
      setPassword(event.target.value);
      setStrength(calculateStrength(event.target.value));
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
      <div id="register-form-container">
         <form className="ilm-form" id="register-form">
            <div className="form-title" id="register-form-title">
               Register
            </div>
            <div id="register-form-content">
               <div id="register-inputs">
                  <InputForm label="Email" type="email" isRequired={true}></InputForm>
                  <div>
                     <InputForm
                        label="Password"
                        type="password"
                        isRequired={true}
                        value={password}
                        setValue={updatePassword}
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
                  <InputForm label="Confirm Password" type="password" isRequired={true}></InputForm>
                  <button type="submit">Register</button>
               </div>
            </div>
         </form>
      </div>
   );
}
