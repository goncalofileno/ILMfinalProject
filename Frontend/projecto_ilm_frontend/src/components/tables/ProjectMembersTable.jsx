import React from "react";
import { Table, Image } from "react-bootstrap";
import "./ProjectMembersTable.css";

const ProjectMembersTable = ({ members }) => {
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
            <td>{member.name}</td>
            <td>{member.type}</td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
};

export default ProjectMembersTable;
