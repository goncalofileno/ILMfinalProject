import "./MyProjectCard.css";
import ProgressBar from "../bars/ProgressBar";

export default function MyProjectCard() {
  return (
    <>
      <div className="my-project-title">Project name 0 00043143</div>
      <div className="my-project-card">
        <div className="my-projects-banner"></div>
        <div className="my-project-background-color">
          <div className="my-projects-body">
            <div className="my-projects-info">
              <div className="my-projects-body-div">
                Lab <span>Coimbra</span>
              </div>
              <div className="my-projects-body-div">
                Members <span>2/15</span>
              </div>
              <div className="my-projects-body-div">
                Start date <span>08/02/2020</span>
              </div>
              <div className="my-projects-body-div">
                End date <span>08/02/2022</span>
              </div>
            </div>
            <div className="my-projects-banner-content">
              <div className="my-projects-banner-word">Manager</div>
            </div>
          </div>
          <div className="my-projects-footer">
            <div>Progress:</div>
            <div>
              <ProgressBar percentage={50} status="PLANNING"></ProgressBar>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
