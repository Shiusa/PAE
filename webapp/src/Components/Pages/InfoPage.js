import {showNavStyle, awaitFront} from "../../utils/function";

/* eslint-disable prefer-template */
// eslint-disable-next-line import/no-cycle

import {
    getAuthenticatedUser,
} from "../../utils/session";


const InfoPage = async () => {

    

    const main = document.querySelector('main');
    
    awaitFront();

    const user = await getAuthenticatedUser();

    showNavStyle("info");

    const readUserInfo = async () => {
        const options = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': user.token
            }
        }
        const response = await fetch('api/users/' + user.user.id, options);

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
            <input type="text" class="form-control" id="input-firstname" value="${userInfoID.firstname}" placeholder="Prénom" aria-label="Prénom" aria-describedby="basic-addon1">
        </div>
        <div class="input-group mb-3">
            <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-user"></i></span>
            <input type="text" class="form-control" id="input-lastname" value="${userInfoID.lastname}" placeholder="Nom" aria-label="Nom" aria-describedby="basic-addon1">
        </div>
        <div class="input-group mb-3">
            <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-phone"></i></span>
            <input type="text" class="form-control" id="input-phone" value="${userInfoID.phoneNumber}" placeholder="Téléphone" aria-label="Téléphone" aria-describedby="basic-addon1">
        </div>
        <p class="btn btn-primary mt-1" id="btn-save">Enregistrer les modifications</p>
        <p class="btn btn-outline-primary mt-2" id="btn-change-pwd">Modifier mon mot de passe</p>    
    `;

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

    showNavStyle("info");

    

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
                <p class="btn btn-primary mt-2" id="btn-save">Enregistrer le mot de passe</p>
            `;
    
            const btnBack = document.getElementById("btn-back");
            btnBack.addEventListener('click', () => {
                refresh();
            });
        });
    }
};

export default InfoPage;