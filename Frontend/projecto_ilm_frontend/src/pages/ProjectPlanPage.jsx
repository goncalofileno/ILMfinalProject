import React, { useEffect, useState } from "react";
import {
  Container,
  Row,
  Col,
  Modal,
  Button,
  Form,
  ListGroup,
} from "react-bootstrap";
import { useParams } from "react-router-dom";
import { Gantt, ViewMode } from "gantt-task-react";
import "gantt-task-react/dist/index.css";
import AppNavbar from "../components/headers/AppNavbar";
import ProjectTabs from "../components/headers/ProjectTabs";
import { getTasksPage } from "../utilities/services";
import Cookies from "js-cookie";
import { formatTaskStatus } from "../utilities/converters"; // Importe a função
import "./ProjectPlanPage.css";

const parseDate = (dateString) => new Date(dateString);

const transformTasksData = (projectTask, tasks) => {
  const transformedTasks = tasks.map((task) => ({
    id: task.systemTitle,
    name: task.title,
    type: "task",
    start: parseDate(task.initialDate),
    end: parseDate(task.finalDate),
    progress: 0,
    dependencies: task.dependentTasks
      ? task.dependentTasks.map((dep) => dep.systemTitle)
      : [],
    styles: {
      backgroundColor:
        task.status === "DONE"
          ? "#8BC34A"
          : task.status === "IN_PROGRESS"
          ? "#FFEB3B"
          : "#F44336",
      progressColor: "#ffbb54",
      progressSelectedColor: "#ff9e0d",
    },
    rawTask: task,
  }));

  transformedTasks.push({
    id: projectTask.systemTitle,
    name: projectTask.title,
    type: "project",
    start: parseDate(projectTask.initialDate),
    end: parseDate(projectTask.finalDate),
    progress: 0,
    dependencies: [],
    styles: {
      backgroundColor: "#3F51B5",
      progressColor: "#ffbb54",
      progressSelectedColor: "#ff9e0d",
    },
    rawTask: projectTask,
  });

  return transformedTasks;
};

