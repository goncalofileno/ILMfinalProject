import "./Bars.css";
import { formatStatus } from "../../utilities/converters";

export default function ProgressBar({ percentage, status }) {
  function statusColor(status) {
    if (status === "PLANNING") {
      return "rgba(255, 255, 0, 0.5)";
    } else if (status === "READY") {
      return "rgba(89, 89, 89,0.5)";
    } else if (status === "SUBMITTED") {
      return "rgba(134, 89, 45,0.5)";
    } else if (status === "APPROVED") {
      return "rgba(51, 153, 255)";
    } else if (status === "IN_PROGRESS") {
      return "rgba(0, 222, 222, 0.593)";
    } else if (status === "CANCELED") {
      return "rgba(255, 0, 0, 0.5)";
    } else if (status === "FINISHED") {
      return "rgba(0, 179, 60,0.5)";
    } else {
      return "black";
    }
  }

  return (
    <div className="bar-outline">
      <div
        className="bar-inside"
        style={{
          width: status === "CANCELED" ? "100%" : `${percentage}%`,
          backgroundColor: statusColor(status),
        }}
      ></div>
      <div className="bar-status"> {formatStatus(status)}</div>
    </div>
  );
}
