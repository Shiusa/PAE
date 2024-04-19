import {awaitFront, showNavStyle} from "../../utils/function";
import {getAuthenticatedUser} from "../../utils/session";


const renderChart = (internshipStats) => {
  const chartContainer = document.querySelector('.chart-container');
  const percent = (internshipStats.internshipCount/internshipStats.totalStudents)*100;
  console.log(percent)

  // const leftPercentage = 100-percent;
  // const rightPercentage = percent;

  // const leftRotateAngle = -90 + (leftPercentage * 1.8);
  // const rightRotateAngle = 90 - (rightPercentage * 1.8);

  const percentInternship = `<div class="percentage-value left">${percent.toFixed(2)}%</div>`;
  const percentTotal = `<div class="percentage-value right">${(100 - percent).toFixed(2)}%</div>`;
  chartContainer.innerHTML = `
    <div id="chart" style="background: conic-gradient(white 0% ${100-percent}%, #119DB8 ${100-percent}% 100%);">
      <div class="left-zone">${percentInternship}</div>
      <div class="right-zone">${percentTotal}</div>
    </div>
  `;
}

const renderCaption = (internshipStats) => {
  const caption = document.querySelector('.stat-caption');
  caption.innerHTML = `
    <p class="mt-3 mb-3">Total : ${internshipStats.totalStudents} étudiants</p>
    <div class="rounded-5 mb-2 cap-1 px-2">
      <p class="m-0">Pas de stage</p>
    </div>
    <div class="rounded-5 mb-3 cap-2 px-2">
      <p class="m-0">Ont un stage</p>
    </div>
  `;
}

const renderYearOptions = (internshipStats) => {
  const selectYear = document.querySelector('.year-list');
  selectYear.innerHTML = Object.keys(internshipStats).map(
      year => `<option value="${year}" onclick="renderChart(internshipStats.internshipCount)">${year}</option>`).join('');

  selectYear.addEventListener('change', (e) => {
    const selectedYear = e.target.value;
    const selectedStats = internshipStats[selectedYear];
    renderChart(selectedStats);
    renderCaption(selectedStats);
  })
}

const fetchInternshipStat = async () => {
  const user = await getAuthenticatedUser();
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
          <div class="card chart-card dash-row">
            <div class="card-body d-flex flex-column align-items-center">
            
              <p class="mb-0 mt-1">Année académique</p>
              <select class="year-list custom-select-options w-50 rounded-1 text-center mb-4 py-1 border-0">
                <option selected>2023-2024</option>
                <option value="1">One</option>
                <option value="2">Two</option>
                <option value="3">Three</option>
              </select>
              <div class="chart-container w-75 mb-4 d-flex justify-content-center align-items-center" style="width: 100%; height: 200px;">
                <div class="chart-container" >
                  <div id="chart" style="background: conic-gradient(white 0% 25%, #119DB8 25% 100%);">
                  </div>
                </div>
              </div>
              <div class="stat-caption w-75 d-flex flex-column align-items-center rounded-3">
                <p class="mt-3 mb-3">Total : 115 étudiants</p>
                <div class="rounded-5 mb-2 cap-1 px-2">
                  <p class="m-0">Pas de stage</p>
                </div>
                <div class="rounded-5 mb-3 cap-2 px-2">
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
  renderChart(internshipStats[Object.keys(internshipStats)[0]]);
  renderCaption(internshipStats[Object.keys(internshipStats)[0]]);

}

export default AdminDashboardPage;