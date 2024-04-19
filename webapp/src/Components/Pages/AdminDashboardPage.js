import {awaitFront} from "../../utils/function";

const AdminDashboardPage = async () => {

  const main = document.querySelector('main');
  awaitFront();

  main.innerHTML = `
    <div class="container-fluid justify-content-center align-items-center mt-5 mb-5 mx-auto">
      <div class="row">
        <div class="col-md-3">
          <div class="card">
            <div class="card-body d-flex flex-column align-items-center">
            
              <p class="mb-0">Année académique</p>
              <select class="custom-select-options w-50 rounded-1 text-center mb-5">
                <option selected>2023-2024</option>
                <option value="1">One</option>
                <option value="2">Two</option>
                <option value="3">Three</option>
              </select>
              <div class="bg-secondary mb-4" style="width: 100%; height: 200px;">
              </div>
              <div class="legende bg-primary w-75 d-flex flex-column align-items-center rounded-3">
                <p class="mt-3 mb-3">Total : 115 étudiants</p>
                <div class="rounded-5 mb-2 bg-secondary">
                  <p class="m-0">Pas de stage</p>
                </div>
                <div class="rounded-5 mb-3 bg-secondary">
                  <p class="m-0">Ont un stage</p>
                </div>
              </div>
              
            </div>
          </div>
        </div>
        <div class="col-md-9">
          <p>test 3/4</p>
        </div>
      </div>
    </div>
  `;

}

export default AdminDashboardPage;