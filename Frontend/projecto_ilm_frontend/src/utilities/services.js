const baseURL = "http://localhost:8080/projeto_ilm_final/rest/";

async function checkUsername(username, auxiliarToken) {
  try {
    return await fetch(`${baseURL}user/checkUsername`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        username: username,
        Authorization: auxiliarToken,
      },
    });
  } catch (error) {
    console.error("Error during fetching username:", error);
  }
}

async function checkAuxiliarToken(auxiliarToken) {
  try {
    const response = await fetch(`${baseURL}user/checkAuxiliarToken`, {
      method: "GET",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: auxiliarToken,
      },
    });
    return response;
  } catch (error) {
    console.error("Error during fetching auxiliarToken:", error);
  }
}

async function checkEmail(email) {
  try {
    return await fetch(`${baseURL}user/checkEmail`, {
      method: "GET",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        email: email,
      },
    });
  } catch (error) {
    console.error("Error during fetching email:", error);
  }
}

async function registerUser(email, password) {
  let user = {
    mail: email,
    password: password,
  };

  try {
    return await fetch(`${baseURL}user/register`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(user),
    });
  } catch (error) {
    console.error("Error during fetching register:", error);
  }
}

async function getInterests(auxiliarToken) {
  try {
    return await fetch(`${baseURL}interest/all`, {
      method: "GET",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: auxiliarToken,
      },
    });
  } catch (error) {
    console.error("Error during fetching interests:", error);
  }
}

async function getSkills(auxiliarToken) {
  try {
    return await fetch(`${baseURL}skill/all`, {
      method: "GET",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: auxiliarToken,
      },
    });
  } catch (error) {
    console.error("Error during fetching skills:", error);
  }
}

async function getLabs(auxiliarToken) {
  try {
    return await fetch(`${baseURL}lab/`, {
      method: "GET",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: auxiliarToken,
      },
    });
  } catch (error) {
    console.error("Error during fetching labs:", error);
  }
}

async function getLabsWithSessionId() {
  try {
    return await fetch(`${baseURL}lab/all`, {
      method: "GET",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      credentials: "include",
    });
  } catch (error) {
    console.error("Error during fetching labs:", error);
  }
}

async function getProjectsFilters() {
  try {
    return await fetch(`${baseURL}project/filters`, {
      method: "GET",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      credentials: "include",
    });
  } catch (error) {
    console.error("Error during fetching projects filters:", error);
  }
}

async function getAllStatus() {
  try {
    return await fetch(`${baseURL}project/allStatus`, {
      method: "GET",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      credentials: "include",
    });
  } catch (error) {
    console.error("Error during fetching labs:", error);
  }
}

async function createProfile(userProfileDto, auxiliarToken) {
  try {
    const response = await fetch(`${baseURL}user/createProfile`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: auxiliarToken,
      },
      body: JSON.stringify(userProfileDto),
    });

    return response;
  } catch (error) {
    console.error("Error during creating profile:", error);
    throw error;
  }
}

async function uploadProjectPhoto(file, projectName) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = async function () {
      const base64data = reader.result;
      try {
        const response = await fetch(`${baseURL}project/photo/${projectName}`, {
          method: "PATCH",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include",
          body: JSON.stringify({ file: base64data }),
        });

        resolve(response);
      } catch (error) {
        console.error("Error verifying supplier:", error);
        reject(error);
      }
    };
  });
}

async function uploadProfilePicture(file, token) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = async function () {
      const base64data = reader.result;
      try {
        const response = await fetch(`${baseURL}user/uploadProfilePicture`, {
          method: "POST",
          headers: {
            Authorization: token,
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ file: base64data }),
        });

        resolve(response);
      } catch (error) {
        console.error("Error during uploading profile picture:", error);
        reject(error);
      }
    };
  });
}

async function forgetPassword(email) {
  try {
    const response = await fetch(`${baseURL}user/forgetPassword`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        email: email,
      },
    });

    return response;
  } catch (error) {
    console.error("Error during creating profile:", error);
    throw error;
  }
}

async function resetPassword(newPassword, auxiliarToken) {
  try {
    const response = await fetch(`${baseURL}user/resetPassword`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: auxiliarToken,
        newPassword: newPassword,
      },
    });

    return response;
  } catch (error) {
    console.error("Error during creating profile:", error);
    throw error;
  }
}

