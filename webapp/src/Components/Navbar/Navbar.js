

const Navbar = async () => {

  const navbarWrapper = document.querySelector("#navbarWrapper");

  
  let role = true;
  let navbar = ``;
  role = false;

  if(role) {
    navbar = `
      <nav class="d-flex justify-content-center align-items-center">
        <a class="nav-link" href="#" data-uri="/"><div class="nav-btn d-flex justify-content-center align-items-center" id="home">
          <p>Acceuil</p>
        </div></a>
        <div class="nav-logout d-flex justify-content-center align-items-center">
          <p><i class="fa-solid fa-arrow-right-from-bracket"></i></p>
        </div>
      </nav>
    `;

  } else {
    navbar = `
      <nav class="d-flex justify-content-center align-items-center">
        <a class="nav-link" href="#" data-uri="/"><div class="nav-btn d-flex justify-content-center align-items-center" id="home">
          <p>Acceuil</p>
        </div></a>
        <a class="nav-link" href="#" data-uri="/login"><div class="nav-btn d-flex justify-content-center align-items-center" id="login">
          <p>Connexion</p>
        </div></a>
        <a class="nav-link" href="#" data-uri="/register"><div class="nav-btn d-flex justify-content-center align-items-center" id="register">
          <p>Inscription</p>
        </div></a> 
      </nav>
    `;
  }


  

  navbarWrapper.innerHTML = navbar;

};

export default Navbar;