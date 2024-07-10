import React from "react";
import { Modal, Button, Form, ListGroup } from "react-bootstrap";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import { formatTaskStatus } from "../../utilities/converters";
import "./AddTaskModal.css";
import { Trans } from "@lingui/macro";
import "./EditTaskModal.css";

const EditTaskModal = ({
  show,
  handleClose,
  taskDetails,
  handleInputChange,
  handleSave,
  isSaveEnabled,
  titleError,
  availableMembers,
  handleAddMember,
  handleRemoveMember,
  availableTasks,
  handleAddDependentTask,
  handleRemoveDependentTask,
  handleDeleteClick,
}) => {
  const handleQuillChange = (field, value) => {
    handleInputChange({
      target: {
        name: field,
        value: value,
      },
    });
  };

  return (
    <Modal show={show} onHide={handleClose} id="edit-task-modal">
      <Modal.Header closeButton>
        <Modal.Title>
          <Trans>Edit Task</Trans>
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          <div className="row">
            <div className="col-md-6">
              <Form.Group controlId="formTaskTitle">
                <Form.Label>
                  <strong>
                    <Trans>Title</Trans>:
                  </strong>
                </Form.Label>
                <Form.Control
                  type="text"
                  name="title"
                  value={taskDetails.title || ""}
                  onChange={handleInputChange}
                  isInvalid={!!titleError}
                />
                <Form.Control.Feedback type="invalid">
                  {titleError}
                </Form.Control.Feedback>
              </Form.Group>
              <Form.Group controlId="formTaskStatus">
                <Form.Label>
                  <strong>
                    <Trans>Status</Trans>:
                  </strong>
                </Form.Label>
                <Form.Select
                  name="status"
                  value={taskDetails.status || ""}
                  onChange={handleInputChange}
                >
                  <option value="PLANNED">{formatTaskStatus("PLANNED")}</option>
                  <option value="IN_PROGRESS">
                    {formatTaskStatus("IN_PROGRESS")}
                  </option>
                  <option value="DONE">{formatTaskStatus("DONE")}</option>
                </Form.Select>
              </Form.Group>
              <Form.Group controlId="formTaskDescription">
                <Form.Label>
                  <strong>
                    <Trans>Description</Trans>:
                  </strong>
                </Form.Label>
                <ReactQuill
                  value={taskDetails.description || ""}
                  onChange={(value) => handleQuillChange("description", value)}
                  style={{ height: "180px", marginBottom: "50px" }}
                />
              </Form.Group>

              <Form.Group controlId="formTaskInitialDate">
                <Form.Label>
                  <strong>
                    <Trans>Initial Date</Trans>:
                  </strong>
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
                  <strong>
                    <Trans>Final Date</Trans>:
                  </strong>
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
                  min={
                    taskDetails.initialDate
                      ? new Date(
                          new Date(taskDetails.initialDate).getTime() + 86400000
                        )
                          .toISOString()
                          .split("T")[0]
                      : ""
                  }
                />
              </Form.Group>
            </div>
            <div className="col-md-6">
              <Form.Group controlId="formTaskOutColaboration">
                <Form.Label>
                  <strong>
                    <Trans>Out Colaboration</Trans>:
                  </strong>
                </Form.Label>
                <Form.Control
                  type="text"
                  name="outColaboration"
                  value={taskDetails.outColaboration || ""}
                  onChange={(value) =>
                    handleQuillChange("outColaboration", value)
                  }
                  style={{ height: "80px" }}
                />
              </Form.Group>
              <Form.Group controlId="formTaskInCharge">
                <Form.Label>
                  <strong>
                    <Trans>In Charge</Trans>:
                  </strong>
                </Form.Label>
                <Form.Select
                  name="inCharge"
                  value={taskDetails.inCharge || ""}
                  onChange={handleInputChange}
                >
                  {taskDetails.membersOfTask?.map((member) => (
                    <option key={member.id} value={member.systemName}>
                      {member.name}
                    </option>
                  ))}
                </Form.Select>
              </Form.Group>
              <Form.Group controlId="formTaskMembers">
                <Form.Label>
                  <strong>
                    <Trans>Members</Trans>:
                  </strong>
                </Form.Label>
                <ListGroup className="fixed-height-list2">
                  {taskDetails.membersOfTask &&
                    taskDetails.membersOfTask.map((member) => (
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
                            >
                              X
                            </Button>
                          )}
                      </ListGroup.Item>
                    ))}
                </ListGroup>
                <Form.Select
                  onChange={(e) => {
                    handleAddMember(e.target.value);
                    e.target.value = ""; 
                  }}
                >
                  <option value="">
                    <Trans>Add New Member</Trans>
                  </option>
                  {availableMembers.map((member) => (
                    <option key={member.id} value={member.systemUsername}>
                      {member.name}
                    </option>
                  ))}
                </Form.Select>
              </Form.Group>
              <Form.Group controlId="formTaskDependentTasks">
                <Form.Label>
                  <strong>
                    <Trans>Dependent Tasks</Trans>:
                  </strong>
                </Form.Label>
                <ListGroup className="fixed-height-list2">
                  {taskDetails.dependentTasks &&
                    taskDetails.dependentTasks.map((task) => (
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
                <Form.Select
                  onChange={(e) => {
                    handleAddDependentTask(e.target.value);
                    e.target.value = ""; // Reset the selector
                  }}
                >
                  <option value="">
                    <Trans>Add New Dependent Task</Trans>
                  </option>
                  {availableTasks.map((task) => (
                    <option key={task.rawTask.id} value={task.rawTask.id}>
                      {task.name}
                    </option>
                  ))}
                </Form.Select>
              </Form.Group>
            </div>
          </div>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          <Trans>Close</Trans>
        </Button>
        <Button
          variant="danger"
          onClick={handleDeleteClick}
          style={{
            marginLeft: "auto",
            backgroundColor: "#dc3545",
            borderColor: "#dc3545",
          }}
        >
          <Trans>Delete Task</Trans>
        </Button>
        <Button
          variant="primary"
          onClick={handleSave}
          disabled={!isSaveEnabled}
          style={{ backgroundColor: isSaveEnabled ? "#007bff" : "#6c757d" }}
        >
          <Trans>Save changes</Trans>
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default EditTaskModal;
