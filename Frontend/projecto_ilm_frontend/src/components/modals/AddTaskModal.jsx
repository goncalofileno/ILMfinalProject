import React, { useState, useEffect } from "react";
import { Modal, Button, Form, ListGroup } from "react-bootstrap";
import { createTask } from "../../utilities/services";
import Cookies from "js-cookie";
import './AddTaskModal.css';
import { Trans, t } from "@lingui/macro";
import ReactQuill from "react-quill";

const AddTaskModal = ({
  show,
  handleClose,
  projectMembers,
  fetchData,
  tasks,
  systemProjectName,
}) => {
  const currentUsername = Cookies.get("user-systemUsername");

  const getInitialTaskDetails = () => ({
    title: "",
    description: "",
    status: "PLANNED",
    initialDate: "",
    finalDate: "",
    outColaboration: "",
    inCharge: currentUsername,
    membersOfTask: projectMembers.filter(member => member.systemUsername === currentUsername).map(member => ({ ...member, type: "CREATOR" })),
    dependentTasks: [],
    systemProjectName: systemProjectName,
  });

  const [newTaskDetails, setNewTaskDetails] = useState(getInitialTaskDetails);
  const [isSaveEnabled, setIsSaveEnabled] = useState(false);
  const [titleError, setTitleError] = useState("");

  useEffect(() => {
    setNewTaskDetails(getInitialTaskDetails());
  }, [projectMembers, currentUsername, systemProjectName]);

  useEffect(() => {
    setIsSaveEnabled(checkSaveEnabled(newTaskDetails));
  }, [newTaskDetails, titleError]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    const updatedTaskDetails = { ...newTaskDetails, [name]: value };

    if (name === "title") {
      const existingTask = tasks.find((task) => task.name === value);
      if (existingTask) {
        setTitleError(t`Task title must be unique.`);
      } else {
        setTitleError("");
      }
    }

    if (name === "initialDate") {
      const finalDate = new Date(value);
      finalDate.setDate(finalDate.getDate() + 1);
      updatedTaskDetails.finalDate = finalDate.toISOString().split("T")[0];
    }

    setNewTaskDetails(updatedTaskDetails);
    setIsSaveEnabled(checkSaveEnabled(updatedTaskDetails) && !titleError);
  };

  const handleQuillChange = (field, value) => {
    setNewTaskDetails((prevDetails) => ({
      ...prevDetails,
      [field]: value,
    }));
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
      (member) => member.systemUsername === newTaskDetails.inCharge
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
        (member) =>
          member.type === "CREATOR" || member.type === "CREATOR_INCHARGE"
      )?.id,
      inChargeId: inChargeMember ? inChargeMember.id : null,
      systemProjectName: newTaskDetails.systemProjectName,
    };

    try {
      await createTask(newTaskDto);
      handleCloseModal();
      fetchData();
    } catch (error) {
      console.error("Error creating task:", error);
    }
  };

  const handleAddMember = (systemUsername) => {
    const member = projectMembers.find(
      (member) => member.systemUsername === systemUsername
    );
    if (!member) return;
    setNewTaskDetails((prevDetails) => {
      const newMembersOfTask = [...prevDetails.membersOfTask, { ...member, type: "" }];
      const newInCharge = newMembersOfTask.find(
        (taskMember) => taskMember.systemUsername === prevDetails.inCharge
      )
        ? prevDetails.inCharge
        : member.systemUsername;

      return {
        ...prevDetails,
        membersOfTask: newMembersOfTask,
        inCharge: newInCharge,
      };
    });
  };

  const handleRemoveMember = (memberId) => {
    setNewTaskDetails((prevDetails) => {
      const newMembers = prevDetails.membersOfTask.filter(
        (member) => member.id !== memberId || member.type === "CREATOR"
      );
      const newInCharge = newMembers.find(
        (member) => member.systemUsername === prevDetails.inCharge
      )
        ? prevDetails.inCharge
        : newMembers[0]?.systemUsername || "";

      return {
        ...prevDetails,
        membersOfTask: newMembers,
        inCharge: newInCharge,
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
      ) && task.type !== "project" && task.type !== "milestone"
  );

  const handleCloseModal = () => {
    setNewTaskDetails(getInitialTaskDetails());
    setIsSaveEnabled(false);
    setTitleError("");
    handleClose();
  };

  return (
    <Modal show={show} onHide={handleCloseModal} dialogClassName="custom-modal">
      <Modal.Header closeButton>
        <Modal.Title><Trans>Add New Task</Trans></Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          <div className="row">
            <div className="col-md-6">
              <Form.Group controlId="formTaskTitle">
                <Form.Label>
                  <strong><Trans>Title</Trans>:</strong>
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
                  <strong><Trans>Description</Trans>:</strong>
                </Form.Label>
                <ReactQuill
                  value={newTaskDetails.description || ""}
                  onChange={(value) => handleQuillChange("description", value)}
                  style={{ height: "130px", marginBottom: "50px" }}
                />
              </Form.Group>
              <Form.Group controlId="formTaskInitialDate">
                <Form.Label>
                  <strong><Trans>Initial Date</Trans>:</strong>
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
                  <strong><Trans>Final Date</Trans>:</strong>
                </Form.Label>
                <Form.Control
                  type="date"
                  name="finalDate"
                  value={newTaskDetails.finalDate}
                  onChange={handleInputChange}
                  min={
                    newTaskDetails.initialDate
                      ? new Date(
                          new Date(newTaskDetails.initialDate).getTime() + 86400000
                        )
                          .toISOString()
                          .split("T")[0]
                      : ""
                  }
                />
              </Form.Group>
              <Form.Group controlId="formTaskOutColaboration">
                <Form.Label>
                  <strong><Trans>Out Colaboration</Trans>:</strong>
                </Form.Label>
                <Form.Control
                  as="textarea"
                  name="outColaboration"
                  type="text"
                  value={newTaskDetails.outColaboration || ""}
                  onChange={handleInputChange}
                  style={{ height: "70px"}}
                />
              </Form.Group>
            </div>
            <div className="col-md-6">
              <Form.Group controlId="formTaskInCharge">
                <Form.Label>
                  <strong><Trans>In Charge</Trans>:</strong>
                </Form.Label>
                <Form.Control
                  as="select"
                  name="inCharge"
                  value={newTaskDetails.inCharge}
                  onChange={handleInputChange}
                >
                  {newTaskDetails.membersOfTask.map((member) => (
                    <option key={member.id} value={member.systemUsername}>
                      {member.name}
                    </option>
                  ))}
                </Form.Control>
              </Form.Group>
              <Form.Group controlId="formTaskMembers">
                <Form.Label>
                  <strong><Trans>Members</Trans>:</strong>
                </Form.Label>
                <ListGroup className="fixed-height-list2">
                  {newTaskDetails.membersOfTask.map((member) => (
                    <ListGroup.Item key={member.id} className="mini-card">
                      {member.name}{" "}
                      {(member.type === "CREATOR" ||
                        member.type === "CREATOR_INCHARGE") &&
                        "(Creator)"}
                      {member.type !== "CREATOR" &&
                        member.type !== "CREATOR_INCHARGE" && (
                        <Button
                          variant="danger"
                          size="sm"
                          onClick={() => handleRemoveMember(member.id)}
                          className="mini-card-button"
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
                  <option value=""><Trans>Add New Member</Trans></option>
                  {availableMembers.map((member) => (
                    <option key={member.id} value={member.systemUsername}>
                      {member.name}
                    </option>
                  ))}
                </Form.Control>
              </Form.Group>
              <Form.Group controlId="formTaskDependentTasks">
                <Form.Label>
                  <strong><Trans>Dependent Tasks</Trans>:</strong>
                </Form.Label>
                <ListGroup className="fixed-height-list2">
                  {newTaskDetails.dependentTasks.map((task) => (
                    <ListGroup.Item key={task.id} className="mini-card">
                      {task.title}
                      <Button
                        variant="danger"
                        size="sm"
                        onClick={() => handleRemoveDependentTask(task.id)}
                        className="mini-card-button"
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
                  <option value=""><Trans>Add New Dependent Task</Trans></option>
                  {availableTasks.map((task) => (
                    <option key={task.rawTask.id} value={task.rawTask.id}>
                      {task.name}
                    </option>
                  ))}
                </Form.Control>
              </Form.Group>
            </div>
          </div>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleCloseModal}>
          <Trans>Close</Trans>
        </Button>
        <Button
          variant="primary"
          onClick={handleSave}
          disabled={!isSaveEnabled}
          style={{ backgroundColor: isSaveEnabled ? "#007bff" : "#6c757d" }}
        >
          <Trans>Add New Task</Trans>
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default AddTaskModal;