async function loginUser(email, password) {
  let user = {
    mail: email,
    password: password,
    userAgent: navigator.userAgent, // Captura o User-Agent do navegador
  };

  try {
    const response = await fetch(`${baseURL}user/login`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(user),
      credentials: "include", // Inclui os cookies na requisição
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const contentType = response.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
      const data = await response.json(); // Processa a resposta como JSON
      return { status: response.status, data: data }; // Retorna um objeto com o status e os dados
    } else {
      throw new Error("Response is not JSON");
    }
  } catch (error) {
    console.error("Error during fetching login:", error);
    // Retorna um objeto de erro com o status e a mensagem de erro
    return {
      status: error.response ? error.response.status : 500,
      error: error.message,
    };
  }
}

async function getUserProfileImage() {
  try {
    const response = await fetch(`${baseURL}user/profileImage`, {
      method: "GET",
      credentials: "include", // Inclui os cookies na requisição
    });
    if (response.ok) {
      const data = await response.json();
      return data.imageUrl; // Supondo que a resposta contenha o campo imageUrl
    } else {
      console.error("Failed to fetch user image");
      return null;
    }
  } catch (error) {
    console.error("Error fetching user image:", error);
    return null;
  }
}

async function loginWithToken(token) {
  let userAgent = navigator.userAgent; // Captura o User-Agent do navegador

  try {
    const response = await fetch(`${baseURL}user/loginWithToken`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ token, userAgent }),
      credentials: "include", // Inclui os cookies na requisição
    });

    return response;
  } catch (error) {
    console.error("Error during fetching login with token:", error);
  }
}

async function getHomeProjects() {
  try {
    const response = await fetch(`${baseURL}project/homeProjects`, {
      method: "GET",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    });

    return response;
  } catch (error) {
    console.error("Error getting home projects:", error);
    throw error;
  }
}

async function getTableProjects(
  page,
  lab,
  status,
  slotsAvailable,
  nameAsc,
  statusAsc,
  labAsc,
  startDateAsc,
  endDateAsc,
  keyword
) {
  try {
    const response = await fetch(
      `${baseURL}project/tableProjects?page=${page}&lab=${lab}&status=${status}&slotsAvailable=${slotsAvailable}&nameAsc=${nameAsc}&statusAsc=${statusAsc}&labAsc=${labAsc}&startDateAsc=${startDateAsc}&endDateAsc=${endDateAsc}&keyword=${keyword}`,
      {
        method: "GET",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    );

    return response;
  } catch (error) {
    console.error("Error getting table projects:", error);
    throw error;
  }
}

async function getMyProjectsTable(page, lab, status, keyword, type) {
  try {
    const response = await fetch(
      `${baseURL}project/myprojects?page=${page}&lab=${lab}&status=${status}&keyword=${keyword}&memberType=${type}`,
      {
        method: "GET",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    );

    return response;
  } catch (error) {
    console.error("Error getting table projects:", error);
    throw error;
  }
}

async function logoutUser() {
  try {
    const response = await fetch(`${baseURL}user/logout`, {
      method: "POST",
      credentials: "include", // Inclui os cookies na requisição
    });
    return response;
  } catch (error) {
    console.error("Error during fetching logout:", error);
  }
}

const getUserProfile = async (systemUsername) => {
  try {
    const response = await fetch(
      `http://localhost:8080/projeto_ilm_final/rest/user/profile/${systemUsername}`,
      {
        method: "GET",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    return { status: response.status, data: data };
  } catch (error) {
    console.error("Error fetching user profile:", error);
    return {
      status: error.response ? error.response.status : 500,
      error: error.message,
    };
  }
};

async function getUserEditProfile(systemUsername) {
  try {
    const response = await fetch(
      `${baseURL}user/editProfile/${systemUsername}`,
      {
        method: "GET",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    );
    return response;
  } catch (error) {
    console.error("Error fetching user edit profile:", error);
  }
}

async function changeUserPassword(currentPassword, newPassword) {
  try {
    const response = await fetch(`${baseURL}user/changePassword`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify({ currentPassword, newPassword }),
    });
    return response;
  } catch (error) {
    console.error("Error changing user password:", error);
  }
}

async function updateUserProfile(userProfileDto) {
  try {
    const response = await fetch(`${baseURL}user/updateProfile`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      credentials: "include", // Inclui cookies na requisição
      body: JSON.stringify(userProfileDto),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || "Error updating profile");
    }

    return response;
  } catch (error) {
    console.error("Error updating user profile:", error);
    throw error;
  }
}

async function uploadProfilePictureWithSession(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = async function () {
      const base64data = reader.result;
      try {
        const response = await fetch(`${baseURL}user/uploadProfilePicture`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include", // Inclui cookies na requisição
          body: JSON.stringify({ file: base64data }),
        });

        resolve(response);
      } catch (error) {
        console.error("Error during uploading profile picture:", error);
        reject(error);
      }
    };
  });
}

