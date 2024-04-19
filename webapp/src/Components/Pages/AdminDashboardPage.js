import {awaitFront, showNavStyle} from "../../utils/function";
import {getAuthenticatedUser} from "../../utils/session";

const renderYearOptions = (internshipStats) => {
  const selectYear = document.querySelector('.year-list');
  // let optionsYear = '';
  /* let i = 0;
  while (i < internshipStats.length) {
    optionsYear += `<option value="${internshipStats.year}">${internshipStats.year}</option>`
    i += 1;
  } */
  selectYear.innerHTML = Object.keys(internshipStats).map(
      year => `<option value="${year}">${year}</option>`).join('');
}

const fetchInternshipStat = async () => {
  const user = getAuthenticatedUser();
  const response = await fetch('api/internships/stats/year', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': user.token
    }
  });
  return response.json();
}

const AdminDashboardPage = async () => {

  const main = document.querySelector('main');
  awaitFront();
  const internshipStats = await fetchInternshipStat();
  console.log(internshipStats["2023-2024"]);
  showNavStyle("dashboard");

  main.innerHTML = `
    <div class="container-fluid justify-content-center align-items-center mt-5 mb-5 mx-auto">
      <div class="row">
        <div class="col-md-3">
          <div class="card">
            <div class="card-body d-flex flex-column align-items-center">
            
              <p class="mb-0">Année académique</p>
              <select class="year-list custom-select-options w-50 rounded-1 text-center mb-5">
                <option selected>2023-2024</option>
                <option value="1">One</option>
                <option value="2">Two</option>
                <option value="3">Three</option>
              </select>
              <div class="bg-secondary w-75 mb-4 d-flex justify-content-center align-items-center" style="--percent:85; width: 100%; height: 200px;">
                <!--<svg viewBox="0 0 100 100">
                  <circle cx="50" cy="50" r="15" id="chart"></circle>
                </svg>-->
                <div id="chart" style="background: conic-gradient(blue 0% 25%, white 25% 100%)">
                  
                </div>
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

  renderYearOptions(internshipStats);

}

export default AdminDashboardPage;