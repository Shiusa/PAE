// eslint-disable-next-line import/no-cycle
import {Redirect} from "../Router/Router";
import showNavStyle from "../../utils/function";
import {setUserSessionData} from "../../utils/session";
import Navbar from "../Navbar/Navbar";

const RegisterPage = () => {
  const main = document.querySelector('main');
  main.innerHTML = ` 
        <div class="page-login d-flex justify-content-center align-items-center mb-4 mt-5">
          <div class="box-login d-flex justify-content-center align-items-center" id="box-register-left">
            <div class="box-in-login d-flex justify-content-center align-items-center flex-column">
              <h1>Inscription</h1>
              <div class="input-group mb-3">
                <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-user"></i></span>
                <input type="text" class="form-control" id="input-firstname" placeholder="Prénom" aria-label="Prénom" aria-describedby="basic-addon1">
              </div>
              <div class="input-group mb-3">
                <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-user"></i></span>
                <input type="text" class="form-control" id="input-lastname" placeholder="Nom" aria-label="Nom" aria-describedby="basic-addon1">
              </div>
              <div class="input-group mb-3">
                <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-envelope"></i></span>
                <input type="text" class="form-control" id="input-email" placeholder="Adresse email" aria-label="Adresse email" aria-describedby="basic-addon1">
              </div>
              <div class="input-group mb-3">
                <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-phone"></i></span>
                <input type="text" class="form-control" id="input-phone-number" placeholder="Téléphone" aria-label="Téléphone" aria-describedby="basic-addon1">
              </div>
              <div class="input-group mb-3">
                <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-key"></i></span>
                <input type="text" class="form-control" id="input-pwd" placeholder="Mot de passe" aria-label="Mot de passe" aria-describedby="basic-addon1">
              </div>
              <input type="text" id="input-role" value="student">
              <p class="btn-login" id="register-btn">S'inscrire</p>
              <h2 id="error-message">L'adresse email ou<br>le mot de passe est incorrect !</h2>
            </div>
          </div>
          <div class="box-register d-flex justify-content-center align-items-center" id="box-register-right">
            <p class="btn-register">Connexion</p>
          </div>
        </div>
  `;

  showNavStyle("register");

  const loginBtn = document.querySelector(".btn-register");
  loginBtn.addEventListener('click', () => {
    Redirect("/login");
  });

  const registerBtn = document.getElementById("register-btn");
  registerBtn.addEventListener("click", register);


};

async function register(e) {
  e.preventDefault();
  let errorMessage = null;

  const firstname = document.querySelector("#input-firstname").value;
  const lastname = document.querySelector("#input-lastname").value;
  const email = document.querySelector("#input-email").value;
  const phoneNumber = document.querySelector("#input-phone-number").value;
  const password = document.querySelector("#input-pwd").value;
  const role = document.querySelector("#input-role").value;

  try {
     const options = {
       method: "POST", // *GET, POST, PUT, DELETE, etc.
       body: JSON.stringify({
         email,
         lastname,
         firstname,
         phoneNumber,
         password,
         role,
       }), // body data type must match "Content-Type" header
       headers: {
         "Content-Type": "application/json",
       },
     };

    const response = await fetch("/api/users/register", options); // fetch return a promise => we wait for the response

    if (!response.ok) {
      throw new Error(
          `fetch error : ${  response.status  } : ${  response.statusText}`
      );
    }

    const user = await response.json(); // json() returns a promise => we wait for the data

    // eslint-disable-next-line no-use-before-define
    await onUserLogin(user);

  } catch (error) {
    errorMessage = document.getElementById("error-message");
    errorMessage.style.display="block";
  }
}

const onUserLogin = async (userData) => {
    setUserSessionData(userData);
  // re-render the navbar for the authenticated user
  Navbar();
  Redirect("/");
};


export default RegisterPage;