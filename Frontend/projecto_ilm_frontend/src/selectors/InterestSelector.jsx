import React, { useState, useEffect } from "react";
import { Form, Row, Col, Button, Card, ListGroup, InputGroup, Container, Alert } from "react-bootstrap";
import { getInterests } from "../utilities/services";
import { useNavigate, useParams } from "react-router-dom";
//import useAxios from "../axios/axiosConfig"; // Certifique-se de que o caminho está correto

const InterestSelector = ({ selectedInterests, setSelectedInterests }) => {
   const [allInterests, setAllInterests] = useState([]);
   const [suggestions, setSuggestions] = useState([]);
   const [input, setInput] = useState("");
   const [selectedSuggestionIndex, setSelectedSuggestionIndex] = useState(-1); // Adicionando estado para índice da sugestão selecionada
   const { token } = useParams();
   //const axios = useAxios(); // Obtenha a instância de axios chamando a função useAxios

   useEffect(() => {
      getInterests(token)
         .then((response) => {
            return response.json();
         })
         .then((data) => {
            setAllInterests(data);
            console.log("Fetched interests:", data);
         });
   }, []);

   const handleInputChange = (e) => {
      const value = e.target.value;
      setInput(value);
      setSelectedSuggestionIndex(-1); // Resetar índice da sugestão selecionada ao digitar
      if (value) {
         const filteredSuggestions = allInterests.filter(
            (interest) =>
               interest.name.toLowerCase().includes(value.toLowerCase()) &&
               !selectedInterests.some((selected) => selected.name.toLowerCase() === interest.name.toLowerCase())
         );
         setSuggestions(filteredSuggestions);
         console.log("Filtered suggestions:", filteredSuggestions);
      } else {
         setSuggestions([]);
      }
   };

   const handleKeyDown = (e) => {
      if (e.key === "ArrowDown") {
         // Navegar para baixo na lista de sugestões
         setSelectedSuggestionIndex((prevIndex) => Math.min(prevIndex + 1, suggestions.length - 1));
      } else if (e.key === "ArrowUp") {
         // Navegar para cima na lista de sugestões
         setSelectedSuggestionIndex((prevIndex) => Math.max(prevIndex - 1, 0));
      } else if (e.key === "Enter") {
         // Prevenir o envio do formulário
         e.preventDefault();
         // Adicionar sugestão selecionada ao pressionar "Enter"
         if (selectedSuggestionIndex >= 0 && selectedSuggestionIndex < suggestions.length) {
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
      if (!selectedInterests.some((selected) => selected.name.toLowerCase() === interest.name.toLowerCase())) {
         setSelectedInterests([...selectedInterests, interest]);
         setInput("");
         setSuggestions([]);
      }
   };

   const handleAddNewInterest = () => {
      if (input && !selectedInterests.some((interest) => interest.name.toLowerCase() === input.toLowerCase())) {
         const formattedInterest = { id: Math.random(), name: formatInterest(input) }; // Format the input interest
         setSelectedInterests([...selectedInterests, formattedInterest]);
         setInput("");
         setSuggestions([]);
      }
   };

   const handleRemoveInterest = (interestToRemove) => {
      setSelectedInterests(selectedInterests.filter((interest) => interest !== interestToRemove));
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
                  onKeyDown={handleKeyDown} // Adicionando onKeyDown para capturar eventos de teclado
               />
               <Button variant="primary" onClick={handleAddNewInterest}>
                  Add
               </Button>
            </InputGroup>
            <ListGroup>
               {suggestions.map((suggestion, index) => (
                  <ListGroup.Item
                     key={suggestion.id}
                     action
                     onClick={() => handleAddInterest(suggestion)}
                     active={index === selectedSuggestionIndex} // Destacar a sugestão selecionada
                  >
                     {suggestion.name}
                  </ListGroup.Item>
               ))}
            </ListGroup>
         </Form.Group>
         <Row>
            {selectedInterests.length === 0 ? (
               <Col>
                  <Alert variant="info" className="text-center">
                     No interests added yet.
                  </Alert>
               </Col>
            ) : (
               selectedInterests.map((interest) => (
                  <Col key={interest.id} md="auto">
                     <Card className="mt-2">
                        <Card.Body>
                           <Card.Title>{interest.name}</Card.Title>
                           <Button variant="danger" size="sm" onClick={() => handleRemoveInterest(interest)}>
                              X
                           </Button>
                        </Card.Body>
                     </Card>
                  </Col>
               ))
            )}
         </Row>
      </Container>
   );
};

export default InterestSelector;
