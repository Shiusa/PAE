import {showNavStyle, awaitFront} from "../../utils/function";
/* eslint-disable prefer-template */
// eslint-disable-next-line import/no-cycle
import { Redirect } from "../Router/Router";

import {
    getAuthenticatedUser,
} from "../../utils/session";

const DashboardPage = async () => {

    const main = document.querySelector('main');
    awaitFront();

    const user = await getAuthenticatedUser();

    showNavStyle("dashboard");

    const readUserInfo = async () => {
        try {
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
        } catch (err) {
            console.error('DashBoardPage::error: ', err);
            throw err;
        }
    };

    const readInternship = async () => {
        try {
            const options = {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': user.token
                }
            }
            const response = await fetch('api/internships/student/' + user.user.id, options);

            if (!response.ok) {
                throw new Error(
                    `fetch error : ${response.status} : ${response.statusText}`);
            }
            
            const userInfo = await response.json();
            return userInfo;
        } catch (err) {
            console.error('DashBoardPage::error: ', err);
            throw err;
        }
    };

    const readContactById = async (idContact) => {
        try {
            const options = {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': user.token
                }
            }
            const response = await fetch('api/contacts/' + idContact, options);

            if (!response.ok) {
                throw new Error(
                    `fetch error : ${response.status} : ${response.statusText}`);
            }
            
            const userInfo = await response.json();
            return userInfo;
        } catch (err) {
            console.error('DashBoardPage::error: ', err);
            throw err;
        }
    };


    const readAllContactsByStudent = async () => {
        try {
            const options = {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': user.token
                }
            }

            const response = await fetch('api/contacts/all/' + user.user.id, options);

            if (!response.ok) {
                console.log("le status est" + response.status);
                if (response.status === 401) {
                Redirect("/");
                }
                throw new Error(
                    `fetch error : ${response.status} : ${response.statusText}`);
            }
            const contactList = await response.json();
            return contactList;
        } catch (err) {
            console.error('dashboardPage::error: ', err);
            throw err;
        }
    };



    const userInfoID = await readUserInfo();
    const stageInfo = await readInternship();

    main.innerHTML = `        
        <div class="dash d-flex justify-content-center align-items-center mt-5 mb-5 mx-auto">
            <div class="dash-left d-flex justify-content-center align-items-center flex-column ms-3 me-3">
                <div class="dash-year d-flex justify-content-center align-items-center flex-column">
                    <i class="fa-solid fa-calendar-days mt-3"></i>
                    <p class="mt-2">${userInfoID.schoolYear}</p>
                </div>
                <div class="dash-info mt-4 d-flex justify-content-center align-items-center flex-column">   
                    <i class="fa-solid fa-circle-info"></i>
                    <h1 class="mt-2 mb-5">Informations<br>personnelles</h1>
                    <p>${userInfoID.email}</p>
                    <p>${userInfoID.firstname}</p>
                    <p>${userInfoID.lastname}</p>
                    <p>${userInfoID.phoneNumber}</p>
                    <span id="btn-info-change" class="mt-4">Changer mes informations</span>
                </div>
            </div>
            <div class="dash-right d-flex justify-content-center align-items-center flex-column ms-3 me-3">
                <div class="dash-stage d-flex justify-content-center align-items-center">
                    
                </div>
                <div class="dash-en-container mt-4 d-flex justify-content-center align-items-center overflow-hidden">
                    <div class="dash-en d-flex align-items-center flex-column">
                        <div class="table-title d-flex justify-content-center align-items-center mt-3 font-weight-bold">
                                <div class="title-col-1 mt-3">
                                    <p>Nom</p>
                                </div>
                                <div class="title-col-2 mt-3">
                                    <p>Etat</p>
                                </div>
                                <div class="title-col-3 mt-3">
                                    <p>Action</p>
                                </div>
                        </div>
                        <div class="table-line-box overflow-auto">
                            
                        </div>
                    </div>
                    <div class="entreprise-box d-flex justify-contain-center align-items-center">    
                        
                    </div>
                </div>
            </div>
            
        </div>
    `;

    const stageBox = document.querySelector('.dash-stage');
    
    if(stageInfo==null) {

        stageBox.innerHTML = `        
            <div class="stage-bloc">
                    <h1 class="mt-3">Vous n'avez pas de stage</h1>
            </div>
        `;

    } else {
        stageBox.innerHTML = `        
            <div class="stage-bloc">
                <h1 class="mb-3">Votre stage</h1>
                <div class="d-flex">
                    <p class="me-4"><i class="fa-solid fa-signature"></i> ${stageInfo.nameInternship} ${stageInfo.designationInternship}</p>
                    <p><i class="fa-solid fa-location-dot"></i> ${stageInfo.addressInternship}</p>
                </div>
                <p><i class="fa-solid fa-list"></i> ${stageInfo.project}</p>
            </div>
            <div class="respo-bloc">
                <h1 class="mt-3 ms-4">Votre responsable</h1>
                <p class="mt-2 ms-4"><i class="fa-solid fa-user"></i> ${stageInfo.firstnameSupervisor} ${stageInfo.lastnameSupervisor}</p>
                <span class="ms-4"><i class="fa-solid fa-at"></i> ${stageInfo.emailSupervisor}</span>
            </div>
        `;
    }

    

    showNavStyle("dashboard");

    const tableContacts = document.querySelector(".table-line-box");
    const boxInfo = document.querySelector('.entreprise-box');

    const contacts = await readAllContactsByStudent();

    console.log(contacts);

    showContacts(contacts);



    const btnChangeInfo = document.getElementById("btn-info-change");


    btnChangeInfo.addEventListener('click', () => {
        Redirect('/info');
    });

    function showContacts(contactsTable) {
        tableContacts.innerHTML = ``;
    
        let u = 0;
        let info = ``;
        while (u < contactsTable.length) {
            info += `
                <div class="table-line d-flex align-items-center mt-2 mb-2">
                    <i class="line-info fa-solid fa-circle-info" id="${contactsTable[u].id}"></i>
                    <div class="line-col-1">
                        <p class="mx-auto mt-3">${contactsTable[u].nameCompany}<br>${contactsTable[u].designationCompany}</p>
                    </div>
                    <div class="line-col-2 d-flex justify-content-center">
                        <select class="form-select" aria-label="Default select example">
                            <option  value="1">State 1</option>
                            <option value="2" selected>State 2</option>
                            <option value="3">State 3</option>
                            <option value="4">State 4</option>
                        </select>
                    </div>
                    <div class="line-col-3 d-flex justify-content-center align-items-center">
                        <button class="btn btn-primary ms-2 me-2" type="submit">Mettre à jour</button>
                        <button type="button" class="btn-unfollow btn btn-outline-primary ms-2 me-2" id="${contactsTable[u].id}">Ne plus suivre</button>
                    </div>
                </div>
            `;
             u += 1;
        }
        tableContacts.innerHTML = info;
        clickNePlusSuivre();
        clickContactInfo();
    }

    function clickNePlusSuivre() {
        const allUnfollowBtn = document.querySelectorAll(".btn-unfollow");
        allUnfollowBtn.forEach(element => {
            element.addEventListener('click', async () => {
                unfollow(element.id);
                
            });
        });
    }

    function clickContactInfo() {
        const allContactsBtn = document.querySelectorAll(".line-info");
        allContactsBtn.forEach(element => {
            element.addEventListener('click', () => {
                conctactInfo(element.id);
            });
        });
    }

    async function unfollow(idContact) {

        try {
          const options = {
            method: 'POST',
    
            body : JSON.stringify({contactId : idContact}),
            headers: {
              'Content-Type': 'application/json',
              'Authorization': user.token
            },
          };
    
          const response = await fetch("/api/contacts/unsupervise", options);
          if (!response.ok) {    
            if (response.status === 401) {
              Redirect("/");
            }
            throw new Error(
                `fetch error : ${response.status} : ${response.statusText}`);
          }
        } catch (err) {
          console.log("N'a pas pu unfollow")
        }
      }
    

    async function conctactInfo(id) {
        const entrepriseBox = document.querySelector(".entreprise-box");
        const contactInfoJSON = await readContactById(id);
        boxInfo.style.visibility = "visible";
        console.log(contactInfoJSON);
        entrepriseBox.innerHTML = `
                    <div class="entreprise-container d-flex justify-contain-center align-items-center flex-column mx-auto">
                        <i id="btn-back2" class="fa-solid fa-circle-arrow-left" title="Retour"></i>
                        <h1 class="mt-4">${contactInfoJSON.id}</h1>
                        <h2 class="mb-2">Anvers</h2>
                        <div class="entreprise-info">
                            <p class="mt-3"><i class="fa-solid fa-phone"></i> 0412345678</p>
                            <p class="mt-1"><i class="fa-solid fa-map-location-dot"></i> Avenue des marais 80, Scharbeek 1500</p>
                        
                            <div class="d-flex mt-3 align-items-center">
                                <p class="fw-bold me-4">Type de rencontre</p>
                                <div class="ent-radio form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio1" value="option1">
                                    <label class="form-check-label" for="inlineRadio1">Dans l'entreprise</label>
                                </div>
                                <div class="ent-radio form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio2" value="option2">
                                    <label class="form-check-label" for="inlineRadio2">A Distance</label>
                                </div>
                            </div>
                            <div class="d-flex mt-2">
                                <p class="fw-bold me-4">Etat</p>
                                <select class="form-select" aria-label="Default select example">
                                    <option  value="1">State 1</option>
                                    <option value="2" selected>State 2</option>
                                    <option value="3">State 3</option>
                                    <option value="4">State 4</option>
                                </select>
                            </div>
                            <div class="d-flex mt-4 mb-2">
                                <p class="fw-bold me-4">Raison</p>
                                <textarea name="raison" placeholder="raison"></textarea>
                            </div>
                            <button class="btn btn-primary mb-2 ms-3" type="submit">Mettre à jour</button>
                        </div>
                    </div>
        `;

        const btnBack = document.getElementById('btn-back2');
        btnBack.addEventListener('click', () => {
            boxInfo.style.visibility = "hidden";
            boxInfo.innerHTML = ``;
        });
    }
};

export default DashboardPage;