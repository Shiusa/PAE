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
      const response = await fetch('api/internships/student/' + user.user.id,
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
    //TODO (Brian)
  }
}

export default InternshipPage;