async function updatePassword(currentPassword, newPassword) {
  try {
    const response = await fetch(`${baseURL}user/updatePassword`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      credentials: "include", // Inclui os cookies na requisição
      body: JSON.stringify({ currentPassword, newPassword }),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || "Error updating password");
    }

    return response;
  } catch (error) {
    console.error("Error updating password:", error);
    throw error;
  }
}

async function getReceivedMessages(
  sessionId,
  page = 1,
  pageSize = 10,
  unread = false
) {
  try {
    const response = await fetch(
      `${baseURL}mail/received?page=${page}&pageSize=${pageSize}&unread=${unread}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Cookie: `session-id=${sessionId}`,
        },
        credentials: "include",
      }
    );
    return response.json();
  } catch (error) {
    console.error("Error fetching received messages:", error);
  }
}

async function getSentMessages(sessionId, page = 1, pageSize = 10) {
  try {
    const response = await fetch(
      `${baseURL}mail/sent?page=${page}&pageSize=${pageSize}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Cookie: `session-id=${sessionId}`,
        },
        credentials: "include",
      }
    );
    return response.json();
  } catch (error) {
    console.error("Error fetching sent messages:", error);
  }
}

async function markMailAsSeen(sessionId, mailId) {
  try {
    const response = await fetch(`${baseURL}mail/seen/${mailId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Cookie: `session-id=${sessionId}`,
      },
      credentials: "include",
    });
    return response;
  } catch (error) {
    console.error("Error marking mail as seen:", error);
  }
}

async function markMailAsDeleted(sessionId, mailId) {
  try {
    const response = await fetch(`${baseURL}mail/deleted/${mailId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Cookie: `session-id=${sessionId}`,
      },
      credentials: "include",
    });
    return response;
  } catch (error) {
    console.error("Error marking mail as deleted:", error);
  }
}

async function sendMail(sessionId, mailDto) {
  try {
    const response = await fetch(`${baseURL}mail/send`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Cookie: `session-id=${sessionId}`,
      },
      credentials: "include",
      body: JSON.stringify(mailDto),
    });
    return response;
  } catch (error) {
    console.error("Error sending mail:", error);
  }
}

async function getContacts(sessionId) {
  try {
    const response = await fetch(`${baseURL}mail/contacts`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Cookie: `session-id=${sessionId}`,
      },
      credentials: "include",
    });
    return response;
  } catch (error) {
    console.error("Error fetching contacts:", error);
  }
}

async function getUnreadNumber(sessionId) {
  try {
    const response = await fetch(`${baseURL}mail/unreadNumber`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Cookie: `session-id=${sessionId}`,
      },
      credentials: "include",
    });
    if (response.ok) {
      return await response.json();
    } else {
      console.error("Failed to fetch unread number:", response.statusText);
      return { unreadNumber: 0 }; // Retorna 0 em caso de falha
    }
  } catch (error) {
    console.error("Error fetching unread number:", error);
    return { unreadNumber: 0 }; // Retorna 0 em caso de erro
  }
}

async function searchMails(
  sessionId,
  query,
  page = 1,
  pageSize = 10,
  unread = false
) {
  try {
    const response = await fetch(
      `${baseURL}mail/search?query=${query}&page=${page}&pageSize=${pageSize}&unread=${unread}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Cookie: `session-id=${sessionId}`,
        },
        credentials: "include",
      }
    );
    return response.json();
  } catch (error) {
    console.error("Error searching mails:", error);
  }
}

async function searchSentMails(sessionId, query, page, pageSize) {
  try {
    const response = await fetch(
      `${baseURL}mail/searchSent?query=${query}&page=${page}&pageSize=${pageSize}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Cookie: `session-id=${sessionId}`,
        },
        credentials: "include",
      }
    );
    return response.json();
  } catch (error) {
    console.error("Error searching sent mails:", error);
  }
}

