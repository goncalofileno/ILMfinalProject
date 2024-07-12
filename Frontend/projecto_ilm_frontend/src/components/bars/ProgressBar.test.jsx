import { render } from "@testing-library/react";
import ProgressBar from "./ProgressBar";
import React from "react";
import { formatStatus } from "../../utilities/converters";
import { screen } from "@testing-library/react";

jest.mock("../../utilities/converters", () => ({
  formatStatus: jest.fn((status) => status),
}));

describe("ProgressBar", () => {
  it("renders with the correct width and background color", () => {
    const { getByTestId } = render(
      <ProgressBar percentage={50} status="IN_PROGRESS" />
    );

    const barInside = getByTestId("bar-inside");

    expect(barInside).toHaveStyle("width: 50%");
    expect(barInside).toHaveStyle("background-color: rgba(0, 222, 222, 0.593)");
  });

  test("renders the progress bar with correct width and color for CANCELED status", () => {
    render(<ProgressBar percentage={50} status="CANCELED" />);
    const barInside = screen.getByTestId("bar-inside");
    expect(barInside).toHaveStyle("width: 100%");
    expect(barInside).toHaveStyle("background-color: rgba(255, 0, 0, 0.5)");
  });

  test("renders the progress bar with correct width and color for FINISHED status", () => {
    render(<ProgressBar percentage={75} status="FINISHED" />);
    const barInside = screen.getByTestId("bar-inside");
    expect(barInside).toHaveStyle("width: 75%");
    expect(barInside).toHaveStyle("background-color: rgba(0, 179, 60,0.5)");
  });

  test("renders the status text correctly", () => {
    render(<ProgressBar percentage={75} status="FINISHED" />);
    const statusText = screen.getByText("FINISHED");
    expect(statusText).toBeInTheDocument();
  });

  test("calls formatStatus with the correct status", () => {
    render(<ProgressBar percentage={75} status="FINISHED" />);
    expect(formatStatus).toHaveBeenCalledWith("FINISHED");
  });
});
