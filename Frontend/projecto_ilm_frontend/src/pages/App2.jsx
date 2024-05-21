import "./App2.css";
import { useNavigate } from "react-router-dom";
import LoginForm from "../forms/LoginForm";
import LoginHeader from "../headers/LoginHeader";
import { useRef } from "react";
import LoginProjectsCards from "../cards/LoginProjectsCards";
import { Row, Col } from "react-bootstrap";
import { useMediaQuery } from "react-responsive";

function App2() {
   const navigate = useNavigate();
   const contentRef = useRef(null);
   const isPhone = useMediaQuery({ maxWidth: 576 });
   const isTablet = useMediaQuery({ maxWidth: 991 });

   const scrollToContent = () => {
      contentRef.current.scrollIntoView({ behavior: "smooth", block: "start" });
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

         <div>
            <div className={`ilm-login-page ${!isTablet ? "ilm-page" : "ilm-page3"} container-login-page`}>
               <Row className="row-login-page">
                  {!isTablet && (
                     <Col lg={4} md={6}>
                        <h1 className={`ilm-general-title ${isTablet && "take-flight-phone"}`} id="slogan-app">
                           Where Ideas
                        </h1>
                     </Col>
                  )}
                  <Col lg={4} md={12} id="col-description">
                     {isPhone ? (
                        <>
                           <h1 className={`ilm-general-title ${isTablet && "take-flight-phone"}`}>Where Ideas</h1>
                           <h1
                              className={`ilm-general-title ${isTablet && "take-flight-phone"}`}
                              style={{ marginTop: "0px" }}
                           >
                              <span>Take Flight</span>
                           </h1>
                        </>
                     ) : !isTablet ? (
                        <h1 className={`ilm-general-title ${isTablet && "take-flight-phone"}`}>
                           <span>Take Flight</span>
                        </h1>
                     ) : (
                        <>
                           <h1 className={`ilm-general-title ${isTablet && "take-flight-phone"}`}>
                              Where Ideas <span>Take Flight</span>
                           </h1>
                           <h1 className={`ilm-general-title ${isTablet && "take-flight-phone"}`}></h1>
                        </>
                     )}
                     {!isPhone && (
                        <div className="ilm-description-login-register">
                           <div
                              className="ilm-description"
                              style={{ marginLeft: isTablet && "7%", width: isTablet && "90%" }}
                           >
                              Welcome to our Innovation Lab Management app, where creativity meets collaboration and
                              ideas turn into reality. With our app, you can manage and track your projects seamlessly,
                              from brainstorming to implementation, ensuring every idea has the potential to flourish.
                              Join us in driving progress and inspiring change, one innovative project at a time.
                           </div>
                           <div className="div-project-button" style={{ marginTop: isTablet && "0px" }}>
                              <button className="submit-button" id="projects-button" onClick={scrollToContent}>
                                 Some of our Projects
                              </button>
                           </div>
                        </div>
                     )}
                  </Col>

                  <Col lg={4} md={12} xs={12} className="col-login-form">
                     <LoginForm xs={12} />
                  </Col>
                  {isPhone && (
                     <Col id="col-projects-button-phone">
                        <button className="submit-button" id="projects-button-phone" onClick={scrollToContent}>
                           Some of our Projects
                        </button>
                     </Col>
                  )}
               </Row>
            </div>
            <div className="ilm-projects-login-page ilm-page2 ilm-login-page " ref={contentRef}>
               <Row>
                  <h1 className="ilm-general-subTitle" id="projects-title">
                     Projects
                  </h1>
               </Row>
               <Row className="row-project-cards-login">
                  <Col lg={3} md={6} className="col-projects-cards-login">
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color1"
                        title="Aviation"
                        description="From software architectures to system testing, we’ll look after your project from take-off to landing, combining our broad expertise with an in-depth knowledge of the rigorous safety standards in aviation."
                        scrollToRef={scrollToLogin}
                     />
                  </Col>
                  <Col lg={3} md={6} className="col-projects-cards-login">
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color2"
                        title="Defence & Security"
                        description="Peacekeeping relies on real-time information. We offer an arsenal of data system, technology and coordination support to make sure your defence projects go without a hitch."
                     />
                  </Col>

                  <Col lg={3} md={6} className="col-projects-cards-login">
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color3"
                        title="Defence & Security"
                        description="Peacekeeping relies on real-time information. We offer an arsenal of data system, technology and coordination support to make sure your defence projects go without a hitch."
                     />
                  </Col>
                  <Col lg={3} md={6} className="col-projects-cards-login">
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color4"
                        title="Defence & Security"
                        description="Peacekeeping relies on real-time information. We offer an arsenal of data system, technology and coordination support to make sure your defence projects go without a hitch."
                     />
                  </Col>
               </Row>
               <Row className="row-project-cards-login last-row-projects-login">
                  <Col lg={3} md={6} className="col-projects-cards-login">
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color1"
                        title="Defence & Security"
                        description="Peacekeeping relies on real-time information. We offer an arsenal of data system, technology and coordination support to make sure your defence projects go without a hitch."
                     />
                  </Col>
                  <Col lg={3} md={6} className="col-projects-cards-login">
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color2"
                        title="Defence & Security"
                        description="Peacekeeping relies on real-time information. We offer an arsenal of data system, technology and coordination support to make sure your defence projects go without a hitch."
                     />
                  </Col>
                  <Col lg={3} md={6} className="col-projects-cards-login">
                     <LoginProjectsCards
                        cardBkgColor="card-bkg-color3"
                        title="Defence & Security"
                        description="Peacekeeping relies on real-time information. We offer an arsenal of data system, technology and coordination support to make sure your defence projects go without a hitch."
                     />
                  </Col>

                  <Col lg={3} md={6} className="col-projects-cards-login">
                     <div id="see-more-projects-div">
                        <div
                           className="submit-button btn-with-up-arrow"
                           onClick={scrollToLogin}
                           id="see-more-projects-button"
                        >
                           Login to see more projects <i class="fas fa-arrow-up margin-left-arrow"></i>
                        </div>
                     </div>
                  </Col>
               </Row>
            </div>
         </div>
      </>
   );
}

export default App2;
