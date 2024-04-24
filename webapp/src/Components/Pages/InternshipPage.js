import TomSelect from "tom-select";

import {awaitFront, showNavStyle} from "../../utils/function";
/* eslint-disable prefer-template */
// eslint-disable-next-line import/no-cycle
import {Redirect} from "../Router/Router";
import {getAuthenticatedUser,} from "../../utils/session";


const InternshipPage = async () => {
  const main = document.querySelector('main');
  awaitFront();

  const user = await getAuthenticatedUser();

  showNavStyle("internship");

  const readInternship = async () => {
    try {
      const options = {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': user.token
        }
      }
      const response = await fetch('/api/internships/student/' + user.user.id,
          options);

      if (!response.ok) {
        throw new Error(
            `fetch error : ${response.status} : ${response.statusText}`);
      }

      const internship = await response.json();
      if (internship) {
        return internship;
      }
      return null;
    } catch (error) {
      if (error instanceof Error && error.message.startsWith(
          "fetch error : 500")) {
        return null;
      }
      if (error instanceof Error && error.message.startsWith(
          "fetch error : 404")) {
        return null;
      }
      return null;
    }
  };

  const internship = await readInternship();
  if(!internship) {
    main.innerHTML = `
      <div class="dash d-flex justify-content-center align-items-center mt-5 mb-5 mx-auto">
        <div class="dash-content d-flex justify-content-center align-items-center flex-column">
          <div class="dash-info mt-4 d-flex justify-content-center align-items-center flex-column">
              <h1 class="noInternship mt-2 mb-5">Vous n'avez pas de stage.</h1>
              <span id="btn-info-change" class="btn btn-primary mt-4">Voir mes contacts</span>
          </div>
        </div>
      </div>
    `
    const btnChangeInfo = document.getElementById("btn-info-change");
    btnChangeInfo.addEventListener('click', () => {
      Redirect('/dashboard');
    });
  } else {
    // TODO (Brian)
  }
}

const CreateInternshipPage = async () => {
  const contact = {id: 1, company: {id: 2, name: 'LetsBuild', designation: null}, student: 9, meeting: 'A distance', state: 'accepté', schoolYear: '2023-2024'};
  const main = document.querySelector('main');
  awaitFront();

  const user = await getAuthenticatedUser();

  showNavStyle("dashboard");

  /* const createInternship = async (supervisor, project, signatureDate) => {
    const schoolYear = contact.schoolYear;
    const options = {
      method: 'POST', // *GET, POST, PUT, DELETE, etc.
      body: JSON.stringify({
        contact,
        supervisor,
        signatureDate,
        project,
        schoolYear,
      }),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': user.token
      }
    }
    const response = await fetch('api/internships/create', options);

    if (!response.ok) {
      throw new Error(
          `fetch error : ${response.status} : ${response.statusText}`);
    }

    Redirect("/internship");
  }; */

  const getSupervisors = async () => {
    try {
      const options = {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': user.token
        }
      };

      const response = await fetch('/api/supervisors/allByCompany/2', options);

      if(!response.ok) {
        throw new Error(
            `fetch error : ${response.status} : ${response.statusText}`);
      }

      const supervisors = await response.json();
      return supervisors;
    } catch (error) {
      if (error instanceof Error && error.message.startsWith(
          "fetch error : 500")) {
        return null;
      }
      if (error instanceof Error && error.message.startsWith(
          "fetch error : 404")) {
        return null;
      }
      return null;
    }
  };


  const allSupervisors = await getSupervisors();

  let desi = contact.company.designation;
  if(desi === null) desi = "";

  main.innerHTML = `
    <div class="container-create d-flex justify-content-center align-items-end mt-5 mb-5">
      <div class="box-resp d-flex align-items-center flex-column">
        <h1 class="mt-5 mb-5">Responsable</h1>
        <div class="input-group mb-3">
            <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-user"></i></span>
            <input type="text" class="form-control" id="input-firstname" placeholder="Prénom" aria-label="prénom" aria-describedby="basic-addon1">
        </div>
        <div class="input-group mb-3">
            <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-user"></i></span>
            <input type="text" class="form-control" id="input-lastname" placeholder="Nom" aria-label="nom" aria-describedby="basic-addon1">
        </div>
        <div class="input-group mb-3">
            <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-phone"></i></span>
            <input type="text" class="form-control" id="input-phone" placeholder="Téléphone" aria-label="phone" aria-describedby="basic-addon1">
        </div>
        <div class="input-group mb-3">
            <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-envelope"></i></span>
            <input type="text" class="form-control" id="input-email" placeholder="Email (optionnel)" aria-label="email" aria-describedby="basic-addon1">
        </div>
        <div class="input-group mb-3">
            <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-building"></i></span>
            <input type="text" class="form-control" id="input-contact" readonly value="${contact.company.name}" aria-label="company" aria-describedby="basic-addon1">
        </div>
        <button type="button" id="resp-register" class="create-button mt-4 ">Ajouter le responsable</button>
      </div>
      <div class="box-company d-flex align-items-center flex-column justify-content-center">
        <h1 class="mb-5">${contact.company.name}<br>${desi}</h1>
        <div class="input-group mb-3">
            <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-eye"></i></span>
            <input type="text" class="form-control" id="input-supervisor" placeholder="Recherchez..." aria-label="nom" aria-describedby="basic-addon1">
        </div>
        <div class="input-group mb-3">
            <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-signature"></i></span>
            <input type="text" class="form-control" id="input-subject" placeholder="Sujet (Optionnel)" aria-label="nom" aria-describedby="basic-addon1">
        </div>
        <button type="button" id="stage-register" class="create-button mt-4">Enregistrer</button>
      </div>
    </div>
  `;

  const selectElement = document.getElementById('input-supervisor');

  // eslint-disable-next-line no-unused-vars
  const tomSelectInstance = new TomSelect(selectElement, {
    valueField: 'idSupervisor',
    labelField: 'fullName',
    searchField: ['fullName'],
    maxItems: 1,
    render: {
      no_results() {
          return '<div class="no-results">Aucun superviseur trouvé</div>';
      }
    },
  });

  tomSelectInstance.addOption(allSupervisors.map(supervisor => ({
    idSupervisor: supervisor.id,
    fullName: `${supervisor.firstname} ${supervisor.lastname}`,
  })));

  const registerStageBtn = document.getElementById('stage-register');

  registerStageBtn.addEventListener('click', () => {
    console.log(tomSelectInstance.items);
  });

  
  
}

export {InternshipPage, CreateInternshipPage};