import "./InputForm.css";
import { useState } from "react";

export default function PasswordForm({ label, type, value, setValue, warningType, warningTxt, handleOnBlur }) {
   const [visibility, setVisibility] = useState("hidden");

   const handleBlur = () => {
      if (value !== "") {
         handleOnBlur();
         setVisibility("visible");
      }
   };

   return (
      <div className="inputForm">
         <label htmlFor={label}>{label}</label>
         <div className="input-div">
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
