import React from "react";
import { Table, Image } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import "./ProjectMembersTable.css";

const ProjectMembersTable = ({ members }) => {
  const navigate = useNavigate();
  const userSystemUsername = Cookies.get("user-systemUsername");

  const handleNavigateToProfile = (systemUsername) => {
    navigate(`/profile/${systemUsername}`);
  };

  return (
    <Table striped bordered hover className="mt-2">
      <thead>
        <tr>
          <th>Photo</th>
          <th>Name</th>
          <th>Type</th>
        </tr>
      </thead>
      <tbody>
        {members.map((member, index) => (
          <tr key={index}>
            <td>
              <Image
                src={member.profilePicture}
                roundedCircle
                className="member-profile-picture"
              />
            </td>
            <td
              onClick={() => handleNavigateToProfile(member.systemUsername)}
              style={{ cursor: "pointer", color: "black" }}
            >
              {member.systemUsername === userSystemUsername ? "You" : member.name}
            </td>
            <td>{member.type}</td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
};

export default ProjectMembersTable;
