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
       return { status: error.response ? error.response.status : 500, error: error.message };
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
     const response = await fetch(`http://localhost:8080/projeto_ilm_final/rest/user/profile/${systemUsername}`, {
       method: 'GET',
       credentials: 'include',
       headers: {
         'Content-Type': 'application/json'
       }
     });
 
     if (!response.ok) {
       throw new Error(`HTTP error! status: ${response.status}`);
     }
 
     const data = await response.json();
     return { status: response.status, data: data };
   } catch (error) {
     console.error("Error fetching user profile:", error);
     return { status: error.response ? error.response.status : 500, error: error.message };
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
   getUserProfileImage,
   loginWithToken,
   logoutUser,
   getUserProfile, // Exporta a nova função
 };
 