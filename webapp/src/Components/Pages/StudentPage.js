import {
  getAuthenticatedUser,
  getLocalUser,
  getToken,
  setAuthenticatedUser
} from "../../utils/session";
import Navigate from "../../utils/Navigate";
import Navbar from "../Navbar/Navbar";
import {closeForm} from "./ContactPage";

let studentId;

const readUserInfo = async () => {
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': getToken()
    }
  }
  const response = await fetch(`api/users/${studentId}`, options);

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
        'Authorization': getToken()
      }
    }
    const response = await fetch(`api/internships/student/${studentId}`,
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

/* const readContactById = async (idContact) => {
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': getToken()
    }
  }
  const response = await fetch(`api/contacts/${idContact}`, options);

  if (!response.ok) {
    throw new Error(
        `fetch error : ${response.status} : ${response.statusText}`);
  }

  const userInfo = await response.json();
  return userInfo;
}; */

const readAllContactsByStudent = async () => {
  try {
    const options = {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': getToken()
      }
    }

    const response = await fetch(`api/contacts/all/${studentId}`, options);

    if (!response.ok) {
      if (response.status === 401) {
        Navigate("/");
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

const renderContactList = (contactsTable) => {
  const tableContacts = document.querySelector(".table-line-box");
  if (!contactsTable) {
    return;
  }
  tableContacts.innerHTML = ``;

  let u = 0;
  let info = ``;
  while (u < contactsTable.length) {
    let designation;
    if (contactsTable[u].company.designation === null) {
      designation = "";
    } else {
      designation = contactsTable[u].company.designation;
    }
    info += `
                <div class="table-line d-flex align-items-center mt-2 mb-2">
                    <div class="d-flex justify-content-center align-items-center position-relative" style="width: 60%;">
                      <i class="line-info fa-solid fa-circle-info position-absolute" style="left: 0;" id="${contactsTable[u].id}"></i>
                      <div class="line-col-1" >
                          <p class="mx-auto mt-3">${contactsTable[u].company.name}<br>${designation}</p>
                      </div>
                    </div>
                    
                    <div class="line-col-2 d-flex flex-column align-items-center justify-content-center" style="width: 20%;">
                      <p class="m-0">${contactsTable[u].state}</p>
                    </div>
                    
                    <div class="${contactsTable[u].state === 'pris' ? 'd-block'
        : 'd-none'}" style="width: 20%;">
                      <button data-id="${contactsTable[u].id}" class="accept-contact-btn rounded-1 px-0 py-2 w-50 bg-secondary" disabled>Accepter</button>
                    </div>
                </div>
            `;
    u += 1;
  }
  tableContacts.innerHTML = info;
  // clickContactInfo();
}

const renderInternshipInfo = (stageObj) => {
  const stageBox = document.querySelector('.dash-stage');
  if (stageObj) {
    let {designation} = stageObj.contact.company;
    const {address, name} = stageObj.contact.company;
    let {project} = stageObj;
    let {email} = stageObj.contact.company;
    const {lastname, firstname, phoneNumber} = stageObj.supervisor;

    if (designation === null) {
      designation = "";
    }
    if (project === null) {
      project = "";
    }
    if (email === null) {
      email = "";
    }

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
              <span class="ms-4"><i class="fa-solid fa-phone"></i>${phoneNumber}</span>
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
}

const renderStudentInfo = (studentObj) => {
  const studentInfoSide = document.querySelector('.dash-left');
  studentInfoSide.innerHTML = `
    <div class="dash-year d-flex justify-content-center align-items-center flex-column">
        <i class="fa-solid fa-calendar-days mt-3"></i>
        <p class="mt-2">${studentObj.schoolYear}</p>
    </div>
    <div class="dash-info mt-4 d-flex justify-content-center align-items-center flex-column">   
        <i class="fa-solid fa-circle-info"></i>
        <h1 class="mt-2 mb-5">Informations<br>personnelles</h1>
        <p>${studentObj.email}</p>
        <p>${studentObj.firstname}</p>
        <p>${studentObj.lastname}</p>
        <p>${studentObj.phoneNumber}</p>
    </div>
  `;
}

const StudentPage = async (student) => {

  studentId = student;

  const userAuth = await getAuthenticatedUser();
  setAuthenticatedUser(userAuth);
  Navbar();
  const localUser = getLocalUser();
  if (!getToken() || !localUser) {
    Navigate('/');
    return;
  }
  if (localUser.role !== 'Professeur' && localUser.role
      !== 'Administratif') {
    Navigate('/dashboard');
    return;
  }

  const studentInfo = await readUserInfo();
  console.log(studentInfo)
  const internshipInfo = await readInternship();
  console.log(internshipInfo)
  const contactsInfo = await readAllContactsByStudent();
  console.log(contactsInfo)
  // const studentPageContainer = document.querySelector('student-page-container');
  const studentBox = document.querySelector(".add-company-box");
  studentBox.innerHTML = `
          <i id="student-back-btn" class="fa-solid fa-circle-arrow-left sticky-top" title="Retour"></i>
    <div class="add-company-container d-flex justify-contain-center align-items-center flex-column mx-auto w-100 h-100">
          <div class="dash d-flex justify-content-center align-items-center mt-5 mb-5 mx-auto">
            <div class="dash-left d-flex justify-content-center align-items-center flex-column ms-3 me-3">
                
            </div>
            <div class="dash-right d-flex justify-content-center align-items-center flex-column ms-3 me-3">
                <div class="dash-stage d-flex justify-content-center align-items-center">
                    
                </div>
                <div class="dash-en-container mt-4 d-flex justify-content-center align-items-center overflow-hidden">
                    <div class="dash-en d-flex align-items-center flex-column pb-3">
                        <div class="table-title d-flex justify-content-center align-items-center mt-3 font-weight-bold">
                                <div class="title-col-1 mt-3">
                                    <p>Nom</p>
                                </div>
                                <div class="title-col-2 mt-3">
                                    <p>Etat</p>
                                </div>
                                <div class="title-col-3 mt-3">
                                    <p>Action</p>
                                </div>
                        </div>
                        <div class="table-line-box overflow-auto mt-1 rounded-3">
                            
                        </div>
                    </div>
                    <div class="entreprise-box d-flex justify-contain-center align-items-center">    
                        
                    </div>
                </div>
            </div>
            
        </div>
    </div>
  `;

  renderStudentInfo(studentInfo);
  renderInternshipInfo(internshipInfo);
  renderContactList(contactsInfo);

  studentBox.style.visibility = "visible";

  const addCompanyContainer = document.querySelector(
      '.add-company-container');
  addCompanyContainer.classList.add('slide-in');

  const btnBack = document.getElementById('student-back-btn');
  btnBack.addEventListener('click', () => {
    closeForm();
  });

};

export default StudentPage;