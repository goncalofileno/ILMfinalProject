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

export { formatLab, formatStatus, formatStatusDropDown, formatSkill };
