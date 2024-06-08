import "./Asides.css";
import "../../pages/CreateProfilePage.css";
import { Form } from "react-bootstrap";
import { useState, useEffect } from "react";
import { getLabsWithSessionId } from "../../utilities/services";
import { formatLab } from "../../utilities/converters";

export default function AsideProjectsTable() {
  const [publicProfile, setPublicProfile] = useState(false);
  const [labs, setLabs] = useState([]);

  useEffect(() => {
    getLabsWithSessionId()
      .then((response) => response.json())
      .then((data) => {
        setLabs(data);
      });
  }, []);

  return (
    <div className="aside-background">
      <div className="aside">
        <div id="first-div">
          <Form.Label className="custom-label" style={{ color: "white" }}>
            Lab
          </Form.Label>
          <Form.Control as="select" className="custom-focus">
            <option value="">All Labs</option>
            {labs.map((lab, index) => (
              <option key={index} value={lab.local}>
                {formatLab(lab.local)}
              </option>
            ))}
          </Form.Control>
        </div>
        <div>
          <Form.Label className="custom-label" style={{ color: "white" }}>
            Status
          </Form.Label>
          <Form.Control as="select" className="custom-focus">
            {" "}
            <option value="">All Status</option>
            <option value="0">Planning</option>
            <option value="1">Ready</option>
            <option value="2">Submitted</option>
            <option value="3">Approved</option>
            <option value="4">In Progress</option>
            <option value="5">Canceled</option>
            <option value="6">Finished</option>
          </Form.Control>
        </div>
        <div className="custom-switch" id="slots-switch">
          <Form.Label
            className="custom-label"
            id="label-slots"
            style={{ color: "white" }}
          >
            Slots Available
          </Form.Label>
          <Form.Check
            type="switch"
            checked={publicProfile}
            onChange={(e) => setPublicProfile(e.target.checked)}
          />
        </div>
      </div>
    </div>
  );
}
