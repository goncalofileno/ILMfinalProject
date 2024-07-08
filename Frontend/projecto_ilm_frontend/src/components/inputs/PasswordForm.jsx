import "./InputForm.css";
import { useEffect, useState } from "react";
import { Tooltip, OverlayTrigger } from "react-bootstrap";
import { Trans, t } from "@lingui/macro";

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
            <p><Trans>Password must contain:</Trans></p>
            <ul>
               <li className={conditionsMet.upper ? "text-success" : "text-danger"}><Trans>At least one uppercase letter</Trans></li>
               <li className={conditionsMet.lower ? "text-success" : "text-danger"}><Trans>At least one lowercase letter</Trans></li>
               <li className={conditionsMet.number ? "text-success" : "text-danger"}><Trans>At least one number</Trans></li>
               <li className={conditionsMet.special ? "text-success" : "text-danger"}>
               <Trans>At least one special character (!@#$%^&*)</Trans>
               </li>
               <li className={conditionsMet.length ? "text-success" : "text-danger"}><Trans>Minimum 8 characters</Trans></li>
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
