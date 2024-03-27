// eslint-disable-next-line import/no-cycle
import { Redirect } from "../Router/Router";
import {getAuthenticatedUser} from "../../utils/session";
import {showNavStyle} from "../../utils/function";

const HomePage = async () => {
  showNavStyle("home");

  const user = await getAuthenticatedUser();



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
    emailSpan.innerHTML = ", " + user.user.firstname;
    lienCon.style.display = "none";
  }

};

export default HomePage;