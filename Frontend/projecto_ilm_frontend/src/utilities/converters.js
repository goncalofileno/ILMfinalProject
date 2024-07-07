import Cookies from "js-cookie";

// Define mappings for English
const labMappingsEn = {
  VILA_REAL: "Vila Real",
};

const statusMappingsEn = {
  IN_PROGRESS: "IN PROGRESS",
};

const statusDropDownMappingsEn = {
  IN_PROGRESS: "In Progress",
};

const userInProjectTypeMappingsEn = {
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

const taskStatusMappingsEn = {
  PLANNED: "Planned",
  IN_PROGRESS: "In Progress",
  DONE: "Done",
};

// Define mappings for Portuguese
const labMappingsPt = {
  VILA_REAL: "Vila Real",
};

const statusMappingsPt = {
  IN_PROGRESS: "EM PROGRESSO",
};

const statusDropDownMappingsPt = {
  IN_PROGRESS: "Em Progresso",
  PLANNING: "Em Planeamento",
  FINISHED: "Concluído",
  CANCELED: "Cancelado",
  READY: "Pronto",
  APPROVED: "Aprovado",
};

const userInProjectTypeMappingsPt = {
  CREATOR: "Criador",
  MANAGER: "Manager",
  MEMBER: "Membro",
  MEMBER_BY_INVITATION: "Membro por Convite",
  MEMBER_BY_APPLIANCE: "Membro por Candidatura",
  PENDING_BY_APPLIANCE: "Pendente por Candidatura",
  PENDING_BY_INVITATION: "Pendente por Convite",
  EXMEMBER: "Ex-Membro",
  ADMIN: "Administrador",
  GUEST: "Convidado",
};

const taskStatusMappingsPt = {
  PLANNED: "Planejado",
  IN_PROGRESS: "Em Progresso",
  DONE: "Concluído",
};

// Define mappings for project states
const stateProjectMappingsEn = {
  PLANNING: "Planning",
  READY: "Ready",
  APPROVED: "Approved",
  IN_PROGRESS: "In Progress",
  CANCELED: "Canceled",
  FINISHED: "Finished",
};

const stateProjectMappingsPt = {
  PLANNING: "Em Planeamento",
  READY: "Pronto",
  APPROVED: "Aprovado",
  IN_PROGRESS: "Em Progresso",
  CANCELED: "Cancelado",
  FINISHED: "Concluído",
};

const resourceTypeMappingsEn = {
  RESOURCE: "Resource",
  COMPONENT: "Component",
};

const resourceTypeMappingsPt = {
  RESOURCE: "Recurso",
  COMPONENT: "Componente",
};

// Function to get the current language from cookies
function getCurrentLanguage() {
  return Cookies.get("user-language") || "ENGLISH";
}

// Format functions with language checks
function formatLab(lab) {
  const language = getCurrentLanguage();
  const mappings = language === "PORTUGUESE" ? labMappingsPt : labMappingsEn;
  return mappings[lab] || lab.charAt(0) + lab.slice(1).toLowerCase();
}

function formatStatus(status) {
  const language = getCurrentLanguage();
  const mappings = language === "PORTUGUESE" ? statusMappingsPt : statusMappingsEn;
  return mappings[status] || status;
}

function formatStatusDropDown(status) {
  const language = getCurrentLanguage();
  const mappings = language === "PORTUGUESE" ? statusDropDownMappingsPt : statusDropDownMappingsEn;
  return mappings[status] || status.charAt(0) + status.slice(1).toLowerCase();
}

const formatSkill = (skill) => {
  return skill
    .toLowerCase()
    .split(" ")
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
    .join(" ");
};

function formatResourceType(type) {
  const language = getCurrentLanguage();
  const mappings = language === "PORTUGUESE" ? resourceTypeMappingsPt : resourceTypeMappingsEn;
  return mappings[type] || type.charAt(0) + type.slice(1).toLowerCase();
}

function formatTypeUserInProject(type) {
  const language = getCurrentLanguage();
  const mappings = language === "PORTUGUESE" ? userInProjectTypeMappingsPt : userInProjectTypeMappingsEn;
  return mappings[type] || type;
}

function translateUserInProjectType(type) {
  const language = getCurrentLanguage();
  const mappings = language === "PORTUGUESE" ? userInProjectTypeMappingsPt : userInProjectTypeMappingsEn;
  return mappings[type] || type;
}

function formatTypeUserInProjectInMaiusculas(type) {
  const language = getCurrentLanguage();
  const mappings = language === "PORTUGUESE" ? userInProjectTypeMappingsPt : userInProjectTypeMappingsEn;
  return mappings[type] || type.charAt(0) + type.slice(1).toLowerCase();
}

function formatTaskStatus(status) {
  const language = getCurrentLanguage();
  const mappings = language === "PORTUGUESE" ? taskStatusMappingsPt : taskStatusMappingsEn;
  return mappings[status] || status.charAt(0) + status.slice(1).toLowerCase();
}

// Function to format project states with language checks
function formatProjectState(state) {
  const language = getCurrentLanguage();
  const mappings = language === "PORTUGUESE" ? stateProjectMappingsPt : stateProjectMappingsEn;
  return mappings[state] || state.charAt(0) + state.slice(1).toLowerCase();
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
  formatProjectState,
  formatTypeUserInProjectInMaiusculas,
};
