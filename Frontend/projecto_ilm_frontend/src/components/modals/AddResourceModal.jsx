import "./Modals.css";
import InputForm from "../inputs/InputForm";
import { Form } from "react-bootstrap";
import { useState } from "react";
import AutoCompleteForm from "../inputs/AutoCompleteForm";

export default function AddResourceModal({ isModalActive, setIsModalActive }) {
  const [name, setName] = useState("");
  const [type, setType] = useState("");
  const [brand, setBrand] = useState("");
  const [serialNumber, setSerialNumber] = useState("");
  const [supplier, setSupplier] = useState("");
  const [supplierContact, setSupplierContact] = useState("");
  const [description, setDescription] = useState("");
  const [observations, setObservations] = useState("");
  const elements = ["Apple", "Banana", "Cherry", "Date", "Grape", "Orange"];

  return (
    <>
      {isModalActive && (
        <div>
          <div
            className="modal-background"
            onClick={() => setIsModalActive(false)}
          ></div>
          <form className="ilm-modal" style={{ width: "40%" }}>
            <div className="modal-content">
              <h3 className="modal-title">Resource Creation</h3>
              <AutoCompleteForm
                label="label"
                suggestions={elements}
                value={name}
                setValue={setName}
              ></AutoCompleteForm>
              <div className="modal-body" style={{ gap: "15px" }}>
                <div className="modal-row">
                  <div style={{ width: "45%" }}>
                    <InputForm
                      label="Name"
                      type="text"
                      value={name}
                      setValue={setName}
                    />
                  </div>

                  <div style={{ width: "45%" }}>
                    <InputForm
                      label="Type"
                      type="text"
                      value={type}
                      setValue={setType}
                    />
                  </div>
                </div>
                <div className="modal-row">
                  <div style={{ width: "45%" }}>
                    <InputForm
                      label="Brand"
                      type="text"
                      value={brand}
                      setValue={setBrand}
                    />
                  </div>
                  <div style={{ width: "45%" }}>
                    <InputForm
                      label="Serial Number"
                      type="text"
                      value={serialNumber}
                      setValue={setSerialNumber}
                    />
                  </div>
                </div>
                <div className="modal-row">
                  {" "}
                  <div style={{ width: "45%" }}>
                    <InputForm
                      label="Supplier"
                      type="text"
                      value={supplier}
                      setValue={setSupplier}
                    />
                  </div>
                  <div style={{ width: "45%" }}>
                    <InputForm
                      label="Supplier Contact"
                      type="text"
                      value={supplierContact}
                      setValue={setSupplierContact}
                    />
                  </div>
                </div>
                <Form.Group controlId="formDescription">
                  <Form.Label className="custom-label">Description</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    style={{ resize: "none", cursor: "text" }}
                    className="custom-focus"
                    value={description}
                    setValue={setDescription}
                  />
                </Form.Group>
                <Form.Group controlId="formObservations">
                  <Form.Label className="custom-label">Observations</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    style={{ resize: "none", cursor: "text" }}
                    className="custom-focus"
                    value={observations}
                    setValue={setObservations}
                  />
                </Form.Group>
                <div className="modal-row">
                  <button className="secondary-button" id="cancel-resource-btn">
                    Cancel
                  </button>
                  <button className="submit-button" id="create-resource-btn">
                    Create
                  </button>
                </div>
              </div>
            </div>
          </form>
        </div>
      )}
    </>
  );
}
