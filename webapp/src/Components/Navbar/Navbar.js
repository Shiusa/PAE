import {getUserSessionData} from "../../utils/session";

const Navbar = async () => {

  const navbarWrapper = document.querySelector("#navbarWrapper");

  const isConnected = getUserSessionData();

  let navbar = ``;

  if(isConnected) {
    navbar = `
      <nav class="d-flex justify-content-center align-items-center flex-wrap">
        <a class="nav-link nav-btn d-flex justify-content-center align-items-center" id="home" href="#" data-uri="/">
          <p data-uri="/">Accueil</p>
        </a>
        <a class="nav-link nav-btn d-flex justify-content-center align-items-center" id="dashboard" href="#" data-uri="/dashboard">
          <p data-uri="/dashboard">Tableau de<br>bord</p>
        </a>
        <a class="nav-link nav-btn d-flex justify-content-center align-items-center" id="contact" href="#" data-uri="/contact">
          <p data-uri="/contact">Contact</p>
        </a>
        <a class="nav-link nav-btn d-flex justify-content-center align-items-center" id="info" href="#" data-uri="/info">
          <p data-uri="/info">Mes infos</p>
        </a>
        <a class="nav-link nav-logout d-flex justify-content-center align-items-center" href="#" data-uri="/logout">
          <p data-uri="/logout"><i class="fa-solid fa-arrow-right-from-bracket" data-uri="/logout"></i></p>
        </a>
      </nav>
    `;

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