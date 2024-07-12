import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import MailNotificationBanner from "./MailNotificationBanner";
import { act } from "react-dom/test-utils";

jest.useFakeTimers();

describe("MailNotificationBanner", () => {
  const message = "John Doe sent you a message";
  const onClick = jest.fn();
  const onEnd = jest.fn();

  beforeEach(() => {
    jest.clearAllTimers();
    jest.clearAllMocks();
  });

  test("renders correctly with given props", () => {
    render(
      <MailNotificationBanner
        message={message}
        onClick={onClick}
        onEnd={onEnd}
      />
    );
    expect(
      screen.getAllByText((content, element) => {
        return element.textContent === "John Doe sent you a message";
      }).length
    ).toBeGreaterThan(0);
  });

  test("calls onClick handler when alert is clicked", () => {
    render(
      <MailNotificationBanner
        message={message}
        onClick={onClick}
        onEnd={onEnd}
      />
    );
    fireEvent.click(screen.getByRole("alert"));
    expect(onClick).toHaveBeenCalledTimes(1);
  });

  test("calls onEnd handler after 3 seconds", () => {
    render(
      <MailNotificationBanner
        message={message}
        onClick={onClick}
        onEnd={onEnd}
      />
    );
    act(() => {
      jest.advanceTimersByTime(3000);
    });
    expect(onEnd).toHaveBeenCalledTimes(1);
  });

  test("formats the message correctly", () => {
    render(
      <MailNotificationBanner
        message={message}
        onClick={onClick}
        onEnd={onEnd}
      />
    );
    expect(
      screen.getAllByText((content, element) => {
        return element.textContent === "John Doe sent you a message";
      }).length
    ).toBeGreaterThan(0);
  });

  test("becomes visible after rendering", () => {
    render(
      <MailNotificationBanner
        message={message}
        onClick={onClick}
        onEnd={onEnd}
      />
    );
    const alert = screen.getByRole("alert");
    expect(alert).toHaveClass("show");
  });
  test("is hidden initially", () => {
    render(
      <MailNotificationBanner
        message={message}
        onClick={onClick}
        onEnd={onEnd}
      />
    );
    const alert = screen.getByRole("alert");
    expect(alert).toHaveClass("fade");
  });
});
