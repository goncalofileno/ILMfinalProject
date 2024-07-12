import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import ForgetPassModal from "./ForgetPassModal";
import alertStore from "../../stores/alertStore";

// Mocking the services and alertStore
jest.mock("../../utilities/services");
jest.mock("../../stores/alertStore");

jest.mock("@lingui/macro", () => ({
  t: (msg) => msg,
}));

describe("ForgetPassModal Component", () => {
  const setIsModalActive = jest.fn();
  const setMessage = jest.fn();
  const setVisibility = jest.fn();
  const setType = jest.fn();

  beforeEach(() => {
    alertStore.mockReturnValue({
      setMessage,
      setVisibility,
      setType,
    });
  });

  test("does not render when modal is inactive", () => {
    render(
      <ForgetPassModal
        isModalActive={false}
        setIsModalActive={setIsModalActive}
      />
    );
    expect(screen.queryByText("Forget Password")).not.toBeInTheDocument();
  });
});
