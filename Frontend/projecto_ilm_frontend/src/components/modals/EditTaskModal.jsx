import React from "react";
import { Modal, Button, Form, ListGroup } from "react-bootstrap";
import { formatTaskStatus } from "../../utilities/converters";

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
  handleDeleteClick, // Adicione esta linha
}) => {
  return (
    <Modal show={show} onHide={handleClose}>
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
                taskDetails.finalDate ? taskDetails.finalDate.split("T")[0] : ""
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
              onChange={handleInputChange}
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
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        
        <Button
          variant="danger"
          onClick={handleDeleteClick} // Adicione esta linha
          style={{ marginLeft: "auto" }}
        >
          Delete Task
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
  );
};

export default EditTaskModal;
