import "./Modals.css";
import "./ForgetPassModal.css";
import InputForm from "../inputs/InputForm";
import { useState } from "react";
import { checkEmail } from "../../utilities/services";

export default function ForgetPassModal({ isModalActive, setIsModalActive }) {
   const [email, setEmail] = useState("");
   const [warningType, setWarningType] = useState("");
   const [warningTxt, setWarningTxt] = useState("");

   const handleOnBlur = () => {
      checkEmail(email).then((response) => {
         if (response.status !== 409) {
            setWarningType("incorrect");
            setWarningTxt("Email not found");
         }
      });
   };

   return (
      <>
         {isModalActive && (
            <div className="modal-background" onClick={() => setIsModalActive(false)}>
               <div className="ilm-modal">
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
                        <button className="submit-button" id="submit-forget-pass">
                           Submit
                        </button>
                     </div>
                  </div>
               </div>
            </div>
         )}
      </>
   );
}
