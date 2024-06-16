import "./Bars.css";

export default function ProgressBar({ percentage, status }) {
  return (
    <div className="bar-outline">
      <div className="bar-inside" style={{ width: `${percentage}%` }}></div>
      <div className="bar-status"> {status}</div>
    </div>
  );
}
