import "./MyProjectCard.css";
import ProgressBar from "../bars/ProgressBar";
import { useNavigate } from "react-router-dom";
import { Trans, t } from "@lingui/macro";

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
  systemProjectName,
  photo,
}) {
  const navigate = useNavigate();
  console.log(name + " " + photo);
  return (
    <>
      <div
        style={{ height: "100%" }}
        onClick={() => navigate(`/project/${systemProjectName}/info`)}
      >
        <div className="my-project-title">{name}</div>
        <div className="my-project-card">
          <div
            className="my-projects-banner"
            style={{
              backgroundImage:
                photo === undefined || photo === null
                  ? "url(https://cdn.pixabay.com/photo/2016/03/29/08/48/project-1287781_1280.jpg)"
                  : `url(${photo})`,
            }}
          ></div>
          <div className="my-project-background-color">
            <div className="my-projects-body">
              <div className="my-projects-info">
                <div className="my-projects-body-div">
                <Trans>Lab</Trans> <span>{lab}</span>
                </div>
                <div className="my-projects-body-div">
                <Trans>Members</Trans>{" "}
                  <span>
                    {members}/{maxMembers}
                  </span>
                </div>
                <div className="my-projects-body-div">
                <Trans>Start date</Trans> <span>{startDate}</span>
                </div>
                <div className="my-projects-body-div">
                <Trans>End date</Trans> <span>{endDate}</span>
                </div>
              </div>
              <div className="my-projects-banner-content">
                <div className="my-projects-banner-word">{typeMember}</div>
              </div>
            </div>
            <div className="my-projects-footer">
              <div><Trans>Progress</Trans>:</div>
              <div>
                <ProgressBar
                  percentage={progress}
                  status={status}
                ></ProgressBar>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
