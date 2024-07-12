import { render, screen, waitFor } from "@testing-library/react";
import StandardModal from "./StandardModal";
import React from "react";

describe("StandardModal", () => {
  test("renders the modal correctly", () => {
    render(
      <StandardModal
        modalType="success"
        message="This is a success message"
        setModalActive={() => {}}
        modalActive={true}
      />
    );

    const modalElement = screen.getByText("This is a success message");
    expect(modalElement).toBeInTheDocument();
  });

  test("hides the modal after a certain time", async () => {
    jest.useFakeTimers();

    render(
      <StandardModal
        modalType="success"
        message="This is a success message"
        setModalActive={() => {}}
        modalActive={true}
      />
    );

    expect(screen.getByText("This is a success message")).toBeInTheDocument();

    jest.advanceTimersByTime(3000);

    await waitFor(() => {
      const modalElement = screen.queryByText("This is a success message");
      expect(modalElement).toBeInTheDocument();
      expect(modalElement).toHaveClass(
        "fade standard-modal alert alert-success show"
      );
    });
  });
});
