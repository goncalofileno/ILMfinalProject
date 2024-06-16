import "./AutoCompleteForm.css";

import React, { useState } from "react";
import { FormControl, ListGroup, InputGroup, Container } from "react-bootstrap";

const Autocomplete = ({ label, suggestions, value, setValue }) => {
  const [query, setQuery] = useState("");
  const [filteredSuggestions, setFilteredSuggestions] = useState([]);
  const [showSuggestions, setShowSuggestions] = useState(false);

  const handleInputChange = (e) => {
    setValue(e.target.value);
    setQuery(e.target.value);

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
    setQuery(suggestion);
    setFilteredSuggestions([]);
    setShowSuggestions(false);
  };

  return (
    <div className="div-autocomplete">
      <Container id="autocomplete-container">
        <label htmlFor="autocomplete-input" className="autocomplete-label">
          {label}
        </label>
        <InputGroup>
          <FormControl
            value={query}
            onChange={handleInputChange}
            className="ilm-input autocomplete-input"
            id="autocomplete-input"
          />
        </InputGroup>
        {showSuggestions && filteredSuggestions.length > 0 && (
          <ListGroup id="suggestions-list">
            {filteredSuggestions.map((suggestion, index) => (
              <ListGroup.Item
                key={index}
                action
                onClick={() => handleSuggestionClick(suggestion)}
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
