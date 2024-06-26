import "./Modals.css";
import InputForm from "../inputs/InputForm";
import { Form } from "react-bootstrap";
import { useEffect, useState } from "react";
import AutoCompleteForm from "../inputs/AutoCompleteForm";
import {
  getResourcesFilters,
  createResource,
  findSupplierContact,
  getResource,
  editResource,
} from "../../utilities/services";
import { formatResourceType } from "../../utilities/converters";

export default function AddResourceModal({
  isModalActive,
  setIsModalActive,
  resourceId,
  resourceSupplier,
  setResourceSupplier,
  setResourceId,
}) {
  const [name, setName] = useState("");
  const [type, setType] = useState("");
  const [brand, setBrand] = useState("");
  const [serialNumber, setSerialNumber] = useState("");
  const [supplier, setSupplier] = useState("");
  const [supplierContact, setSupplierContact] = useState("");
  const [description, setDescription] = useState("");
  const [observations, setObservations] = useState("");
  const [namesSuggestions, setNamesSuggestions] = useState([]);
  const [typesSuggestions, setTypesSuggestions] = useState([]);
  const [brandsSuggestions, setBrandsSuggestions] = useState([]);
  const [suppliersSuggestions, setSuppliersSuggestions] = useState([]);
  const [modalType, setModalType] = useState("success");
  const [modalErrorText, setModalErrorText] = useState("");
  const [modalErrorVisible, setModalErrorVisible] = useState(false);
  const [supplierContactDisabled, setSupplierContactDisabled] = useState(true);

  useEffect(() => {
    getResourcesFilters(true)
      .then((response) => response.json())
      .then((data) => {
        setNamesSuggestions(data.names);
        setTypesSuggestions(data.types);
        setBrandsSuggestions(data.brands);
        setSuppliersSuggestions(data.suppliers);
      });
  }, []);

  useEffect(() => {
    console.log(resourceSupplier);
    console.log("id " + resourceId);
    if (resourceId != null) {
      getResource(resourceId, resourceSupplier)
        .then((response) => response.json())
        .then((data) => {
          setName(data.name);
          setType(data.type);
          setBrand(data.brand);
          setSupplier(data.supplierName);
          setSupplierContact(data.supplierContact);
          setSerialNumber(data.serialNumber);
          setDescription(data.description);
          setObservations(data.observation);
        });
    }
  }, [isModalActive]);

  useEffect(() => {
    if (modalErrorVisible) {
      setTimeout(() => {
        setModalErrorVisible(false);
        setModalErrorText("");
      }, 3000);
    }
  }, [modalErrorVisible]);

  const handleCancel = () => {
    setIsModalActive(false);
    setBrand("");
    setDescription("");
    setName("");
    setObservations("");
    setSerialNumber("");
    setSupplier("");
    setSupplierContact("");
    setType("");
    setResourceId(null);
    setResourceSupplier(null);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (
      name !== "" &&
      type !== "" &&
      brand !== "" &&
      serialNumber !== "" &&
      supplier !== "" &&
      description !== "" &&
      observations !== "" &&
      supplierContact !== ""
    ) {
      if (resourceSupplier === null) {
        console.log("nome " + name);
        createResource(
          name,
          description,
          type,
          brand,
          serialNumber,
          supplier,
          supplierContact,
          observations
        ).then((response) => {
          if (response.ok) {
            setModalType("success");
            setModalErrorText("Resource created successfully.");
            setModalErrorVisible(true);
            setTimeout(() => {
              window.location.reload();
              setTimeout(() => {
                setIsModalActive(false);
              }, 500);
            }, 1000);
          } else if (response.status === 400) {
            setModalErrorText("This resource already exists.");
            setModalErrorVisible(true);
            setModalType("danger");
          }
        });
      } else {
        console.log("2");
        editResource(
          resourceId,
          name,
          description,
          type,
          brand,
          serialNumber,
          supplier,
          supplierContact,
          observations,
          resourceSupplier
        ).then((response) => {
          if (response.ok) {
            setModalType("success");
            setModalErrorText("Resource edited successfully.");
            setModalErrorVisible(true);
            setTimeout(() => {
              window.location.reload();
              setTimeout(() => {
                setIsModalActive(false);
              }, 500);
            }, 1000);
          } else if (response.status === 400) {
            setModalErrorText("This resource already exists.");
            setModalErrorVisible(true);
            setModalType("danger");
          }
        });
      }
    } else {
      setModalErrorText("Please fill in all required fields.");
      setModalErrorVisible(true);
      setModalType("danger");
    }
  };

  const handleSupplierBlur = () => {
    if (supplier === "") {
      setSupplierContactDisabled(true);
      setSupplierContact("");
    } else {
      findSupplierContact(supplier).then((response) => {
        if (response.status === 404) {
          setSupplierContact("");
          setSupplierContactDisabled(false);
        } else if (response.ok) {
          return response.json().then((data) => {
            setSupplierContact(data);
            setSupplierContactDisabled(true);
          });
        }
      });
    }
  };

  return (
    <>
      {isModalActive && (
        <div>
          <div className="modal-background" onClick={handleCancel}></div>
          <form
            className="ilm-modal"
            style={{ width: "40%" }}
            onSubmit={handleSubmit}
          >
            <div className="modal-content">
              <div
                style={{
                  display: "flex",
                  flexDirection: "row",
                  justifyContent: "space-between",
                  width: "100%",
                }}
              >
                <h3 className="modal-title">Resource Creation</h3>
                {modalErrorVisible && (
                  <div
                    id={
                      modalType === "success"
                        ? "success-resource-creation"
                        : "bad-resource-creation"
                    }
                  >
                    {modalErrorText}
                  </div>
                )}
              </div>

              <div className="modal-body" style={{ gap: "15px" }}>
                <div className="modal-row">
                  <div style={{ width: "45%" }}>
                    <AutoCompleteForm
                      label="Name"
                      suggestions={namesSuggestions}
                      value={name}
                      setValue={setName}
                    ></AutoCompleteForm>
                  </div>

                  <div style={{ width: "45%" }}>
                    <Form.Label className="custom-label">Type</Form.Label>
                    <Form.Control
                      as="select"
                      className="custom-focus"
                      value={type}
                      onChange={(e) => setType(e.target.value)}
                    >
                      {" "}
                      <option value="">Select Type</option>
                      {typesSuggestions.map((type, index) => (
                        <option key={index} value={type}>
                          {formatResourceType(type)}
                        </option>
                      ))}
                    </Form.Control>
                  </div>
                </div>
                <div className="modal-row">
                  <div style={{ width: "45%" }}>
                    <AutoCompleteForm
                      label="Brand"
                      suggestions={brandsSuggestions}
                      value={brand}
                      setValue={setBrand}
                    ></AutoCompleteForm>
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
                    <AutoCompleteForm
                      label="Supplier"
                      suggestions={suppliersSuggestions}
                      value={supplier}
                      setValue={setSupplier}
                      handleOnBlurFunctionExists={true}
                      handleOnBlurFunction={handleSupplierBlur}
                    ></AutoCompleteForm>
                  </div>
                  <div style={{ width: "45%" }}>
                    <InputForm
                      label="Supplier Contact"
                      type="text"
                      value={supplierContact}
                      setValue={setSupplierContact}
                      disabled={supplierContactDisabled}
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
                    onChange={(e) => setDescription(e.target.value)}
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
                    onChange={(e) => setObservations(e.target.value)}
                  />
                </Form.Group>
                <div className="modal-row">
                  <button
                    className="secondary-button"
                    id="cancel-resource-btn"
                    onClick={handleCancel}
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="submit-button"
                    id="create-resource-btn"
                  >
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
