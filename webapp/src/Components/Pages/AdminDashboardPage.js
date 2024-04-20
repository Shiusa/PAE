import {awaitFront, showNavStyle} from "../../utils/function";
import {getAuthenticatedUser} from "../../utils/session";

const hideTooltip = () => {
  document.getElementById('tooltip').style.display = 'none';
}

const drawPieChart = (canvas, dataSet, colors) => {

  // Check percentage & colors having same length
  if (dataSet.length !== colors.length) {
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
  const totalValue = dataSet.reduce(
      (total, currentValue) => total + currentValue, 0);

  // Start angle for each slice
  let startAngle = 0;

  // Draw slice
  const drawSlice = (currentAngle, endAngle, color, duration) => {
    const startTime = performance.now();
    const endTime = startTime + duration;
    const draw = () => {
      const now = performance.now();
      const progress = Math.min((now - startTime) / duration, 1);
      const angle = currentAngle + (endAngle - currentAngle) * progress;

      ctx.fillStyle = color;
      ctx.beginPath();
      ctx.moveTo(centerX, centerY);
      ctx.arc(centerX, centerY, radius, currentAngle, angle);
      ctx.closePath();
      ctx.fill();

      if (now < endTime) {
        requestAnimationFrame(draw);
      }
    };
    requestAnimationFrame(draw);
  };

  // Draw each slice of pie chart
  dataSet.forEach((percentage, index) => {
    // Calc angle for each slice
    const sliceAngle = (percentage / totalValue) * 2 * Math.PI;

    // Draw slice
    drawSlice(startAngle, startAngle + sliceAngle, colors[index],
        250);

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
      for (let i = 0; i < dataSet.length; i += 1) {
        const sliceAngle = (dataSet[i] / totalValue) * 2 * Math.PI;
        if (normalizedAngle >= cumulativeAngle && normalizedAngle
            <= cumulativeAngle + sliceAngle) {
          hoveredSliceIndex = i;
          break;
        }
        cumulativeAngle += sliceAngle;
      }

      // If hovered slice, show tooltip
      if (hoveredSliceIndex !== -1) {
        const percentage = (dataSet[hoveredSliceIndex] / totalValue)
            * 100;
        // Show tooltip next to cursor
        tooltip.textContent = `${percentage.toFixed(2)}%`;
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

  chartContainer.innerHTML = `
    <canvas class="myChart" width="164" height="164"></canvas>
  `;
  const chartCanvas = document.querySelector('.myChart');
  drawPieChart(chartCanvas, [percent, 100 - percent], ['#119DB8', 'white']);

}

const renderCaption = (internshipStats) => {

  const caption = document.querySelector('.stat-caption');
  caption.innerHTML = `
    <p class="mt-3 mb-0">Total : ${internshipStats.totalStudents} étudiants</p>
    <p class="mt-0 mb-3">Ont un stage : ${internshipStats.internshipCount} étudiants</p>
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
    <div class="container-fluid justify-content-center align-items-center mt-5 mb-5 mx-auto" style="border: none">
      <div class="row mx-2">
        <div class="col-md-3">
          <div class="card chart-card dash-row rounded-4">
            <div class="card-body d-flex flex-column align-items-center justify-content-around py-4">
            
              <div class="d-flex flex-column align-items-center w-50">
                <p class="mb-0 mb-2">Année académique</p>
                <select class="year-list custom-select-options rounded-1 text-center mb-4 py-1 border-0 w-100">
                  <option selected>2023-2024</option>
                  <option value="1">One</option>
                  <option value="2">Two</option>
                  <option value="3">Three</option>
                </select>
              </div>
              
              <div class="chart-container w-75 mb-4 d-flex justify-content-center align-items-center" style="width: 100%; height: 200px;">
                <div class="chart-container" >
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
          <div class="dash-row">
            <div class="rounded-4 dash-row p-4" style="border: 1px solid #119DB8; margin-left: 4rem;">
              <div class="col-md-12 d-flex flex-column justify-content-center align-items-center overflow-hidden">
              
                <div class="w-100 d-flex justify-content-center align-items-center border adminCompanyListTitle">
                  <div class="d-flex align-items-center justify-content-center" style="width: 30%">
                      <p class="p-2 m-0 text-center">Nom</p>
                  </div>
                  <div class="d-flex align-items-center justify-content-center" style="width: 30%">
                      <p class="p-2 m-0 text-center">Appellation</p>
                  </div>
                  <div class="d-flex align-items-center justify-content-center" style="width: 20%">
                      <p class="p-2 m-0 text-center">Numéro de téléphone</p>
                  </div>
                  <div class="d-flex align-items-center justify-content-center" style="width: 10%">
                      <p class="p-2 m-0 text-center">Pris en stage</p>
                  </div>
                  <div class="d-flex align-items-center justify-content-center" style="width: 10%">
                      <p class="p-2 m-0 text-center">Black-listé</p>
                  </div>
                </div>
                
                <div class="w-100 d-flex flex-column justify-content-center align-items-center overflow-auto">
                
                  <div class="w-100 d-flex align-items-center justify-content-center rounded-3 border my-3 py-3 adminCompanyListTile">
                    <div class="d-flex align-items-center justify-content-center" style="width: 30%; border-right: 2px solid white;">
                      <p class="p-0 m-0 text-center">BNP Paribas</p>
                    </div>
                    <div class="d-flex align-items-center justify-content-center" style="width: 30%; border-right: 2px solid white;">
                      <p class="p-0 m-0 text-center">Bruxelles</p>
                    </div>
                    <div class="d-flex align-items-center justify-content-center" style="width: 20%; border-right: 2px solid white;">
                      <p class="p-0 m-0 text-center">02700500400</p>
                    </div>
                    <div class="d-flex align-items-center justify-content-center" style="width: 10%; border-right: 2px solid white;">
                      <p class="p-0 m-0 text-center">60</p>
                    </div>
                    <div class="d-flex align-items-center justify-content-center" style="width: 10%">
                      <p class="p-0 m-0 text-center">NON</p>
                    </div>
                  </div>
                  
                </div>
                <!--<p>test 3/4</p>-->
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `;

  // Create tooltip
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