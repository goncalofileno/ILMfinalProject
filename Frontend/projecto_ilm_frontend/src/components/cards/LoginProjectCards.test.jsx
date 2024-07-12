import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import LoginProjectsCards from "./LoginProjectsCards";
import { Trans } from "@lingui/macro";
import { useMediaQuery } from "react-responsive";

// Mock dependencies
jest.mock("@lingui/macro", () => ({
  Trans: ({ children }) => <>{children}</>,
}));

jest.mock("react-responsive", () => ({
  useMediaQuery: jest.fn(),
}));

describe("LoginProjectsCards", () => {
  const defaultProps = {
    cardBkgColor: "bg-color",
    title: "Project Title",
    description: "Project Description",
    scrollToRef: jest.fn(),
  };

  beforeEach(() => {
    useMediaQuery.mockReturnValue(false);
  });

  test("renders title and description", () => {
    render(<LoginProjectsCards {...defaultProps} />);
    expect(screen.getByText("Project Title")).toBeInTheDocument();
    expect(screen.getByText("Project Description")).toBeInTheDocument();
  });

  test("renders 'Login to see more details' text", () => {
    render(<LoginProjectsCards {...defaultProps} />);
    expect(screen.getByText("Login to see more details")).toBeInTheDocument();
  });

  test("calls scrollToRef on 'more details' click", () => {
    render(<LoginProjectsCards {...defaultProps} />);
    fireEvent.click(screen.getByText("Login to see more details"));
    expect(defaultProps.scrollToRef).toHaveBeenCalled();
  });

  test("applies tablet font size when isTablet is true", () => {
    useMediaQuery.mockReturnValue(true); // Simulate tablet view
    render(<LoginProjectsCards {...defaultProps} />);
    expect(screen.getByText("Project Title")).toHaveStyle("fontSize: 20px");
  });

  test("does not apply tablet font size when isTablet is false", () => {
    useMediaQuery.mockReturnValue(false); // Simulate non-tablet view
    render(<LoginProjectsCards {...defaultProps} />);
    expect(screen.getByText("Project Title")).not.toHaveStyle("fontSize: 20px");
  });
});
