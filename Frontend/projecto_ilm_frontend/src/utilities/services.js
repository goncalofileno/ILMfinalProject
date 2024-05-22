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

export { registerUser };
