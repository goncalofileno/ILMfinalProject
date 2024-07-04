import {
  Page,
  View,
  Document,
  StyleSheet,
  Text,
  Image,
} from "@react-pdf/renderer";
import { useRef } from "react";

import { useState, useEffect } from "react";
import html2canvas from "html2canvas";
import jsPDF from "jspdf";
import AppNavbar from "../headers/AppNavbar";
import "./StatisticsPdf.css";
import { Chart } from "react-google-charts";
import { Row, Col } from "react-bootstrap";
import { getAppStatistics } from "../../utilities/services";

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
  const [averageExecutionProjectTime, setAverageExecutionProjectTime] =
    useState(0);
  const [supplierWithMostResources, setSupplierWithMostResources] =
    useState(null);
  const [resourcesNumberSupplier, setResourcesNumberSupplier] = useState([]);
  const [projectStatusNumberPerLab, setProjectStatusNumberPerLab] = useState(
    []
  );

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
  }, []);

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
    title: "Members per Lab",
    backgroundColor: "transparent",
    chartArea: { width: "80%" },
  };

  const optionsProjectsPerLab = {
    title: "Projects per Lab",
    backgroundColor: "transparent",
    chartArea: { width: "80%" },
  };

  const optionsMemberPerLabPdf = {
    title: "Members per Lab",
    backgroundColor: "transparent",
    chartArea: { width: "100%" },
  };

  const optionsProjectsPerLabPdf = {
    title: "Projects per Lab",
    backgroundColor: "transparent",
    chartArea: { width: "100%" },
  };

  const optionsProjectStatus = {
    title: "Number of projects per status in each lab",
    backgroundColor: "transparent",
    chartArea: { width: "65%" },
    isStacked: true,
    hAxis: {
      title: "Projects",
      minValue: 0,
    },
    vAxis: {
      title: "Lab",
    },
  };

  const optionsProjectStatusPdf = {
    title: "Number of projects per status in each lab",
    backgroundColor: "transparent",
    chartArea: { width: "55%" },
    isStacked: true,
    hAxis: {
      title: "Projects",
      minValue: 0,
    },
    vAxis: {
      title: "Lab",
    },
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
      <AppNavbar />

      {/* Screen display */}
      <div className="screen-display">
        <div className="ilm-pageb" style={{ paddingTop: "20px" }}>
          <h1 className="page-title">
            <span className="app-slogan-1">ILM </span>
            <span className="app-slogan-2">Statistics</span>
          </h1>
          <Row style={{ height: "3%" }}></Row>
          <Row style={{ height: "42%" }}>
            <Col sm={1}></Col>

            <Col sm={7}>
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
                <Col sm={5} style={{ height: "100%" }}>
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
            <Col sm={3} className="col-app-stats">
              <div className="app-stats">
                <div>
                  Total users: <span>{totalUsers}</span>
                </div>
                <div>
                  Average users in project: <span>{averageUsersInProject}</span>
                </div>

                {averageExecutionProjectTime != "NaN" && (
                  <div>
                    Average execution time per project:{" "}
                    <span>{formatTimeEN(averageExecutionProjectTime)}</span>
                  </div>
                )}
                <div>
                  Supplier with most Resources:{" "}
                  <span>{supplierWithMostResources}</span> with{" "}
                  <span>{resourcesNumberSupplier}</span> resources
                </div>
              </div>
            </Col>
            <Col sm={1}></Col>
          </Row>
          <Row style={{ height: "3%" }}></Row>
          <Row style={{ height: "40%" }}>
            <Col sm={1}></Col>
            <Col sm={7} className="row-pie-charts">
              <Chart
                chartType="BarChart"
                width="100%"
                height="90%"
                data={projectStatusNumberPerLab}
                options={optionsProjectStatus}
              />
            </Col>
            <Col sm={3} className="col-button">
              {" "}
              <button
                onClick={exportToPDF}
                className="submit-button"
                style={{ paddingLeft: "35px", paddingRight: "35px" }}
              >
                Export to PDF
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
                  <Row className="pdf-title">ILM Statistics</Row>
                  <Row>
                    <Col sm={12}>
                      <div className="app-stats-pdf">
                        <div>
                          Total users: <span>{totalUsers}</span>
                        </div>
                        <div>
                          Average users in project:{" "}
                          <span>{averageUsersInProject}</span>
                        </div>

                        {averageExecutionProjectTime != "NaN" && (
                          <div>
                            Average execution time per project:{" "}
                            <span>
                              {formatTimeEN(averageExecutionProjectTime)}
                            </span>
                          </div>
                        )}
                        <div>
                          Supplier with most Resources:{" "}
                          <span>{supplierWithMostResources}</span> with{" "}
                          <span>{resourcesNumberSupplier}</span> resources
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
