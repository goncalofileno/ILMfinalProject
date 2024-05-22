const baseURL = "http://localhost:8080/projeto_ilm_final/rest/";

async function registerUser(email, password) {
   let user = {
      mail: email,
      password: password,
   };

   return await fetch(`${baseURL}user/register`, {
      method: "POST",
      headers: {
         Accept: "application/json",
         "Content-Type": "application/json",
      },
      body: JSON.stringify(user),
   });
}

async function getInterests() {
   return await fetch(`${baseURL}interest/all`, {
      method: "GET",
      headers: {
         Accept: "application/json",
         "Content-Type": "application/json",
      },
   });
}

async function getSkills() {
   return await fetch(`${baseURL}skill/all`, {
      method: "GET",
      headers: {
         Accept: "application/json",
         "Content-Type": "application/json",
      },
   });
}

async function getLabs() {
   return await fetch(`${baseURL}lab/all`, {
      method: "GET",
      headers: {
         Accept: "application/json",
         "Content-Type": "application/json",
      },
   });
}

async function checkUsername(username) {
   return await fetch(`${baseURL}user/checkUsername`, {
      method: "POST",
      headers: {
         Accept: "application/json",
         "Content-Type": "application/json",
         username: username,
      },
   });
}

export { registerUser, getInterests, getLabs, getSkills, checkUsername };
