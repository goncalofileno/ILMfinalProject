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
  Modal,
} from "react-bootstrap";
import { getSkills } from "../../utilities/services";
import { useParams } from "react-router-dom";
import SkillCard from "./SkillCard"; // Importação do novo componente SkillCard
import "./SkillSelector.css"; // Certifique-se de que o caminho está correto

const SkillSelector = ({ selectedSkills, setSelectedSkills }) => {
  const [allSkills, setAllSkills] = useState([]);
  const [suggestions, setSuggestions] = useState([]);
  const [input, setInput] = useState("");
  const [selectedSuggestionIndex, setSelectedSuggestionIndex] = useState(-1);
  const [showModal, setShowModal] = useState(false);
  const [newSkillType, setNewSkillType] = useState("");
  const [newSkillName, setNewSkillName] = useState("");
  const { token } = useParams();

  useEffect(() => {
    getSkills(token)
      .then((response) => {
        return response.json();
      })
      .then((data) => {
        setAllSkills(data);
        console.log("Fetched skills:", data);
      });
  }, [token]);

  const handleInputChange = (e) => {
    const value = e.target.value;
    setInput(value);
    setSelectedSuggestionIndex(-1);
    if (value) {
      const filteredSuggestions = allSkills.filter(
        (skill) =>
          skill.name.toLowerCase().includes(value.toLowerCase()) &&
          !selectedSkills.some(
            (selected) =>
              selected.name.toLowerCase() === skill.name.toLowerCase()
          )
      );
      setSuggestions(filteredSuggestions);
      console.log("Filtered suggestions:", filteredSuggestions);
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
        handleAddSkill(suggestions[selectedSuggestionIndex]);
      } else {
        handleAddNewSkill();
      }
    }
  };

  const formatSkill = (skill) => {
    return skill
      .toLowerCase()
      .split(" ")
      .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
      .join(" ");
  };

  const handleAddSkill = (skill) => {
    if (
      !selectedSkills.some(
        (selected) => selected.name.toLowerCase() === skill.name.toLowerCase()
      )
    ) {
      setSelectedSkills([...selectedSkills, skill]);
      setInput("");
      setSuggestions([]);
    }
  };

  const handleAddNewSkill = () => {
    if (
      input &&
      !selectedSkills.some(
        (skill) => skill.name.toLowerCase() === input.toLowerCase()
      )
    ) {
      setNewSkillName(formatSkill(input));
      setShowModal(true);
    }
  };

  const handleSaveNewSkill = () => {
    if (newSkillType && newSkillName) {
      const newSkill = {
        id: Math.random(),
        name: newSkillName,
        type: newSkillType,
      }; // Format the input skill
      setSelectedSkills([...selectedSkills, newSkill]);
      setInput("");
      setSuggestions([]);
      setShowModal(false);
      setNewSkillType("");
      setNewSkillName("");
    }
  };

  const handleRemoveSkill = (skillToRemove) => {
    setSelectedSkills(
      selectedSkills.filter((skill) => skill !== skillToRemove)
    );
  };

  return (
    <Container>
      <Form.Group controlId="formSkills">
        <Form.Label>Skills:</Form.Label>
        <InputGroup className="mb-3">
          <Form.Control
            type="text"
            placeholder="Add a skill"
            value={input}
            onChange={handleInputChange}
            onKeyDown={handleKeyDown}
            className="custom-focus"
          />
          <Button
            variant="primary"
            onClick={handleAddNewSkill}
            style={{
              backgroundColor: "rgb(192, 23, 34)",
              borderColor: "rgb(192, 23, 34)",
            }}
          >
            Add
          </Button>
        </InputGroup>
        <ListGroup className="suggestions-list">
          {suggestions.map((suggestion, index) => (
            <ListGroup.Item
              key={suggestion.id}
              action
              onClick={() => handleAddSkill(suggestion)}
              active={index === selectedSuggestionIndex}
            >
              {suggestion.name}
            </ListGroup.Item>
          ))}
        </ListGroup>
      </Form.Group>
      <div className="fixed-container">
        <Row>
          {selectedSkills.length === 0 ? (
            <Col>No skills added yet.</Col>
          ) : (
            selectedSkills.map((skill) => (
              <Col key={skill.id} md="auto">
                <SkillCard skill={skill} onRemove={handleRemoveSkill} />
              </Col>
            ))
          )}
        </Row>
      </div>
      <Modal show={showModal} onHide={() => setShowModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>Select Skill Type for {newSkillName}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group>
            <Form.Label>Skill Type</Form.Label>
            <Form.Control
              as="select"
              value={newSkillType}
              onChange={(e) => setNewSkillType(e.target.value)}
              className="custom-focus"
            >
              <option value="">Select a type</option>
              <option value="KNOWLEDGE">Knowledge</option>
              <option value="SOFTWARE">Software</option>
              <option value="HARDWARE">Hardware</option>
              <option value="TOOLS">Tools</option>
            </Form.Control>
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Cancel
          </Button>
          <Button variant="primary" onClick={handleSaveNewSkill}>
            Save
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
};

export default SkillSelector;
