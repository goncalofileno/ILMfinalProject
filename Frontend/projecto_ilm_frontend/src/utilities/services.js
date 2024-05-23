const baseURL = "https://localhost:8443/projeto_ilm_final/rest/";

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

async function uploadProfilePicture(profilePicture, auxiliarToken) {
   const formData = new FormData();
   formData.append("profilePicture", profilePicture);

   try {
      const response = await fetch(`${baseURL}user/uploadProfilePicture`, {
         method: "POST",
         headers: {
            Authorization: auxiliarToken,
         },
         body: formData,
      });

      return response;
   } catch (error) {
      console.error("Error during uploading profile picture:", error);
      throw error;
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
};