async function getUserProjects(sessionId, inviteeUsername) {
  try {
    const response = await fetch(
      `${baseURL}project/userOwnerProjectsToInvite?inviteeUsername=${inviteeUsername}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Cookie: `session-id=${sessionId}`,
        },
        credentials: "include",
      }
    );

    if (response.ok) {
      const data = await response.json();
      if (data.message) {
        return { success: false, message: data.message };
      } else {
        return { success: true, data };
      }
    } else {
      const errorMessage = await response.json();
      return { success: false, message: errorMessage.message };
    }
  } catch (error) {
    console.error("Error fetching user projects:", error);
    return { success: false, message: "An error occurred. Please try again." };
  }
}

async function inviteUserToProject(sessionId, projectName, systemUsername) {
  try {
    const response = await fetch(
      `${baseURL}project/inviteUser?projectName=${projectName}&systemUsername=${systemUsername}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    );

    return response;
  } catch (error) {
    console.error("Error inviting user to project:", error);
  }
}

async function getAllResources(
  page,
  brand,
  type,
  supplier,
  keyword,
  nameAsc,
  typeAsc,
  brandAsc,
  supplierAsc
) {
  try {
    const response = await fetch(
      `${baseURL}resource/getResources?page=${page}&brand=${brand}&type=${type}&supplier=${supplier}&keyword=${keyword}&nameAsc=${nameAsc}&typeAsc=${typeAsc}&brandAsc=${brandAsc}&supplierAsc=${supplierAsc}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    );

    return response;
  } catch (error) {
    console.error("Error receiving all resources:", error);
  }
}

async function getAllResourcesCreatingProject(
  page,
  brand,
  type,
  supplier,
  keyword,
  nameAsc,
  typeAsc,
  brandAsc,
  supplierAsc,
  rejectedIds
) {
  try {
    const response = await fetch(
      `${baseURL}resource/getResources?page=${page}&brand=${brand}&type=${type}&supplier=${supplier}&keyword=${keyword}&nameAsc=${nameAsc}&typeAsc=${typeAsc}&brandAsc=${brandAsc}&supplierAsc=${supplierAsc}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(rejectedIds),
      }
    );

    return response;
  } catch (error) {
    console.error("Error receiving all resources:", error);
  }
}

async function addInitialResources(projectSystemName, resourcesIds) {
  try {
    const response = await fetch(`${baseURL}project/${projectSystemName}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(resourcesIds),
    });

    return response;
  } catch (error) {
    console.error("Error receiving all resources:", error);
  }
}

async function getProjectResources(projectSystemName) {
  try {
    const response = await fetch(
      `${baseURL}resource/project/${projectSystemName}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    );

    return response;
  } catch (error) {
    console.error("Error receiving all resources:", error);
  }
}

async function getResourcesFilters(withNames, withTypes) {
  try {
    const response = await fetch(
      `${baseURL}resource/filters?withNames=${withNames}&withTypes=${withTypes}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    );

    return response;
  } catch (error) {
    console.error("Error receiving all resources:", error);
  }
}

async function getResource(id, supplierName) {
  try {
    const response = await fetch(`${baseURL}resource/${id}/${supplierName}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    return response;
  } catch (error) {
    console.error("Error receiving all resources:", error);
  }
}

async function createResource(
  name,
  description,
  type,
  brand,
  serialNumber,
  supplier,
  supplierContact,
  observations
) {
  let resourceCreationDto = {
    name: name,
    description: description,
    type: type,
    brand: brand,
    serialNumber: serialNumber,
    supplierName: supplier,
    supplierContact: supplierContact,
    observations: observations,
  };

  console.log(resourceCreationDto);

  try {
    const response = await fetch(`${baseURL}resource`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(resourceCreationDto),
    });

    return response;
  } catch (error) {
    console.error("Error receiving all resources:", error);
  }
}

async function editResource(
  id,
  name,
  description,
  type,
  brand,
  serialNumber,
  supplier,
  supplierContact,
  observations,
  oldSupplierName
) {
  let resourceDto = {
    id: id,
    name: name,
    description: description,
    type: type,
    brand: brand,
    serialNumber: serialNumber,
    supplierName: supplier,
    supplierContact: supplierContact,
    observation: observations,
    oldSupplierName: oldSupplierName,
  };

  try {
    const response = await fetch(`${baseURL}resource`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(resourceDto),
    });

    return response;
  } catch (error) {
    console.error("Error receiving all resources:", error);
  }
}

