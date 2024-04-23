import {getAuthenticatedUser, setAuthenticatedUser} from "../../utils/session";

const Navbar = async () => {

  const navbarWrapper = document.querySelector("#navbarWrapper");

  const userConnected = await getAuthenticatedUser();
  if (userConnected) {
    setAuthenticatedUser(userConnected);
  }
  /* if (isConnected) {
    setLocalUser(isConnected.user)
  } */
  // console.log(getLocalUser())

  let navbar = ``;

  if (userConnected) {
    const userRole = userConnected.user.role;
    if (userRole === "Etudiant") {
      navbar = `
        <nav class="d-flex justify-content-center align-items-center flex-wrap">
          <a class="nav-link nav-btn d-flex justify-content-center align-items-center" id="home" href="#" data-uri="/">
            <p data-uri="/">Accueil</p>
          </a>
          <a class="nav-link nav-btn d-flex justify-content-center align-items-center" id="dashboard" href="#" data-uri="/dashboard">
            <p data-uri="/dashboard">Tableau de<br>bord</p>
          </a>
          <a class="nav-link nav-btn d-flex justify-content-center align-items-center" id="contact" href="#" data-uri="/contact">
            <p data-uri="/contact">Prendre<br>contact</p>
          </a>
          <a class="nav-link nav-btn d-flex justify-content-center align-items-center" id="internship" href="#" data-uri="/internship">
            <p data-uri="/internship">Modifier mon stage</p>
          </a>
          <a class="nav-link nav-logout d-flex justify-content-center align-items-center" href="#" data-uri="/logout">
            <p data-uri="/logout"><i class="fa-solid fa-arrow-right-from-bracket" data-uri="/logout"></i></p>
          </a>
        </nav>
      `;
    }

    if (userRole === "Professeur" || userRole === "Administratif") {
      navbar = `
        <nav class="d-flex justify-content-center align-items-center flex-wrap">
          <a class="nav-link nav-btn d-flex justify-content-center align-items-center" id="home" href="#" data-uri="/">
            <p data-uri="/">Accueil</p>
          </a>
          <a class="nav-link nav-btn d-flex justify-content-center align-items-center" id="dashboard" href="#" data-uri="/adminBoard">
            <p data-uri="/adminBoard">Tableau de<br>bord</p>
          </a>
          <a class="nav-link nav-btn d-flex justify-content-center align-items-center" id="userList" href="#" data-uri="/userList">
            <p data-uri="/userList">Utilisateurs<br>liste</p>
          </a>
          <a class="nav-link nav-logout d-flex justify-content-center align-items-center" href="#" data-uri="/logout">
            <p data-uri="/logout"><i class="fa-solid fa-arrow-right-from-bracket" data-uri="/logout"></i></p>
          </a>
        </nav>
      `;
    }
  } else {
    navbar = `
      <nav class="d-flex justify-content-center align-items-center flex-wrap">
        <a class="nav-link nav-btn d-flex justify-content-center align-items-center" id="home" href="#" data-uri="/">
          <p data-uri="/">Accueil</p>
        </a>
        <a class="nav-link nav-btn d-flex justify-content-center align-items-center" id="login" href="#" data-uri="/login">
          <p data-uri="/login">Connexion</p>
        </a>
        <a class="nav-link nav-btn d-flex justify-content-center align-items-center" id="register" href="#" data-uri="/register">
          <p data-uri="/register">Inscription</p>
        </a> 
      </nav>
    `;
  }
  navbarWrapper.innerHTML = navbar;

};

export default Navbar;