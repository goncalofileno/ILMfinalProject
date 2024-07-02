function formatLab(lab) {
  if (lab === "VILA_REAL") return "Vila Real";
  return lab.charAt(0) + lab.slice(1).toLowerCase();
}

function formatStatus(status) {
  if (status === "IN_PROGRESS") {
    status = "IN PROGRESS";
    return status;
  }
  return status;
}

function formatStatusDropDown(status) {
  if (status === "IN_PROGRESS") {
    status = "In Progress";
    return status;
  }
  return status.charAt(0) + status.slice(1).toLowerCase();
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

function translateUserInProjectType(type) {
  switch (type) {
    case "CREATOR":
      return "Creator";
    case "MANAGER":
      return "Manager";
    case "MEMBER":
      return "Member";
    case "MEMBER_BY_INVITATION":
      return "Member by Invitation";
    case "MEMBER_BY_APPLIANCE":
      return "Member by Application";
    case "PENDING_BY_APPLIANCE":
      return "Pending by Application";
    case "PENDING_BY_INVITATION":
      return "Pending by Invitation";
    case "EXMEMBER":
      return "Ex-Member";
    case "ADMIN":
      return "Admin";
    case "GUEST":
      return "Guest";
    default:
      return type;
  }
}

export {
  formatLab,
  formatStatus,
  formatStatusDropDown,
  formatSkill,
  formatResourceType,
  formatTypeUserInProject,
  translateUserInProjectType
};
