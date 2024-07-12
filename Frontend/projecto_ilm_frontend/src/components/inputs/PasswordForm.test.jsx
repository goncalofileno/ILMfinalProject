import { render, screen, fireEvent } from "@testing-library/react";
import PasswordForm from "./PasswordForm";
import React from "react";

describe("PasswordForm", () => {
  test("renders the label correctly", () => {
    render(
      <PasswordForm
        label="Password"
        type="password"
        value=""
        setValue={() => {}}
        warningType=""
        warningTxt=""
        handleOnBlur={() => {}}
        setShowTooltip={() => {}}
        showTolltip={false}
        conditionsMet={{
          upper: false,
          lower: false,
          number: false,
          special: false,
          length: false,
        }}
      />
    );

    const labelElement = screen.getByText("Password");
    expect(labelElement).toBeInTheDocument();
  });

  test("renders the input field correctly", () => {
    render(
      <PasswordForm
        label="Password"
        type="password"
        value="testpassword"
        setValue={() => {}}
        warningType=""
        warningTxt=""
        handleOnBlur={() => {}}
        setShowTooltip={() => {}}
        showTolltip={false}
        conditionsMet={{
          upper: false,
          lower: false,
          number: false,
          special: false,
          length: false,
        }}
      />
    );

    const inputElement = screen.getByLabelText("Password");
    expect(inputElement).toBeInTheDocument();
    expect(inputElement.value).toBe("testpassword");
  });

  test("renders the warning text correctly", () => {
    render(
      <PasswordForm
        label="Password"
        type="password"
        value=""
        setValue={() => {}}
        warningType="incorrect"
        warningTxt="Invalid password"
        handleOnBlur={() => {}}
        setShowTooltip={() => {}}
        showTolltip={false}
        conditionsMet={{
          upper: false,
          lower: false,
          number: false,
          special: false,
          length: false,
        }}
      />
    );

    const warningTextElement = screen.getByText("Invalid password");
    expect(warningTextElement).toBeInTheDocument();
  });

  test("calls handleOnBlur function correctly on input blur", () => {
    const handleOnBlurMock = jest.fn();

    render(
      <PasswordForm
        label="Password"
        type="password"
        value="testpassword"
        setValue={() => {}}
        warningType=""
        warningTxt=""
        handleOnBlur={handleOnBlurMock}
        setShowTooltip={() => {}}
        showTolltip={false}
        conditionsMet={{
          upper: false,
          lower: false,
          number: false,
          special: false,
          length: false,
        }}
      />
    );

    const inputElement = screen.getByLabelText("Password");
    fireEvent.blur(inputElement);

    expect(handleOnBlurMock).toHaveBeenCalled();
  });

  test("disables the input field correctly", () => {
    render(
      <PasswordForm
        label="Password"
        type="password"
        value=""
        setValue={() => {}}
        warningType=""
        warningTxt=""
        handleOnBlur={() => {}}
        setShowTooltip={() => {}}
        showTolltip={false}
        conditionsMet={{
          upper: false,
          lower: false,
          number: false,
          special: false,
          length: false,
        }}
      />
    );

    const inputElement = screen.getByLabelText("Password");
    expect(inputElement).toBeEnabled();
  });
});
