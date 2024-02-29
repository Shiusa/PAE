// eslint-disable-next-line import/no-cycle
import { Redirect } from "../Router/Router";
import {getUserSessionData} from "../../utils/session";

const HomePage = () => {

  const user = getUserSessionData();

  const main = document.querySelector('main');

  main.innerHTML = `
        <div class="temp">
          <h1>Bienvenue sur proStage<span id="test-email"></span></h1>
          <p class="con">Clique ici pour te connecter</p>
        </div>
  `;

  document.querySelector(".con").addEventListener("click", () => {
    Redirect("/login");
  });



  const emailSpan = document.getElementById("test-email");
  const lienCon = document.querySelector(".con");

  if(user) {
    // eslint-disable-next-line
    emailSpan.innerHTML = ", " + user.user.prenom;
    lienCon.style.display = "none";
  }


  const allNav = document.querySelectorAll(".nav-btn");

  allNav.forEach((nav) => {
    const newNav = nav;
    newNav.style.boxShadow = "0px 0px 0px";
  });

  const homeNav = document.getElementById("home");
  homeNav.style.boxShadow = "8px 8px 0px var(--ma-couleur)";
};

export default HomePage;