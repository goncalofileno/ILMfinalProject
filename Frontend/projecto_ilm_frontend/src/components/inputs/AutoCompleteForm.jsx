import "./AutoCompleteForm.css";

import React, { useState } from "react";
import { FormControl, ListGroup, InputGroup, Container } from "react-bootstrap";

const Autocomplete = ({
  label,
  suggestions,
  value,
  setValue,
  handleOnBlurFunctionExists,
  handleOnBlurFunction,
}) => {
  const [filteredSuggestions, setFilteredSuggestions] = useState([]);
  const [showSuggestions, setShowSuggestions] = useState(false);
  const [activeSuggestionIndex, setActiveSuggestionIndex] = useState(-1);

  const handleInputChange = (e) => {
    setValue(e.target.value);

    if (e.target.value.length > 0) {
      const filtered = suggestions.filter((suggestion) =>
        suggestion.toLowerCase().includes(e.target.value.toLowerCase())
      );
      setFilteredSuggestions(filtered);
      setShowSuggestions(true);
    } else {
      setFilteredSuggestions([]);
      setShowSuggestions(false);
    }
  };

  const handleSuggestionClick = (suggestion) => {
    setValue(suggestion);
    setFilteredSuggestions([]);
    setShowSuggestions(false);
  };

  const handleOnBlur = () => {
    setShowSuggestions(false);
    if (handleOnBlurFunctionExists) handleOnBlurFunction();
  };

  const handleKeyDown = (e) => {
    if (e.key === "ArrowDown") {
      if (activeSuggestionIndex < filteredSuggestions.length - 1) {
        setActiveSuggestionIndex(activeSuggestionIndex + 1);
      }
    } else if (e.key === "ArrowUp") {
      if (activeSuggestionIndex > 0) {
        setActiveSuggestionIndex(activeSuggestionIndex - 1);
      }
    } else if (e.key === "Enter") {
      e.preventDefault();
      if (
        activeSuggestionIndex >= 0 &&
        activeSuggestionIndex < filteredSuggestions.length
      ) {
        const suggestion = filteredSuggestions[activeSuggestionIndex];

        setValue(suggestion);
        setFilteredSuggestions([]);
        setShowSuggestions(false);
        setActiveSuggestionIndex(-1);
      }
    }
  };

  return (
    <div className="div-autocomplete">
      <Container id="autocomplete-container">
        <label htmlFor="autocomplete-input" className="autocomplete-label">
          {label}
        </label>
        <InputGroup>
          <FormControl
            value={value}
            onChange={handleInputChange}
            className="ilm-input autocomplete-input"
            id="autocomplete-input"
            onBlur={handleOnBlur}
            onKeyDown={handleKeyDown}
          />
        </InputGroup>
        {showSuggestions && filteredSuggestions.length > 0 && (
          <ListGroup id="suggestions-list">
            {filteredSuggestions.map((suggestion, index) => (
              <ListGroup.Item
                key={index}
                onMouseDown={() => handleSuggestionClick(suggestion)}
                active={index === activeSuggestionIndex}
              >
                {suggestion}
              </ListGroup.Item>
            ))}
          </ListGroup>
        )}
      </Container>
    </div>
  );
};

export default Autocomplete;
