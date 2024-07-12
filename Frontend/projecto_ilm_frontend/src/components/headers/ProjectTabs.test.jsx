import React from "react";
import { render, screen } from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import ProjectTabs from "./ProjectTabs";

jest.mock("@lingui/macro", () => ({
  t: (msg) => msg,
}));
jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useParams: () => ({ systemProjectName: "testProject" }),
  useLocation: () => ({ pathname: "/project/testProject/info" }),
}));

describe("ProjectTabs Component", () => {
  const renderComponent = (typeOfUserSeingProject) => {
    return render(
      <MemoryRouter initialEntries={["/project/testProject/info"]}>
        <Routes>
          <Route
            path="/project/:systemProjectName/:tab"
            element={
              <ProjectTabs
                typeOfUserSeingProject={typeOfUserSeingProject}
                projectName="Test Project"
              />
            }
          />
        </Routes>
      </MemoryRouter>
    );
  };

  test("renders correctly for different user types", () => {
    renderComponent("USER");
    expect(screen.getByText("Info")).toBeInTheDocument();
    expect(screen.getByText("Plan")).toBeInTheDocument();
    expect(screen.getByText("Logs")).toBeInTheDocument();
    expect(screen.getByText("Resources")).toBeInTheDocument();
    expect(screen.getByText("Chat")).toBeInTheDocument();
  });

  test("filters tabs correctly for 'ADMIN'", () => {
    renderComponent("ADMIN");
    expect(screen.getByText("Info")).toBeInTheDocument();
    expect(screen.getByText("Resources")).toBeInTheDocument();
    expect(screen.queryByText("Plan")).not.toBeInTheDocument();
    expect(screen.queryByText("Logs")).not.toBeInTheDocument();
    expect(screen.queryByText("Chat")).not.toBeInTheDocument();
  });

  test("does not show tabs for restricted user types", () => {
    renderComponent("PENDING_BY_APPLIANCE");
    expect(screen.queryByText("Info")).not.toBeInTheDocument();
    expect(screen.queryByText("Plan")).not.toBeInTheDocument();
    expect(screen.queryByText("Logs")).not.toBeInTheDocument();
    expect(screen.queryByText("Resources")).not.toBeInTheDocument();
    expect(screen.queryByText("Chat")).not.toBeInTheDocument();
  });
});
