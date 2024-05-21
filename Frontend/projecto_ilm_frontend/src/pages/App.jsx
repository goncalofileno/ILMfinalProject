import "./App.css";
import { useNavigate } from "react-router-dom";
import LoginForm from "../forms/LoginForm";
import LoginHeader from "../headers/LoginHeader";
import { useRef, useEffect } from "react";
import LoginProjectsCards from "../cards/LoginProjectsCards";
import { Row, Col, Container } from "react-bootstrap";
import { useMediaQuery } from "react-responsive";

function App() {
   const navigate = useNavigate();
   const contentRef = useRef(null);
   const isComputer = useMediaQuery({ minWidth: 1200 });

   useEffect(() => {
      const updateSizeVariables = () => {
         document.documentElement.style.setProperty("--vh", `${window.screen.height * 0.01}px`);
         document.documentElement.style.setProperty("--vw", `${window.screen.width * 0.01}px`);
      };

      console.log(document.documentElement.style.getPropertyValue("--vh"));
      updateSizeVariables();
   }, []);

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
            <div className="ilm-page">
               <Container className="outer-container">
                  <Row className="outer-container">
                     <Col xl={8} lg={6}>
                        <Row>
                           <Col lg={6}>
                              <h1 className="app-slogan">Where Ideas</h1>
                           </Col>
                           <Col lg={6}>
                              <h1 className="app-slogan app-slogan-2">Take Flight</h1>
                           </Col>
                        </Row>
                        <Row>
                           <Col lg={6}></Col>
                           <Col>
                              {" "}
                              <div className="ilm-description">
                                 Welcome to our Innovation Lab Management app, where creativity meets collaboration and
                                 ideas turn into reality. With our app, you can manage and track your projects
                                 seamlessly, from brainstorming to implementation, ensuring every idea has the potential
                                 to flourish. Join us in driving progress and inspiring change, one innovative project
                                 at a time.
                              </div>
                              <div className="div-project-button">
                                 <button className="submit-button" id="projects-button" onClick={scrollToContent}>
                                    Some of our Projects
                                 </button>
                              </div>
                           </Col>
                        </Row>
                     </Col>
                     <Col xl={4} lg={6}>
                        <LoginForm />
                     </Col>
                  </Row>
               </Container>
            </div>
            <div className="ilm-page2" ref={contentRef}>
               <Container className="outer-container">
                  <Row>
                     <h1 className="ilm-general-subTitle">Projects</h1>
                  </Row>
                  <Row className="row-margin-top-20 last-row-padd-bott">
                     <Col lg={3}>
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
                     <Col lg={3}>
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
                     <Col lg={3}>
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
                     <Col lg={3}>
                        <div className="flex-col-gap-16" id="last-column-projects">
                           <LoginProjectsCards
                              cardBkgColor="card-bkg-color3"
                              title="Aviation"
                              description="From software architectures to system testing, we’ll look after your project from take-off to landing, combining our broad expertise with an in-depth knowledge of the rigorous safety standards in aviation."
                              scrollToRef={scrollToLogin}
                           />
                           <div className="see-more-projects-button-div">
                              <div
                                 className="submit-button btn-with-up-arrow"
                                 onClick={scrollToLogin}
                                 id="see-more-projects-button"
                              >
                                 Login to see more projects <i class="fas fa-arrow-up margin-left-arrow"></i>
                              </div>
                           </div>
                        </div>
                     </Col>
                  </Row>
               </Container>
            </div>
         </div>
      </>
   );
}

export default App;
