import React from "react";
import { Card, Button } from "react-bootstrap";
import "./InterestCard.css"; 
import { Trans, t } from "@lingui/macro";

const InterestCard = ({ interest, onRemove }) => {
  return (
    <Card className="mt-2 interest-card">
      <div className="div-mestra-button-card">
        <div className="button-card">
          <Button
            variant="danger"
            size="sm"
            className="remove-btn"
            onClick={() => onRemove(interest)}
          >
            x
          </Button>
        </div>
      </div>

      <Card.Body className="interest-card-body">
        <div className="card-title-line">
          <Card.Title className="interest-title">{interest.name}</Card.Title>
        </div>
      </Card.Body>
    </Card>
  );
};

export default InterestCard;
