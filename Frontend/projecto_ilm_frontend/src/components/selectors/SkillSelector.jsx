import React, { useState, useEffect } from "react";
import {
  Form,
  Row,
  Col,
  Button,
  ListGroup,
  InputGroup,
  Container,
  Modal,
} from "react-bootstrap";
import { getSkills } from "../../utilities/services";
import { useParams } from "react-router-dom";
import SkillCard from "./SkillCard";
import "./SkillSelector.css";
import { formatSkill } from "../../utilities/converters";
import { Trans, t } from "@lingui/macro";

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
        console.log(data);
        setAllSkills(data);
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
      };
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
        <Form.Label style={{ fontWeight: "500" }}>
          <Trans>Skills</Trans>:
        </Form.Label>
        <InputGroup className="mb-3">
          <Form.Control
            type="text"
            placeholder={t`Add a skill`}
            value={input}
            onChange={handleInputChange}
            onKeyDown={handleKeyDown}
            className="custom-focus"
          />
          <Button
            variant="primary"
            onClick={handleAddNewSkill}
            style={{
              backgroundColor: "#f39c12",
              borderColor: "#f39c12",
            }}
          >
            <Trans>Add</Trans>
          </Button>
        </InputGroup>
        {suggestions.length > 0 && (
          <ListGroup
            className={`suggestions-list ${
              suggestions.length > 0 ? "show" : ""
            }`}
            style={{ width: "25%" }}
          >
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
        )}
      </Form.Group>
      <div className="fixed-container">
        <Row>
          {selectedSkills.length === 0 ? (
            <Col>
              <Trans>No skills added yet.</Trans>
            </Col>
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
          <Modal.Title>
            <Trans>Select Skill Type for</Trans> {newSkillName}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group>
            <Form.Label>
              <Trans>Skill Type</Trans>
            </Form.Label>
            <Form.Select
              value={newSkillType}
              onChange={(e) => setNewSkillType(e.target.value)}
              className="custom-focus"
            >
              <option value="">
                <Trans>Select a type</Trans>
              </option>
              <option value="KNOWLEDGE">
                <Trans>Knowledge</Trans>
              </option>
              <option value="SOFTWARE">
                <Trans>Software</Trans>
              </option>
              <option value="HARDWARE">
                <Trans>Hardware</Trans>
              </option>
              <option value="TOOLS">
                <Trans>Tools</Trans>
              </option>
            </Form.Select>
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            <Trans>Cancel</Trans>
          </Button>
          <Button variant="primary" style={{backgroundColor:"#f39c12", borderBlockColor:"#f39c12"}} onClick={handleSaveNewSkill}>
            <Trans>Save</Trans>
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
};

export default SkillSelector;
