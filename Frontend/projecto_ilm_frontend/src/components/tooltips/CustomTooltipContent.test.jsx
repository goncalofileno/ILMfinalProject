import React from "react";
import { render, screen } from "@testing-library/react";
import CustomTooltipContent from "./CustomTooltipContent";
import { Trans } from "@lingui/macro";
import { act } from "react";
const getDurationInDays = (start, end) => {
  const durationInMilliseconds = end.getTime() - start.getTime();
  return Math.floor(durationInMilliseconds / (1000 * 60 * 60 * 24)) + 1;
}; // Mock data
const mockTask = {
  name: "Test Task",
  start: new Date("2023-01-01"),
  end: new Date("2023-01-10"),
  rawTask: {
    status: "In Progress",
    projectState: "Active",
  },
};

jest.mock("@lingui/macro", () => ({
  Trans: ({ children }) => <>{children}</>,
}));

jest.mock("../../utilities/converters", () => ({
  formatTaskStatus: jest.fn((status) => status),
  formatStatusDropDown: jest.fn((state) => state),
}));

describe("CustomTooltipContent", () => {
  test("renders task name", () => {
    render(
      <CustomTooltipContent
        task={mockTask}
        fontSize="14px"
        fontFamily="Arial"
      />
    );
    expect(screen.getByText("Test Task")).toBeInTheDocument();
  });
  test("renders task status", () => {
    render(
      <CustomTooltipContent
        task={mockTask}
        fontSize="14px"
        fontFamily="Arial"
      />
    );
    expect(screen.getByText("In Progress")).toBeInTheDocument();
  });

  test("renders start date", () => {
    render(
      <CustomTooltipContent
        task={mockTask}
        fontSize="14px"
        fontFamily="Arial"
      />
    );
    expect(screen.getByText("1/1/2023")).toBeInTheDocument();
  });

  test("renders end date", () => {
    render(
      <CustomTooltipContent
        task={mockTask}
        fontSize="14px"
        fontFamily="Arial"
      />
    );
    expect(screen.getByText("1/10/2023")).toBeInTheDocument();
  });
});
