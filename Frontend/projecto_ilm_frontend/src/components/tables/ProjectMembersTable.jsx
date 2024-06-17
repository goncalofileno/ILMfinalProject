import React from "react";
import { Table } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const ProjectMembersTable = ({ members }) => {
  const navigate = useNavigate();

  const handleMemberClick = (systemUsername) => {
    navigate(`/profile/${systemUsername}`);
  };

  return (
    <Table striped bordered hover className="mt-4">
      <thead>
        <tr>
          <th>Name</th>
          <th>Type</th>
        </tr>
      </thead>
      <tbody>
        {members.map((member) => (
          <tr key={member.systemUsername} onClick={() => handleMemberClick(member.systemUsername)}>
            <td>{member.name}</td>
            <td>{member.type}</td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
};

export default ProjectMembersTable;
