import "./InputForm.css";

export default function InputForm({ label, type, isRequired, value, setValue }) {
   return (
      <div className="inputForm">
         <label htmlFor={label}>{label}</label>
         <input
            type={type}
            className="ilm-input"
            id={label}
            value={value}
            onChange={setValue}
            required={isRequired ? true : false}
         />
      </div>
   );
}
