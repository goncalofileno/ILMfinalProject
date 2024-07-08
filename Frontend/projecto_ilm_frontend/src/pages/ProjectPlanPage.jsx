import React, { useEffect, useState } from "react";
import { Container, Row, Col, Button, Form, Alert } from "react-bootstrap";
import { useParams, useNavigate } from "react-router-dom";
import { Gantt, ViewMode } from "gantt-task-react";
import "gantt-task-react/dist/index.css";
import AppNavbar from "../components/headers/AppNavbar";
import ProjectTabs from "../components/headers/ProjectTabs";
import { getTasksPage, updateTask, deleteTask } from "../utilities/services";
import Cookies from "js-cookie";
import EditTaskModal from "../components/modals/EditTaskModal";
import AddTaskModal from "../components/modals/AddTaskModal";
import DeleteTaskModal from "../components/modals/DeleteTaskModal";
import ProgressBar from "../components/bars/ProgressBar";
import CustomTooltipContent from "../components/tooltips/CustomTooltipContent";
import "./ProjectPlanPage.css";
import { Trans, t } from "@lingui/macro";

const parseDate = (dateString) => new Date(dateString);

const transformTasksData = (projectTask, tasks, projectProgress) => {
  const transformedTasks = tasks
    .filter((task) => task.initialDate && task.finalDate)
    .map((task) => ({
      id: task.systemTitle,
      name: task.title,
      type: task.systemTitle.endsWith("_final_presentation")
        ? "milestone"
        : "task",
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

  if (projectTask.initialDate && projectTask.finalDate) {
    transformedTasks.unshift({
      id: projectTask.systemTitle,
      name: projectTask.title,
      type: "project",
      start: parseDate(projectTask.initialDate),
      end: parseDate(projectTask.finalDate),
      dependencies: [],
      styles: {
        backgroundColor: "#3F51B5",
        progressColor: "#3F51B5",
        progressSelectedColor: "#3F51B5",
      },
      rawTask: projectTask,
      isDisable: true,
      hideChildren: true,
    });
  }

  const finalPresentationTask = transformedTasks.find((task) =>
    task.id.endsWith("_final_presentation")
  );
  if (finalPresentationTask) {
    transformedTasks.push(
      transformedTasks.splice(
        transformedTasks.indexOf(finalPresentationTask),
        1
      )[0]
    );
  }

  return transformedTasks;
};

const formatToLocalDateTime = (date) => {
  return `${date}T00:00:00`;
};

const ProjectPlanPage = () => {
  const { systemProjectName, taskSystemTitle } = useParams();
  const navigate = useNavigate();
  const [tasks, setTasks] = useState([]);
  const [viewMode, setViewMode] = useState(ViewMode.Month);
  const [listCellWidth, setListCellWidth] = useState("");
  const [error, setError] = useState(null);
  const [projectName, setProjectName] = useState("");
  const [userSeingTasksType, setUserSeingTasksType] = useState("CREATOR");
  const [selectedTask, setSelectedTask] = useState(null);
  const [taskDetails, setTaskDetails] = useState({});
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isAddModalVisible, setIsAddModalVisible] = useState(false);
  const [isDeleteModalVisible, setIsDeleteModalVisible] = useState(false);
  const [isSaveEnabled, setIsSaveEnabled] = useState(false);
  const [projectMembers, setProjectMembers] = useState([]);
  const [taskTitles, setTaskTitles] = useState([]);
  const [titleError, setTitleError] = useState("");
  const [percentage, setPercentage] = useState(0);
  const [projectState, setProjectState] = useState("IN_PROGRESS");

  const fetchData = async () => {
    const sessionId = Cookies.get("session-id");
    try {
      const data = await getTasksPage(sessionId, systemProjectName);
      console.log("Tasks data:", data);
      const transformedTasks = transformTasksData(
        data.projectTask,
        data.tasks,
        data.projectProgress
      );
      setProjectName(data.projectName);
      setUserSeingTasksType(data.userSeingTasksType);
      setTasks(transformedTasks);
      setProjectMembers(data.projectMembers);
      setTaskTitles(data.tasks.map((task) => task.title));
      setPercentage(data.projectProgress);
      setProjectState(data.projectState);

      if (taskSystemTitle) {
        const taskToOpen = transformedTasks.find(
          (task) => task.id === taskSystemTitle
        );
        if (taskToOpen) {
          handleTaskClick(taskToOpen);
        }
      }
    } catch (error) {
      console.error("Fetch error:", error);
      setError(error.message);
    }
  };

  useEffect(() => {
    fetchData();
  }, [systemProjectName]);

  const handleTaskClick = (task) => {
    if (task.type === "project") return;

    navigate(`/project/${systemProjectName}/plan/${task.id}`, {
      replace: true,
    });

    const inChargeMember = task.rawTask.membersOfTask.find(
      (member) =>
        member.type === "INCHARGE" || member.type === "CREATOR_INCHARGE"
    )?.systemName;
    setSelectedTask(task);
    setTaskDetails({ ...task.rawTask, inCharge: inChargeMember || "" });
    setIsModalVisible(true);
    setIsSaveEnabled(false);
  };

  const handleCloseModal = () => {
    navigate(`/project/${systemProjectName}/plan`, { replace: true });
    setIsModalVisible(false);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    let error = "";

    if (name === "title" && taskTitles.includes(value)) {
      error = "Task title must be unique.";
      setTitleError(error);
    } else {
      setTitleError("");
    }

    const newTaskDetails = { ...taskDetails, [name]: value };
    setTaskDetails(newTaskDetails);
    setIsSaveEnabled(checkSaveEnabled(newTaskDetails) && !error);
  };

  const checkSaveEnabled = (details) => {
    return (
      details.title &&
      details.description &&
      details.status &&
      details.initialDate &&
      details.finalDate &&
      details.inCharge &&
      !titleError
    );
  };

  const handleSave = async () => {
    const updateTaskDto = {
      id: taskDetails.id,
      title: taskDetails.title,
      systemTitle: taskDetails.systemTitle,
      description: taskDetails.description,
      status: taskDetails.status,
      initialDate: formatToLocalDateTime(taskDetails.initialDate.split("T")[0]),
      finalDate: formatToLocalDateTime(taskDetails.finalDate.split("T")[0]),
      outColaboration: taskDetails.outColaboration || "",
      dependentTaskIds: taskDetails.dependentTasks
        ? taskDetails.dependentTasks.map((task) => task.id)
        : [],
      memberIds: taskDetails.membersOfTask
        ? taskDetails.membersOfTask.map((member) => member.id)
        : [],
      creatorId: taskDetails.membersOfTask.find(
        (member) =>
          member.type === "CREATOR" || member.type === "CREATOR_INCHARGE"
      )?.id,
      inChargeId: taskDetails.membersOfTask.find(
        (member) => member.systemName === taskDetails.inCharge
      )?.id,
      systemProjectName: systemProjectName,
    };

    console.log("Update Task DTO:", updateTaskDto);

    try {
      await updateTask(updateTaskDto);
      handleCloseModal();
      fetchData();
      setTaskDetails({});
      taskSystemTitle = null;
    } catch (error) {
      console.error("Error updating task:", error);
    }
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

  const handleDateChange = async (task) => {
    const inChargeUsername = taskDetails.inCharge || task.rawTask.inCharge;

    const inChargeMember = task.rawTask.membersOfTask.find(
      (member) => member.systemUsername === inChargeUsername
    );

    const updateTaskDto = {
      id: task.rawTask.id,
      title: task.rawTask.title,
      systemTitle: task.rawTask.systemTitle,
      description: task.rawTask.description,
      status: task.rawTask.status,
      initialDate: formatToLocalDateTime(
        task.start.toISOString().split("T")[0]
      ),
      finalDate: formatToLocalDateTime(task.end.toISOString().split("T")[0]),
      outColaboration: task.rawTask.outColaboration || "",
      dependentTaskIds: task.rawTask.dependentTasks
        ? task.rawTask.dependentTasks.map((dep) => dep.id)
        : [],
      memberIds: task.rawTask.membersOfTask
        ? task.rawTask.membersOfTask.map((member) => member.id)
        : [],
      creatorId: task.rawTask.membersOfTask.find(
        (member) =>
          member.type === "CREATOR" || member.type === "CREATOR_INCHARGE"
      )?.id,
      inChargeId: inChargeMember?.id || null,
      systemProjectName: systemProjectName,
    };

    console.log("Update Task DTO on Date Change:", updateTaskDto);

    try {
      await updateTask(updateTaskDto);
      fetchData();
      setTaskDetails({});
    } catch (error) {
      console.error("Error updating task on date change:", error);
    }
  };

  const handleDeleteClick = () => {
    setIsModalVisible(false);
    setIsDeleteModalVisible(true);
  };

  const handleConfirmDelete = async () => {
    console.log("Task to delete:", taskDetails);
    const deleteTaskDto = {
      id: taskDetails.id,
      title: taskDetails.title,
      systemTitle: taskDetails.systemTitle,
      description: taskDetails.description,
      status: taskDetails.status,
      initialDate: formatToLocalDateTime(taskDetails.initialDate.split("T")[0]),
      finalDate: formatToLocalDateTime(taskDetails.finalDate.split("T")[0]),
      outColaboration: taskDetails.outColaboration || "",
      dependentTaskIds: taskDetails.dependentTasks
        ? taskDetails.dependentTasks.map((task) => task.id)
        : [],
      memberIds: taskDetails.membersOfTask
        ? taskDetails.membersOfTask.map((member) => member.id)
        : [],
      creatorId: taskDetails.membersOfTask.find(
        (member) =>
          member.type === "CREATOR" || member.type === "CREATOR_INCHARGE"
      )?.id,
      inChargeId: taskDetails.membersOfTask.find(
        (member) => member.systemName === taskDetails.inCharge
      )?.id,
      systemProjectName: systemProjectName,
    };

    console.log("Delete Task DTO:", deleteTaskDto);

    try {
      await deleteTask(deleteTaskDto);
      setIsDeleteModalVisible(false);
      fetchData();
      setTaskDetails({});
    } catch (error) {
      console.error("Error deleting task:", error);
    }
  };

  const handleViewModeChange = (e) => {
    setViewMode(e.target.value);
  };

  const handleListCellWidthChange = () => {
    setListCellWidth(listCellWidth === "" ? "170px" : "");
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

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (!tasks.length) {
    return <div>Loading...</div>;
  }

  return (
    <>
      <AppNavbar />
      <div className="bckg-color-ilm-page ilm-pageb">
        <ProjectTabs typeOfUserSeingProject={userSeingTasksType} />
        <Container style={{ height: "91%" }}>
          <Row>
            <Col>
              <Row>
                <div style={{ width: "500px" }}>
                  <Button
                    onClick={() => setIsAddModalVisible(true)}
                    disabled={["CANCELED", "READY"].includes(projectState)}
                  >
                    Add New Task
                  </Button>
                </div>
                <div style={{ width: "500px" }}>
                  <Form.Group controlId="viewModeSelector">
                    <Form.Label>View Mode</Form.Label>
                    <Form.Control
                      as="select"
                      value={viewMode}
                      onChange={handleViewModeChange}
                      disabled={["CANCELED", "READY"].includes(projectState)}
                    >
                      <option value={ViewMode.Day}>Day</option>
                      <option value={ViewMode.Month}>Month</option>
                      <option value={ViewMode.Year}>Year</option>
                    </Form.Control>
                  </Form.Group>
                </div>
                <div>
                  <Form.Group controlId="listCellWidthToggle">
                    <Form.Check
                      type="checkbox"
                      label="Toggle List Cell Width"
                      onChange={handleListCellWidthChange}
                      disabled={["CANCELED", "READY"].includes(projectState)}
                    />
                  </Form.Group>
                </div>
              </Row>
              <Row>
                {tasks.length > 0 && (
                  <Gantt
                    tasks={tasks}
                    viewMode={viewMode}
                    onDateChange={
                      !["CANCELED", "READY"].includes(projectState)
                        ? handleDateChange
                        : undefined
                    }
                    onProgressChange={(task) =>
                      console.log("Task progress changed:", task)
                    }
                    onDoubleClick={
                      !["CANCELED", "READY"].includes(projectState)
                        ? handleTaskClick
                        : undefined
                    }
                    onDelete={(task) =>
                      !["CANCELED", "READY"].includes(projectState)
                        ? handleDeleteClick(task)
                        : undefined
                    }
                    listCellWidth={listCellWidth}
                    columnWidth={100}
                    TooltipContent={CustomTooltipContent}
                  />
                )}
              </Row>
            </Col>
          </Row>
          <Row>
            <div style={{ marginTop: "15px" }}>
              <h5>Project Progress:</h5>
            </div>
            <div>
              <ProgressBar percentage={percentage} status={projectState} />
            </div>
          </Row>
          <Row>
            <div style={{ marginTop: "15px" }}>
              <h5>Legend:</h5>
            </div>
            <div>
              <ul>
                <li>
                  <span
                    style={{
                      backgroundColor: "#8BC34A",
                      padding: "2px 8px",
                      borderRadius: "4px",
                    }}
                  >
                    DONE
                  </span>{" "}
                  - Completed tasks
                </li>
                <li>
                  <span
                    style={{
                      backgroundColor: "#FFEB3B",
                      padding: "2px 8px",
                      borderRadius: "4px",
                    }}
                  >
                    IN PROGRESS
                  </span>{" "}
                  - Tasks in progress
                </li>
                <li>
                  <span
                    style={{
                      backgroundColor: "#F44336",
                      padding: "2px 8px",
                      borderRadius: "4px",
                    }}
                  >
                    PLANNED
                  </span>{" "}
                  - Planned tasks
                </li>
                <li>
                  <span
                    style={{
                      backgroundColor: "#3F51B5",
                      padding: "2px 8px",
                      borderRadius: "4px",
                    }}
                  >
                    PROJECT
                  </span>{" "}
                  - Project
                </li>
              </ul>
            </div>
          </Row>
          {["CANCELED", "READY"].includes(projectState) && (
            <Alert variant="danger" className="standard-modal">
              The project is {projectState.toLowerCase()} and no tasks can be added or changes made.
            </Alert>
          )}
        </Container>
      </div>

      <EditTaskModal
        show={isModalVisible}
        handleClose={handleCloseModal}
        taskDetails={taskDetails}
        handleInputChange={handleInputChange}
        handleSave={handleSave}
        handleDeleteClick={handleDeleteClick}
        isSaveEnabled={isSaveEnabled}
        titleError={titleError}
        availableMembers={availableMembers}
        handleAddMember={handleAddMember}
        handleRemoveMember={handleRemoveMember}
        availableTasks={availableTasks}
        handleAddDependentTask={handleAddDependentTask}
        handleRemoveDependentTask={handleRemoveDependentTask}
        disabled={["CANCELED", "READY"].includes(projectState)}
      />

      <AddTaskModal
        show={isAddModalVisible}
        handleClose={() => setIsAddModalVisible(false)}
        projectMembers={projectMembers}
        fetchData={fetchData}
        tasks={tasks}
        titleError={titleError}
        systemProjectName={systemProjectName}
        disabled={["CANCELED", "READY"].includes(projectState)}
      />

      <DeleteTaskModal
        show={isDeleteModalVisible}
        handleClose={() => setIsDeleteModalVisible(false)}
        handleConfirmDelete={handleConfirmDelete}
      />
    </>
  );
};

export default ProjectPlanPage;
