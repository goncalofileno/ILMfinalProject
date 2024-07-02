import React, { useState, useEffect } from "react";
import { Modal, Button, Form, ListGroup } from "react-bootstrap";
import { createTask } from "../../utilities/services";
import { formatTaskStatus } from "../../utilities/converters";
import Cookies from "js-cookie";

const AddTaskModal = ({ show, handleClose, projectMembers, fetchData, tasks, systemProjectName }) => {
  const currentUsername = Cookies.get("user-systemUsername");

  const [newTaskDetails, setNewTaskDetails] = useState({
    title: "",
    description: "",
    status: "PLANNED",
    initialDate: "",
    finalDate: "",
    outColaboration: "",
    inCharge: currentUsername,
    membersOfTask: [],
    dependentTasks: [],
    systemProjectName: systemProjectName, // Adicione o nome do projeto aqui
  });
  const [isSaveEnabled, setIsSaveEnabled] = useState(false);
  const [titleError, setTitleError] = useState("");

  useEffect(() => {
    const currentUser = projectMembers.find(member => member.systemUsername === currentUsername);
    if (currentUser) {
      setNewTaskDetails(prevDetails => ({
        ...prevDetails,
        membersOfTask: [{ ...currentUser, type: "CREATOR" }],
        inCharge: currentUser.systemName
      }));
    }
  }, [projectMembers, currentUsername]);

  useEffect(() => {
    setIsSaveEnabled(checkSaveEnabled(newTaskDetails));
  }, [newTaskDetails, titleError]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    const updatedTaskDetails = { ...newTaskDetails, [name]: value };

    if (name === "title") {
      const existingTask = tasks.find((task) => task.name === value);
      if (existingTask) {
        setTitleError("Task title must be unique.");
      } else {
        setTitleError("");
      }
    }

    setNewTaskDetails(updatedTaskDetails);
    setIsSaveEnabled(checkSaveEnabled(updatedTaskDetails) && !titleError);
  };

  const checkSaveEnabled = (details) => {
    return (
      details.title &&
      details.description &&
      details.initialDate &&
      details.finalDate &&
      !titleError
    );
  };

  const handleSave = async () => {
    const inChargeMember = newTaskDetails.membersOfTask.find(
      (member) => member.systemName === newTaskDetails.inCharge
    );

    const newTaskDto = {
      title: newTaskDetails.title,
      systemTitle: newTaskDetails.title.replace(/\s+/g, "").toLowerCase(),
      description: newTaskDetails.description,
      status: newTaskDetails.status,
      initialDate: `${newTaskDetails.initialDate}T00:00:00`,
      finalDate: `${newTaskDetails.finalDate}T00:00:00`,
      outColaboration: newTaskDetails.outColaboration || "",
      dependentTaskIds: newTaskDetails.dependentTasks.map((task) => task.id),
      memberIds: newTaskDetails.membersOfTask.map((member) => member.id),
      creatorId: newTaskDetails.membersOfTask.find(
        (member) => member.type === "CREATOR" || member.type === "CREATOR_INCHARGE"
      )?.id,
      inChargeId: inChargeMember?.id, // Correctly set inChargeId
      systemProjectName: newTaskDetails.systemProjectName,
    };

    console.log("New Task DTO:", newTaskDto);

    try {
      await createTask(newTaskDto);
      handleCloseModal();
      fetchData(); // Re-fetch the data to update the state
    } catch (error) {
      console.error("Error creating task:", error);
    }
  };

  const handleAddMember = (systemUsername) => {
    const member = projectMembers.find(
      (member) => member.systemUsername === systemUsername
    );
    if (!member) return;
    setNewTaskDetails((prevDetails) => ({
      ...prevDetails,
      membersOfTask: [...prevDetails.membersOfTask, { ...member, type: "" }],
    }));
  };

  const handleRemoveMember = (memberId) => {
    setNewTaskDetails((prevDetails) => {
      const newMembers = prevDetails.membersOfTask.filter(
        (member) => member.id !== memberId || member.type === "CREATOR"
      );
      return {
        ...prevDetails,
        membersOfTask: newMembers,
      };
    });
  };

  const handleAddDependentTask = (taskId) => {
    const task = tasks.find((task) => task.rawTask.id === parseInt(taskId));
    if (!task) return;
    setNewTaskDetails((prevDetails) => ({
      ...prevDetails,
      dependentTasks: [...prevDetails.dependentTasks, task.rawTask],
    }));
  };

  const handleRemoveDependentTask = (taskId) => {
    setNewTaskDetails((prevDetails) => ({
      ...prevDetails,
      dependentTasks: prevDetails.dependentTasks.filter(
        (task) => task.id !== taskId
      ),
    }));
  };

  const availableMembers = projectMembers.filter(
    (member) =>
      !newTaskDetails.membersOfTask.some(
        (taskMember) => taskMember.id === member.id
      )
  );

  const availableTasks = tasks.filter(
    (task) =>
      !newTaskDetails.dependentTasks.some(
        (depTask) => depTask.id === task.rawTask.id
      )
  );

  const handleCloseModal = () => {
    setNewTaskDetails({
      title: "",
      description: "",
      status: "PLANNED",
      initialDate: "",
      finalDate: "",
      outColaboration: "",
      inCharge: currentUsername,
      membersOfTask: [{ ...projectMembers.find(member => member.systemUsername === currentUsername), type: "CREATOR" }],
      dependentTasks: [],
      systemProjectName: newTaskDetails.systemProjectName, // Mantenha o nome do projeto ao fechar
    });
    setIsSaveEnabled(false);
    setTitleError("");
    handleClose();
  };

  return (
    <Modal show={show} onHide={handleCloseModal}>
      <Modal.Header closeButton>
        <Modal.Title>Add New Task</Modal.Title>
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
              value={newTaskDetails.title}
              onChange={handleInputChange}
              isInvalid={!!titleError}
            />
            <Form.Control.Feedback type="invalid">
              {titleError}
            </Form.Control.Feedback>
          </Form.Group>
          <Form.Group controlId="formTaskDescription">
            <Form.Label>
              <strong>Description:</strong>
            </Form.Label>
            <Form.Control
              as="textarea"
              name="description"
              value={newTaskDetails.description}
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
              value={newTaskDetails.status}
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
              value={newTaskDetails.initialDate}
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
              value={newTaskDetails.finalDate}
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
              value={newTaskDetails.outColaboration}
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
              value={newTaskDetails.inCharge}
              onChange={handleInputChange}
            >
              {newTaskDetails.membersOfTask.map((member) => (
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
              {newTaskDetails.membersOfTask.map((member) => (
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
                      disabled={member.type === "CREATOR"} // Disable remove button for creator
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
              {newTaskDetails.dependentTasks.map((task) => (
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
        <Button variant="secondary" onClick={handleCloseModal}>
          Close
        </Button>
        <Button
          variant="primary"
          onClick={handleSave}
          disabled={!isSaveEnabled}
          style={{ backgroundColor: isSaveEnabled ? "#007bff" : "#6c757d" }}
        >
          Add New Task
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default AddTaskModal;
