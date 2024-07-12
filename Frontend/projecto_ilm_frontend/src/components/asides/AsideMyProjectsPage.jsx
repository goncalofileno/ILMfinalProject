import Form from "react-bootstrap/Form";
import { useState, useEffect } from "react";
import {
  formatLab,
  formatStatusDropDown,
  formatTypeUserInProject,
} from "../../utilities/converters";
import { getProjectsFilters } from "../../utilities/services";
import { Trans } from "@lingui/macro";
import { useMediaQuery } from "react-responsive";
import React from "react";

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
  isVisible,
}) {
  const [labs, setLabs] = useState([]);
  const [status, setStatus] = useState([]);
  const [typesMember, setTypesMember] = useState([]);
  const isMobile = useMediaQuery({ query: "(max-width: 768px)" });

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
                  <option key={index} value={lab}>
                    {formatLab(lab)}
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
            <div className="div-control-form">
              <Form.Label className="custom-label" style={{ color: "white" }}>
                <Trans>Member Type</Trans>
              </Form.Label>
              <Form.Select
                className="custom-focus"
                value={selectedTypeMember}
                onChange={(e) => {
                  setSelectedTypeMember(e.target.value);
                  setCurrentPage(1);
                  setNavigateTableProjectsTrigger(
                    !navigateTableProjectsTrigger
                  );
                }}
              >
                {" "}
                <option value="">
                  <Trans>All Types</Trans>
                </option>
                {typesMember.map((typeMember, index) => (
                  <option key={index} value={typeMember}>
                    {formatTypeUserInProject(typeMember)}
                  </option>
                ))}
              </Form.Select>
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
                setSelectedTypeMember("");
                setCurrentPage(1);
                setKeyword("");
                setNavigateTableProjectsTrigger(!navigateTableProjectsTrigger);
              }}
            >
              <Trans>Clear Filters</Trans>
            </button>
          </div>
        </div>
      )}
    </>
  );
}
