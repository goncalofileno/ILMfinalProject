import AppNavbar from "../components/headers/AppNavbar";
import ProjectTabs from "../components/headers/ProjectTabs";
import { Row, Col, Form, InputGroup, Button, Alert } from "react-bootstrap";
import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import Cookies from "js-cookie";
import ResourcesProjectCreationTable from "../components/tables/ResourcesProjectCreationTable";
import YourResourcesProjectCreationTable from "../components/tables/YourResourcesProjectCreationTable";
import TablePagination from "../components/paginations/TablePagination";
import {
  getResourcesFilters,
  getAllResourcesCreatingProject,
  addInitialResources,
  getProjectResources,
} from "../utilities/services";
import AddResourceModal from "../components/modals/AddResourceModal";
import StandardModal from "../components/modals/StandardModal";
import { useMediaQuery } from "react-responsive";
import { Trans, t } from "@lingui/macro";

export default function ProjectProfileResourcesPage() {
  const { systemProjectName } = useParams();
  const [brands, setBrands] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [resources, setResources] = useState([]);
  const [totalPages, setTotalPages] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [brand, setBrand] = useState("");
  const [supplier, setSupplier] = useState("");
  const [keyword, setKeyword] = useState("");
  const [nameAsc, setNameAsc] = useState("");
  const [typeAsc, setTypeAsc] = useState("");
  const [brandAsc, setBrandAsc] = useState("");
  const [supplierAsc, setSupplierAsc] = useState("");
  const [type, setType] = useState("");
  const [resourcesTableTrigger, setResourcesTableTrigger] = useState(false);
  const [isModalActive, setIsModalActive] = useState(false);
  const [resourceId, setResourceId] = useState(null);
  const [resourceSupplier, setResourceSupplier] = useState(null);
  const [yourResources, setYourResources] = useState(
    Cookies.get("yourResources") ? JSON.parse(Cookies.get("yourResources")) : []
  );
  const [currentLanguage, setCurrentLanguage] = useState(
    Cookies.get("user-language") || "ENGLISH"
  );
  const [selectedResource, setSelectedResource] = useState({});
  const [modalType, setModalType] = useState("warning");
  const [modalMessage, setModalMessage] = useState("");
  const [modalActive, setModalActive] = useState(false);
  const [selectedResources, setSelectedResources] = useState([]);
  const [userInProjectType, setUserInProjectType] = useState("");
  const [projectName, setProjectName] = useState("");
  const [projectState, setProjectState] = useState("");
  const isTablet = useMediaQuery({ query: "(max-width: 991px)" });
  const navigate = useNavigate();

  useEffect(() => {
    Cookies.set("yourResources", JSON.stringify(yourResources));
  }, [yourResources]);

  useEffect(() => {
    if (yourResources.length === 0) {
      getProjectResources(systemProjectName)
        .then((response) => response.json())
        .then((data) => {
          setYourResources(data.resources);
          setUserInProjectType(data.userInProjectTypeENUM);
          setProjectName(data.projectName);
          setProjectState(data.projectStatus);
          Cookies.set("yourResources", JSON.stringify(data.resources));
          const rejectedIdsDto = {
            rejectedIds: data.resources.map(
              (resource) => resource.resourceSupplierId
            ),
          };
          getAllResourcesCreatingProject(
            currentPage,
            brand,
            type,
            supplier,
            keyword,
            nameAsc,
            typeAsc,
            brandAsc,
            supplierAsc,
            rejectedIdsDto
          )
            .then((response) => response.json())
            .then((data) => {
              setResources(data.tableResources);
              setTotalPages(data.maxPageNumber);
            });
        });
    } else {
      const rejectedIdsDto = {
        rejectedIds: yourResources.map(
          (resource) => resource.resourceSupplierId
        ),
      };
      getAllResourcesCreatingProject(
        currentPage,
        brand,
        type,
        supplier,
        keyword,
        nameAsc,
        typeAsc,
        brandAsc,
        supplierAsc,
        rejectedIdsDto
      )
        .then((response) => response.json())
        .then((data) => {
          setResources(data.tableResources);
          setTotalPages(data.maxPageNumber);
        });
    }

    getResourcesFilters(false, false)
      .then((response) => response.json())
      .then((data) => {
        setBrands(data.brands);
        setSuppliers(data.suppliers);
      });
  }, []);

  useEffect(() => {
    const rejectedIdsDto = {
      rejectedIds: yourResources.map((resource) => resource.resourceSupplierId),
    };
    getAllResourcesCreatingProject(
      currentPage,
      brand,
      type,
      supplier,
      keyword,
      nameAsc,
      typeAsc,
      brandAsc,
      supplierAsc,
      rejectedIdsDto
    )
      .then((response) => response.json())
      .then((data) => {
        setResources(data.tableResources);
        setTotalPages(data.maxPageNumber);
      });
  }, [resourcesTableTrigger]);

  useEffect(() => {
    return () => {
      Cookies.set("yourResources", []);
      Cookies.remove("yourResources");
    };
  }, []);

  const sortByName = () => {
    if (typeAsc !== "") setTypeAsc("");
    if (brandAsc !== "") setBrandAsc("");
    if (supplierAsc !== "") setSupplierAsc("");
    if (nameAsc === "") setNameAsc(true);
    else setNameAsc(!nameAsc);
    setResourcesTableTrigger((prev) => !prev);
  };

  const sortByType = () => {
    if (nameAsc !== "") setNameAsc("");
    if (brandAsc !== "") setBrandAsc("");
    if (supplierAsc !== "") setSupplierAsc("");
    if (typeAsc === "") setTypeAsc(true);
    else setTypeAsc(!typeAsc);
    setResourcesTableTrigger((prev) => !prev);
  };

  const sortByBrand = () => {
    if (nameAsc !== "") setNameAsc("");
    if (typeAsc !== "") setTypeAsc("");
    if (supplierAsc !== "") setSupplierAsc("");
    if (brandAsc === "") setBrandAsc(true);
    else setBrandAsc(!brandAsc);
    setResourcesTableTrigger((prev) => !prev);
  };

  const sortBySupplier = () => {
    if (nameAsc !== "") setNameAsc("");
    if (typeAsc !== "") setTypeAsc("");
    if (brandAsc !== "") setBrandAsc("");
    if (supplierAsc === "") setSupplierAsc(true);
    else setSupplierAsc(!supplierAsc);
    setResourcesTableTrigger((prev) => !prev);
  };

  const handleSubmit = () => {
    const rejectedIdsDto = {
      rejectedIds: yourResources.map((resource) => resource.resourceSupplierId),
    };
    addInitialResources(systemProjectName, rejectedIdsDto).then((response) => {
      if (response.status === 200) {
        setModalType("success");
        setModalMessage(t`The resources have been saved successfully!`);
        setModalActive(true);
      }
    });
  };

  const isProjectInactive =
    projectState === "CANCELED" || projectState === "READY";

  return (
    <>
      <AppNavbar setCurrentLanguage={setCurrentLanguage} />

      <div
        className={
          isTablet ? "ilm-page-mobile" : "bckg-color-ilm-page ilm-pageb"
        }
        style={{ paddingBottom: isTablet && "150px" }}
      >
        <ProjectTabs
          typeOfUserSeingProject={userInProjectType}
          projectName={projectName}
        />
        <Row className="row-container2" style={{ marginTop: "0px" }}>
          {isProjectInactive && (
            <>
              <div className="background-disabled"></div>
              <Row>
                <Col>
                  <Alert variant="danger" className="standard-modal">
                    <Trans>
                      The project is {projectState.toLowerCase()}, and you can't
                      change the resources in the project.
                    </Trans>
                  </Alert>
                </Col>
              </Row>
            </>
          )}
          <Col xs={1} sm={1}></Col>
          <Col xs={11} sm={11} lg={5} style={{ height: isTablet && "650px" }}>
            <Row style={{ height: !isTablet ? "100%" : "85%" }}>
              <Row
                style={{ height: "17%", marginBottom: "0.5%" }}
                className="justify-align-content"
              >
                <Col xs={8} sm={8}>
                  <InputGroup className="gap-10px">
                    <Form.Control
                      type="text"
                      placeholder={t`Search for resources`}
                      style={{ borderRadius: "10px", cursor: "text" }}
                      className="custom-focus"
                      value={keyword}
                      onChange={(e) => setKeyword(e.target.value)}
                      disabled={isProjectInactive}
                    />
                    <Button
                      variant="primary"
                      id="primary-btn-boot"
                      onClick={() => setResourcesTableTrigger((prev) => !prev)}
                      disabled={isProjectInactive}
                    >
                      <i className="fas fa-search"></i>
                    </Button>
                    <Button
                      variant="secondary"
                      style={{ borderRadius: "10px" }}
                      onClick={() => {
                        setKeyword("");
                        setResourcesTableTrigger((prev) => !prev);
                      }}
                      disabled={isProjectInactive}
                    >
                      <Trans>Clear</Trans>
                    </Button>
                  </InputGroup>
                </Col>
                <Col xs={3} sm={3}>
                  <Form.Control
                    as="select"
                    className="custom-focus"
                    style={{ marginBottom: "5px" }}
                    value={brand}
                    onChange={(e) => {
                      setBrand(e.target.value);
                      setResourcesTableTrigger((prev) => !prev);
                    }}
                    disabled={isProjectInactive}
                  >
                    <option value="">{t`All Brands`}</option>
                    {brands.map((brand, index) => (
                      <option key={index} value={brand}>
                        {brand}
                      </option>
                    ))}
                  </Form.Control>
                  <Form.Control
                    as="select"
                    className="custom-focus"
                    value={supplier}
                    onChange={(e) => {
                      setSupplier(e.target.value);
                      setResourcesTableTrigger((prev) => !prev);
                    }}
                    disabled={isProjectInactive}
                  >
                    <option value="">{t`All Suppliers`}</option>
                    {suppliers.map((supplier, index) => (
                      <option key={index} value={supplier}>
                        {supplier}
                      </option>
                    ))}
                  </Form.Control>
                </Col>
                <Col xs={1} sm={1}></Col>
              </Row>
              <Row style={{ height: !isTablet ? "83%" : "75%" }}>
                <Col xs={11} sm={11}>
                  <ResourcesProjectCreationTable
                    resources={resources}
                    sortByBrand={sortByBrand}
                    sortByName={sortByName}
                    sortBySupplier={sortBySupplier}
                    sortByType={sortByType}
                    nameAsc={nameAsc}
                    typeAsc={typeAsc}
                    brandAsc={brandAsc}
                    supplierAsc={supplierAsc}
                    setIsModalActive={setIsModalActive}
                    setResourceId={setResourceId}
                    setResourceSupplier={setResourceSupplier}
                    setYourResources={setYourResources}
                    yourResources={yourResources}
                    setResourcesTableTrigger={setResourcesTableTrigger}
                    setSelectedResource={setSelectedResource}
                    setSelectedResources={setSelectedResources}
                    disabled={isProjectInactive}
                  />
                </Col>
                <Col xs={1} sm={1}></Col>
              </Row>
            </Row>
            {isTablet && (
              <Row
                className="last-row-resources-creation"
                style={{ marginTop: "0px" }}
              >
                <Col xs={1} sm={1}></Col>
                <Col xs={2} sm={2}>
                  {" "}
                  <button
                    className="submit-button"
                    id="btn-add-project-table-projects"
                    onClick={() => {
                      setIsModalActive(true);
                    }}
                  >
                    Add Resource
                  </button>
                </Col>
                <Col xs={6} sm={5} md={4}></Col>
                <Col xs={1} sm={1} className="table-resources-pagination">
                  <TablePagination
                    totalPages={totalPages}
                    currentPage={currentPage}
                    setCurrentPage={setCurrentPage}
                    setNavigateTableTrigger={setResourcesTableTrigger}
                  ></TablePagination>
                </Col>
                <Col xs={2} sm={3} md={4}></Col>
              </Row>
            )}
          </Col>

          <Col
            xs={11}
            sm={11}
            lg={5}
            style={{
              maxHeight: "100%",

              height: isTablet ? "600px" : "100%",
            }}
          >
            <Row style={{ height: "100%" }}>
              <Row
                style={{
                  height: !isTablet ? "17%" : "10%",
                  marginBottom: !isTablet ? "0.5%" : "0px",
                }}
              >
                <Col xs={1} sm={1}></Col>
                <Col xs={11} sm={11}>
                  <h4 className="h4-resources-project-creat">
                    <Trans>Your Resources</Trans>
                  </h4>
                </Col>
              </Row>
              <Row style={{ height: "83%" }}>
                <Col xs={1} sm={1}></Col>
                <Col
                  xs={11}
                  sm={11}
                  style={{
                    maxHeight: "100%",
                    height: "100%",
                  }}
                >
                  <YourResourcesProjectCreationTable
                    resources={yourResources}
                    setResources={setYourResources}
                    setIsModalActive={setIsModalActive}
                    setResourceSupplier={setResourceSupplier}
                    setResourceId={setResourceId}
                    setResourcesTableTrigger={setResourcesTableTrigger}
                    setSelectedResource={setSelectedResource}
                    setSelectedResources={setSelectedResources}
                    disabled={isProjectInactive}
                  />
                </Col>
              </Row>
            </Row>
            {isTablet && (
              <Row className="last-row-resources-creation">
                <Col xs={1} sm={1}></Col>
                <Col xs={3} sm={3}></Col>
                <Col xs={4} sm={4}>
                  <button
                    className="submit-button"
                    style={{ width: "100%" }}
                    onClick={handleSubmit}
                  >
                    Save Resources
                  </button>
                </Col>
                <Col xs={1} sm={1}></Col>
              </Row>
            )}
          </Col>
          <Col xs={1} sm={1}></Col>
        </Row>
        {!isTablet && (
          <Row className="last-row-resources-creation">
            <Col xs={1} sm={1}></Col>
            <Col xs={2} sm={2}>
              {" "}
              <button
                className="submit-button"
                id="btn-add-project-table-projects"
                onClick={() => {
                  setIsModalActive(true);
                }}
              >
                <Trans>Add Resource</Trans>
              </button>
            </Col>
            <Col xs={1} sm={1} className="table-resources-pagination">
              <TablePagination
                totalPages={totalPages}
                currentPage={currentPage}
                setCurrentPage={setCurrentPage}
                setNavigateTableTrigger={setResourcesTableTrigger}
              ></TablePagination>
            </Col>
            <Col xs={3} sm={3}></Col>
            <Col xs={4} sm={4}>
              <button
                className="submit-button"
                style={{ width: "100%" }}
                onClick={handleSubmit}
              >
                <Trans>Save Resources</Trans>
              </button>
            </Col>
            <Col xs={1} sm={1}></Col>
          </Row>
        )}
      </div>
      <AddResourceModal
        isModalActive={isModalActive}
        setIsModalActive={setIsModalActive}
        resourceId={resourceId}
        setResourceId={setResourceId}
        resourceSupplier={resourceSupplier}
        setResourceSupplier={setResourceSupplier}
        resource={selectedResource}
        yourResources={selectedResources}
        setSelectedResources={setSelectedResources}
        setYourResources={setYourResources}
        setTableTrigger={setResourcesTableTrigger}
        disabled={isProjectInactive}
      />
      <StandardModal
        modalType={modalType}
        message={modalMessage}
        modalActive={modalActive}
        setModalActive={setModalActive}
      />
    </>
  );
}
