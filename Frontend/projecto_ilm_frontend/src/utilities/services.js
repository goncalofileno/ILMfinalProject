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
  console.log("Email: ", email);
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
    return await fetch(`${baseURL}lab/all`, {
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

async function uploadProfilePicture(file, token) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = async function () {
      const base64data = reader.result;
      console.log("Base64 data: ", base64data);

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
      console.log("Base64 data: ", base64data);

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

async function getReceivedMessages(sessionId, page = 1, pageSize = 10) {
  try {
    const response = await fetch(
      `${baseURL}mail/received?page=${page}&pageSize=${pageSize}`,
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

async function searchMails(sessionId, query, page = 1, pageSize = 10) {
  try {
    const response = await fetch(
      `${baseURL}mail/search?query=${query}&page=${page}&pageSize=${pageSize}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Cookie: `session-id=${sessionId}`,
        },
        credentials: "include",
      }
    );
    return response.json(); // Assumindo que a resposta JSON será um objeto com 'mails' e 'totalMails'
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

async function getUserProjects(sessionId) {
  try {
    const response = await fetch(
      `${baseURL}project/userOwnerProjectsToInvite`,
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
      return await response.json();
    } else {
      console.error("Failed to fetch user projects:", response.statusText);
      return [];
    }
  } catch (error) {
    console.error("Error fetching user projects:", error);
    return [];
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

async function getAllResources() {
  try {
    const response = await fetch(`${baseURL}resource`, {
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

async function getAllResourcesTypes() {
  try {
    const response = await fetch(`${baseURL}resource/types`, {
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

async function getAllSuppliersNames() {
  try {
    const response = await fetch(`${baseURL}supplier/names`, {
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

async function getAllBrands() {
  try {
    const response = await fetch(`${baseURL}resource/brands`, {
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
  getAllResourcesTypes,
  getAllSuppliersNames,
  getAllBrands,
};
