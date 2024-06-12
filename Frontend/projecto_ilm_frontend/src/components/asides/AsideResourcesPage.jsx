import "./Asides.css";
import "../../pages/CreateProfilePage.css";
import { Form } from "react-bootstrap";
import {
  getAllResourcesTypes,
  getAllSuppliersNames,
  getAllBrands,
} from "../../utilities/services";
import { useEffect, useState } from "react";
import { formatResourceType } from "../../utilities/converters";

export default function AsideResourcesPage() {
  const [types, setTypes] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [brands, setBrands] = useState([]);
  useEffect(() => {
    getAllResourcesTypes()
      .then((response) => response.json())
      .then((data) => {
        setTypes(data);
      });
    getAllSuppliersNames()
      .then((response) => response.json())
      .then((data) => {
        setSuppliers(data);
      });
    getAllBrands()
      .then((response) => response.json())
      .then((data) => {
        setBrands(data);
      });
  }, []);

  return (
    <div className="aside-background">
      <div className="aside">
        <div className="div-control-form" id="first-div">
          <Form.Label className="custom-label" style={{ color: "white" }}>
            Brand
          </Form.Label>
          <Form.Control as="select" className="custom-focus">
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
            Type
          </Form.Label>
          <Form.Control as="select" className="custom-focus">
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
            Supplier
          </Form.Label>
          <Form.Control as="select" className="custom-focus">
            {" "}
            <option value="">All Suppliers</option>
            {suppliers.map((supplier, index) => (
              <option key={index} value={supplier}>
                {supplier}
              </option>
            ))}
          </Form.Control>
        </div>
      </div>
    </div>
  );
}
