import { showNavStyle, awaitFront } from "../../utils/function";
/* eslint-disable prefer-template */
// eslint-disable-next-line import/no-cycle
import { Redirect } from "../Router/Router";
import { getAuthenticatedUser, } from "../../utils/session";

const InternshipPage = async () => {
  awaitFront()
  const main = document.querySelector('main');

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
        main.innerHTML = "";
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
  if (internship === undefined) {
    main.innerHTML = `
      <div class="dash d-flex justify-content-center align-items-center mt-5 mb-5 mx-auto">
        <div class="dash-content d-flex justify-content-center align-items-center flex-column">
          <div class="dash-info mt-4 d-flex justify-content-center align-items-center flex-column">
              <h1 class="noInternship mt-2 mb-5">Vous n'avez pas de stage</h1>
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
    const divMaster = document.createElement("div");
    divMaster.className = "row justify-content-center my-4 display-flex";

    const divInternshipMother = document.createElement("div");
    divInternshipMother.className = "box-internshipMother justify-content-center ";
    const divInternshipChild = document.createElement("div");
    divInternshipChild.className = "box-internshipChild row justify-content-center my-4 display-flex";
    const divLeft = document.createElement("div");
    divLeft.className = "row justify-content-center my-4 display-flex col-md-6";
    const divRight = document.createElement("div");
    divRight.className = "row justify-content-center my-4 display-flex col-md-6";

    // title
    const title = document.createElement("h1");
    title.innerHTML = "Votre Stage";
    title.className = "text-center textCSS"
    divInternshipChild.appendChild(title);

    // company name
    const labelCompanyName = document.createElement("label");
    labelCompanyName.innerText = "Entreprise :";
    labelCompanyName.className = "textCSS";
    const valueLabelCompanyName = document.createElement("p");
    valueLabelCompanyName.innerText = `${internship.contact.company.name}`;
    valueLabelCompanyName.className = "valueLabel textCSS";
    labelCompanyName.appendChild(valueLabelCompanyName);
    divLeft.appendChild(labelCompanyName);
    divLeft.appendChild(document.createElement("br"))

    // company designation
    if (internship.contact.company.designation) {
      const labelCompanyDesignation = document.createElement("label");
      labelCompanyDesignation.innerText = "Appellation :";
      labelCompanyDesignation.className = "textCSS";
      const valueLabelCompanyDesignation = document.createElement("p");
      valueLabelCompanyDesignation.innerText = `${internship.contact.company.designation}`;
      valueLabelCompanyDesignation.className = "valueLabel textCSS";
      labelCompanyDesignation.appendChild(valueLabelCompanyDesignation);
      divLeft.appendChild(labelCompanyDesignation);
      divLeft.appendChild(document.createElement("br"));
    }

    // company address
    const labelCompanyAddress = document.createElement("label");
    labelCompanyAddress.innerText = "Adresse :";
    labelCompanyAddress.className = "textCSS";
    const valueLabelCompanyAddress = document.createElement("p");
    valueLabelCompanyAddress.innerText = `${internship.contact.company.address}`;
    valueLabelCompanyAddress.className = "valueLabel textCSS";
    labelCompanyAddress.appendChild(valueLabelCompanyAddress);
    divLeft.appendChild(labelCompanyAddress);
    divLeft.appendChild(document.createElement("br"))

    // company phone number
    if (internship.contact.company.phoneNumber) {
      const labelCompanyPhoneNumber = document.createElement("label");
      labelCompanyPhoneNumber.innerText = "Téléphone :";
      labelCompanyPhoneNumber.className = "textCSS";
      const valueLabelCompanyPhoneNumber = document.createElement("p");
      valueLabelCompanyPhoneNumber.innerText = `${internship.contact.company.phoneNumber}`;
      valueLabelCompanyPhoneNumber.className = "valueLabel textCSS";
      labelCompanyPhoneNumber.appendChild(valueLabelCompanyPhoneNumber);
      divLeft.appendChild(labelCompanyPhoneNumber);
      divLeft.appendChild(document.createElement("br"));
    }

    // company email
    if (internship.contact.company.email) {
      const labelCompanyEmail = document.createElement("label");
      labelCompanyEmail.innerText = "Email :";
      labelCompanyEmail.className = "textCSS";
      const valueLabelCompanyEmail = document.createElement("p");
      valueLabelCompanyEmail.innerText = `${internship.contact.company.email}`;
      valueLabelCompanyEmail.className = "valueLabel textCSS";
      labelCompanyEmail.appendChild(valueLabelCompanyEmail);
      divLeft.appendChild(labelCompanyEmail);
      divLeft.appendChild(document.createElement("br"));
    }

    // internship project
    const labelInternshipProject = document.createElement("label");
    labelInternshipProject.innerText = "Sujet du stage :";
    labelInternshipProject.className = "textCSS";
    const valueLabelInternshipProject = document.createElement("p");
    if (internship.project) {
      valueLabelInternshipProject.innerHTML = `${internship.project}`;
    } else {
      valueLabelInternshipProject.innerHTML = `Pas encore de sujet de stage`;
    }
    const iconEditProject = document.createElement('i');
    iconEditProject.className = "fa-solid fa-pencil";
    iconEditProject.addEventListener("click", () => {
      labelInternshipProject.innerHTML = "";
      labelInternshipProject.innerText = "Sujet du stage :";
      const newValueLabelInternshipProject = document.createElement('input');
      newValueLabelInternshipProject.value = `${internship.project}`;
      newValueLabelInternshipProject.className = "newValueLabel textCSS";
      const submitButton = document.createElement("i");
      submitButton.className = "submitProjectButton fa-solid fa-check";
      labelInternshipProject.appendChild(newValueLabelInternshipProject);
      labelInternshipProject.appendChild(submitButton);
      const errorMessage = document.createElement("p");
      labelInternshipProject.appendChild(errorMessage);


      submitButton.addEventListener("click", async() => {
        try {
          const options = {
            method: "POST", 
            body: JSON.stringify({
              project : newValueLabelInternshipProject.value,
              version : internship.version,
              internshipId : internship.id
            }),
            headers: {
              "Content-Type": "application/json",
              'Authorization': user.token
            },
          };

          const response = await fetch("/api/internships/editProject", options); 

          if (!response.ok) {
            throw new Error(
              `fetch error : ${response.status} : ${response.statusText}`
            );
          }

          const editProject = await response.json();
          Redirect("/internship") 
          return editProject;

        } catch (error) {
          if (error instanceof Error && error.message.startsWith("fetch error : 400")) {
            errorMessage.innerText = "le sujet du projet est vide !"
          }
          errorMessage.id = "error-message"
          errorMessage.style.display = "block";
        }
        return null;
      });
    });
    valueLabelInternshipProject.className = "valueLabel textCSS d-flex justify-content-between align-items-center";
    valueLabelInternshipProject.appendChild(document.createTextNode(' '));
    valueLabelInternshipProject.appendChild(iconEditProject)
    labelInternshipProject.appendChild(valueLabelInternshipProject);
    divLeft.appendChild(labelInternshipProject);
    divLeft.appendChild(document.createElement("br"))

    // supervisor name
    const labelSupervisorName = document.createElement("label");
    labelSupervisorName.innerText = "Responsable :";
    labelSupervisorName.className = "textCSS";
    const valueLabelSupervisorNameValue = document.createElement("p");
    valueLabelSupervisorNameValue.innerText = `${internship.supervisor.lastname} ${internship.supervisor.firstname}`;
    valueLabelSupervisorNameValue.className = "valueLabel textCSS";
    labelSupervisorName.appendChild(valueLabelSupervisorNameValue);
    divRight.appendChild(labelSupervisorName);
    divRight.appendChild(document.createElement("br"));

    // supervisor phone number
    const labelSupervisorPhoneNumber = document.createElement("label");
    labelSupervisorPhoneNumber.innerText = "Téléphone :";
    labelSupervisorPhoneNumber.className = "textCSS";
    const valueLabelSupervisorPhoneNumber = document.createElement("p");
    valueLabelSupervisorPhoneNumber.innerText = `${internship.supervisor.phoneNumber}`;
    valueLabelSupervisorPhoneNumber.className = "valueLabel textCSS";
    labelSupervisorPhoneNumber.appendChild(valueLabelSupervisorPhoneNumber);
    divRight.appendChild(labelSupervisorPhoneNumber);
    divRight.appendChild(document.createElement("br"));


    // supervisor email
    const labelSupervisorEmail = document.createElement("label");
    labelSupervisorEmail.innerText = "Email :";
    labelSupervisorEmail.className = "textCSS";
    const valueLabelSupervisorEmail = document.createElement("p");
    valueLabelSupervisorEmail.innerText = `${internship.supervisor.email}`;
    valueLabelSupervisorEmail.className = "valueLabel textCSS";
    labelSupervisorEmail.appendChild(valueLabelSupervisorEmail);
    divRight.appendChild(labelSupervisorEmail);
    divRight.appendChild(document.createElement("br"));

    divInternshipChild.appendChild(divLeft)
    divInternshipChild.appendChild(divRight)
    divInternshipMother.appendChild(divInternshipChild)

    divMaster.appendChild(divInternshipMother);

    main.appendChild(divMaster);
  }
}

export default InternshipPage;