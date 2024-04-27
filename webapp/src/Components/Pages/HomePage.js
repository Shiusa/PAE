import {getAuthenticatedUser} from "../../utils/session";
import {showNavStyle} from "../../utils/function";
import Navigate from "../../utils/Navigate";

const HomePage = async () => {

  const user = await getAuthenticatedUser();
  showNavStyle("home");

  const main = document.querySelector('main');

  main.innerHTML = `
        <div class="temp">
          <h1>Bienvenue sur proStage<span id="test-email"></span></h1>
          <p class="con">Clique ici pour te connecter</p>
        </div>
  `;

  document.querySelector(".con").addEventListener("click", () => {
    Navigate("/login");
  });

  const emailSpan = document.getElementById("test-email");
  const lienCon = document.querySelector(".con");

  if (user) {
    // eslint-disable-next-line
    emailSpan.innerHTML = ", " + user.user.firstname;
    lienCon.style.display = "none";
  }

};

export default HomePage;