async function findSupplierContact(supplierName) {
  try {
    const response = await fetch(
      `${baseURL}supplier/contact?supplierName=${supplierName}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    );
    console.log(response);
    return response;
  } catch (error) {
    console.error("Error verifying supplier:", error);
  }
}

async function createProject(projectCreationDto) {
  console.log(projectCreationDto);
  try {
    const response = await fetch(`${baseURL}project`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(projectCreationDto),
    });

    return response;
  } catch (error) {
    console.error("Error verifying supplier:", error);
  }
}

async function getUnreadNotificationsCount(sessionId) {
  try {
    const response = await fetch(`${baseURL}notification/unreadCount`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      cookies: `session-id=${sessionId}`,
    });

    if (!response.ok) {
      throw new Error("Failed to fetch unread notifications count");
    }

    const data = await response.json();
    return data; // Directly returning the number
  } catch (error) {
    console.error("Error fetching unread notifications count:", error);
    return null;
  }
}

async function fetchNotifications(sessionId, page) {
  try {
    const response = await fetch(
      `${baseURL}notification/userNotifications?page=${page}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        cookies: `session-id=${sessionId}`,
      }
    );
    if (!response.ok) {
      throw new Error("Failed to fetch notifications");
    }
    return await response.json();
  } catch (error) {
    console.error("Error fetching notifications:", error);
    return [];
  }
}

async function respondToInvite(sessionId, projectName, response) {
  try {
    const endpoint = `${baseURL}project/respondToInvite?projectName=${encodeURIComponent(
      projectName
    )}&response=${response}`;
    const fetchResponse = await fetch(endpoint, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      cookies: `session-id=${sessionId}`,
    });

    if (!fetchResponse.ok) {
      const errorMessage = await fetchResponse.json();
      throw new Error(errorMessage.message);
    }

    return await fetchResponse.json();
  } catch (error) {
    console.error("Error responding to invite:", error);
    return { error: error.message };
  }
}

