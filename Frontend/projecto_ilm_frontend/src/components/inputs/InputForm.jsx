import "./InputForm.css";

export default function InputForm({ label, type, value, setValue, requirementNotMet }) {
   return (
      <div className="inputForm">
         <label htmlFor={label}>{label}</label>
         <div className="input-div">
            <input type={type} className="ilm-input" id={label} value={value} onChange={setValue} />
            <div
               className="required-field-inputForm"
               style={{ visibility: requirementNotMet === true ? "visible" : "hidden" }}
            >
               Please complete this required field.
            </div>
         </div>
      </div>
   );
}
