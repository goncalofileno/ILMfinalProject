import React, { useState, useEffect } from "react";
import {
  Form,
  Row,
  Col,
  Button,
  ListGroup,
  InputGroup,
  Container,
  Alert,
} from "react-bootstrap";
import { getInterests } from "../../utilities/services";
import { useParams } from "react-router-dom";
import InterestCard from "./InterestCard"; // Import the new InterestCard component
import "./InterestSelector.css"; // Ensure the path is correct

const InterestSelector = ({ selectedInterests, setSelectedInterests }) => {
  const [allInterests, setAllInterests] = useState([]);
  const [suggestions, setSuggestions] = useState([]);
  const [input, setInput] = useState("");
  const [selectedSuggestionIndex, setSelectedSuggestionIndex] = useState(-1);
  const { token } = useParams();

  useEffect(() => {
    getInterests(token)
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        setAllInterests(data);
      });
  }, [token]);

  const handleInputChange = (e) => {
    const value = e.target.value;
    setInput(value);
    setSelectedSuggestionIndex(-1);
    if (value) {
      const filteredSuggestions = allInterests.filter(
        (interest) =>
          interest.name.toLowerCase().includes(value.toLowerCase()) &&
          !selectedInterests.some(
            (selected) =>
              selected.name.toLowerCase() === interest.name.toLowerCase()
          )
      );
      setSuggestions(filteredSuggestions);
    } else {
      setSuggestions([]);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "ArrowDown") {
      setSelectedSuggestionIndex((prevIndex) =>
        Math.min(prevIndex + 1, suggestions.length - 1)
      );
    } else if (e.key === "ArrowUp") {
      setSelectedSuggestionIndex((prevIndex) => Math.max(prevIndex - 1, 0));
    } else if (e.key === "Enter") {
      e.preventDefault();
      if (
        selectedSuggestionIndex >= 0 &&
        selectedSuggestionIndex < suggestions.length
      ) {
        handleAddInterest(suggestions[selectedSuggestionIndex]);
      } else {
        handleAddNewInterest();
      }
    }
  };

  const formatInterest = (interest) => {
    return interest
      .toLowerCase()
      .split(" ")
      .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
      .join(" ");
  };

  const handleAddInterest = (interest) => {
    if (
      !selectedInterests.some(
        (selected) =>
          selected.name.toLowerCase() === interest.name.toLowerCase()
      )
    ) {
      setSelectedInterests([...selectedInterests, interest]);
      setInput("");
      setSuggestions([]);
    }
  };

  const handleAddNewInterest = () => {
    if (
      input &&
      !selectedInterests.some(
        (interest) => interest.name.toLowerCase() === input.toLowerCase()
      )
    ) {
      const formattedInterest = {
        id: Math.random(),
        name: formatInterest(input),
      };
      setSelectedInterests([...selectedInterests, formattedInterest]);
      setInput("");
      setSuggestions([]);
    }
  };

  const handleRemoveInterest = (interestToRemove) => {
    setSelectedInterests(
      selectedInterests.filter((interest) => interest !== interestToRemove)
    );
  };

  return (
    <Container>
      <Form.Group controlId="formInterests">
        <Form.Label>Interests:</Form.Label>
        <InputGroup className="mb-3">
          <Form.Control
            type="text"
            placeholder="Add an interest"
            value={input}
            onChange={handleInputChange}
            onKeyDown={handleKeyDown}
            className="custom-focus"
          />
          <Button
            variant="primary"
            onClick={handleAddNewInterest}
            style={{
              backgroundColor: "#f39c12",
              borderColor: "#f39c12",
            }}
          >
            Add
          </Button>
        </InputGroup>
        {suggestions.length > 0 && (
          <ListGroup className={`suggestions-list ${suggestions.length > 0 ? 'show' : ''}`}>
            {suggestions.map((suggestion, index) => (
              <ListGroup.Item
                key={suggestion.id}
                action
                onClick={() => handleAddInterest(suggestion)}
                active={index === selectedSuggestionIndex}
              >
                {suggestion.name}
              </ListGroup.Item>
            ))}
          </ListGroup>
        )}
      </Form.Group>
      <div className="fixed-container">
        <Row>
          {selectedInterests.length === 0 ? (
            <Col>No interests added yet.</Col>
          ) : (
            selectedInterests.map((interest) => (
              <Col key={interest.id} md="auto">
                <InterestCard
                  interest={interest}
                  onRemove={handleRemoveInterest}
                />
              </Col>
            ))
          )}
        </Row>
      </div>
    </Container>
  );
};

export default InterestSelector;