const ProjectPlanPage = () => {
  const { systemProjectName } = useParams();
  const [tasks, setTasks] = useState([]);
  const [viewMode, setViewMode] = useState(ViewMode.Year);
  const [error, setError] = useState(null);
  const [projectName, setProjectName] = useState("");
  const [userSeingTasksType, setUserSeingTasksType] = useState("CREATOR");
  const [selectedTask, setSelectedTask] = useState(null);
  const [taskDetails, setTaskDetails] = useState({});
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isSaveEnabled, setIsSaveEnabled] = useState(false);
  const [projectMembers, setProjectMembers] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const sessionId = Cookies.get("session-id");
      try {
        const data = await getTasksPage(sessionId, systemProjectName);
        const transformedTasks = transformTasksData(
          data.projectTask,
          data.tasks
        );
        setProjectName(data.projectName);
        setUserSeingTasksType(data.userSeingTasksType);
        setTasks(transformedTasks);
        setProjectMembers(data.projectMembers);
      } catch (error) {
        console.error("Fetch error:", error);
        setError(error.message);
      }
    };
    fetchData();
  }, [systemProjectName]);

  const handleTaskClick = (task) => {
    const inChargeMember = task.rawTask.membersOfTask.find(
      (member) =>
        member.type === "INCHARGE" || member.type === "CREATOR_INCHARGE"
    )?.systemName;
    setSelectedTask(task);
    setTaskDetails({ ...task.rawTask, inCharge: inChargeMember || "" });
    setIsModalVisible(true);
    setIsSaveEnabled(false);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setTaskDetails((prevDetails) => ({ ...prevDetails, [name]: value }));
    setIsSaveEnabled(true);
  };

  const handleSave = () => {
    // Implement save logic here
    console.log("Saved task details:", taskDetails);
    setIsModalVisible(false);
  };

  const handleAddMember = (systemUsername) => {
    const member = projectMembers.find(
      (member) => member.systemUsername === systemUsername
    );
    if (!member) return;
    setTaskDetails((prevDetails) => ({
      ...prevDetails,
      membersOfTask: [...prevDetails.membersOfTask, { ...member, type: "" }],
    }));
    setIsSaveEnabled(true);
  };

  const handleRemoveMember = (memberId) => {
    setTaskDetails((prevDetails) => {
      const newMembers = prevDetails.membersOfTask.filter(
        (member) => member.id !== memberId
      );

      const inChargeMember = newMembers.find(
        (member) => member.type === "INCHARGE"
      );

      if (!inChargeMember) {
        const creator = newMembers.find(
          (member) =>
            member.type === "CREATOR" || member.type === "CREATOR_INCHARGE"
        );
        if (creator) {
          return {
            ...prevDetails,
            membersOfTask: newMembers,
            inCharge: creator.systemName,
          };
        }
      }

      return {
        ...prevDetails,
        membersOfTask: newMembers,
      };
    });
    setIsSaveEnabled(true);
  };

  const handleAddDependentTask = (taskId) => {
    const task = tasks.find((task) => task.rawTask.id === parseInt(taskId));
    if (!task) return;
    setTaskDetails((prevDetails) => ({
      ...prevDetails,
      dependentTasks: [...prevDetails.dependentTasks, task.rawTask],
    }));
    setIsSaveEnabled(true);
  };

  const handleRemoveDependentTask = (taskId) => {
    setTaskDetails((prevDetails) => ({
      ...prevDetails,
      dependentTasks: prevDetails.dependentTasks.filter(
        (task) => task.id !== taskId
      ),
    }));
    setIsSaveEnabled(true);
  };

  const availableMembers = projectMembers.filter(
    (member) =>
      !taskDetails.membersOfTask?.some(
        (taskMember) => taskMember.id === member.id
      )
  );

  const availableTasks = tasks.filter(
    (task) =>
      !taskDetails.dependentTasks?.some(
        (depTask) => depTask.id === task.rawTask.id
      ) &&
      task.rawTask.id !== taskDetails.id &&
      task.type !== "project"
  );

  return (
    <>
      <AppNavbar />
      <div className="bckg-color-ilm-page ilm-pageb">
        <ProjectTabs typeOfUserSeingProject={userSeingTasksType} />
        <Container style={{ height: "91%" }}>
          <Row>
            <Col>
              <h1>Project Plan</h1>
              <h2>{projectName}</h2>
              <Gantt
                tasks={tasks}
                viewMode={viewMode}
                onDateChange={(task) => console.log("Task date changed:", task)}
                onProgressChange={(task) =>
                  console.log("Task progress changed:", task)
                }
                onDoubleClick={handleTaskClick}
                onClick={handleTaskClick}
              />
            </Col>
          </Row>
        </Container>
      </div>

      <Modal show={isModalVisible} onHide={() => setIsModalVisible(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Edit Task</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group controlId="formTaskTitle">
              <Form.Label>
                <strong>Title:</strong>
              </Form.Label>
              <Form.Control
                type="text"
                name="title"
                value={taskDetails.title || ""}
                onChange={handleInputChange}
              />
            </Form.Group>
            <Form.Group controlId="formTaskDescription">
              <Form.Label>
                <strong>Description:</strong>
              </Form.Label>
              <Form.Control
                as="textarea"
                name="description"
                value={taskDetails.description || ""}
                onChange={handleInputChange}
              />
            </Form.Group>
            <Form.Group controlId="formTaskStatus">
              <Form.Label>
                <strong>Status:</strong>
              </Form.Label>
              <Form.Control
                as="select"
                name="status"
                value={taskDetails.status || ""}
                onChange={handleInputChange}
              >
                <option value="PLANNED">{formatTaskStatus("PLANNED")}</option>
                <option value="IN_PROGRESS">
                  {formatTaskStatus("IN_PROGRESS")}
                </option>
                <option value="DONE">{formatTaskStatus("DONE")}</option>
              </Form.Control>
            </Form.Group>
            <Form.Group controlId="formTaskInitialDate">
              <Form.Label>
                <strong>Initial Date:</strong>
              </Form.Label>
              <Form.Control
                type="date"
                name="initialDate"
                value={
                  taskDetails.initialDate
                    ? taskDetails.initialDate.split("T")[0]
                    : ""
                }
                onChange={handleInputChange}
              />
            </Form.Group>
            <Form.Group controlId="formTaskFinalDate">
              <Form.Label>
                <strong>Final Date:</strong>
              </Form.Label>
              <Form.Control
                type="date"
                name="finalDate"
                value={
                  taskDetails.finalDate
                    ? taskDetails.finalDate.split("T")[0]
                    : ""
                }
                onChange={handleInputChange}
              />
            </Form.Group>
            <Form.Group controlId="formTaskOutColaboration">
              <Form.Label>
                <strong>Out Colaboration:</strong>
              </Form.Label>
              <Form.Control
                type="text"
                name="outColaboration"
                value={taskDetails.outColaboration || ""}
                onChange={handleInputChange}
              />
            </Form.Group>
            <Form.Group controlId="formTaskInCharge">
              <Form.Label>
                <strong>In Charge:</strong>
              </Form.Label>
              <Form.Control
                as="select"
                name="inCharge"
                value={taskDetails.inCharge || ""}
                onChange={(e) => {
                  const newInCharge = e.target.value;
                  setTaskDetails((prevDetails) => ({
                    ...prevDetails,
                    inCharge: newInCharge,
                  }));
                  setIsSaveEnabled(true);
                }}
              >
                {taskDetails.membersOfTask?.map((member) => (
                  <option key={member.id} value={member.systemName}>
                    {member.name}
                  </option>
                ))}
              </Form.Control>
            </Form.Group>
            <Form.Group controlId="formTaskMembers">
              <Form.Label>
                <strong>Members:</strong>
              </Form.Label>
              <ListGroup>
                {taskDetails.membersOfTask &&
                  taskDetails.membersOfTask.map((member) => (
                    <ListGroup.Item key={member.id}>
                      {member.name}{" "}
                      {(member.type === "CREATOR" ||
                        member.type === "CREATOR_INCHARGE") &&
                        "(Creator)"}
                      {(member.type !== "CREATOR" &&
                        member.type !== "CREATOR_INCHARGE") && (
                        <Button
                          variant="danger"
                          size="sm"
                          onClick={() => handleRemoveMember(member.id)}
                          style={{
                            fontSize: "0.6rem",
                            marginLeft: "10px",
                          }}
                        >
                          X
                        </Button>
                      )}
                    </ListGroup.Item>
                  ))}
              </ListGroup>
              <Form.Control
                as="select"
                onChange={(e) => {
                  handleAddMember(e.target.value);
                  e.target.value = ""; // Reset the selector
                }}
              >
                <option value="">Add New Member</option>
                {availableMembers.map((member) => (
                  <option key={member.id} value={member.systemUsername}>
                    {member.name}
                  </option>
                ))}
              </Form.Control>
            </Form.Group>
            <Form.Group controlId="formTaskDependentTasks">
              <Form.Label>
                <strong>Dependent Tasks:</strong>
              </Form.Label>
              <ListGroup>
                {taskDetails.dependentTasks &&
                  taskDetails.dependentTasks.map((task) => (
                    <ListGroup.Item key={task.id}>
                      {task.title}
                      <Button
                        variant="danger"
                        size="sm"
                        onClick={() => handleRemoveDependentTask(task.id)}
                        style={{ fontSize: "0.6rem", marginLeft: "10px" }}
                      >
                        X
                      </Button>
                    </ListGroup.Item>
                  ))}
              </ListGroup>
              <Form.Control
                as="select"
                onChange={(e) => {
                  handleAddDependentTask(e.target.value);
                  e.target.value = ""; // Reset the selector
                }}
              >
                <option value="">Add New Dependent Task</option>
                {availableTasks.map((task) => (
                  <option key={task.rawTask.id} value={task.rawTask.id}>
                    {task.name}
                  </option>
                ))}
              </Form.Control>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setIsModalVisible(false)}>
            Close
          </Button>
          <Button
            variant="primary"
            onClick={handleSave}
            disabled={!isSaveEnabled}
            style={{ backgroundColor: isSaveEnabled ? "#007bff" : "#6c757d" }}
          >
            Save changes
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default ProjectPlanPage;
