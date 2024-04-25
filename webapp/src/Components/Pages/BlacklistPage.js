import { getAuthenticatedUser } from "../../utils/session";
// eslint-disable-next-line import/no-cycle
import { Redirect } from "../Router/Router";
import {awaitFront} from "../../utils/function";

const BlacklistPage = async(companyId) => {
    awaitFront();
    const user = await getAuthenticatedUser();

    const getCompanyInfo = async (id) => {
        try {
            const options = {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': user.token
                }
            };
    
            const response = await fetch(`/api/companies/${id}`, options);
    
            if (!response.ok) {
                throw new Error(
                    `fetch error : ${response.status} : ${response.statusText}`
                );
            }
    
            const company = await response.json(); 
            return company; 
    
        } catch (error) {
            return null;
        }
    };
    const renderPage = (company) => {
        const main = document.querySelector('main');
        main.innerHTML = "";
        const centralDiv = document.createElement("div");
        centralDiv.className = "text-center";
        main.appendChild(centralDiv);
        const title = document.createElement("h1");
        title.innerHTML = "Blacklister une entreprise :";
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
        const errorMessage = document.createElement("h2");
        errorMessage.id = "error-message";
        errorMessage.style.display = 'block';
        main.appendChild(errorMessage);
        
        const submitButton = document.querySelector("#submitButton");
        const blacklistMotivation = document.querySelector("#blacklistMotivation");
        
        submitButton.addEventListener("click", async () => {
            try {
                const options = {
                    method: "POST",
                    body: JSON.stringify({
                        company : company.id,
                        blacklistMotivation : blacklistMotivation.value,
                    }),
                    headers: {
                    "Content-Type": "application/json",
                    'Authorization': user.token
                    },
                };
    
                const response = await fetch("/api/companies/blacklist", options);
    
                if (!response.ok) {
                    throw new Error(
                        `fetch error : ${response.status} : ${response.statusText}`
                    );
                }else{
                    Redirect("/adminBoard");
                }
    
            } catch (error) {
                errorMessage.innerText = "Cette entreprise est déjà blacklistée";
            }
        });
    }
    const company = await getCompanyInfo(companyId);
    renderPage(company.company);
};



export default BlacklistPage;