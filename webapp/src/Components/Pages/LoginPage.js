// eslint-disable-next-line import/no-cycle
import {Redirect} from "../Router/Router";

// eslint-disable-next-line import/no-cycle
import Navbar from "../Navbar/Navbar";

// eslint-disable-next-line import/no-cycle
import {
  getUserSessionData,
  setUserSessionData,
  setUserStorageData
} from "../../utils/session";

const onUserLogin = async (userData) => {
  if (document.getElementById("stayconnected").checked) {
    setUserStorageData(userData)
  }
  else {
    setUserSessionData(userData);
  }
  // re-render the navbar for the authenticated user
  Navbar();
  Redirect("/");
};

async function login(e){
  e.preventDefault();
  let errorMessage= null;

  const email = document.querySelector("#input-email").value;
  const password = document.querySelector("#input-pwd").value;

  const userTemp = getUserSessionData();

  if (userTemp) {
    // re-render the navbar for the authenticated user
    Navbar();
    Redirect("/");
  } else {
    try {
      const options = {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify({
          email,
          password,
        }), // body data type must match "Content-Type" header
        headers: {
          "Content-Type": "application/json",
        },
      };

      const response = await fetch("/api/users/login", options); // fetch return a promise => we wait for the response

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
};

const LoginPage = () => {
  const main = document.querySelector('main');
  main.innerHTML = `
        <div class="page-login d-flex justify-content-center align-items-center">
          <div class="box-register d-flex justify-content-center align-items-center">
            <p class="btn-register">Inscription</p>
          </div>
          <div class="box-login d-flex justify-content-center align-items-center">
            <div class="box-in-login d-flex justify-content-center align-items-center flex-column">
              <h1>Connexion</h1>
              <div class="input-group mb-3">
                <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-envelope"></i></span>
                <input type="text" class="form-control" id="input-email" placeholder="Adresse email" aria-label="Adresse email" aria-describedby="basic-addon1">
              </div>
              <div class="input-group mb-3">
                <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-key"></i></span>
                <input type="password" class="form-control" id="input-pwd" placeholder="Mot de passe" aria-label="Mot de passe" aria-describedby="basic-addon1">
              </div>
              <div class="form-check mb-3">
                <input class="form-check-input" type="checkbox" value="" id="stayconnected">
                <label class="form-check-label" for="stayconnected">
                  Se souvenir de moi
                </label>
              </div>
              <h2 id="error-message">L'adresse email ou<br>le mot de passe est incorrect !</h2>
              <p class="btn-login" id="login-btn">Se connecter</p>
            </div>
          </div>
        </div>
    `;

    const registerBtn = document.querySelector(".btn-register");
    registerBtn.addEventListener('click', () => {
      Redirect("/register");
    });

    const allNav = document.querySelectorAll(".nav-btn");

    allNav.forEach((nav) => {
      const newNav = nav;
      newNav.style.boxShadow = "0px 0px 0px";
    });

    const logNav = document.getElementById("login");
    logNav.style.boxShadow = "8px 8px 0px var(red)";

    const loginBtn = document.getElementById("login-btn");

    loginBtn.addEventListener("click", login);
};


export default LoginPage;