import {awaitFront, showNavStyle} from "../../utils/function";
/* eslint-disable prefer-template */
// eslint-disable-next-line import/no-cycle
import {Redirect} from "../Router/Router";

import {getAuthenticatedUser,} from "../../utils/session";

const CreateInternshipPage = async (contact) => {

  const main = document.querySelector('main');
  awaitFront();

  const user = await getAuthenticatedUser();

  showNavStyle("dashboard");

  const createInternship = async (supervisor, project, signatureDate) => {
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
  };

  const renderPage = async () => {
    const centralDiv = document.createElement("div");
    centralDiv.className = "text-center";
    main.appendChild(centralDiv);
    const title = document.createElement("h1");
    title.innerHTML = "Créer un stage :";
    title.appendChild(document.createElement('br'));
    title.innerHTML += `${company.name}`
    if(company.designation != null){
      title.innerHTML += `, ${company.designation}`;
    }
    title.id = "title";
    title.className = "my-5";
    centralDiv.appendChild(title);
    const textAera = document.createElement("textarea");
    textAera.id = "blacklistMotivation";
    textAera.className = "col-md-6 border-3 rounded";
    textAera.rows = 10;
    textAera.placeholder = `Quelle est la motivation de blacklister l'entreprise ${company.name} ?`;
    centralDiv.appendChild(textAera);
    const button = document.createElement("button");
    button.innerText = "Blacklister l'entrepise";
    button.className = "btn-primary my-5 text-white rounded";
    button.id = "submitButton";
    centralDiv.appendChild(document.createElement("br"));
    centralDiv.appendChild(button);

    const submitButton = document.querySelector("#submitButton");
    const blacklistMotivation = document.querySelector("#blacklistMotivation");

    let supervisor, project, signatureDate;

    submitButton.addEventListener("click", await createInternship(supervisor, project, signatureDate));
  }
  const urlParams = new URLSearchParams(window.location.search);
  const companyId = urlParams.get('id');
  const company = await getCompanyInfo(companyId);
  renderPage(company.company);

}

export default CreateInternshipPage;