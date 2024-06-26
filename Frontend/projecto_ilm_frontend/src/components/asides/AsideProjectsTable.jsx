import "./Asides.css";
import "../../pages/CreateProfilePage.css";
import { Form } from "react-bootstrap";
import { useState, useEffect } from "react";
import { getLabsWithSessionId, getAllStatus } from "../../utilities/services";
import { formatLab, formatStatusDropDown } from "../../utilities/converters";

export default function AsideProjectsTable({
  selectedLab,
  setSelectedLab,
  selectedStatus,
  setSelectedStatus,
  slotsAvailable,
  setSlotsAvailable,
  navigateTableProjectsTrigger,
  setNavigateTableProjectsTrigger,
  setCurrentPage,
  setKeyword,
  setNameAsc,
  setStatusAsc,
  setLabAsc,
  setStartDateAsc,
  setEndDateAsc,
}) {
  const [labs, setLabs] = useState([]);
  const [status, setStatus] = useState([]);

  useEffect(() => {
    getLabsWithSessionId()
      .then((response) => response.json())
      .then((data) => {
        setLabs(data);
      });
    getAllStatus()
      .then((response) => response.json())
      .then((data) => {
        setStatus(data);
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
              <option key={index} value={lab.local}>
                {formatLab(lab.local)}
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
            checked={slotsAvailable}
            onChange={(e) => {
              setSlotsAvailable(e.target.checked);
              setCurrentPage(1);
              setNavigateTableProjectsTrigger(!navigateTableProjectsTrigger);
            }}
          />
        </div>
        <div className="table-label-color">
          <div id="your-projects-color">Your Projects</div>
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
            setSlotsAvailable(false);
            setCurrentPage(1);
            setKeyword("");
            setNameAsc("");
            setStatusAsc("");
            setLabAsc("");
            setStartDateAsc("");
            setEndDateAsc("");
            setNavigateTableProjectsTrigger(!navigateTableProjectsTrigger);
          }}
        >
          Clear Filters
        </button>
      </div>
    </div>
  );
}
