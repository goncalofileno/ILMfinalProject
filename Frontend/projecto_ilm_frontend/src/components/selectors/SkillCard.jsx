import React from "react";
import { Card, Button } from "react-bootstrap";
import "./SkillCard.css"; // Certifique-se de que o caminho esteja correto

const SkillCard = ({ skill, onRemove }) => {
  return (
    <Card className="mt-2 skill-card">
      <div className="div-mestra-button-card">
        <div className="button-card">
          <Button
            variant="danger"
            size="sm"
            className="remove-btn"
            onClick={() => onRemove(skill)}
          >
            x
          </Button>
        </div>
      </div>

      <Card.Body className="skill-card-body">
        <div className="card-title-line">
          <Card.Title className="skill-title">{skill.name}</Card.Title>
        </div>
        <Card.Subtitle className="mb-2 text-muted skill-type">
          {skill.type}
        </Card.Subtitle>
      </Card.Body>
    </Card>
  );
};

export default SkillCard;
