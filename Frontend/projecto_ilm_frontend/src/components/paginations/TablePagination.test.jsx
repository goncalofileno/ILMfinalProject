import { render, screen, fireEvent } from "@testing-library/react";
import TablePagination from "./TablePagination";
import React from "react";

describe("TablePagination", () => {
  test("handles page click correctly", () => {
    const totalPages = 10;
    const currentPage = 1;
    const setCurrentPage = jest.fn();
    const setNavigateTableTrigger = jest.fn();

    render(
      <TablePagination
        totalPages={totalPages}
        currentPage={currentPage}
        setCurrentPage={setCurrentPage}
        setNavigateTableTrigger={setNavigateTableTrigger}
      />
    );

    // Simulate a page click
    fireEvent.click(screen.getByText("2"));

    // Add assertions to verify the setCurrentPage and setNavigateTableTrigger functions were called
    expect(setCurrentPage).toHaveBeenCalledWith(2);
    expect(setNavigateTableTrigger).toHaveBeenCalled();
  });
});
