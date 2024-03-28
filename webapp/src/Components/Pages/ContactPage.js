import {showNavStyle, awaitFront} from "../../utils/function";
import {getAuthenticatedUser} from "../../utils/session";

const ContactPage = async () => {
  
    
    const main = document.querySelector('main');

    const user = await getAuthenticatedUser();

    showNavStyle("contact");
    awaitFront();

    const readAllCompanies = async () => {
        try {
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
        } catch (err) {
            console.error('DashBoardPage::error: ', err);
            throw err;
        }
    };

    const companiesTable = await readAllCompanies();

    console.log(companiesTable);

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
            info += `
            <div class="user-info-box d-flex justify-content-center align-items-center mt-2">
              <p>${companies[u].name} ${companies[u].designation}</p>
              <p>${companies[u].email}</p>
              <p>${companies[u].phoneNumber}</p>
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
            });
        });
    }

    async function createContact(idCompany) {
        try {
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
        } catch (err) {
            console.error('DashBoardPage::error: ', err);
            throw err;
        }
    }

};

export default ContactPage;