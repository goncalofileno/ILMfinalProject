import React, { useEffect, useState } from "react";
import AppNavbar from "../components/headers/AppNavbar";
import { useParams } from "react-router-dom";
import { Alert, Card, Col, Container, Row } from "react-bootstrap";
import { getProjectInfoPage } from "../utilities/services";
import ProjectTabs from "../components/headers/ProjectTabs";
import ProjectMembersTable from "../components/tables/ProjectMembersTable";
import "./ProjectProfilePageInfo.css";

const ProjectProfilePageInfo = () => {
  const { systemProjectName } = useParams();
  const [projectInfo, setProjectInfo] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProjectInfo = async () => {
      const data = await getProjectInfoPage(systemProjectName);
      if (data.error) {
        setError(data.error);
      } else {
        setProjectInfo(data);
      }
    };
    fetchProjectInfo();
  }, [systemProjectName]);

  if (error) {
    return <Alert variant="danger">{error}</Alert>;
  }

  if (!projectInfo) {
    return <div>Loading...</div>;
  }

  return (
    <>
      <AppNavbar />
      <div className="bckg-color-ilm-page ilm-pageb">
        <Container className="mt-4">
          <ProjectTabs typeOfUserSeingProject={projectInfo.typeOfUserSeingProject} />
          <Row className="justify-content-md-center">
            <Col md="8">
              <Card className="shadow-sm">
                <Card.Body>
                  <Card.Title>{projectInfo.title}</Card.Title>
                  <Card.Subtitle className="mb-2 text-muted">{projectInfo.state}</Card.Subtitle>
                  <Card.Text>
                    <strong>Description:</strong> {projectInfo.description}
                  </Card.Text>
                  <Card.Text>
                    <strong>Start Date:</strong> {new Date(projectInfo.startDate).toLocaleDateString()}
                  </Card.Text>
                  <Card.Text>
                    <strong>End Date:</strong> {new Date(projectInfo.endDate).toLocaleDateString()}
                  </Card.Text>
                  <Card.Text>
                    <strong>Lab:</strong> {projectInfo.lab}
                  </Card.Text>
                  <Card.Text>
                    <strong>Progress:</strong> {projectInfo.progress}%
                  </Card.Text>
                  <Card.Text>
                    <strong>Max Members:</strong> {projectInfo.maxMembers}
                  </Card.Text>
                  <Card.Text>
                    <strong>Creator:</strong> {projectInfo.creator.name} ({projectInfo.creator.systemUsername})
                  </Card.Text>
                  <Card.Text>
                    <strong>Keywords:</strong>
                    <Row>
                      {projectInfo.keywords.length > 0 ? (
                        projectInfo.keywords.map((keyword, index) => (
                          <Col md="auto" key={index}>
                            <div className="skill-interest-card mb-3">
                              {keyword}
                            </div>
                          </Col>
                        ))
                      ) : (
                        <Col>
                          <p className="centered-message">No keywords available.</p>
                        </Col>
                      )}
                    </Row>
                  </Card.Text>
                  <Card.Text>
                    <strong>Skills:</strong>
                    <Row>
                      {projectInfo.skills.length > 0 ? (
                        projectInfo.skills.map((skill) => (
                          <Col md="auto" key={skill.id}>
                            <div className="skill-interest-card mb-3">
                              <p className="mb-1"><strong>{skill.name}</strong></p>
                              <p className="text-muted mb-1" style={{ fontSize: "0.85em" }}>{skill.type}</p>
                            </div>
                          </Col>
                        ))
                      ) : (
                        <Col>
                          <p className="centered-message">No skills available.</p>
                        </Col>
                      )}
                    </Row>
                  </Card.Text>
                  <Card.Text>
                    <strong>States to Change:</strong> {projectInfo.statesToChange.join(", ")}
                  </Card.Text>
                  <Card.Text>
                    <strong>Type of User Seeing Project:</strong> {projectInfo.typeOfUserSeingProject}
                  </Card.Text>
                  <ProjectMembersTable members={projectInfo.members} />
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Container>
      </div>
    </>
  );
};

export default ProjectProfilePageInfo;
