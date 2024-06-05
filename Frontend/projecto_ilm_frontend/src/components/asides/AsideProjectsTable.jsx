import "./Asides.css";
import "../../pages/CreateProfilePage.css";
import { Form } from "react-bootstrap";
import { useState } from "react";

export default function AsideProjectsTable() {
   const [publicProfile, setPublicProfile] = useState(false);

   return (
      <div className="aside-background">
         <div className="aside">
            <div id="first-div">
               <Form.Label className="custom-label" style={{ color: "white" }}>
                  Lab
               </Form.Label>
               <Form.Control as="select" className="custom-focus">
                  <option value="">All Labs</option>
               </Form.Control>
            </div>
            <div>
               <Form.Label className="custom-label" style={{ color: "white" }}>
                  Status
               </Form.Label>
               <Form.Control as="select" className="custom-focus">
                  {" "}
                  <option value="">All Status</option>
               </Form.Control>
            </div>
            <div className="custom-switch" id="slots-switch">
               <Form.Label className="custom-label" id="label-slots" style={{ color: "white" }}>
                  Slots Available
               </Form.Label>
               <Form.Check type="switch" checked={publicProfile} onChange={(e) => setPublicProfile(e.target.checked)} />
            </div>
         </div>
      </div>
   );
}
