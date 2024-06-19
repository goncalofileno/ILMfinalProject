import "./MyProjectCard.css";
import ProgressBar from "../bars/ProgressBar";

export default function MyProjectCard({
  name,
  lab,
  members,
  maxMembers,
  startDate,
  endDate,
  progress,
  status,
  typeMember,
}) {
  return (
    <>
      <div className="my-project-title">{name}</div>
      <div className="my-project-card">
        <div className="my-projects-banner"></div>
        <div className="my-project-background-color">
          <div className="my-projects-body">
            <div className="my-projects-info">
              <div className="my-projects-body-div">
                Lab <span>{lab}</span>
              </div>
              <div className="my-projects-body-div">
                Members{" "}
                <span>
                  {members}/{maxMembers}
                </span>
              </div>
              <div className="my-projects-body-div">
                Start date <span>{startDate}</span>
              </div>
              <div className="my-projects-body-div">
                End date <span>{endDate}</span>
              </div>
            </div>
            <div className="my-projects-banner-content">
              <div className="my-projects-banner-word">{typeMember}</div>
            </div>
          </div>
          <div className="my-projects-footer">
            <div>Progress:</div>
            <div>
              <ProgressBar percentage={progress} status={status}></ProgressBar>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
