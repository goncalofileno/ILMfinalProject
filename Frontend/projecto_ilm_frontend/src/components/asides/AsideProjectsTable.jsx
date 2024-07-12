import "./Asides.css";
import "../../pages/CreateProfilePage.css";
import { Form } from "react-bootstrap";
import { useState, useEffect } from "react";
import { getLabsWithSessionId, getAllStatus } from "../../utilities/services";
import { formatLab, formatStatusDropDown } from "../../utilities/converters";
import { useMediaQuery } from "react-responsive";
import { Trans, t } from "@lingui/macro";

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
  isVisible,
}) {
  const [labs, setLabs] = useState([]);
  const [status, setStatus] = useState([]);
  const isMobile = useMediaQuery({ query: "(max-width: 768px)" });

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
    <>
      {((isVisible && isMobile) || !isMobile) && (
        <div className={!isMobile && "aside-background"}>
          <div className={!isMobile ? "aside" : "aside-Mobile"}>
            <div className="div-control-form" id="first-div">
              <Form.Label className="custom-label" style={{ color: "white" }}>
                <Trans>Lab</Trans>
              </Form.Label>
              <Form.Select
                className="custom-focus"
                value={selectedLab}
                onChange={(e) => {
                  setSelectedLab(e.target.value);
                  setCurrentPage(1);
                  setNavigateTableProjectsTrigger(
                    !navigateTableProjectsTrigger
                  );
                }}
              >
                <option value="">
                  <Trans>All Labs</Trans>
                </option>
                {labs.map((lab, index) => (
                  <option key={index} value={lab.local}>
                    {formatLab(lab.local)}
                  </option>
                ))}
              </Form.Select>
            </div>
            <div className="div-control-form">
              <Form.Label className="custom-label" style={{ color: "white" }}>
                <Trans>Status</Trans>
              </Form.Label>
              <Form.Select
                className="custom-focus"
                value={selectedStatus}
                onChange={(e) => {
                  setSelectedStatus(e.target.value);
                  setCurrentPage(1);
                  setNavigateTableProjectsTrigger(
                    !navigateTableProjectsTrigger
                  );
                }}
              >
                {" "}
                <option value="">
                  <Trans>All Status</Trans>
                </option>
                {status.map((status, index) => (
                  <option key={index} value={status}>
                    {formatStatusDropDown(status)}
                  </option>
                ))}
              </Form.Select>
            </div>
            <div className="custom-switch" id="slots-switch">
              <Form.Label
                className="custom-label"
                id="label-slots"
                style={{ color: "white" }}
              >
                <Trans>Slots Available</Trans>
              </Form.Label>
              <Form.Check
                type="switch"
                checked={slotsAvailable}
                onChange={(e) => {
                  setSlotsAvailable(e.target.checked);
                  setCurrentPage(1);
                  setNavigateTableProjectsTrigger(
                    !navigateTableProjectsTrigger
                  );
                }}
              />
            </div>
            <button
              className="secondary-button"
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
              <Trans>Clear Filters</Trans>
            </button>
            <div className="table-label-color">
              <div id="your-projects-color">
                <Trans>Your Projects</Trans>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
