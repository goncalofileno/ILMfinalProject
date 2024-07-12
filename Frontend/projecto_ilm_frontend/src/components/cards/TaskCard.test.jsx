import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import TaskCard from "./TaskCard";
import { Trans } from "@lingui/macro";

// Mock dependencies
jest.mock("@lingui/macro", () => ({
  Trans: ({ children }) => <>{children}</>,
}));

describe("TaskCard", () => {
  const mockHandleTaskClick = jest.fn();

  const defaultProps = {
    task: {
      rawTask: {
        title: "Test Task",
        initialDate: "2023-01-01",
        finalDate: "2023-12-31",
        membersOfTask: [
          { type: "INCHARGE", name: "John Doe" },
          { type: "MEMBER", name: "Jane Doe" },
        ],
        dependentTasks: [
          { title: "Dependent Task 1" },
          { title: "Dependent Task 2" },
        ],
        status: "IN_PROGRESS",
      },
    },
    handleTaskClick: mockHandleTaskClick,
  };

  test("renders task title", () => {
    render(<TaskCard {...defaultProps} />);
    expect(screen.getByText("Test Task")).toBeInTheDocument();
  });

  test("renders start date", () => {
    render(<TaskCard {...defaultProps} />);
    expect(screen.getByText("1/1/2023")).toBeInTheDocument();
  });

  test("renders end date", () => {
    render(<TaskCard {...defaultProps} />);
    expect(screen.getByText("12/31/2023")).toBeInTheDocument();
  });

  test("renders in charge member", () => {
    render(<TaskCard {...defaultProps} />);
    expect(screen.getByText("John Doe")).toBeInTheDocument();
  });

  test("renders dependent tasks", () => {
    render(<TaskCard {...defaultProps} />);
    expect(
      screen.getByText("Dependent Task 1, Dependent Task 2")
    ).toBeInTheDocument();
  });

  test("calls handleTaskClick when task card is clicked", () => {
    render(<TaskCard {...defaultProps} />);
    fireEvent.click(screen.getByText("Test Task"));
    expect(mockHandleTaskClick).toHaveBeenCalled();
  });
});
