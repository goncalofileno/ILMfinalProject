import AppNavbar from "../components/headers/AppNavbar";
import AsideResourcesPage from "../components/asides/AsideResourcesPage";
import ResourcesTable from "../components/tables/ResourcesTable";
import { useEffect, useState } from "react";
import { getAllResources } from "../utilities/services";
import { useLocation, useNavigate } from "react-router-dom";

export default function ResourcesPage() {
  const query = new URLSearchParams(useLocation().search);
  const navigate = useNavigate();
  const [resources, setResources] = useState([]);
  const [totalPages, setTotalPages] = useState(null);
  const [currentPage, setCurrentPage] = useState(
    parseInt(query.get("currentPage")) || 1
  );
  const [brand, setBrand] = useState(query.get("brand") || "");
  const [type, setType] = useState(query.get("type") || "");
  const [supplier, setSupplier] = useState(query.get("supplier") || "");
  const [keyword, setKeyword] = useState(query.get("search_query") || "");
  const [nameAsc, setNameAsc] = useState(query.get("nameAsc") || "");
  const [typeAsc, setTypeAsc] = useState(query.get("typeAsc") || "");
  const [brandAsc, setBrandAsc] = useState(query.get("brandAsc") || "");
  const [supplierAsc, setSupplierAsc] = useState(
    query.get("supplierAsc") || ""
  );
  const [navigateTableResourcesTrigger, setNavigateTableResourcesTrigger] =
    useState(false);

  const sortByName = () => {
    if (typeAsc !== "") setTypeAsc("");
    if (brandAsc !== "") setBrandAsc("");
    if (supplierAsc !== "") setSupplierAsc("");
    else setNameAsc(!nameAsc);
    setNavigateTableResourcesTrigger((prev) => !prev);
  };

  const sortByType = () => {
    if (nameAsc !== "") setNameAsc("");
    if (brandAsc !== "") setBrandAsc("");
    if (supplierAsc !== "") setSupplierAsc("");
    else setTypeAsc(!typeAsc);
    setNavigateTableResourcesTrigger((prev) => !prev);
  };

  const sortByBrand = () => {
    if (nameAsc !== "") setNameAsc("");
    if (typeAsc !== "") setTypeAsc("");
    if (supplierAsc !== "") setSupplierAsc("");
    else setBrandAsc(!brandAsc);
    setNavigateTableResourcesTrigger((prev) => !prev);
  };

  const sortBySupplier = () => {
    if (nameAsc !== "") setNameAsc("");
    if (typeAsc !== "") setTypeAsc("");
    if (brandAsc !== "") setBrandAsc("");
    else setSupplierAsc(!supplierAsc);
    setNavigateTableResourcesTrigger((prev) => !prev);
  };

  useEffect(() => {
    getAllResources(
      currentPage,
      brand,
      type,
      supplier,
      keyword,
      nameAsc,
      typeAsc,
      brandAsc,
      supplierAsc
    )
      .then((response) => response.json())
      .then((data) => {
        setResources(data.tableResources);
        setTotalPages(data.maxPageNumber);
      });

    const queryParamsObj = {};

    if (currentPage) queryParamsObj.currentPage = currentPage;
    if (brand !== "") queryParamsObj.brand = brand;
    if (type !== "") queryParamsObj.type = type;
    if (supplier !== "") queryParamsObj.supplier = supplier;
    if (nameAsc !== "") queryParamsObj.nameAsc = nameAsc;
    if (brandAsc !== "") queryParamsObj.brandAsc = brandAsc;
    if (typeAsc !== "") queryParamsObj.typeAsc = typeAsc;
    if (supplierAsc !== "") queryParamsObj.supplierAsc = supplierAsc;
    if (keyword) queryParamsObj.search_query = keyword;

    const queryParams = new URLSearchParams(queryParamsObj).toString();

    navigate(`/resources?${queryParams}`);
  }, [navigateTableResourcesTrigger]);
  return (
    <>
      <AppNavbar />
      <AsideResourcesPage
        brand={brand}
        setBrand={setBrand}
        type={type}
        setType={setType}
        supplier={supplier}
        setSupplier={setSupplier}
        setCurrentPage={setCurrentPage}
        setNavigateTableResourcesTrigger={setNavigateTableResourcesTrigger}
      />
      <div className="ilm-pageb-with-aside">
        <h1 className="page-title">
          <span className="app-slogan-1">All </span>
          <span className="app-slogan-2">Resources</span>
        </h1>
        <div className="table-margin-top">
          <ResourcesTable
            resources={resources}
            totalPages={totalPages}
            currentPage={currentPage}
            setCurrentPage={setCurrentPage}
            keyword={keyword}
            setKeyword={setKeyword}
            setNavigateTableResourcesTrigger={setNavigateTableResourcesTrigger}
            sortByName={sortByName}
            sortByType={sortByType}
            sortByBrand={sortByBrand}
            sortBySupplier={sortBySupplier}
          />
        </div>
      </div>
    </>
  );
}
