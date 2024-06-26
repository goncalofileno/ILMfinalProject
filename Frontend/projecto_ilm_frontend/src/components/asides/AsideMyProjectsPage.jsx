import Form from "react-bootstrap/Form";
import { useState, useEffect } from "react";
import {
  formatLab,
  formatStatusDropDown,
  formatTypeUserInProject,
} from "../../utilities/converters";
import { getProjectsFilters } from "../../utilities/services";

export default function AsideMyProjectsPage({
  selectedLab,
  setSelectedLab,
  selectedStatus,
  setSelectedStatus,
  selectedTypeMember,
  setSelectedTypeMember,
  navigateTableProjectsTrigger,
  setNavigateTableProjectsTrigger,
  setCurrentPage,
  setKeyword,
}) {
  const [labs, setLabs] = useState([]);
  const [status, setStatus] = useState([]);
  const [typesMember, setTypesMember] = useState([]);

  useEffect(() => {
    getProjectsFilters()
      .then((response) => response.json())
      .then((data) => {
        setLabs(data.labs);
        setStatus(data.states);
        setTypesMember(data.userTypesInProject);
      });
  }, []);

  return (
    <div className="aside-background">
      <div className="aside">
        <div className="div-control-form" id="first-div">
          <Form.Label className="custom-label" style={{ color: "white" }}>
            Lab
          </Form.Label>
          <Form.Control
            as="select"
            className="custom-focus"
            value={selectedLab}
            onChange={(e) => {
              setSelectedLab(e.target.value);
              setCurrentPage(1);
              setNavigateTableProjectsTrigger(!navigateTableProjectsTrigger);
            }}
          >
            <option value="">All Labs</option>
            {labs.map((lab, index) => (
              <option key={index} value={lab}>
                {formatLab(lab)}
              </option>
            ))}
          </Form.Control>
        </div>
        <div className="div-control-form">
          <Form.Label className="custom-label" style={{ color: "white" }}>
            Status
          </Form.Label>
          <Form.Control
            as="select"
            className="custom-focus"
            value={selectedStatus}
            onChange={(e) => {
              setSelectedStatus(e.target.value);
              setCurrentPage(1);
              setNavigateTableProjectsTrigger(!navigateTableProjectsTrigger);
            }}
          >
            {" "}
            <option value="">All Status</option>
            {status.map((status, index) => (
              <option key={index} value={status}>
                {formatStatusDropDown(status)}
              </option>
            ))}
          </Form.Control>
        </div>
        <div className="div-control-form">
          <Form.Label className="custom-label" style={{ color: "white" }}>
            Member Type
          </Form.Label>
          <Form.Control
            as="select"
            className="custom-focus"
            value={selectedTypeMember}
            onChange={(e) => {
              setSelectedTypeMember(e.target.value);
              setCurrentPage(1);
              setNavigateTableProjectsTrigger(!navigateTableProjectsTrigger);
            }}
          >
            {" "}
            <option value="">All Types</option>
            {typesMember.map((typeMember, index) => (
              <option key={index} value={typeMember}>
                {formatTypeUserInProject(typeMember)}
              </option>
            ))}
          </Form.Control>
        </div>

        <button
          className="terciary-button"
          style={{
            width: "100%",
            paddingTop: "5px",
            paddingBottom: "5px",
            fontWeight: "500",
          }}
          onClick={() => {
            setSelectedLab("");
            setSelectedStatus("");
            setSelectedTypeMember("");
            setCurrentPage(1);
            setKeyword("");
            setNavigateTableProjectsTrigger(!navigateTableProjectsTrigger);
          }}
        >
          Clear Filters
        </button>
      </div>
    </div>
  );
}