async function getProjectInfoPage(systemProjectName) {
  try {
    const response = await fetch(
      `${baseURL}project/projectInfoPage?projectName=${encodeURIComponent(
        systemProjectName
      )}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    );
    if (!response.ok) {
      throw new Error("Failed to fetch project info");
    }
    return await response.json();
  } catch (error) {
    console.error("Error fetching project info:", error);
    return { error: error.message };
  }
}

async function approveOrRejectProject(
  sessionId,
  projectSystemName,
  approve,
  reason
) {
  try {
    const endpoint = `${baseURL}project/approveOrRejectProject?projectSystemName=${encodeURIComponent(
      projectSystemName
    )}&approve=${approve}&reason=${encodeURIComponent(reason)}`;
    const fetchResponse = await fetch(endpoint, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    const responseJson = await fetchResponse.json();

    if (!fetchResponse.ok) {
      throw new Error(responseJson.message || "An error occurred");
    }

    return responseJson;
  } catch (error) {
    console.error("Error approving or rejecting project:", error);
    return { error: error.message };
  }
}

async function joinProject(sessionId, projectSystemName) {
  try {
    const endpoint = `${baseURL}project/joinProject?projectSystemName=${encodeURIComponent(
      projectSystemName
    )}`;
    const fetchResponse = await fetch(endpoint, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    const responseJson = await fetchResponse.json();

    if (!fetchResponse.ok) {
      throw new Error(responseJson.message || "An error occurred");
    }

    return responseJson;
  } catch (error) {
    console.error("Error joining project:", error);
    return { error: error.message };
  }
}

async function cancelProject(sessionId, projectSystemName, reason) {
  try {
    const endpoint = `${baseURL}project/cancelProject?projectSystemName=${encodeURIComponent(
      projectSystemName
    )}&reason=${encodeURIComponent(reason)}`;
    const fetchResponse = await fetch(endpoint, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    const responseJson = await fetchResponse.json();

    if (!fetchResponse.ok) {
      throw new Error(responseJson.message || "An error occurred");
    }

    return responseJson;
  } catch (error) {
    console.error("Error canceling project:", error);
    return { error: error.message };
  }
}

async function markReasonAsRead(sessionId, projectSystemName) {
  try {
    const endpoint = `${baseURL}project/markReasonAsRead?projectSystemName=${encodeURIComponent(
      projectSystemName
    )}`;
    const fetchResponse = await fetch(endpoint, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    const responseJson = await fetchResponse.json();

    if (!fetchResponse.ok) {
      throw new Error(responseJson.message || "An error occurred");
    }

    return responseJson;
  } catch (error) {
    console.error("Error marking reason as read:", error);
    return { error: error.message };
  }
}

async function changeProjectState(
  sessionId,
  projectSystemName,
  newState,
  reason
) {
  try {
    const endpoint = `${baseURL}project/changeProjectState?projectSystemName=${encodeURIComponent(
      projectSystemName
    )}&newState=${encodeURIComponent(newState)}&reason=${encodeURIComponent(
      reason || ""
    )}`;
    const fetchResponse = await fetch(endpoint, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    const responseJson = await fetchResponse.json();

    if (!fetchResponse.ok) {
      throw new Error(responseJson.message || "An error occurred");
    }

    return responseJson;
  } catch (error) {
    console.error("Error changing project state:", error);
    return { error: error.message };
  }
}

async function getProjectLogsAndNotes(sessionId, projectSystemName) {
  try {
    const endpoint = `${baseURL}log/logsAndNotes?systemProjectName=${encodeURIComponent(
      projectSystemName
    )}`;
    const fetchResponse = await fetch(endpoint, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    const responseJson = await fetchResponse.json();

    if (!fetchResponse.ok) {
      throw new Error(responseJson.message || "An error occurred");
    }

    return responseJson;
  } catch (error) {
    console.error("Error fetching logs and notes:", error);
    return { error: error.message };
  }
}

async function markAsDone(sessionId, noteId, done) {
  try {
    const endpoint = `${baseURL}note/markAsDone?id=${noteId}&done=${done}`;
    const fetchResponse = await fetch(endpoint, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    if (!fetchResponse.ok) {
      const responseJson = await fetchResponse.json();
      throw new Error(responseJson.message || "An error occurred");
    }

    return { success: true };
  } catch (error) {
    console.error("Error marking note as done:", error);
    return { error: error.message };
  }
}

async function createNote(sessionId, noteDto, systemProjectName) {
  try {
    const endpoint = `${baseURL}note/create?systemProjectName=${encodeURIComponent(
      systemProjectName
    )}`;
    const fetchResponse = await fetch(endpoint, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(noteDto),
    });

    // Ensure response is valid JSON
    const responseText = await fetchResponse.text();
    let responseJson;
    try {
      responseJson = JSON.parse(responseText);
    } catch (e) {
      throw new Error("Invalid JSON response");
    }

    if (!fetchResponse.ok) {
      throw new Error(responseJson.error || "An error occurred");
    }

    return responseJson;
  } catch (error) {
    console.error("Error creating note:", error);
    return { error: error.message };
  }
}

async function getTasksSuggestions(sessionId, systemProjectName) {
  try {
    const endpoint = `${baseURL}task/tasksSuggestions?systemProjectName=${encodeURIComponent(
      systemProjectName
    )}`;
    const fetchResponse = await fetch(endpoint, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    const responseJson = await fetchResponse.json();

    if (!fetchResponse.ok) {
      throw new Error(responseJson.message || "An error occurred");
    }

    return responseJson;
  } catch (error) {
    console.error("Error fetching task suggestions:", error);
    return { error: error.message };
  }
}

async function getUserProjectCreation(
  systemProjectName,
  rejectedUsers,
  currentPage,
  lab,
  keyword
) {
  let RejectedIdsDto = {
    rejectedIds: rejectedUsers,
  };

  try {
    console.log("RejectedUsersDto", RejectedIdsDto);
    const response = await fetch(
      `${baseURL}user/userProjectCreation/${systemProjectName}?page=${currentPage}&lab=${lab}&keyword=${keyword}`,
      {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(RejectedIdsDto),
      }
    );

    return response;
  } catch (error) {
    console.error("Error during creating profile:", error);
    throw error;
  }
}

async function addMembers(systemProjectName, projectCreationMembersDto) {
  try {
    const response = await fetch(
      `${baseURL}project/members/${systemProjectName}`,
      {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(projectCreationMembersDto),
      }
    );

    return response;
  } catch (error) {
    console.error("Error during creating profile:", error);
    throw error;
  }
}

async function checkProjectName(projectName) {
  try {
    const response = await fetch(`${baseURL}project/checkName/${projectName}`, {
      method: "GET",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    return response;
  } catch (error) {
    console.error("Error during creating profile:", error);
    throw error;
  }
}
async function getChatPage(sessionId, projectSystemName) {
  try {
    const endpoint = `${baseURL}message/chatPage?projectSystemName=${encodeURIComponent(
      projectSystemName
    )}`;
    const fetchResponse = await fetch(endpoint, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    const responseJson = await fetchResponse.json();
    console.log("API Response:", responseJson); // Adicione este log para verificar a resposta

    if (!fetchResponse.ok) {
      throw new Error(responseJson.message || "An error occurred");
    }

    return responseJson;
  } catch (error) {
    console.error("Error fetching chat messages:", error);
    return { error: error.message, messages: [], projectMembers: [] };
  }
}

async function sendChatMessage(sessionId, projectSystemName, messageContent) {
  try {
    const endpoint = `${baseURL}message/sendMessage?projectSystemName=${encodeURIComponent(
      projectSystemName
    )}`;
    const messageDto = {
      message: messageContent,
    };

    const fetchResponse = await fetch(endpoint, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(messageDto),
    });

    if (!fetchResponse.ok) {
      const responseJson = await fetchResponse.json();
      throw new Error(responseJson.message || "An error occurred");
    }

    return { message: messageContent, date: new Date().toISOString() };
  } catch (error) {
    console.error("Error sending chat message:", error);
    return { error: error.message };
  }
}

async function markNotificationAsClicked(sessionId, notificationIds) {
  try {
    const endpoint = `${baseURL}notification/markMessageNotificationClicked`;

    const fetchResponse = await fetch(endpoint, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(notificationIds),
    });

    if (!fetchResponse.ok) {
      const responseJson = await fetchResponse.json();
      throw new Error(responseJson.message || "An error occurred");
    }

    return { success: true };
  } catch (error) {
    console.error("Error marking notifications as clicked:", error);
    return { error: error.message };
  }
}

async function getProjectMembersPage(sessionId, projectSystemName) {
  try {
    const endpoint = `${baseURL}project/getProjectMembersPage?projectSystemName=${encodeURIComponent(
      projectSystemName
    )}`;

    const fetchResponse = await fetch(endpoint, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    if (!fetchResponse.ok) {
      const responseJson = await fetchResponse.json();
      throw new Error(responseJson.message || "An error occurred");
    }

    return await fetchResponse.json();
  } catch (error) {
    console.error("Error fetching project members page:", error);
    return { error: error.message };
  }
}

async function removeUserFromProject(
  sessionId,
  projectSystemName,
  userId,
  reason
) {
  try {
    const endpoint = `${baseURL}project/removeUserFromProject?projectSystemName=${encodeURIComponent(
      projectSystemName
    )}&userId=${userId}`;

    const fetchResponse = await fetch(endpoint, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(reason),
    });

    if (!fetchResponse.ok) {
      const responseJson = await fetchResponse.json();
      throw new Error(responseJson.message || "An error occurred");
    }

    return await fetchResponse.json();
  } catch (error) {
    console.error("Error removing user from project:", error);
    return { error: error.message };
  }
}

async function changeUserInProjectType(
  sessionId,
  projectSystemName,
  userId,
  newType
) {
  try {
    const endpoint = `${baseURL}project/changeUserInProjectType?projectSystemName=${encodeURIComponent(
      projectSystemName
    )}&userId=${userId}&newType=${newType}`;

    const fetchResponse = await fetch(endpoint, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    if (!fetchResponse.ok) {
      const responseJson = await fetchResponse.json();
      throw new Error(responseJson.message || "An error occurred");
    }

    return await fetchResponse.json();
  } catch (error) {
    console.error("Error changing user type in project:", error);
    return { error: error.message };
  }
}

async function respondToApplication(
  sessionId,
  projectSystemName,
  userId,
  response,
  reason = ""
) {
  try {
    const endpoint = `${baseURL}project/respondToApplication?projectSystemName=${encodeURIComponent(
      projectSystemName
    )}&userId=${userId}&response=${response}`;
    const fetchResponse = await fetch(endpoint, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(reason),
    });

    if (!fetchResponse.ok) {
      const responseJson = await fetchResponse.json();
      throw new Error(responseJson.message || "An error occurred");
    }

    return { success: true };
  } catch (error) {
    console.error("Error responding to application:", error);
    return { error: error.message };
  }
}

async function removeInvitation(sessionId, projectSystemName, userId) {
  try {
    const response = await fetch(
      `${baseURL}project/removeInvitation?projectSystemName=${projectSystemName}&userId=${userId}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    );

    return response.json();
  } catch (error) {
    console.error("Error removing invitation:", error);
  }
}

