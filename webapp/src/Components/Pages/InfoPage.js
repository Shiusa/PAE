import showNavStyle from "../../utils/function";


const InfoPage = () => {

    const main = document.querySelector('main');
    main.innerHTML = `        
        <div class="">
          <div class="">
            <h1>Voici vos informations :</h1>
            <h2>Vous pouvez les modifier à tout moment</h2>
            <div class="input-group mb-3">
                <span class="input-group-text"><i class="fa-solid fa-user"></i></span>
                <p class="form-control">james.hutch@vinci.be</p>
            </div>
            <div class="input-group mb-3">
                <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-user"></i></span>
                <input type="text" class="form-control" id="input-surname" value="James" placeholder="Prénom" aria-label="Prénom" aria-describedby="basic-addon1">
            </div>
            <div class="input-group mb-3">
                <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-user"></i></span>
                <input type="text" class="form-control" id="input-name" value="Hutch" placeholder="Nom" aria-label="Nom" aria-describedby="basic-addon1">
            </div>
            <div class="input-group mb-3">
                <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-phone"></i></span>
                <input type="text" class="form-control" id="input-phone" value="0471475854" placeholder="Téléphone" aria-label="Téléphone" aria-describedby="basic-addon1">
            </div>
            <p class="btn-login" id="login-btn">Enregistrer les modifications</p>
          </div>
          <div class=""></div>
          <div class=""></div>
        </div>
    `;


    showNavStyle("info");
};

export default InfoPage;