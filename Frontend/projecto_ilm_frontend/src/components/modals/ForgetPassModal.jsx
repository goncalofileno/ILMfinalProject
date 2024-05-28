import "./Modals.css";
import "./ForgetPassModal.css";
import InputForm from "../inputs/InputForm";
import { useState } from "react";
import { checkEmail, forgetPassword } from "../../utilities/services";
import alertStore from "../../stores/alertStore";

export default function ForgetPassModal({ isModalActive, setIsModalActive }) {
   const [email, setEmail] = useState("");
   const [warningType, setWarningType] = useState("");
   const [warningTxt, setWarningTxt] = useState("");
   const { setMessage, setVisibility, setType } = alertStore();

   const handleOnBlur = () => {
      checkEmail(email).then((response) => {
         if (response.status !== 409) {
            setWarningType("incorrect");
            setWarningTxt("Email not found");
         } else {
            setWarningTxt("");
         }
      });
   };

   const handleSubmit = (e) => {
      e.preventDefault();
      forgetPassword(email).then((response) => {
         if (response.status === 200) {
            setIsModalActive(false);
            setVisibility("visible");
            setMessage("Password reset email sent");
            setType("success");
         } else {
            setVisibility("visible");
            setMessage("Error sending password reset email");
            setType("danger");
         }
      });
   };

   return (
      <>
         {isModalActive && (
            <>
               <div className="modal-background" onClick={() => setIsModalActive(false)}></div>
               <form className="ilm-modal" onSubmit={handleSubmit}>
                  <div className="modal-content">
                     <h3 className="modal-title">Forget Password</h3>
                     <div className="modal-body">
                        <InputForm
                           label="Enter your email:"
                           type="text"
                           value={email}
                           setValue={setEmail}
                           warningType={warningType}
                           warningTxt={warningTxt}
                           handleOnBlur={handleOnBlur}
                           onBlurActive={true}
                        ></InputForm>
                     </div>
                     <div className="modal-buttons">
                        <button type="submit" className="submit-button" id="submit-forget-pass">
                           Submit
                        </button>
                     </div>
                  </div>
               </form>
            </>
         )}
      </>
   );
}
