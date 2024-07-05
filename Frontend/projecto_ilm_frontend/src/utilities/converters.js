const labMappings = {
  VILA_REAL: "Vila Real",
};

function formatLab(lab) {
  return labMappings[lab] || lab.charAt(0) + lab.slice(1).toLowerCase();
}

const statusMappings = {
  IN_PROGRESS: "IN PROGRESS",
};

function formatStatus(status) {
  return statusMappings[status] || status;
}

const statusDropDownMappings = {
  IN_PROGRESS: "In Progress",
};

function formatStatusDropDown(status) {
  return statusDropDownMappings[status] || status.charAt(0) + status.slice(1).toLowerCase();
}

const formatSkill = (skill) => {
  return skill
    .toLowerCase()
    .split(" ")
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
    .join(" ");
};

function formatResourceType(type) {
  return type.charAt(0) + type.slice(1).toLowerCase();
}

function formatTypeUserInProject(type) {
  return type.charAt(0) + type.slice(1).toLowerCase();
}

const userInProjectTypeMappings = {
  CREATOR: "Creator",
  MANAGER: "Manager",
  MEMBER: "Member",
  MEMBER_BY_INVITATION: "Member by Invitation",
  MEMBER_BY_APPLIANCE: "Member by Application",
  PENDING_BY_APPLIANCE: "Pending by Application",
  PENDING_BY_INVITATION: "Pending by Invitation",
  EXMEMBER: "Ex-Member",
  ADMIN: "Admin",
  GUEST: "Guest",
};

function translateUserInProjectType(type) {
  return userInProjectTypeMappings[type] || type;
}

const taskStatusMappings = {
  PLANNED: "Planned",
  IN_PROGRESS: "In Progress",
  DONE: "Done",
};

function formatTaskStatus(status) {
  return taskStatusMappings[status] || status.charAt(0) + status.slice(1).toLowerCase();
}

export {
  formatLab,
  formatStatus,
  formatStatusDropDown,
  formatSkill,
  formatResourceType,
  formatTypeUserInProject,
  translateUserInProjectType,
  formatTaskStatus,
};
