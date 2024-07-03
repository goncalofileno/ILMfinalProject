import React, { useEffect, useState } from "react";
import {
  Container,
  Row,
  Col,
  Button,
  Form,
} from "react-bootstrap";
import { useParams } from "react-router-dom";
import { Gantt, ViewMode } from "gantt-task-react";
import "gantt-task-react/dist/index.css";
import AppNavbar from "../components/headers/AppNavbar";
import ProjectTabs from "../components/headers/ProjectTabs";
import { getTasksPage, updateTask, deleteTask } from "../utilities/services"; // Importe a função deleteTask
import Cookies from "js-cookie";
import EditTaskModal from "../components/modals/EditTaskModal";
import AddTaskModal from "../components/modals/AddTaskModal";
import DeleteTaskModal from "../components/modals/DeleteTaskModal"; // Importe o modal de confirmação de eliminação
import { formatTaskStatus } from "../utilities/converters";
import "./ProjectPlanPage.css";

const parseDate = (dateString) => new Date(dateString);

const transformTasksData = (projectTask, tasks) => {
  const transformedTasks = tasks
    .filter((task) => task.initialDate && task.finalDate) // Filter tasks with valid dates
    .map((task) => ({
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

  if (projectTask.initialDate && projectTask.finalDate) {
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
  }

  return transformedTasks;
};

const formatToLocalDateTime = (date) => {
  return `${date}T00:00:00`;
};

const ProjectPlanPage = () => {
  const { systemProjectName } = useParams();
  const [tasks, setTasks] = useState([]);
  const [viewMode, setViewMode] = useState(ViewMode.Month);
  const [listCellWidth, setListCellWidth] = useState(""); // Novo estado para listCellWidth
  const [error, setError] = useState(null);
  const [projectName, setProjectName] = useState("");
  const [userSeingTasksType, setUserSeingTasksType] = useState("CREATOR");
  const [selectedTask, setSelectedTask] = useState(null);
  const [taskDetails, setTaskDetails] = useState({});
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isAddModalVisible, setIsAddModalVisible] = useState(false); // New state for AddTaskModal
  const [isDeleteModalVisible, setIsDeleteModalVisible] = useState(false); // Novo estado para o modal de confirmação de eliminação
  const [isSaveEnabled, setIsSaveEnabled] = useState(false);
  const [projectMembers, setProjectMembers] = useState([]);
  const [taskTitles, setTaskTitles] = useState([]);
  const [titleError, setTitleError] = useState("");

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
      setTaskTitles(data.tasks.map((task) => task.title));
    } catch (error) {
      console.error("Fetch error:", error);
      setError(error.message);
    }
  };

  useEffect(() => {
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
      details.title ||
      details.description ||
      details.status ||
      details.initialDate ||
      details.finalDate ||
      details.inCharge ||
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
      setIsModalVisible(false);
      fetchData(); // Re-fetch the data to update the state
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
    const updateTaskDto = {
      id: task.rawTask.id,
      title: task.rawTask.title,
      systemTitle: task.rawTask.systemTitle,
      description: task.rawTask.description,
      status: task.rawTask.status,
      initialDate: formatToLocalDateTime(task.start.toISOString().split("T")[0]),
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
      inChargeId: task.rawTask.membersOfTask.find(
        (member) =>
          member.systemName ===
          (task.rawTask.inCharge || taskDetails.inCharge)
      )?.id,
        systemProjectName: systemProjectName,
    };

    console.log("Update Task DTO on Date Change:", updateTaskDto);

    try {
      await updateTask(updateTaskDto);
      fetchData(); // Re-fetch the data to update the state
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
      fetchData(); // Re-fetch the data to update the state
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
              <Button onClick={() => setIsAddModalVisible(true)}>
                Add New Task
              </Button>
              <Form.Group controlId="viewModeSelector">
                <Form.Label>View Mode</Form.Label>
                <Form.Control
                  as="select"
                  value={viewMode}
                  onChange={handleViewModeChange}
                >
                  <option value={ViewMode.Day}>Day</option>
                  <option value={ViewMode.Week}>Week</option>
                  <option value={ViewMode.Month}>Month</option>
                  <option value={ViewMode.Year}>Year</option>
                </Form.Control>
              </Form.Group>
              <Form.Group controlId="listCellWidthToggle">
                <Form.Check
                  type="checkbox"
                  label="Toggle List Cell Width"
                  onChange={handleListCellWidthChange}
                />
              </Form.Group>
              {tasks.length > 0 && (
                <Gantt
                  tasks={tasks}
                  viewMode={viewMode}
                  onDateChange={handleDateChange}
                  onProgressChange={(task) =>
                    console.log("Task progress changed:", task)
                  }
                  onDoubleClick={handleTaskClick}
                  onDelete={(task) => console.log("Task deleted:", task)}
                  listCellWidth={listCellWidth}
                />
              )}
            </Col>
          </Row>
        </Container>
      </div>

      <EditTaskModal
        show={isModalVisible}
        handleClose={() => setIsModalVisible(false)}
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
      />

      <AddTaskModal
        show={isAddModalVisible}
        handleClose={() => setIsAddModalVisible(false)}
        projectMembers={projectMembers}
        fetchData={fetchData}
        tasks={tasks}
        titleError={titleError}
        systemProjectName={systemProjectName}
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
