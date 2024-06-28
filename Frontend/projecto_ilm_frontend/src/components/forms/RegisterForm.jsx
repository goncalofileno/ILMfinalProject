import "./RegisterForm.css";
import InputForm from "../inputs/InputForm";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { registerUser, checkEmail } from "../../utilities/services";
import PasswordForm from "../inputs/PasswordForm";

export default function RegisterForm({
  setShowAlert,
  setRegisterMessage,
  setRegisterMessageType,
}) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [strength, setStrength] = useState(0);
  const [confirmPassword, setConfirmPassword] = useState("");
  const [warningTypeEmail, setWarningTypeEmail] = useState("");
  const [warningTxtEmail, setWarningTxtEmail] = useState("");
  const [warningTypePassword, setWarningTypePassword] = useState("");
  const [warningTxtPassword, setWarningTxtPassword] = useState("");
  const [showTolltip, setShowTooltip] = useState(false);
  const [warningTypeConfirmPassword, setWarningTypeConfirmPassword] =
    useState("");
  const [warningTxtConfirmPassword, setWarningTxtConfirmPassword] =
    useState("");

  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    if (warningTypePassword === "incorrect") setShowTooltip(true);
    if (
      email !== "" &&
      password !== "" &&
      confirmPassword !== "" &&
      warningTypeEmail === "success" &&
      warningTypePassword === "success" &&
      confirmPassword === password
    ) {
      registerUser(email, password).then((response) => {
        if (response.status === 201) {
          setRegisterMessageType("success");
          setRegisterMessage(
            "Registered successfully. Please Verify your email."
          );
          setShowAlert(true);
          setTimeout(() => {
            setShowAlert(false);
            navigate("/");
          }, 2000);
        }
      });
    } else {
      setRegisterMessageType("danger");
      setRegisterMessage("Invalid data. Please try again.");
      setShowAlert(true);
      setTimeout(() => {
        setShowAlert(false);
      }, 2000);
    }
  };

  const handleOnBlurEmail = () => {
    checkEmail(email).then((response) => {
      if (response.status == 200) {
        setWarningTypeEmail("success");
        setWarningTxtEmail("Email is valid");
      } else if (response.status == 409 || response.status == 400) {
        setWarningTypeEmail("incorrect");
        setWarningTxtEmail("This email is invalid");
      }
    });
  };

  const handleOnBlurPassword = () => {
    if (strength >= 4) {
      setWarningTypePassword("success");
      setWarningTxtPassword("Password is strong");
    } else {
      setWarningTypePassword("incorrect");
      setWarningTxtPassword("Password must be strong");
    }
  };

  const handleOnBlurConfirmPassword = () => {
    if (password === confirmPassword) {
      setWarningTypeConfirmPassword("success");
      setWarningTxtConfirmPassword("Passwords match");
    } else {
      setWarningTypeConfirmPassword("incorrect");
      setWarningTxtConfirmPassword("Passwords do not match");
    }
  };

  const handleCancel = () => {
    navigate("/");
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

  const [conditionsMet, setConditionsMet] = useState({
    upper: false,
    lower: false,
    number: false,
    special: false,
    length: false,
  });

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
              setValue={setEmail}
              warningType={warningTypeEmail}
              warningTxt={warningTxtEmail}
              handleOnBlur={handleOnBlurEmail}
              onBlurActive={true}
            ></InputForm>
            <div>
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
              setValue={setConfirmPassword}
              warningType={warningTypeConfirmPassword}
              warningTxt={warningTxtConfirmPassword}
              handleOnBlur={handleOnBlurConfirmPassword}
              onBlurActive={true}
            ></InputForm>
            <div id="buttons-register-form">
              <button
                type="button"
                className="secondary-button"
                id="cancel-register-form"
                onClick={handleCancel}
              >
                Cancel
              </button>
              <div></div>
              <button
                type="submit"
                className="submit-button"
                id="submit-register-form"
                onClick={handleSubmit}
              >
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
