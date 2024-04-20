import {awaitFront, showNavStyle} from "../../utils/function";
import {getAuthenticatedUser} from "../../utils/session";

const hideTooltip = () => {
  document.getElementById('tooltip').style.display = 'none';
}

const drawPieChart = (canvas, percentages, colors) => {
  // Check percentage & colors having same length
  if (percentages.length !== colors.length) {
    console.error(
        "Les tableaux de pourcentages et de couleurs doivent avoir la même longueur.");
    return;
  }

  // Get 2D context from canvas
  const ctx = canvas.getContext('2d');

  // Center of circle
  const centerX = canvas.width / 2;
  const centerY = canvas.height / 2;

  // Circle radius (get min from half width & height)
  const radius = Math.min(centerX, centerY);

  // Total percentage
  const totalPercentage = percentages.reduce(
      (total, percentage) => total + percentage, 0);

  // Start angle for each slice
  // let startAngle = -0.5 * Math.PI;
  let startAngle = 0;

  // Draw slice
  const drawSlice = (currentAngle, endAngle, color) => {
    ctx.fillStyle = color;
    ctx.beginPath();
    ctx.moveTo(centerX, centerY);
    ctx.arc(centerX, centerY, radius, currentAngle, endAngle);
    ctx.closePath();
    ctx.fill();
  }

  // Draw each slice of pie chart
  percentages.forEach((percentage, index) => {
    // Calc angle for each slice
    const sliceAngle = (percentage / totalPercentage) * 2 * Math.PI;

    // Draw slice
    drawSlice(startAngle, startAngle + sliceAngle, colors[index]);

    // Update starting angle for next slice
    startAngle += sliceAngle;
  });

  const tooltip = document.querySelector('#tooltip');

  canvas.addEventListener('mousemove', (event) => {
    const rect = canvas.getBoundingClientRect();
    const mouseX = event.clientX - rect.left;
    const mouseY = event.clientY - rect.top;

    const distanceFromCenter = Math.sqrt(
        (mouseX - centerX) ** 2 + (mouseY - centerY) ** 2);

    if (distanceFromCenter <= radius) {
      // Calc angle from hovered slice
      const angle = Math.atan2(mouseY - centerY, mouseX - centerX);
      const normalizedAngle = (angle < 0) ? (2 * Math.PI + angle) : angle;

      // Find hovered slice index
      let hoveredSliceIndex = -1;
      let cumulativeAngle = 0;
      for (let i = 0; i < percentages.length; i += 1) {
        const sliceAngle = (percentages[i] / totalPercentage) * 2 * Math.PI;
        if (normalizedAngle >= cumulativeAngle && normalizedAngle
            <= cumulativeAngle + sliceAngle) {
          hoveredSliceIndex = i;
          break;
        }
        cumulativeAngle += sliceAngle;
      }

      // If hovered slice, show tooltip
      if (hoveredSliceIndex !== -1) {
        const percentage = (percentages[hoveredSliceIndex] / totalPercentage)
            * 100;
        const text = `${percentage.toFixed(2)}%`;

        // Show tooltip next to cursor
        tooltip.textContent = text;
        tooltip.style.left = `${event.pageX + 10}px`;
        tooltip.style.top = `${event.pageY - 20}px`;
        tooltip.style.display = 'block';
      }
    }
  });

  canvas.addEventListener('mouseleave', () => {
    hideTooltip();
  })

}

const renderChart = (internshipStats) => {
  const chartContainer = document.querySelector('.chartCT');
  const percent = (internshipStats.internshipCount
      / internshipStats.totalStudents) * 100;
  console.log(percent)

  // const leftPercentage = 100-percent;
  // const rightPercentage = percent;

  // const leftRotateAngle = -90 + (leftPercentage * 1.8);
  // const rightRotateAngle = 90 - (rightPercentage * 1.8);

  /* const percentInternship = `<div class="percentage-value left">${percent.toFixed(
      2)}%</div>`;
  const percentTotal = `<div class="percentage-value right">${(100
      - percent).toFixed(2)}%</div>`; */
  /* chartContainer.innerHTML = `
    <div id="chart" style="background: conic-gradient(white 0% ${100
  - percent}%, #119DB8 ${100 - percent}% 100%);">
      <div class="left-zone">${percentInternship}</div>
      <div class="right-zone">${percentTotal}</div>
    </div>
  `; */
  chartContainer.innerHTML = `
    <canvas class="myChart" width="150" height="150"></canvas>
  `;
  const chartCanvas = document.querySelector('.myChart');
  drawPieChart(chartCanvas, [percent, 100 - percent], ['#119DB8', 'white']);

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
      year => `<option value="${year}" onclick="renderChart(internshipStats.internshipCount)">${year}</option>`).join(
      '');

  selectYear.addEventListener('change', (e) => {
    const selectedYear = e.target.value;
    const selectedStats = internshipStats[selectedYear];
    renderChart(selectedStats);
    renderCaption(selectedStats);
    hideTooltip();
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
                  <!--<div id="chart" style="background: conic-gradient(white 0% 25%, #119DB8 25% 100%);">
                  </div>-->
                  <div class="chartCT d-flex justify-content-center align-items-center">
                    <canvas class="myChart"></canvas>
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

  // Créer un élément pour le tooltip
  const tooltip = document.createElement('div');
  tooltip.id = 'tooltip';
  tooltip.style.position = 'absolute';
  tooltip.style.display = 'none';
  tooltip.style.background = 'rgba(0, 0, 0, 0.3)';
  tooltip.style.color = '#fff';
  tooltip.style.padding = '5px';
  tooltip.style.borderRadius = '5px';
  document.body.appendChild(tooltip);

  renderYearOptions(internshipStats);
  renderChart(internshipStats[Object.keys(internshipStats)[0]]);
  renderCaption(internshipStats[Object.keys(internshipStats)[0]]);

}

export default AdminDashboardPage;