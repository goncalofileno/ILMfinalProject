import { render, screen, fireEvent } from "@testing-library/react";
import InputForm from "./InputForm";
import React from "react";

describe("InputForm", () => {
  test("renders the label correctly", () => {
    render(
      <InputForm
        label="Username"
        type="text"
        value=""
        setValue={() => {}}
        warningType=""
        warningTxt=""
        handleOnBlur={() => {}}
        onBlurActive={false}
        disabled={false}
      />
    );

    const labelElement = screen.getByText("Username");
    expect(labelElement).toBeInTheDocument();
  });

  test("renders the input field correctly", () => {
    render(
      <InputForm
        label="Username"
        type="text"
        value="testuser"
        setValue={() => {}}
        warningType=""
        warningTxt=""
        handleOnBlur={() => {}}
        onBlurActive={false}
        disabled={false}
      />
    );

    const inputElement = screen.getByLabelText("Username");
    expect(inputElement).toBeInTheDocument();
    expect(inputElement.value).toBe("testuser");
  });

  test("calls setValue function correctly on input change", () => {
    const setValueMock = jest.fn();

    render(
      <InputForm
        label="Username"
        type="text"
        value=""
        setValue={setValueMock}
        warningType=""
        warningTxt=""
        handleOnBlur={() => {}}
        onBlurActive={false}
        disabled={false}
      />
    );

    const inputElement = screen.getByLabelText("Username");
    fireEvent.change(inputElement, { target: { value: "testuser" } });

    expect(setValueMock).toHaveBeenCalledWith("testuser");
  });

  test("renders the warning text correctly", () => {
    render(
      <InputForm
        label="Username"
        type="text"
        value=""
        setValue={() => {}}
        warningType="incorrect"
        warningTxt="Invalid username"
        handleOnBlur={() => {}}
        onBlurActive={false}
        disabled={false}
      />
    );

    const warningTextElement = screen.getByText("Invalid username");
    expect(warningTextElement).toBeInTheDocument();
  });

  test("calls handleOnBlur function correctly on input blur", () => {
    const handleOnBlurMock = jest.fn();

    render(
      <InputForm
        label="Username"
        type="text"
        value="testuser"
        setValue={() => {}}
        warningType=""
        warningTxt=""
        handleOnBlur={handleOnBlurMock}
        onBlurActive={true}
        disabled={false}
      />
    );

    const inputElement = screen.getByLabelText("Username");
    fireEvent.blur(inputElement);

    expect(handleOnBlurMock).toHaveBeenCalled();
  });

  test("disables the input field correctly", () => {
    render(
      <InputForm
        label="Username"
        type="text"
        value=""
        setValue={() => {}}
        warningType=""
        warningTxt=""
        handleOnBlur={() => {}}
        onBlurActive={false}
        disabled={true}
      />
    );

    const inputElement = screen.getByLabelText("Username");
    expect(inputElement).toBeDisabled();
  });
});
