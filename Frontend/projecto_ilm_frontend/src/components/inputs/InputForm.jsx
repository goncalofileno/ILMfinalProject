import "./InputForm.css";
import { useState } from "react";
import React from "react";

// The InputForm component is used to create a form input field with a label. The component takes in the following props:
// label: The label for the input field.
// type: The type of input field (e.g., text, password).
// value: The value of the input field.
// setValue: A function to set the value of the input field.
// warningType: The type of warning to display (e.g., success, incorrect).
// warningTxt: The text to display as a warning.
// handleOnBlur: A function to handle the onBlur event.
// onBlurActive: A boolean to determine if the onBlur event should be active.

export default function InputForm({
  label,
  type,
  value,
  setValue,
  warningType,
  warningTxt,
  handleOnBlur,
  onBlurActive,
  disabled,
}) {
  const [visibility, setVisibility] = useState("hidden");

  const handleBlur = () => {
    if (value !== "") {
      handleOnBlur();
      setVisibility("visible");
    }
  };

  const handleBlurInactive = () => {
    console.log(" ");
  };

  return (
    <div className="inputForm">
      <label htmlFor={label} style={{ color: disabled && "grey" }}>
        {label}
      </label>
      <div className="input-div">
        <input
          onBlur={onBlurActive ? handleBlur : handleBlurInactive}
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
          onChange={(e) => setValue(e.target.value)}
          onFocus={() => setVisibility("hidden")}
          disabled={disabled !== null && disabled}
        />
        <div
          className={`warning-txt-inputForm ${
            warningType === "success"
              ? "success-warning"
              : warningType === "incorrect" && "incorrect-warning"
          }`}
          style={{ visibility: visibility }}
        >
          {warningTxt}
        </div>
      </div>
    </div>
  );
}
