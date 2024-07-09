import React from "react";
import { Table, Image } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import "./ProjectMembersTable.css";
import "./Tables.css";
import { formatTypeUserInProject, formatTypeUserInProjectPublic } from "../../utilities/converters";
import { Trans, t } from "@lingui/macro";
import { useState } from "react";

const ProjectMembersTable = ({ members }) => {
  const navigate = useNavigate();
  const userSystemUsername = Cookies.get("user-systemUsername");
  const NUMBER_OF_MEMBERS_PAGE = 7;

  const handleNavigateToProfile = (systemUsername) => {
    navigate(`/profile/${systemUsername}`);
  };

  return (
    <table style={{ height: "365px" }} className="table-users-project-table">
      <thead id="table-users-project-head">
        <tr id="table-users-project">
          <th>
            <Trans>Photo</Trans>
          </th>
          <th>
            <Trans>Name</Trans>
          </th>
          <th>
            <Trans>Type</Trans>
          </th>
        </tr>
      </thead>
      <tbody
        id="body-table-users-project"
        style={{
          overflow: NUMBER_OF_MEMBERS_PAGE - members.length > 0 && "hidden",
        }}
      >
        {members.map((member, index) => (
          <tr
            key={index}
            onClick={() => handleNavigateToProfile(member.systemUsername)}
          >
            <td>
              <Image
                src={member.profilePicture}
                roundedCircle
                className="member-profile-picture"
              />
            </td>
            <td style={{ cursor: "pointer", color: "black" }}>
              {member.systemUsername === userSystemUsername
                ? t`You`
                : member.name}
            </td>
            <td>{formatTypeUserInProjectPublic(member.type)}</td>
          </tr>
        ))}
        {members.length < NUMBER_OF_MEMBERS_PAGE &&
          Array(NUMBER_OF_MEMBERS_PAGE - members.length)
            .fill()
            .map((index) => (
              <tr key={index + members.length}>
                <td></td>
                <td></td>
                <td></td>
              </tr>
            ))}
      </tbody>
    </table>
  );
};

export default ProjectMembersTable;
