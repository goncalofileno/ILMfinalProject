import "./Headers.css";

export default function LoginHeader() {
  return (
    <div className="ilm-headers">
      <div className="app-navbar">
        <div className="logo"> </div>
        <div className="nav-right">
          <select className="language-dropdown">
            <option className="option-flag" value="ENGLISH">
              🇬🇧
            </option>
            <option className="option-flag" value="PORTUGUESE">
              🇵🇹
            </option>
          </select>
        </div>
      </div>
    </div>
  );
}
