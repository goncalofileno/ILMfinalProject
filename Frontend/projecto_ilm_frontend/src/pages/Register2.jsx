import RegisterForm from "../forms/RegisterForm";
import LoginHeader from "../headers/LoginHeader";
import "./App.css";
import { useRef } from "react";
import LoginProjectsCards from "../cards/LoginProjectsCards";
import { Row, Col } from "react-bootstrap";

export default function Register2() {
   const contentRef = useRef(null);

   const scrollToContent = () => {
      contentRef.current.scrollIntoView({ behavior: "smooth" });
   };

   const scrollToLogin = () => {
      window.scrollTo({
         top: 0,
         behavior: "smooth", // Smooth scroll effect
      });
   };
   return (
      <>
         <LoginHeader />
         <div className="background-img-container">
            <div className="background-img-app"></div>
         </div>
         <div>
            <div className="container-fluid ilm-page container-login-page ">
               <Row>
                  <Col sm={4}>
                     <h1 className="ilm-general-title" id="slogan-app">
                        Where Ideas
                     </h1>
                  </Col>
                  <Col sm={4} id="col-description">
                     <h1 className="ilm-general-title">
                        <span>Take Flight</span>
                     </h1>
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
                  </Col>
                  <Col sm={4}>
                     <RegisterForm />
                  </Col>
               </Row>
            </div>
            <div className=" container-fluid ilm-projects-login-page ilm-page2" ref={contentRef}>
               <Row>
                  <h1 className="ilm-general-subTitle" id="projects-title">
                     Projects
                  </h1>
               </Row>
               <Row className="row-project-cards-login">
                  <Col sm={3}>
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color1"
                        title="Aviation"
                        description="From software architectures to system testing, we’ll look after your project from take-off to landing, combining our broad expertise with an in-depth knowledge of the rigorous safety standards in aviation."
                        scrollToRef={scrollToLogin}
                     />
                  </Col>
                  <Col sm={3}>
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color1"
                        title="Defence & Security"
                        description="Peacekeeping relies on real-time information. We offer an arsenal of data system, technology and coordination support to make sure your defence projects go without a hitch."
                     />
                  </Col>

                  <Col sm={3}>
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color2"
                        title="Defence & Security"
                        description="Peacekeeping relies on real-time information. We offer an arsenal of data system, technology and coordination support to make sure your defence projects go without a hitch."
                     />
                  </Col>
                  <Col sm={3}>
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color2"
                        title="Defence & Security"
                        description="Peacekeeping relies on real-time information. We offer an arsenal of data system, technology and coordination support to make sure your defence projects go without a hitch."
                     />
                  </Col>
               </Row>
               <Row className="row-project-cards-login">
                  <Col sm={3}>
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color3"
                        title="Defence & Security"
                        description="Peacekeeping relies on real-time information. We offer an arsenal of data system, technology and coordination support to make sure your defence projects go without a hitch."
                     />
                  </Col>
                  <Col sm={3}>
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color3"
                        title="Defence & Security"
                        description="Peacekeeping relies on real-time information. We offer an arsenal of data system, technology and coordination support to make sure your defence projects go without a hitch."
                     />
                  </Col>
                  <Col sm={3}>
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color4"
                        title="Defence & Security"
                        description="Peacekeeping relies on real-time information. We offer an arsenal of data system, technology and coordination support to make sure your defence projects go without a hitch."
                     />
                  </Col>

                  <Col sm={3}>
                     <div id="see-more-projects-div">
                        <button
                           className="submit-button btn-with-up-arrow"
                           style={{ width: "40.5vh" }}
                           onClick={scrollToLogin}
                        >
                           Login to see more projects <i class="fas fa-arrow-up margin-left-arrow"></i>
                        </button>
                     </div>
                  </Col>
               </Row>
            </div>
         </div>
      </>
   );
}