async function getProjectDetails(systemProjectName) {
  try {
    const response = await fetch(
      `${baseURL}project/details/${systemProjectName}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    );

    if (!response.ok) {
      throw new Error("Failed to fetch project details");
    }

    return response.json();
  } catch (error) {
    console.error("Error fetching project details:", error);
    return null;
  }
}

async function updateProject(projectUpdateDto, systemProjectName) {
  try {
    const response = await fetch(
      `${baseURL}project/updateProject/${systemProjectName}`,
      {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(projectUpdateDto),
      }
    );

    if (!response.ok) {
      const responseJson = await response.json();
      throw new Error(responseJson.message || "An error occurred");
    }

    return { success: true };
  } catch (error) {
    console.error("Error updating project:", error);
    return { error: error.message };
  }
}

const getTasksPage = async (sessionId, systemProjectName) => {
  console.log(`Fetching tasks for project: ${systemProjectName}`);

  try {
    const response = await fetch(
      `${baseURL}task/tasksPage?systemProjectName=${encodeURIComponent(
        systemProjectName
      )}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    );

    if (!response.ok) {
      throw new Error("HTTP error!");
    }

    return await response.json();
  } catch (error) {
    console.error("Error fetching tasks:", error);
    return { error: error.message };
  }
};

const updateTask = async (updateTaskDto) => {
  try {
    const response = await fetch(`${baseURL}task/updateTask`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(updateTaskDto),
    });

    if (!response.ok) {
      const responseJson = await response.json();
      throw new Error(responseJson.message || "An error occurred");
    }

    return { success: true };
  } catch (error) {
    console.error("Error updating task:", error);
    return { error: error.message };
  }
};

