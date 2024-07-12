import React from "react";
import { render, screen } from "@testing-library/react";
import MyProjectCard from "./MyProjectCard";
import { Trans } from "@lingui/macro";
import { useNavigate } from "react-router-dom";
import { useMediaQuery } from "react-responsive";

// Mock dependencies
jest.mock("react-router-dom", () => ({
  useNavigate: jest.fn(),
}));

jest.mock("react-responsive", () => ({
  useMediaQuery: jest.fn(),
}));

jest.mock("@lingui/macro", () => ({
  Trans: ({ children }) => <>{children}</>,
}));

jest.mock("../../utilities/converters", () => ({
  formatTypeUserInProject: jest.fn((type) => type),
  formatTypeUserInProjectInMaiusculas: jest.fn((type) => type.toUpperCase()),
}));

jest.mock("../bars/ProgressBar", () => () => <div>ProgressBar</div>);

describe("MyProjectCard", () => {
  const mockNavigate = jest.fn();
  const mockUseMediaQuery = jest.fn();

  beforeEach(() => {
    useNavigate.mockReturnValue(mockNavigate);
    useMediaQuery.mockImplementation(mockUseMediaQuery);
  });

  const defaultProps = {
    name: "Test Project",
    lab: "Test Lab",
    members: 5,
    maxMembers: 10,
    startDate: "2023-01-01",
    endDate: "2023-12-31",
    progress: 50,
    status: "In Progress",
    typeMember: "Member",
    systemProjectName: "test-project",
    photo: null,
  };

  test("renders project name", () => {
    render(<MyProjectCard {...defaultProps} />);
    expect(screen.getByText("Test Project")).toBeInTheDocument();
  });

  test("renders lab name", () => {
    render(<MyProjectCard {...defaultProps} />);
    expect(screen.getByText("Test Lab")).toBeInTheDocument();
  });

  test("renders members count", () => {
    render(<MyProjectCard {...defaultProps} />);
    expect(screen.getByText("5/10")).toBeInTheDocument();
  });

  test("renders start date", () => {
    render(<MyProjectCard {...defaultProps} />);
    expect(screen.getByText("2023-01-01")).toBeInTheDocument();
  });

  test("renders end date", () => {
    render(<MyProjectCard {...defaultProps} />);
    expect(screen.getByText("2023-12-31")).toBeInTheDocument();
  });

  test("renders progress bar", () => {
    render(<MyProjectCard {...defaultProps} />);
    expect(screen.getByText("ProgressBar")).toBeInTheDocument();
  });

  test("renders member type", () => {
    render(<MyProjectCard {...defaultProps} />);
    expect(screen.getByText("MEMBER")).toBeInTheDocument();
  });
});
