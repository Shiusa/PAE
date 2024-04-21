import {awaitFront, showNavStyle} from "../../utils/function";
import {getAuthenticatedUser} from "../../utils/session";

const closeForm = () => {
  const addCompanyContainer = document.querySelector(
      '.add-company-container');
  addCompanyContainer.classList.remove('slide-in');
  addCompanyContainer.classList.add('slide-out');
  const entrepriseBox = document.querySelector(".add-company-box");

  setTimeout(() => {
    entrepriseBox.style.visibility = "hidden";
    entrepriseBox.innerHTML = ``;
  }, 300);
}

const renderRegisterCompanyForm = async () => {
  const entrepriseBox = document.querySelector(".add-company-box");
  entrepriseBox.innerHTML = `
    <div class="add-company-container d-flex justify-contain-center align-items-center flex-column mx-auto rounded-4">
      <div class="w-100 h-100 rounded-4 py-1" style="background: #119DB8">
        <div class="h-100 col-md-8 offset-md-2 rounded-1 py-3 px-5" style="background: white">
          <i id="company-back-btn" class="fa-solid fa-circle-arrow-left" title="Retour"></i>
          <div class="h-100 rounded-4 mx-5 d-flex flex-column justify-content-center align-items-center" style="border: 2px solid #119DB8">
            <h1 class="mb-5">Ajouter une entreprise</h1>
            <div class="input-group mb-3 w-75">
              <span class="input-group-text prepend-add-company rounded-start-5" id="basic-addon1"><i class="fa-solid fa-user"></i></span>
              <input type="text" class="form-control input-add-company rounded-end-5" id="input-name" placeholder="Nom" aria-label="Nom" aria-describedby="basic-addon1">
            </div>
            <div class="input-group mb-3 w-75">
              <span class="input-group-text prepend-add-company rounded-start-5" id="basic-addon1"><i class="fa-solid fa-user"></i></span>
              <input type="text" class="form-control input-add-company rounded-end-5" id="input-designation" placeholder="Appellation" aria-label="Appellation" aria-describedby="basic-addon1">
            </div>
            <div class="input-group mb-3 w-75">
              <span class="input-group-text prepend-add-company rounded-start-5" id="basic-addon1"><i class="fa-solid fa-user"></i></span>
              <input type="text" class="form-control input-add-company rounded-end-5" id="input-adress" placeholder="Adresse" aria-label="Adresse" aria-describedby="basic-addon1">
            </div>
            <div class="input-group mb-3 w-75">
              <span class="input-group-text prepend-add-company rounded-start-5" id="basic-addon1"><i class="fa-solid fa-phone"></i></span>
              <input type="text" class="form-control input-add-company rounded-end-5" id="input-phone-number" placeholder="Téléphone" aria-label="Téléphone" aria-describedby="basic-addon1">
            </div>
            <div class="input-group mb-3 w-75">
              <span class="input-group-text prepend-add-company rounded-start-5" id="basic-addon1"><i class="fa-solid fa-envelope"></i></span>
              <input type="text" class="form-control input-add-company rounded-end-5" id="input-email" placeholder="Adresse email" aria-label="Adresse email" aria-describedby="basic-addon1">
            </div>
            <!--<p class="btn-login" id="register-btn">Ajouter l'entreprise</p>-->
            <button class="register-company-btn rounded-1 px-2 py-3 w-50 mt-5">Ajouter l'entreprise</button>
            <h2 id="error-message">L'adresse email ou<br>le mot de passe est incorrect !</h2>
          </div>
        </div>
      </div>
    </div>
  `;
  entrepriseBox.style.visibility = "visible";

  const addCompanyContainer = document.querySelector(
      '.add-company-container');
  addCompanyContainer.classList.add('slide-in');

  const btnBack = document.getElementById('company-back-btn');
  btnBack.addEventListener('click', () => {
    closeForm();
  });

  const registerBtn = document.querySelector('.register-company-btn');
  registerBtn.addEventListener('click', async (e) => {
    e.preventDefault();
    closeForm();
  })
}

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
    <div class="container-fluid mt-5 d-flex flex-column rounded-3 overflow-hidden" style="width: 100%; height: 74vh; border: none; border-radius: 0; background: white; position: relative">
      <div class="row">
      
        <div class="col-md-6 offset-md-3 d-flex justify-content-center align-items-center">
          <input type="text" class="form-control rounded-5" placeholder="Rechercher une entreprise">
        </div>
        <div class="col-md-3 d-flex justify-content-end align-items-center" style="padding-right: 5rem">
          <button class="add-company-btn rounded-1 px-2 py-3">Ajouter une entreprise</button>
        </div>
        
        <div class="add-company-box w-100 h-100 d-flex justify-contain-center align-items-center">                    
        </div>
        
      </div>
      
      <div class="row">
        <div class="d-flex justify-content-center align-items-center">
          <div class="users-container w-75 d-flex justify-content-center align-items-center mt-5 flex-column mb-5">
            <h1>Il n'y a aucune entreprise</h1>
          </div>
        </div>
      </div>
    </div>
    
    
    
    
  `;

  const addCompanyBtn = document.querySelector('.add-company-btn');
  addCompanyBtn.addEventListener('click', () => {
    renderRegisterCompanyForm();
  })

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
      element.addEventListener('click', async () => {
        await createContact(element.id);
        await ContactPage();
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