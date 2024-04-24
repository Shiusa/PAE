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

  console.log(await getSupervisors());

  main.innerHTML = '';
  const row = document.createElement("div");
  row.className = "row justify-content-center my-4 display-flex";

  const divCreateSupervisor = document.createElement("div");
  divCreateSupervisor.className = "box-createSupervisor text-center";
  const titleLeft = document.createElement("h1");
  titleLeft.innerText = "Responsable";
  titleLeft.style.color = "#FFFFFF";
  divCreateSupervisor.appendChild(titleLeft);

  const divChooseSupervisorMother = document.createElement("div");
  divChooseSupervisorMother.className = "box-chooseSupervisorMother justify-content-center ";
  const divChooseSupervisorChild = document.createElement("div");
  divChooseSupervisorChild.className = "box-chooseSupervisorChild text-center";
  const titleRight = document.createElement("h1");
  titleRight.innerHTML = `${contact.company.name}`;
  titleRight.style.color = "#119DB8";
  if(contact.designation){
    titleRight.innerHTML += ` <br> ${contact.designation}`;
  }
  divChooseSupervisorChild.appendChild(titleRight);
  divChooseSupervisorMother.appendChild(divChooseSupervisorChild)

  row.appendChild(divCreateSupervisor);
  row.appendChild(divChooseSupervisorMother);

  main.appendChild(row);

}

export {InternshipPage, CreateInternshipPage};