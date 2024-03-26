// eslint-disable-next-line import/no-cycle
import {Redirect} from "../Router/Router";
import {showNavStyle} from "../../utils/function";

const RegisterPage = () => {
  const main = document.querySelector('main');
  main.innerHTML = ` 
        <div class="page-login d-flex justify-content-center align-items-center mb-4 mt-5">
          <div class="box-login d-flex justify-content-center align-items-center" id="box-register-left">
            <div class="box-in-login d-flex justify-content-center align-items-center flex-column">
              <h1>Inscription</h1>
              <div class="input-group mb-3">
                <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-user"></i></span>
                <input type="text" class="form-control" placeholder="Prénom" aria-label="Prénom" aria-describedby="basic-addon1">
              </div>
              <div class="input-group mb-3">
                <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-user"></i></span>
                <input type="text" class="form-control" placeholder="Nom" aria-label="Nom" aria-describedby="basic-addon1">
              </div>
              <div class="input-group mb-3">
                <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-envelope"></i></span>
                <input type="text" class="form-control" placeholder="Adresse email" aria-label="Adresse email" aria-describedby="basic-addon1">
              </div>
              <div class="input-group mb-3">
                <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-phone"></i></span>
                <input type="text" class="form-control" placeholder="Téléphone" aria-label="Téléphone" aria-describedby="basic-addon1">
              </div>
              <div class="input-group mb-3">
                <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-key"></i></span>
                <input type="text" class="form-control" placeholder="Mot de passe" aria-label="Mot de passe" aria-describedby="basic-addon1">
              </div>
              <p class="btn-login">S'inscrire</p>
            </div>
          </div>
          <div class="box-register d-flex justify-content-center align-items-center" id="box-register-right">
            <p class="btn-register">Connexion</p>
          </div>
        </div>
  `;

  showNavStyle("register");

  const registerBtn = document.querySelector(".btn-register");
  registerBtn.addEventListener('click', () => {
    Redirect("/login");
  });


};


export default RegisterPage;