import "./InputForm.css";
import { useEffect, useState } from "react";
import { Tooltip, OverlayTrigger } from "react-bootstrap";

export default function PasswordForm({
   label,
   type,
   value,
   setValue,
   warningType,
   warningTxt,
   handleOnBlur,
   setShowTooltip,
   showTolltip,
   conditionsMet,
}) {
   const [visibility, setVisibility] = useState("hidden");

   const handleBlur = () => {
      if (value !== "") {
         handleOnBlur();
         setVisibility("visible");
      }
   };

   const renderTooltip = (props) => (
      <Tooltip id="password-tooltip" {...props}>
         <div>
            <p>Password must contain:</p>
            <ul>
               <li className={conditionsMet.upper ? "text-success" : "text-danger"}>At least one uppercase letter</li>
               <li className={conditionsMet.lower ? "text-success" : "text-danger"}>At least one lowercase letter</li>
               <li className={conditionsMet.number ? "text-success" : "text-danger"}>At least one number</li>
               <li className={conditionsMet.special ? "text-success" : "text-danger"}>
                  At least one special character (!@#$%^&*)
               </li>
               <li className={conditionsMet.length ? "text-success" : "text-danger"}>Minimum 8 characters</li>
            </ul>
         </div>
      </Tooltip>
   );
   return (
      <div className="inputForm">
         <label htmlFor={label}>{label}</label>

         <div className="input-div">
            <OverlayTrigger placement="top" show={showTolltip} delay={{ show: 250, hide: 400 }} overlay={renderTooltip}>
               <input
                  onBlur={handleBlur}
                  type={type}
                  className={`ilm-input ${
                     visibility === "visible"
                        ? warningType === "success"
                           ? "success-input"
                           : warningType === "incorrect" && "incorrect-input"
                        : ""
                  }`}
                  id={label}
                  value={value}
                  onChange={setValue}
                  onFocus={() => setVisibility("hidden")}
               />
            </OverlayTrigger>
            <div
               className={`warning-txt-inputForm ${
                  warningType === "success" ? "success-warning" : warningType === "incorrect" && "incorrect-warning"
               }`}
               style={{ visibility: visibility }}
            >
               {warningTxt}
            </div>
         </div>
      </div>
   );
}
