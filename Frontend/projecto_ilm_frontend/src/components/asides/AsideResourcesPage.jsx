import "./Asides.css";
import "../../pages/CreateProfilePage.css";
import { Form } from "react-bootstrap";
import { getResourcesFilters } from "../../utilities/services";
import { useEffect, useState } from "react";
import { formatResourceType } from "../../utilities/converters";

export default function AsideResourcesPage({
  brand,
  setBrand,
  type,
  setType,
  supplier,
  setSupplier,
  setCurrentPage,
  setNavigateTableResourcesTrigger,
  setKeyword,
  setNameAsc,
  setTypeAsc,
  setBrandAsc,
  setSupplierAsc,
}) {
  const [types, setTypes] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [brands, setBrands] = useState([]);
  useEffect(() => {
    getResourcesFilters(false)
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        setTypes(data.types);
        setSuppliers(data.suppliers);
        setBrands(data.brands);
      });
  }, []);

  const handleBrandChange = (e) => {
    setBrand(e.target.value);
    setCurrentPage(1);
    setNavigateTableResourcesTrigger((prev) => !prev);
  };

  const handleTypeChange = (e) => {
    setType(e.target.value);
    setCurrentPage(1);
    setNavigateTableResourcesTrigger((prev) => !prev);
  };

  const handleSupplierChange = (e) => {
    setSupplier(e.target.value);
    setCurrentPage(1);
    setNavigateTableResourcesTrigger((prev) => !prev);
  };

  return (
    <div className="aside-background">
      <div className="aside">
        <div className="div-control-form" id="first-div">
          <Form.Label className="custom-label" style={{ color: "white" }}>
            Type
          </Form.Label>
          <Form.Control
            as="select"
            className="custom-focus"
            value={type}
            onChange={handleTypeChange}
          >
            {" "}
            <option value="">All Types</option>
            {types.map((type, index) => (
              <option key={index} value={type}>
                {formatResourceType(type)}
              </option>
            ))}
          </Form.Control>
        </div>
        <div className="div-control-form">
          <Form.Label className="custom-label" style={{ color: "white" }}>
            Brand
          </Form.Label>
          <Form.Control
            as="select"
            className="custom-focus"
            value={brand}
            onChange={handleBrandChange}
          >
            <option value="">All Brands</option>
            {brands.map((brand, index) => (
              <option key={index} value={brand}>
                {brand}
              </option>
            ))}
          </Form.Control>
        </div>
        <div className="div-control-form">
          <Form.Label className="custom-label" style={{ color: "white" }}>
            Supplier
          </Form.Label>
          <Form.Control
            as="select"
            className="custom-focus"
            value={supplier}
            onChange={handleSupplierChange}
          >
            {" "}
            <option value="">All Suppliers</option>
            {suppliers.map((supplier, index) => (
              <option key={index} value={supplier}>
                {supplier}
              </option>
            ))}
          </Form.Control>
        </div>
        <button
          className="terciary-button"
          style={{
            width: "100%",
            paddingTop: "5px",
            paddingBottom: "5px",
            fontWeight: "500",
          }}
          onClick={(e) => {
            setBrand("");
            setType("");
            setSupplier("");
            setCurrentPage(1);
            setKeyword("");
            setNameAsc("");
            setTypeAsc("");
            setBrandAsc("");
            setSupplierAsc("");
            setNavigateTableResourcesTrigger((prev) => !prev);
          }}
        >
          Clear Filters
        </button>
      </div>
    </div>
  );
}
