import React, { useState, useEffect } from "react";
import { Form, Row, Col, Button, Card, ListGroup, InputGroup, Container, Alert, Modal } from "react-bootstrap";
import useAxios from "../axios/axiosConfig"; // Certifique-se de que o caminho está correto

const SkillSelector = ({ selectedSkills, setSelectedSkills }) => {
  const [allSkills, setAllSkills] = useState([]);
  const [suggestions, setSuggestions] = useState([]);
  const [input, setInput] = useState("");
  const [selectedSuggestionIndex, setSelectedSuggestionIndex] = useState(-1);
  const [showModal, setShowModal] = useState(false);
  const [newSkillType, setNewSkillType] = useState("");
  const [newSkillName, setNewSkillName] = useState("");
  const axios = useAxios();

  useEffect(() => {
    const fetchSkills = async () => {
      try {
        const response = await axios.get("/skill/all");
        setAllSkills(response.data);
        console.log("Fetched skills:", response.data);
      } catch (error) {
        console.error("Error fetching skills", error);
      }
    };

    fetchSkills();
  }, []);

  const handleInputChange = (e) => {
    const value = e.target.value;
    setInput(value);
    setSelectedSuggestionIndex(-1);
    if (value) {
      const filteredSuggestions = allSkills.filter(
        (skill) =>
          skill.name.toLowerCase().includes(value.toLowerCase()) &&
          !selectedSkills.some((selected) => selected.name.toLowerCase() === skill.name.toLowerCase())
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
      setSelectedSuggestionIndex((prevIndex) => 
        Math.max(prevIndex - 1, 0)
      );
    } else if (e.key === "Enter") {
      e.preventDefault();
      if (selectedSuggestionIndex >= 0 && selectedSuggestionIndex < suggestions.length) {
        handleAddSkill(suggestions[selectedSuggestionIndex]);
      } else {
        handleAddNewSkill();
      }
    }
  };

  const formatSkill = (skill) => {
    return skill
      .toLowerCase()
      .split(' ')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
  };

  const handleAddSkill = (skill) => {
    if (!selectedSkills.some((selected) => selected.name.toLowerCase() === skill.name.toLowerCase())) {
      setSelectedSkills([...selectedSkills, skill]);
      setInput("");
      setSuggestions([]);
    }
  };

  const handleAddNewSkill = () => {
    if (input && !selectedSkills.some((skill) => skill.name.toLowerCase() === input.toLowerCase())) {
      setNewSkillName(formatSkill(input));
      setShowModal(true);
    }
  };

  const handleSaveNewSkill = () => {
    if (newSkillType && newSkillName) {
      const newSkill = { id: Math.random(), name: newSkillName, type: newSkillType }; // Format the input skill
      setSelectedSkills([...selectedSkills, newSkill]);
      setInput("");
      setSuggestions([]);
      setShowModal(false);
      setNewSkillType("");
      setNewSkillName("");
    }
  };

  const handleRemoveSkill = (skillToRemove) => {
    setSelectedSkills(selectedSkills.filter((skill) => skill !== skillToRemove));
  };

  return (
    <Container>
      <Form.Group controlId="formSkills">
        <Form.Label>Skills</Form.Label>
        <InputGroup className="mb-3">
          <Form.Control
            type="text"
            placeholder="Add a skill"
            value={input}
            onChange={handleInputChange}
            onKeyDown={handleKeyDown}
          />
          <Button variant="primary" onClick={handleAddNewSkill}>
            Add
          </Button>
        </InputGroup>
        <ListGroup>
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
      <Row>
        {selectedSkills.length === 0 ? (
          <Col>
            <Alert variant="info" className="text-center">
              No skills added yet.
            </Alert>
          </Col>
        ) : (
          selectedSkills.map((skill) => (
            <Col key={skill.id} md="auto">
              <Card className="mt-2">
                <Card.Body>
                  <Card.Title>{skill.name}</Card.Title>
                  <Card.Subtitle className="mb-2 text-muted">{skill.type}</Card.Subtitle>
                  <Button
                    variant="danger"
                    size="sm"
                    onClick={() => handleRemoveSkill(skill)}
                  >
                    X
                  </Button>
                </Card.Body>
              </Card>
            </Col>
          ))
        )}
      </Row>
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Select Skill Type</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group>
            <Form.Label>Skill Type</Form.Label>
            <Form.Control as="select" value={newSkillType} onChange={(e) => setNewSkillType(e.target.value)}>
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
