import "./App.css";
import { useNavigate } from "react-router-dom";
import RegisterForm from "../components/forms/RegisterForm";
import LoginHeader from "../components/headers/LoginHeader";
import { useRef, useState } from "react";
import LoginProjectsCards from "../components/cards/LoginProjectsCards";
import { Row, Col, Container } from "react-bootstrap";
import { useMediaQuery } from "react-responsive";
import { Alert } from "react-bootstrap";

function Register() {
   const navigate = useNavigate();
   const contentRef = useRef(null);
   const isComputer = useMediaQuery({ minWidth: 1200 });
   const isTablet = useMediaQuery({ minWidth: 992, maxWidth: 1200 });
   const isSmallTablet = useMediaQuery({ minWidth: 768, maxWidth: 992 });
   const isPhone = useMediaQuery({ maxWidth: 768 });
   const [showAlert, setShowAlert] = useState(false);

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
         <div className="page-content">
            <div className={isComputer ? "ilm-page" : isTablet ? "ilm-pageb" : "ilm-page-noheight"}>
               <Container fluid className="outer-container">
                  <Row className="outer-row">
                     <Col xl={8} lg={6} sm={12}>
                        <Row>
                           <Col xl={6} lg={12} md={6} sm={12}>
                              <h1 className="app-slogan app-slogan-1" style={{ textAlign: isSmallTablet && "end" }}>
                                 Where Ideas
                              </h1>
                           </Col>
                           <Col xl={6} lg={12} md={6} sm={12}>
                              <h1
                                 className="app-slogan app-slogan-2"
                                 style={{ textAlign: (isTablet || isPhone) && "center" }}
                              >
                                 Take Flight
                              </h1>
                           </Col>
                        </Row>
                        <Row>
                           {isComputer && <Col lg={6}></Col>}
                           {!isPhone && (
                              <Col>
                                 {" "}
                                 <div className="ilm-description">
                                    Welcome to our Innovation Lab Management app, where creativity meets collaboration
                                    and ideas turn into reality. With our app, you can manage and track your projects
                                    seamlessly, from brainstorming to implementation, ensuring every idea has the
                                    potential to flourish. Join us in driving progress and inspiring change, one
                                    innovative project at a time.
                                 </div>
                                 <div className="div-project-button">
                                    <button
                                       className="submit-button"
                                       id="projects-button"
                                       onClick={scrollToContent}
                                       style={{ marginBottom: !isComputer && !isTablet && "100px" }}
                                    >
                                       Some of our Projects
                                    </button>
                                 </div>
                              </Col>
                           )}
                        </Row>
                     </Col>
                     <Col xl={4} lg={6} className="align-items-center" style={{ marginTop: isPhone && "50px" }}>
                        <Alert
                           variant="success"
                           id="alert-message-register"
                           style={{ visibility: showAlert ? "visible" : "hidden" }}
                        >
                           You have registered in ILM with success.<p> Verify your email to activate your account.</p>
                        </Alert>
                        <RegisterForm setShowAlert={setShowAlert} />
                     </Col>
                     {isPhone && (
                        <Col>
                           <div className="div-project-button">
                              <button className="submit-button" id="projects-button" onClick={scrollToContent}>
                                 Some of our Projects
                              </button>
                           </div>
                        </Col>
                     )}
                  </Row>
               </Container>
            </div>
            <div className="ilm-page2" ref={contentRef} style={{ paddingTop: "60px" }}>
               <Container className="outer-container">
                  <Row>
                     <h1 className="ilm-general-subTitle">Projects</h1>
                  </Row>
                  <Row className="row-margin-top-20 last-row-padd-bott">
                     <Col lg={3} md={6} className={!isComputer && !isTablet && "col-margin-bottom-16"}>
                        <div className="flex-col-gap-16">
                           <LoginProjectsCards
                              cardBkgColor="card-bkg-color1"
                              title="Aviation"
                              description="From software architectures to system testing, we’ll look after your project from take-off to landing, combining our broad expertise with an in-depth knowledge of the rigorous safety standards in aviation."
                              scrollToRef={scrollToLogin}
                           />
                           <LoginProjectsCards
                              cardBkgColor="card-bkg-color1"
                              title="Aviation"
                              description="From software architectures to system testing, we’ll look after your project from take-off to landing, combining our broad expertise with an in-depth knowledge of the rigorous safety standards in aviation."
                              scrollToRef={scrollToLogin}
                           />
                        </div>
                     </Col>
                     <Col lg={3} md={6} className={!isComputer && !isTablet && "col-margin-bottom-16"}>
                        <div className="flex-col-gap-16">
                           <LoginProjectsCards
                              cardBkgColor="card-bkg-color4"
                              title="Aviation"
                              description="From software architectures to system testing, we’ll look after your project from take-off to landing, combining our broad expertise with an in-depth knowledge of the rigorous safety standards in aviation."
                              scrollToRef={scrollToLogin}
                           />
                           <LoginProjectsCards
                              cardBkgColor="card-bkg-color4"
                              title="Aviation"
                              description="From software architectures to system testing, we’ll look after your project from take-off to landing, combining our broad expertise with an in-depth knowledge of the rigorous safety standards in aviation."
                              scrollToRef={scrollToLogin}
                           />
                        </div>
                     </Col>
                     <Col lg={3} md={6} className={!isComputer && !isTablet && "col-margin-bottom-16"}>
                        <div className="flex-col-gap-16">
                           <LoginProjectsCards
                              cardBkgColor="card-bkg-color2"
                              title="Aviation"
                              description="From software architectures to system testing, we’ll look after your project from take-off to landing, combining our broad expertise with an in-depth knowledge of the rigorous safety standards in aviation."
                              scrollToRef={scrollToLogin}
                           />
                           <LoginProjectsCards
                              cardBkgColor="card-bkg-color2"
                              title="Aviation"
                              description="From software architectures to system testing, we’ll look after your project from take-off to landing, combining our broad expertise with an in-depth knowledge of the rigorous safety standards in aviation."
                              scrollToRef={scrollToLogin}
                           />
                        </div>
                     </Col>
                     <Col lg={3} md={6}>
                        <div className="flex-col-gap-16">
                           <LoginProjectsCards
                              cardBkgColor="card-bkg-color3"
                              title="Aviation"
                              description="From software architefasdfadfadsfadsfadsdsafctures to system testing, we’ll look after your project from take-off to landing, combining our broad expertise with an in-depth knowledge of the rigorous safety standards in aviation."
                              scrollToRef={scrollToLogin}
                           />
                           <LoginProjectsCards
                              cardBkgColor="card-bkg-color3"
                              title="Aviation"
                              description="From software architectures to system testing, we’ll look after your project from take-off to landing, combining our broad expertise with an in-depth knowledge of the rigorous safety standards in aviation."
                              scrollToRef={scrollToLogin}
                           />
                        </div>
                     </Col>
                  </Row>
               </Container>
            </div>
         </div>
      </>
   );
}

export default Register;
