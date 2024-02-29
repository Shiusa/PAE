const Navbar = async () => {

  const navbarWrapper = document.querySelector("#navbarWrapper");

  const tokenLocalStorage = localStorage.getItem('accessToken');
  const tokenSessionStorage = sessionStorage.getItem('accessToken');

  const isConnected = !!tokenLocalStorage || !!tokenSessionStorage;

  let navbar = ``;

  if(isConnected) {
    navbar = `
      <nav class="d-flex justify-content-center align-items-center">
        <a class="nav-link nav-btn d-flex justify-content-center align-items-center" id="home" href="#" data-uri="/">
          <p data-uri="/">Accueil</p>
        </a>
        <a class="nav-link nav-logout d-flex justify-content-center align-items-center" href="#" data-uri="/">
          <p data-uri="/"><i class="fa-solid fa-arrow-right-from-bracket" data-uri="/"></i></p>
        </a>
      </nav>
    `;

  } else {
    navbar = `
      <nav class="d-flex justify-content-center align-items-center">
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