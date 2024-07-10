import "./Asides.css";
import "../../pages/CreateProfilePage.css";
import { Form } from "react-bootstrap";
import { getResourcesFilters } from "../../utilities/services";
import { useEffect, useState } from "react";
import { formatResourceType } from "../../utilities/converters";
import { Trans } from "@lingui/macro";
import { useMediaQuery } from "react-responsive";

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
  isVisible,
}) {
  const isMobile = useMediaQuery({ query: "(max-width: 768px)" });
  const [types, setTypes] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [brands, setBrands] = useState([]);
  useEffect(() => {
    getResourcesFilters(false, true)
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
    <>
      {((isVisible && isMobile) || !isMobile) && (
        <div className={!isMobile && "aside-background"}>
          <div className={!isMobile ? "aside" : "aside-Mobile"}>
            <div className="div-control-form" id="first-div">
              <Form.Label className="custom-label" style={{ color: "white" }}>
                <Trans>Type</Trans>
              </Form.Label>
              <Form.Select
                className="custom-focus"
                value={type}
                onChange={handleTypeChange}
              >
                {" "}
                <option value="">
                  <Trans>All Types</Trans>
                </option>
                {types.map((type, index) => (
                  <option key={index} value={type}>
                    {formatResourceType(type)}
                  </option>
                ))}
              </Form.Select>
            </div>
            <div className="div-control-form">
              <Form.Label className="custom-label" style={{ color: "white" }}>
                <Trans>Brand</Trans>
              </Form.Label>
              <Form.Select
                className="custom-focus"
                value={brand}
                onChange={handleBrandChange}
              >
                <option value="">
                  <Trans>All Brands</Trans>
                </option>
                {brands.map((brand, index) => (
                  <option key={index} value={brand}>
                    {brand}
                  </option>
                ))}
              </Form.Select>
            </div>
            <div className="div-control-form">
              <Form.Label className="custom-label" style={{ color: "white" }}>
                <Trans>Supplier</Trans>
              </Form.Label>
              <Form.Select
                className="custom-focus"
                value={supplier}
                onChange={handleSupplierChange}
              >
                {" "}
                <option value="">
                  <Trans>All Suppliers</Trans>
                </option>
                {suppliers.map((supplier, index) => (
                  <option key={index} value={supplier}>
                    {supplier}
                  </option>
                ))}
              </Form.Select>
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
              <Trans>Clear Filters</Trans>
            </button>
          </div>
        </div>
      )}
    </>
  );
}
