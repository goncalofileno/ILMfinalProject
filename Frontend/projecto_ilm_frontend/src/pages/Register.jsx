import RegisterForm from "../forms/RegisterForm";
import LoginHeader from "../headers/LoginHeader";
import "./App.css";
import { useRef } from "react";

export default function Register() {
   const contentRef = useRef(null);

   const scrollToContent = () => {
      contentRef.current.scrollIntoView({ behavior: "smooth" });
   };
   return (
      <>
         <LoginHeader />
         <div className="flex-column">
            <div className="flex-row">
               <div className="space-after-table">
                  {" "}
                  <h4>Hello! 👋</h4>
                  <h4>We're Innovation Lab Management.</h4>
                  <div className="background-img-container">
                     <div className="background-img-app"></div>
                  </div>
               </div>
               <div className="ilm-description-login-register">
                  <h1 className="ilm-general-title">
                     Where Ideas <span>Take Flight</span>
                  </h1>
                  <div>
                     Welcome to our Innovation Lab Management app, where creativity meets collaboration and ideas turn
                     into reality. With our app, you can manage and track your projects seamlessly, from brainstorming
                     to implementation, ensuring every idea has the potential to flourish. Join us in driving progress
                     and inspiring change, one innovative project at a time.
                  </div>
                  <button className="submit-button" id="projects-button" onClick={scrollToContent}>
                     Some of our Projects
                  </button>
               </div>
               <RegisterForm />;
            </div>
            <div className="ilm-projects-login-page ilm-page2" ref={contentRef}>
               <h1 className="ilm-general-title" id="projects-title">
                  Projects
               </h1>
            </div>
         </div>
      </>
   );
}
