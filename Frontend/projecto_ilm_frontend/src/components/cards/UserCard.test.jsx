import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import UserCard from "./UserCard";
import { Trans } from "@lingui/macro";
import { Alert } from "react-bootstrap";

// Mock dependencies
jest.mock("@lingui/macro", () => ({
  Trans: ({ children }) => <>{children}</>,
}));

jest.mock("react-bootstrap", () => ({
  Alert: ({ children, ...props }) => <div {...props}>{children}</div>,
}));

describe("UserCard", () => {
  const defaultProps = {
    name: "John Doe",
    lab: "Lab A",
    skills: [
      { name: "Skill 1", type: "Type A", inProject: true },
      { name: "Skill 2", type: "Type B", inProject: false },
    ],
    img: null,
    id: "1",
    systemUsername: "johndoe",
    usersInProject: [],
    setUsersInProject: jest.fn(),
    rejectedUsers: [],
    setRejectedUsers: jest.fn(),
    setGetUsersTrigger: jest.fn(),
    maxMembers: 5,
    numberOfMembersInProject: 3,
    publicProfile: true,
  };

  test("renders user name and lab", () => {
    render(<UserCard {...defaultProps} />);
    expect(screen.getByText("John Doe")).toBeInTheDocument();
    expect(screen.getByText("Lab A")).toBeInTheDocument();
  });

  test("renders skills if public profile", () => {
    render(<UserCard {...defaultProps} />);
    expect(screen.getByText("Skill 1")).toBeInTheDocument();
    expect(screen.getByText("Skill 2")).toBeInTheDocument();
  });

  test("renders private profile alert if not public profile", () => {
    render(<UserCard {...defaultProps} publicProfile={false} />);
    expect(
      screen.getByText("This user has a private profile")
    ).toBeInTheDocument();
  });

  test("calls setUsersInProject and setRejectedUsers on add button click", () => {
    render(<UserCard {...defaultProps} />);
    fireEvent.click(screen.getByText("Add"));
    expect(defaultProps.setUsersInProject).toHaveBeenCalled();
    expect(defaultProps.setRejectedUsers).toHaveBeenCalled();
  });

  test("calls setRejectedUsers on reject button click", () => {
    render(<UserCard {...defaultProps} />);
    fireEvent.click(screen.getByText("Reject"));
    expect(defaultProps.setRejectedUsers).toHaveBeenCalled();
  });

  test("disables add button if max members reached", () => {
    render(<UserCard {...defaultProps} numberOfMembersInProject={4} />);
    expect(screen.getByText("Add")).toBeDisabled();
  });

  test("enables add button if number of members is less than max members", () => {
    render(<UserCard {...defaultProps} numberOfMembersInProject={2} />);
    expect(screen.getByText("Add")).not.toBeDisabled();
  });
});
