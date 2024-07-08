import {
  Page,
  View,
  Document,
  StyleSheet,
  Text,
  Image,
} from "@react-pdf/renderer";
import { useRef } from "react";
import pdfIcon from "../../resources/icons/other/pdf.png";
import { useState, useEffect } from "react";
import html2canvas from "html2canvas";
import jsPDF from "jspdf";
import Cookies from "js-cookie";
import AppNavbar from "../headers/AppNavbar";
import "./StatisticsPdf.css";
import { Chart } from "react-google-charts";
import { Row, Col } from "react-bootstrap";
import { getAppStatistics } from "../../utilities/services";
import { useMediaQuery } from "react-responsive";
import { Trans, t } from "@lingui/macro";

// Create styles
const styles = StyleSheet.create({
  page: {
    flexDirection: "row",
    backgroundColor: "white",
  },
  section: {
    margin: 10,
    padding: 10,
    flexGrow: 1,
  },
});

const StatisticsPdf = () => {
  const [membersPerLab, setMembersPerLab] = useState([]);
  const [projectsPerLab, setProjectsPerLab] = useState([]);
  const [totalUsers, setTotalUsers] = useState(0);
  const [averageUsersInProject, setAverageUsersInProject] = useState(0);
  const [averageExecutionProjectTime, setAverageExecutionProjectTime] = useState(0);
  const [supplierWithMostResources, setSupplierWithMostResources] = useState(null);
  const [resourcesNumberSupplier, setResourcesNumberSupplier] = useState([]);
  const [currentLanguage, setCurrentLanguage] = useState(Cookies.get("user-language") || "ENGLISH");
  const [projectStatusNumberPerLab, setProjectStatusNumberPerLab] = useState(
    []
  );
  const isTablet = useMediaQuery({ query: "(max-width: 1199px)" });

  useEffect(() => {
    getAppStatistics().then((response) => {
      return response.json().then((data) => {
        console.log(data);
        const transformedMembersPerLab = data.membersPerLab.map(
          ({ workLocal, members }) => [workLocal, members]
        );
        transformedMembersPerLab.unshift(["Lab", "Members"]);
        setMembersPerLab(transformedMembersPerLab);
        const transformedProjectsPerLab = data.projectsPerLab.map(
          ({ workLocal, projects }) => [workLocal, projects]
        );
        transformedProjectsPerLab.unshift(["Lab", "Projects"]);
        setProjectsPerLab(transformedProjectsPerLab);
        setTotalUsers(data.totalUsers);
        setAverageUsersInProject(data.averageUsersInProject);
        setAverageExecutionProjectTime(data.averageExecutionTimePerProject);

        setSupplierWithMostResources(data.supplierWithMostResources.supplier);
        setResourcesNumberSupplier(data.supplierWithMostResources.resources);
        const transformedProjectStatusNumberPerLab =
          data.projectsStatusNumberPerLab.map(({ lab, statusNumber }) => [
            lab,
            statusNumber[0].number,
            statusNumber[1].number,
            statusNumber[2].number,
            statusNumber[3].number,
            statusNumber[4].number,
            statusNumber[5].number,
          ]);
        transformedProjectStatusNumberPerLab.unshift([
          "lab",
          "Planning",
          "Ready",
          "Approved",
          "In Progress",
          "Canceled",
          "Finished",
        ]);
        setProjectStatusNumberPerLab(transformedProjectStatusNumberPerLab);
      });
    });
  }, [currentLanguage]);

  const pieRef = useRef();

  const exportToPDF = async () => {
    try {
      if (!pieRef.current) {
        console.warn("Component is not mounted or pieRef is not available");
        return;
      }
      const canvas = await html2canvas(pieRef.current);
      const dataUrl = canvas.toDataURL("image/png");
      // Further processing

      const pdf = new jsPDF();
      pdf.addImage(dataUrl, "PNG", 0, 0);
      pdf.save("pie-chart.pdf");
    } catch (error) {
      console.error("Error exporting to PDF:", error);
    }
  };

  const optionsMemberPerLab = {
    title: t`Members per Lab`,
    backgroundColor: "transparent",
    chartArea: { width: "80%" },
  };

  const optionsProjectsPerLab = {
    title: t`Projects per Lab`,
    backgroundColor: "transparent",
    chartArea: { width: "80%" },
  };

  const optionsMemberPerLabPdf = {
    title: t`Members per Lab`,
    backgroundColor: "transparent",
    chartArea: { width: "100%" },
  };

  const optionsProjectsPerLabPdf = {
    title: t`Projects per Lab`,
    backgroundColor: "transparent",
    chartArea: { width: "100%" },
  };

  const optionsProjectStatus = {
    title: t`Number of projects per status in each lab`,
    backgroundColor: "transparent",
    chartArea: { width: "65%" },
    isStacked: true,
    hAxis: {
      title: t`Projects`,
      minValue: 0,
    },
    vAxis: {
      title: "Lab",
    },
  };

  const optionsProjectStatusPdf = {
    title: t`Number of projects per status in each lab`,
    backgroundColor: "transparent",
    chartArea: { width: "55%" },
    isStacked: true,
    hAxis: {
      title: t`Projects`,
      minValue: 0,
    },
    vAxis: {
      title: t`Lab`,
    },
  };

  const formatTime = (minutes) => {
    const userLanguage = Cookies.get("user-language") || "ENGLISH";
    if (userLanguage === "PORTUGUESE") {
      return formatTimePT(minutes);
    } else {
      return formatTimeEN(minutes);
    }
  };

  const formatTimeEN = (minutes) => {
    if (minutes >= 1440) {
      const days = Math.floor(minutes / 1440);
      const remainingMinutes = minutes % 1440;
      const hours = Math.floor(remainingMinutes / 60);
      const mins = remainingMinutes % 60;

      // Formatting for days, hours, and minutes
      let formattedTime = `${days} day${days !== 1 ? "s" : ""}`;
      if (hours > 0) {
        formattedTime += `, ${hours} hour${hours !== 1 ? "s" : ""}`;
      }
      if (mins > 0) {
        formattedTime += `, ${mins} minute${mins !== 1 ? "s" : ""}`;
      }

      return formattedTime;
    } else if (minutes >= 60) {
      const hours = Math.floor(minutes / 60);
      const mins = minutes % 60;

      // Formatting for hours and minutes
      let formattedTime = `${hours} hour${hours !== 1 ? "s" : ""}`;
      if (mins > 0) {
        formattedTime += `, ${mins} minute${mins !== 1 ? "s" : ""}`;
      }

      return formattedTime;
    } else {
      // Only minutes
      return `${minutes} minute${minutes !== 1 ? "s" : ""}`;
    }
  };

  const formatTimePT = (minutes) => {
    if (minutes >= 1440) {
      const days = Math.floor(minutes / 1440);
      const remainingMinutes = minutes % 1440;
      const hours = Math.floor(remainingMinutes / 60);
      const mins = remainingMinutes % 60;

      // Formatação para dias, horas e minutos
      let formattedTime = `${days} dia${days !== 1 ? "s" : ""}`;
      if (hours > 0) {
        formattedTime += `, ${hours} hora${hours !== 1 ? "s" : ""}`;
      }
      if (mins > 0) {
        formattedTime += `, ${mins} minuto${mins !== 1 ? "s" : ""}`;
      }

      return formattedTime;
    } else if (minutes >= 60) {
      const hours = Math.floor(minutes / 60);
      const mins = minutes % 60;

      // Formatação para horas e minutos
      let formattedTime = `${hours} hora${hours !== 1 ? "s" : ""}`;
      if (mins > 0) {
        formattedTime += `, ${mins} minuto${mins !== 1 ? "s" : ""}`;
      }

      return formattedTime;
    } else {
      // Apenas minutos
      return `${minutes} minuto${minutes !== 1 ? "s" : ""}`;
    }
  };

  return (
    <div>
      <AppNavbar setCurrentLanguage={setCurrentLanguage}/>

      {/* Screen display */}
      <div
        className="screen-display"
        style={{ height: isTablet && "unset", position: isTablet && "unset" }}
      >
        <div
          className={!isTablet ? "ilm-pageb" : "ilm-pageb-noheight"}
          style={{ paddingTop: "20px" }}
        >
          <h1 className="page-title">
            <span className="app-slogan-1">ILM </span>
            <span className="app-slogan-2"><Trans>Statistics</Trans></span>
          </h1>
          <Row style={{ height: "3%" }}></Row>
          <Row style={{ height: !isTablet && "42%", width: "100%" }}>
            <Col sm={1}></Col>

            <Col sm={12} xl={7} style={{ height: "100%" }}>
              <Row style={{ height: "100%" }} className="row-pie-charts">
                <Col sm={5} style={{ height: "100%" }}>
                  <Chart
                    chartType="PieChart"
                    data={membersPerLab}
                    options={optionsMemberPerLab}
                    width={"100%"}
                    height={"100%"}
                  />
                </Col>
                <Col sm={12} xl={5} style={{ height: "100%" }}>
                  {" "}
                  <Chart
                    chartType="PieChart"
                    data={projectsPerLab}
                    options={optionsProjectsPerLab}
                    width={"100%"}
                    height={"100%"}
                  />
                </Col>
              </Row>
            </Col>
            <Col xl={3} className="col-app-stats">
              <div className="app-stats">
                <div>
                <Trans>Total users</Trans>: <span>{totalUsers}</span>
                </div>
                <div>
                <Trans>Average users in project</Trans>: <span>{averageUsersInProject}</span>
                </div>

                {averageExecutionProjectTime !== "NaN" && (
                  <div>
                    <Trans>Average execution time per project</Trans>:{" "}
                    <span>{formatTime(averageExecutionProjectTime)}</span>
                  </div>
                )}
                <div>
                <Trans>Supplier with most Resources</Trans>:{" "}
                  <span>{supplierWithMostResources}</span> <Trans>with</Trans>{" "}
                  <span>{resourcesNumberSupplier}</span> <Trans>resources</Trans>
                </div>
              </div>
            </Col>
            <Col sm={1}></Col>
          </Row>
          <Row style={{ height: "3%" }}></Row>
          <Row style={{ height: !isTablet && "40%", width: "100%" }}>
            <Col sm={1}></Col>
            <Col sm={12} xl={7} className="row-pie-charts">
              <Chart
                chartType="BarChart"
                width="100%"
                height="90%"
                data={projectStatusNumberPerLab}
                options={optionsProjectStatus}
              />
            </Col>
            <Col sm={12} xl={3} className="col-button">
              {" "}
              <button
                onClick={exportToPDF}
                className="submit-button"
                style={{
                  paddingLeft: "35px",
                  paddingRight: "35px",
                  display: "flex",
                  alignItems: "center",
                }}
              >
                <Trans>Export to PDF</Trans>
                <img
                  src={pdfIcon}
                  style={{
                    height: "30px",
                    aspectRatio: "1/1",
                    marginLeft: "10px",
                  }}
                />
              </button>
            </Col>
            <Col sm={1}></Col>
          </Row>
        </div>
      </div>

      <>
        <Document>
          <Page size="A4" style={styles.page}>
            <View style={styles.section}>
              <div>
                <div className="pdf-page" ref={pieRef}>
                  <Row className="pdf-title"><Trans>ILM Statistics</Trans></Row>
                  <Row>
                    <Col sm={12}>
                      <div className="app-stats-pdf">
                        <div>
                        <Trans>Total users</Trans>: <span>{totalUsers}</span>
                        </div>
                        <div>
                        <Trans>Average users in project</Trans>:{" "}
                          <span>{averageUsersInProject}</span>
                        </div>

                        {averageExecutionProjectTime !== "NaN" && (
                          <div>
                            <Trans>Average execution time per project</Trans>:{" "}
                            <span>
                              {formatTime(averageExecutionProjectTime)}
                            </span>
                          </div>
                        )}
                        <div>
                        <Trans>Supplier with most Resources</Trans>:{" "}
                          <span>{supplierWithMostResources}</span> <Trans>with</Trans>{" "}
                          <span>{resourcesNumberSupplier}</span> <Trans>resources</Trans>
                        </div>
                      </div>
                    </Col>
                  </Row>
                  <Row className="row-pdf-pie-charts">
                    <Col sm={6}>
                      <Chart
                        chartType="PieChart"
                        data={membersPerLab}
                        options={optionsMemberPerLabPdf}
                        width={"100%"}
                        height={"100%"}
                      />
                    </Col>
                    <Col sm={6}>
                      <Chart
                        chartType="PieChart"
                        data={projectsPerLab}
                        options={optionsProjectsPerLabPdf}
                        width={"100%"}
                        height={"100%"}
                      />
                    </Col>
                  </Row>
                  <Row className="row-pdf-pie-charts">
                    <Col sm={12}>
                      <Chart
                        chartType="BarChart"
                        width="100%"
                        height="100%"
                        data={projectStatusNumberPerLab}
                        options={optionsProjectStatusPdf}
                      />
                    </Col>
                  </Row>
                </div>
              </div>
            </View>
          </Page>
        </Document>{" "}
      </>
    </div>
  );
};

export default StatisticsPdf;
