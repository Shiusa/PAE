import {showNavStyle, awaitFront} from "../../utils/function";
/* eslint-disable prefer-template */
// eslint-disable-next-line import/no-cycle
import {Redirect} from "../Router/Router";

import {
  getAuthenticatedUser,
} from "../../utils/session";

const DashboardPage = async () => {

  const main = document.querySelector('main');
  awaitFront();

  const user = await getAuthenticatedUser();

  showNavStyle("dashboard");

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

  const readInternship = async () => {
    try {
      const options = {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': user.token
        }
      }
      const response = await fetch('api/internships/student/' + user.user.id,
          options);

      if (!response.ok) {
        throw new Error(
            `fetch error : ${response.status} : ${response.statusText}`);
      }

      const userInfo = await response.json();
      if (userInfo) {
        return userInfo;
      }
      return null;
    } catch (error) {
      if (error instanceof Error && error.message.startsWith("fetch error : 500")) {
        return null;
      }
      return null;
    }
  };

  const readContactById = async (idContact) => {
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
        if (response.status === 401) {
          Redirect("/");
        }
        throw new Error(
            `fetch error : ${response.status} : ${response.statusText}`);
      }
      const contactList = await response.json();
      if (contactList) {
        return contactList;
      }
      return null;
    } catch (error) {
      return null;
    }

  };

  const userInfoID = await readUserInfo();
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
                                <div class="title-col-3 mt-3">
                                    <p>Etat</p>
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


  const stageInfo = await readInternship();

  if (stageInfo) {
    let {designation} = stageInfo.company;
    const {address, name} = stageInfo.company;
    let {project} = stageInfo.internship;
    let {email} = stageInfo.supervisor;
    const {lastname, firstname} = stageInfo.supervisor;

    if(designation === null) designation = "";
    if(project === null) project = "";
    if(email === null) email = "";

    stageBox.innerHTML = `        
          <div class="stage-bloc">
              <h1 class="mb-3">Votre stage</h1>
              <div class="d-flex">
                  <p class="me-4"><i class="fa-solid fa-signature"></i> ${name} ${designation}</p>
                  <p><i class="fa-solid fa-location-dot"></i> ${address}</p>
              </div>
              <p><i class="fa-solid fa-list"></i> ${project}</p>
          </div>
          <div class="respo-bloc">
              <h1 class="mt-3 ms-4">Votre responsable</h1>
              <p class="mt-2 ms-4"><i class="fa-solid fa-user"></i> ${firstname} ${lastname}</p>
              <span class="ms-4"><i class="fa-solid fa-at"></i>${email}</span>
          </div>
      `;
  } else {
    stageBox.innerHTML = `        
          <div class="stage-bloc">
                  <h1 class="mt-3">Vous n'avez pas de stage</h1>
          </div>
      `;
  }


  showNavStyle("dashboard");

  const tableContacts = document.querySelector(".table-line-box");
  const boxInfo = document.querySelector('.entreprise-box');

  const contacts = await readAllContactsByStudent();

  showContacts(contacts);

  const btnChangeInfo = document.getElementById("btn-info-change");

  if (btnChangeInfo) {
    btnChangeInfo.addEventListener('click', () => {
      Redirect('/info');
    });
  }


  function showContacts(contactsTable) {
    if (!contactsTable) {
      return;
    }
    tableContacts.innerHTML = ``;

    let u = 0;
    let info = ``;
    while (u < contactsTable.length) {
      let designation;
      if (contactsTable[u].designationCompany === null) {
        designation = "";
      } else {
        designation = contactsTable[u].designationCompany;
      }
      info += `
                <div class="table-line d-flex align-items-center mt-2 mb-2">
                    <i class="line-info fa-solid fa-circle-info" id="${contactsTable[u].id}"></i>
                    <div class="line-col-1" >
                        <p class="mx-auto mt-3">${contactsTable[u].company.name}<br>${designation}</p>
                    </div>
                    <div class="line-col-2 d-flex align-items-center justify-content-center">
                      <p><option  value="1">${contactsTable[u].state}</option></p>
                    </div>
                </div>
            `;
      u += 1;
    }
    tableContacts.innerHTML = info;
    clickContactInfo();
  }

  function clickContactInfo() {
    const allContactsBtn = document.querySelectorAll(".line-info");
    allContactsBtn.forEach(element => {
      element.addEventListener('click', (e) => {
        e.preventDefault();
        conctactInfo(element.id);
      });
    });
  }

  async function conctactInfo(id) {
    const entrepriseBox = document.querySelector(".entreprise-box");
    const contactInfoJSON = await readContactById(id);
    const meetingType = contactInfoJSON.meeting;

    const checkedSurPlace = meetingType === "Dans l entreprise" ? 'checked' : '';
    const checkedADistance = meetingType === "A distance" ? 'checked' : '';

    let refusal;
    if(!contactInfoJSON.reasonRefusal) refusal = "";
    else refusal = contactInfoJSON.reasonRefusal;

    boxInfo.style.visibility = "visible";


    entrepriseBox.innerHTML = `
                    <div class="entreprise-container d-flex justify-contain-center align-items-center flex-column mx-auto">
                        <i id="btn-back2" class="fa-solid fa-circle-arrow-left" title="Retour"></i>
                        <h1 class="mt-4">${contactInfoJSON.id}</h1>
                        <div class="entreprise-info">
                            <p class="mt-3"><i id="phoneNumber" class="fa-solid fa-phone"></i></p>
                            <p class="mt-1"><i id="address" class="fa-solid fa-map-location-dot"></i></p>
                        
                            <div class="radioButton d-flex mt-3 align-items-center">
                                <p class="fw-bold me-4">Type de rencontre</p>
                                <div class="ent-radio form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio1" value="Dans l entreprise" ${checkedSurPlace}>
                                    <label class="form-check-label" for="inlineRadio1">Dans l'entreprise</label>
                                </div>
                                <div class="ent-radio form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio2" value="A distance" ${checkedADistance}>
                                    <label class="form-check-label" for="inlineRadio2">A Distance</label>
                                </div>
                            </div>
                            <div class="d-flex mt-2">
                                <p class="fw-bold me-4">Etat</p>
                                <select id="selectedState" class="form-select" aria-label="Default select example">
                                    <option value="basic" selected>${contactInfoJSON.state}</option>
                                    <!--<option value="started">initié</option>-->
                                    <option value="admitted">pris</option>
                                    <option value="accepted">accepté</option>
                                    <option value="turnedDown">refusé</option>
                                    <option value="unsupervised">ne plus suivre</option>
                                    <!--<option value="onHold">suspendu</option>-->
                                </select>
                            </div>
                            <div class="d-flex mt-4 mb-2"> 
                                <p class="fw-bold me-4">Raison</p>
                                <textarea id="refusalReason" name="raison" placeholder="Raison du refus">${refusal}</textarea>
                            </div>
                            <button id="updateBtn" class="btn btn-primary mb-2 ms-3" type="submit">Mettre à jour</button>
                            <h2 id="error-message"></h2>
                        </div>
                    </div>
        `

    let phone;
    let address;
    const {address: address1, phoneNumber} = contactInfoJSON.company;
    if (phoneNumber === null) {
      phone = "";
    } else {
      phone = phoneNumber;
    }
    if (address1 === null) {
      address = "";
    } else {
      address = address1;
    }

    document.getElementById("phoneNumber").innerHTML = phone;
    document.getElementById("address").innerHTML = address;

    const updateState = document.getElementById("updateBtn");
    updateState.addEventListener('click', async (e) => {
      e.preventDefault();
      const options = {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': user.token
        }
      }
      const newState = document.getElementById("selectedState").value;
      let meeting;
      const refusalReason = document.getElementById("refusalReason").value;
      const roleRadioBtn = document.querySelectorAll(
          '.radioButton input[type="radio"]');
      roleRadioBtn.forEach(button => {
        if (button.checked) {
          meeting = button.value;
        }
      });

      const errorMessage = document.getElementById("error-message");
      switch (newState) {
        case "admitted":
          options.body = JSON.stringify({"contactId": id, "meeting": meeting});
          try {
            const response = await fetch("/api/contacts/admit", options);
            if (!response.ok) {
              throw new Error(
                  `fetch error : ${response.status} : ${response.statusText}`
              );
            }
          } catch (error) {
            if (error instanceof Error && error.message.startsWith(
                "fetch error : 400")) {
              errorMessage.innerHTML = "Veuillez renseigner un type de rencontre correct, où vérifier que vous pouvez mettre le nouvel état."
              errorMessage.style.display = "block";
              return;
            }
            if (error instanceof Error && error.message.startsWith(
                "fetch error : 403")) {
              errorMessage.innerHTML = "Vous ne pouvez pas changer ce contact."
              errorMessage.style.display = "block";
              return;
            }
            errorMessage.innerHTML = "Erreur interne, veuillez réessayer."
            errorMessage.style.display = "block";
            return;
          }
          Redirect("/dashboard");
          break;
        case "turnedDown":
          options.body = JSON.stringify(
              {contactId: id, reasonForRefusal: refusalReason});
          try {
            const response = await fetch("/api/contacts/turnDown", options);
            if (!response.ok) {
              throw new Error(
                  `fetch error : ${response.status} : ${response.statusText}`
              );
            }
          } catch (error) {
            if (error instanceof Error && error.message.startsWith(
                "fetch error : 400")) {
              errorMessage.innerHTML = "Veuillez entrer la raison du refus.";
              errorMessage.style.display = "block";
              return;
            }
            if (error instanceof Error && error.message.startsWith(
                "fetch error : 403")) {
              errorMessage.innerHTML = "Vous ne pouvez pas refusé un contact non pris.";
              errorMessage.style.display = "block";
              return;
            }
            if (error instanceof Error && error.message.startsWith(
                "fetch error : 404")) {
              errorMessage.innerHTML = "Veuillez entrer un contact existant.";
              errorMessage.style.display = "block";
              return;
            }
            errorMessage.innerHTML = "Erreur interne, veuillez réessayer."
            errorMessage.style.display = "block";
            return;
          }
          Redirect("/dashboard");
          break;

        case "unsupervised":
          options.body = JSON.stringify({contactId: id});
          try {
            const response = await fetch("/api/contacts/unsupervise", options);
            if (!response.ok) {
              throw new Error(
                  `fetch error : ${response.status} : ${response.statusText}`
              );
            }
          } catch (error) {
            if (error instanceof Error && error.message.startsWith(
                "fetch error : 400")) {
              errorMessage.innerHTML = "Veuillez entrer un contact ou vérifiez que vous pouvez effectuer ce changement.";
              errorMessage.style.display = "block";
              return;
            }
            if (error instanceof Error && error.message.startsWith(
                "fetch error : 403")) {
              errorMessage.innerHTML = "Vous n'avez pas le droit.";
              errorMessage.style.display = "block";
              return;
            }
            if (error instanceof Error && error.message.startsWith(
                "fetch error : 404")) {
              errorMessage.innerHTML = "Veuillez entrer un contact existant.";
              errorMessage.style.display = "block";
              return;
            }
            errorMessage.innerHTML = "Erreur interne, veuillez réessayer."
            errorMessage.style.display = "block";
            return;
          }
          Redirect("/dashboard");
          break;
        default:
          errorMessage.innerHTML = "Veuillez entrer un contact ou vérifiez que vous pouvez effectuer ce changement.";
          errorMessage.style.display = "block";
      }
    });

    const btnBack = document.getElementById('btn-back2');
    btnBack.addEventListener('click', () => {
      boxInfo.style.visibility = "hidden";
      boxInfo.innerHTML = ``;
    });
  }
};

export default DashboardPage;