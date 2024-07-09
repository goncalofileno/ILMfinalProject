import React from "react";
import { Card } from "react-bootstrap";
import { Trans } from "@lingui/macro";

const TaskCard = ({ task, handleTaskClick }) => {
  const { title, initialDate, finalDate, membersOfTask, dependentTasks, status } = task.rawTask;
  const inCharge = membersOfTask.find(member => 
    member.type === "INCHARGE" || member.type === "CREATOR_INCHARGE"
  )?.name || "";

  const statusStyles = {
    DONE: "#8bc34a5e",
    IN_PROGRESS: "#ffeb3b58",
    PLANNED: "#f443365e",
  };

  return (
    <Card
      className="mb-3"
      onClick={() => handleTaskClick(task)}
      style={{ backgroundColor: statusStyles[status] }}
    >
      <Card.Body>
        <Card.Title>{title}</Card.Title>
        <Card.Text>
          <strong><Trans>Start Date</Trans>:</strong> {new Date(initialDate).toLocaleDateString()}
        </Card.Text>
        <Card.Text>
          <strong><Trans>End Date</Trans>:</strong> {new Date(finalDate).toLocaleDateString()}
        </Card.Text>
        <Card.Text>
          <strong><Trans>In Charge</Trans>:</strong> {inCharge}
        </Card.Text>
        {dependentTasks.length > 0 && (
          <Card.Text>
            <strong><Trans>Dependencies</Trans>:</strong> {dependentTasks.map(dep => dep.title).join(', ')}
          </Card.Text>
        )}
      </Card.Body>
    </Card>
  );
};

export default TaskCard;
