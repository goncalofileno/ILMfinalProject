import "./Tables.css";
import { formatStatus, formatLab } from "../../utilities/converters";

export default function ProjectsTable({ projects }) {
  function statusColor(status) {
    if (status === "PLANNING") {
      return "rgb(255, 255, 0)";
    } else if (status === "READY") {
      return "rgb(89, 89, 89)";
    } else if (status === "SUBMITTED") {
      return "rgb(134, 89, 45)";
    } else if (status === "APPROVED") {
      return "rgb(51, 153, 255)";
    } else if (status === "IN_PROGRESS") {
      return "blue";
    } else if (status === "CANCELED") {
      return "red";
    } else if (status === "FINISHED") {
      return "rgb(0, 179, 60)";
    } else {
      return "black";
    }
  }
  return (
    <>
      <table className="centered-table">
        <thead>
          <tr>
            <th>Project</th>
            <th>Lab</th>
            <th>Status</th>
            <th>Start date | Final date</th>
            <th>Members</th>
          </tr>
        </thead>
        <tbody>
          {projects.map((project, index) => (
            <tr key={project.index}>
              <td>{project.name}</td>
              <td>{formatLab(project.lab)}</td>
              <td style={{ color: statusColor(project.status) }}>
                {formatStatus(project.status)}
              </td>
              <td>
                {project.startDate} | {project.finalDate}
              </td>
              <td>
                {project.numberOfMembers} / {project.maxMembers}{" "}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </>
  );
}
