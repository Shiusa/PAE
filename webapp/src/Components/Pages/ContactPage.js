import {showNavStyle, awaitFront} from "../../utils/function";
import {getAuthenticatedUser} from "../../utils/session";

const ContactPage = async () => {

  const main = document.querySelector('main');

  const user = await getAuthenticatedUser();

  showNavStyle("contact");
  awaitFront();

  const readAllCompanies = async () => {
    const options = {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': user.token
      }
    }
    const response = await fetch('api/companies/all/user', options);

    if (!response.ok) {
      throw new Error(
          `fetch error : ${response.status} : ${response.statusText}`);
    }

    const companyInfo = await response.json();
    return companyInfo;
  };

  const companiesTable = await readAllCompanies();

  main.innerHTML = `
    <div class="d-flex justify-content-center align-items-center">
      <div class="users-container w-75 d-flex justify-content-center align-items-center mt-5 flex-column mb-5">
        <h1>Il n'y a aucune entreprise</h1>
      </div>
    </div>
  `;

  const companiesContainer = document.querySelector('.users-container');

  showCompaniesList(companiesTable)

  function showCompaniesList(companies) {
    companiesContainer.innerHTML = ``;

    let u = 0;
    let info = ``;
    while (u < companies.length) {
      let designation;
      let email;
      let phone;

      if (companies[u].designation === null) {
        designation = "";
      } else {
        designation = companies[u].designation;
      }

      if (companies[u].email === null) {
        email = "";
      } else {
        email = companies[u].email;
      }

      if (companies[u].phoneNumber === null) {
        phone = "";
      } else {
        phone = companies[u].phoneNumber;
      }
      info += `
            <div class="user-info-box d-flex justify-content-center align-items-center mt-2">
              <p>${companies[u].name} ${designation}</p>
              <p>${email}</p>
              <p>${phone}</p>
            `;
      if (companies[u].blacklisted === false) {
        info += `<button id="${companies[u].id}" class="company-btn btn btn-secondary">Initier</button>`;
      }
      info += `</div>`;
      u += 1;
    }
    companiesContainer.innerHTML = info;
  }

  const companiesBtn = document.querySelectorAll('.company-btn');

  if (companiesBtn) {
    companiesBtn.forEach(element => {
      element.addEventListener('click', () => {
        createContact(element.id);
        ContactPage();
      });
    });
  }

  async function createContact(idCompany) {
    const options = {
      method: 'POST',
      body: JSON.stringify({
        company: idCompany,
      }),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': user.token
      }
    }
    const response = await fetch('api/contacts/start', options);

    if (!response.ok) {
      throw new Error(
          `fetch error : ${response.status} : ${response.statusText}`);
    }

    const companyInfo = await response.json();
    return companyInfo;
  }

};

export default ContactPage;