const createTask = async (newTaskDto) => {
  try {
    const response = await fetch(`${baseURL}task/addTask`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(newTaskDto),
    });

    if (!response.ok) {
      const responseJson = await response.json();
      throw new Error(responseJson.message || "An error occurred");
    }

    return { success: true };
  } catch (error) {
    console.error("Error creating task:", error);
    return { error: error.message };
  }
};

const deleteTask = async (updateTaskDto) => {
  try {
    const response = await fetch(`${baseURL}task/deleteTask`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(updateTaskDto),
    });

    if (!response.ok) {
      const responseJson = await response.json();
      throw new Error(responseJson.message || "An error occurred");
    }

    return { success: true };
  } catch (error) {
    console.error("Error deleting task:", error);
    return { error: error.message };
  }
};

async function getAppStatistics() {
  try {
    const response = await fetch(`${baseURL}statistics`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    return response;
  } catch (error) {
    console.error("Error receiving all resources:", error);
  }
}

const leaveProject = async (sessionId, systemProjectName, reason) => {
  try {
    const response = await fetch(`${baseURL}project/leaveProject?projectSystemName=${systemProjectName}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(reason),
    });
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error leaving project:", error);
    return { error: "An error occurred while leaving the project." };
  }
};

const validateSession = async (sessionId) => {
  try {
    const response = await fetch(`${baseURL}user/validateSession`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Cookie: `session-id=${sessionId}`,
      },
      credentials: "include",
    });

    if (response.status === 200) {
      console.log("Session is valid");
      return true;
    } else {
      console.log("Session is invalid");
      return false;
    }
  } catch (error) {
    console.error("Error validating session:", error);
    return false;
  }
};

export {
  registerUser,
  getInterests,
  getLabs,
  getSkills,
  checkUsername,
  checkAuxiliarToken,
  createProfile,
  uploadProfilePicture,
  checkEmail,
  loginUser,
  forgetPassword,
  resetPassword,
  getHomeProjects,
  getUserProfileImage,
  loginWithToken,
  logoutUser,
  getUserProfile,
  getUserEditProfile,
  updateUserProfile,
  changeUserPassword,
  uploadProfilePictureWithSession,
  updatePassword,
  getReceivedMessages,
  getSentMessages,
  markMailAsSeen,
  markMailAsDeleted,
  sendMail,
  getContacts,
  getUnreadNumber,
  searchMails,
  getTableProjects,
  getLabsWithSessionId,
  getAllStatus,
  searchSentMails,
  getUserProjects,
  inviteUserToProject,
  getAllResources,
  getUnreadNotificationsCount,
  fetchNotifications,
  respondToInvite,
  getProjectInfoPage,
  approveOrRejectProject,
  joinProject,
  cancelProject,
  markReasonAsRead,
  changeProjectState,
  getResourcesFilters,
  createResource,
  getMyProjectsTable,
  getProjectsFilters,
  findSupplierContact,
  getProjectLogsAndNotes,
  getResource,
  editResource,
  markAsDone,
  createNote,
  createProject,
  uploadProjectPhoto,
  getUserProjectCreation,
  getTasksSuggestions,
  sendChatMessage,
  getChatPage,
  markNotificationAsClicked,
  addMembers,
  checkProjectName,
  getProjectMembersPage,
  removeUserFromProject,
  changeUserInProjectType,
  respondToApplication,
  removeInvitation,
  getProjectDetails,
  updateProject,
  getTasksPage,
  updateTask,
  createTask,
  deleteTask,
  getAllResourcesCreatingProject,
  addInitialResources,
  getProjectResources,
  leaveProject,
  validateSession,
  getAppStatistics,
};
