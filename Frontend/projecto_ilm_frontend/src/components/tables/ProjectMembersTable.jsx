import React from "react";
import { Table, Image } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import "./ProjectMembersTable.css";
import "./Tables.css";
import { formatTypeUserInProject } from "../../utilities/converters";

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
          <th>Photo</th>
          <th>Name</th>
          <th>Type</th>
        </tr>
      </thead>
      <tbody id="body-table-users-project">
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
                ? "You"
                : member.name}
            </td>
            <td>{formatTypeUserInProject(member.type)}</td>
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
