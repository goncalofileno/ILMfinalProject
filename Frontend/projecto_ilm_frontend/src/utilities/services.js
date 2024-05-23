const baseURL = "http://localhost:8080/projeto_ilm_final/rest/";

async function checkUsername(username) {
   try {
      return await fetch(`${baseURL}user/checkUsername`, {
         method: "POST",
         headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
            username: username,
         },
      });
   } catch (error) {
      console.error("Error during fetching username:", error);
   }
}

async function checkAuxiliarToken(auxiliarToken) {
   try {
      return await fetch(`${baseURL}user/checkAuxiliarToken`, {
         method: "GET",
         headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
            auxiliarToken: auxiliarToken,
         },
      });
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

async function getInterests() {
   try {
      return await fetch(`${baseURL}interest/all`, {
         method: "GET",
         headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
         },
      });
   } catch (error) {
      console.error("Error during fetching interests:", error);
   }
}

async function getSkills() {
   try {
      return await fetch(`${baseURL}skill/all`, {
         method: "GET",
         headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
         },
      });
   } catch (error) {
      console.error("Error during fetching skills:", error);
   }
}

async function getLabs() {
   try {
      return await fetch(`${baseURL}lab/all`, {
         method: "GET",
         headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
         },
      });
   } catch (error) {
      console.error("Error during fetching labs:", error);
   }
}

export { registerUser, getInterests, getLabs, getSkills, checkUsername, checkAuxiliarToken, checkEmail };
