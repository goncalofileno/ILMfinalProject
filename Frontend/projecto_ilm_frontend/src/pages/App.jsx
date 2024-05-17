import "./App.css";
import { useNavigate } from "react-router-dom";
import LoginForm from "../forms/LoginForm";
import LoginHeader from "../headers/LoginHeader";
import { useRef } from "react";
import LoginProjectsCards from "../cards/LoginProjectsCards";

function App() {
   const navigate = useNavigate();
   const contentRef = useRef(null);
   const loginRef = useRef(null);

   const scrollToContent = () => {
      contentRef.current.scrollIntoView({ behavior: "smooth" });
   };

   const scrollToLogin = () => {
      loginRef.current.scrollIntoView({ behavior: "smooth" });
   };

   return (
      <>
         <LoginHeader />
         <div className="flex-column" ref={loginRef}>
            <div className="flex-row" id="row-img-description-title">
               <div className="flex-column" id="img-description-app">
                  <h1 className="ilm-general-title" id="slogan-app">
                     Where Ideas <span>Take Flight</span>
                  </h1>
                  <div className="flex-row" id="row-img-description">
                     <div className="space-after-table">
                        <div className="background-img-container">
                           <div className="background-img-app"></div>
                        </div>
                     </div>
                     <div className="ilm-description-login-register">
                        <div>
                           Welcome to our Innovation Lab Management app, where creativity meets collaboration and ideas
                           turn into reality. With our app, you can manage and track your projects seamlessly, from
                           brainstorming to implementation, ensuring every idea has the potential to flourish. Join us
                           in driving progress and inspiring change, one innovative project at a time.
                        </div>
                        <button className="submit-button" id="projects-button" onClick={scrollToContent}>
                           Some of our Projects
                        </button>
                     </div>
                  </div>
               </div>
               <LoginForm />
            </div>
            <div className="ilm-projects-login-page ilm-page2" ref={contentRef}>
               <h1 className="ilm-general-subTitle" id="projects-title">
                  Projects
               </h1>
               <div className="flex-row-project-cards">
                  <div className="flex-column-cards">
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color1"
                        title="Aviation"
                        description="From software architectures to system testing, we’ll look after your project from take-off to landing, combining our broad expertise with an in-depth knowledge of the rigorous safety standards in aviation."
                        scrollToRef={scrollToLogin}
                     />
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color1"
                        title="Defence & Security"
                        description="Peacekeeping relies on real-time information. We offer an arsenal of data system, technology and coordination support to make sure your defence projects go without a hitch."
                     />
                  </div>
                  <div className="flex-column-cards">
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color2"
                        title="Defence & Security"
                        description="Peacekeeping relies on real-time information. We offer an arsenal of data system, technology and coordination support to make sure your defence projects go without a hitch."
                     />
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color2"
                        title="Defence & Security"
                        description="Peacekeeping relies on real-time information. We offer an arsenal of data system, technology and coordination support to make sure your defence projects go without a hitch."
                     />
                  </div>
                  <div className="flex-column-cards">
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color3"
                        title="Defence & Security"
                        description="Peacekeeping relies on real-time information. We offer an arsenal of data system, technology and coordination support to make sure your defence projects go without a hitch."
                     />
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color3"
                        title="Defence & Security"
                        description="Peacekeeping relies on real-time information. We offer an arsenal of data system, technology and coordination support to make sure your defence projects go without a hitch."
                     />
                  </div>
                  <div className="flex-column-cards">
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color4"
                        title="Defence & Security"
                        description="Peacekeeping relies on real-time information. We offer an arsenal of data system, technology and coordination support to make sure your defence projects go without a hitch."
                     />
                     <div id="see-more-projects-div">
                        <button
                           className="submit-button btn-with-up-arrow"
                           style={{ width: "100%" }}
                           onClick={scrollToLogin}
                        >
                           Login to see more projects <i class="fas fa-arrow-up margin-left-arrow"></i>
                        </button>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </>
   );
}

export default App;
