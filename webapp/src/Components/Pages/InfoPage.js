import {awaitFront} from "../../utils/function";
import {
  getAuthenticatedUser,
  getLocalUser,
  getToken,
  setAuthenticatedUser,
} from "../../utils/session";
import Navbar from "../Navbar/Navbar";
import Navigate from "../../utils/Navigate";

const InfoPage = async () => {

  const main = document.querySelector('main');

  awaitFront();

  const loggedUser = await getAuthenticatedUser();
  setAuthenticatedUser(loggedUser);
  Navbar();
  const userToken = getToken();
  const localUser = getLocalUser();
  if (!userToken) {
    Navigate('/');
    return;
  }

  const readUserInfo = async () => {
    const options = {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': userToken
      }
    }
    const response = await fetch(`api/users/${localUser.id}`, options);

    if (!response.ok) {
      throw new Error(
          `fetch error : ${response.status} : ${response.statusText}`);
    }

    const userInfo = await response.json();
    return userInfo;
  };

  const userInfoID = await readUserInfo();

  const info = ` 
        <h1 class="">Vos informations</h1>
        <div class="mt-1 mb-1 info-line"></div>
        <h4 class="mt-3">Année académique : ${userInfoID.schoolYear}</h2>
        <h5 class="mt-1 mb-4">${userInfoID.email}</h3>
        <div class="input-group mb-3">
            <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-user"></i></span>
            <input readonly type="text" class="form-control" id="input-firstname" value="${userInfoID.firstname}" placeholder="Prénom" aria-label="Prénom" aria-describedby="basic-addon1">
        </div>
        <div class="input-group mb-3">
            <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-user"></i></span>
            <input readonly type="text" class="form-control" id="input-lastname" value="${userInfoID.lastname}" placeholder="Nom" aria-label="Nom" aria-describedby="basic-addon1">
        </div>
        <div class="input-group mb-3">
            <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-phone"></i></span>
            <input type="text" class="form-control" id="input-phone" value="${userInfoID.phoneNumber}" placeholder="Téléphone" aria-label="Téléphone" aria-describedby="basic-addon1"> 
        </div>
        <h2 id="good-message"></h2>
        <p class="btn btn-primary mt-1" id="btn-save-phone-number">Enregistrer les modifications</p>
        <p class="btn btn-outline-primary mt-2" id="btn-change-pwd">Modifier mon mot de passe</p>    
    `;

  const saveUserInfo = async (e) => {
    e.preventDefault();
    const user = await getAuthenticatedUser();
    setAuthenticatedUser(user);
    Navbar();
    const phoneNumber = document.querySelector("#input-phone").value;
    const options = {
      method: "POST",
      body: JSON.stringify({
        "id": userInfoID.id,
        "email": userInfoID.email,
        "lastname": userInfoID.lastname,
        "firstname": userInfoID.firstname,
        "phoneNumber": phoneNumber,
        "registrationDate": userInfoID.registrationDate,
        "schoolYear": userInfoID.schoolYear,
        "role": userInfoID.role,
        "version": userInfoID.version
      }),
      headers: {
        "Content-Type": "application/json",
        'Authorization': user.token
      },
    };

    const response = await fetch('/api/users/editUser', options);

    if (!response.ok) {
      throw new Error(
          `fetch error : ${response.status} : ${response.statusText}`);
    }

    const savedUserInfo = await response.json();
    if (savedUserInfo) {
      const error = document.querySelector("#good-message");
      error.innerHTML = "Changement effectué.";
      error.style.display = "block";
    }

    return savedUserInfo;
  }

  const saveUserPwd = async (e) => {
    e.preventDefault();
    const user = await getAuthenticatedUser();
    setAuthenticatedUser(user);
    Navbar();
    const oldPassword = document.querySelector('#input-old-pwd').value;
    const newPassword = document.querySelector('#input-new-pwd').value;
    const repeatedPassword = document.querySelector(
        '#input-new-pwd-verif').value;
    const options = {
      method: "POST",
      body: JSON.stringify({
        "id": userInfoID.id,
        "oldPassword": oldPassword,
        "newPassword": newPassword,
        "repeatedPassword": repeatedPassword
      }),
      headers: {
        "Content-Type": "application/json",
        'Authorization': user.token
      },
    };

    const response = await fetch('/api/users/editPassword', options);

    if (!response.ok) {
      throw new Error(
          `fetch error : ${response.status} : ${response.statusText}`);
    }

    const savedUserPassword = await response.json();
    if (savedUserPassword) {
      const error = document.querySelector("#good-message");
      error.innerHTML = "Vous avez modifier votre mot de passe";
      error.style.display = "block";
      Navigate("/dashboard");
    }

    return savedUserPassword;
  }
  main.innerHTML = `        
        <div class="d-flex justify-content-center align-items-center mt-5 mb-5">
          <div class="info-square2"></div>
          <div class="info-square1"></div>
          <div class="info-container d-flex flex-column justify-content-center align-items-center">            
          </div>
        </div>
    `;

  const infoContainer = document.querySelector(".info-container");

  refresh();

  function refresh() {
    infoContainer.innerHTML = info;
    const btnChangePwd = document.getElementById("btn-change-pwd");
    btnChangePwd.addEventListener('click', () => {
      infoContainer.innerHTML = `
                <i id="btn-back" class="fa-solid fa-circle-arrow-left" title="Retour"></i>
                <h1>Modifier mon mot de passe</h1>
                <div class="mt-3 mb-2 info-line"></div>
                <div class="input-group mb-4 mt-5">
                    <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-unlock"></i></span>
                    <input type="password" class="form-control" id="input-old-pwd" placeholder="Mot de passe actuel" aria-label="Prénom" aria-describedby="basic-addon1">
                </div>
                <div class="input-group mb-3">
                    <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-key"></i></span>
                    <input type="password" class="form-control" id="input-new-pwd" placeholder="Nouveau mot de passe" aria-label="Prénom" aria-describedby="basic-addon1">
                </div>
                <div class="input-group mb-3">
                    <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-repeat"></i></span>
                    <input type="password" class="form-control" id="input-new-pwd-verif" placeholder="Répéter le mot de passe" aria-label="Prénom" aria-describedby="basic-addon1">
                </div>
                <h2 id="good-message"></h2>
                <p class="btn btn-primary mt-2" id="btn-save-pwd">Enregistrer le mot de passe</p>
            `;

      const btnBack = document.getElementById("btn-back");
      btnBack.addEventListener('click', () => {
        refresh();
      });
      const saveButtonPwd = document.getElementById("btn-save-pwd");
      saveButtonPwd.addEventListener('click', async (e) => {
        await saveUserPwd(e);
      });
    });
  }

  const saveButton = document.getElementById("btn-save-phone-number");
  console.log(saveButton);
  saveButton.addEventListener('click', async (e) => {
    await saveUserInfo(e);
  });
};

export default InfoPage;