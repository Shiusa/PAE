import {awaitFront, showNavStyle} from "../../utils/function";
import {getToken} from "../../utils/session";
// eslint-disable-next-line import/no-cycle
import {Redirect} from "../Router/Router";

/* const dataCompany = {
  "1": {
    "id": 1,
    "name": "BNP Paribas",
    "designation": "ZBruxelles",
    "address": "Jsp qsdkj qsdlkjqsd",
    "phoneNumber": "027001313",
    "email": "",
    "isBlacklisted": false,
    "blacklistMotivation": "",
    "version": 1,
    "data": {
      "2023-2024": 4,
      "2022-2023": 1
    }
  },
  "2": {
    "id": 2,
    "name": "BNP Paribas",
    "designation": "Namur",
    "address": "2 Jsp qsdkj qsdlkjqsd",
    "phoneNumber": "2027001313",
    "email": "",
    "isBlacklisted": false,
    "blacklistMotivation": "",
    "version": 1,
    "data": {
      "2023-2024": 3,
      "2022-2023": 3
    }
  },
  "3": {
    "id": 2,
    "name": "BNP Paribas",
    "designation": "Namur",
    "address": "2 Jsp qsdkj qsdlkjqsd",
    "phoneNumber": "2027001313",
    "email": "",
    "isBlacklisted": false,
    "blacklistMotivation": "",
    "version": 1,
    "data": {
      "2023-2024": 3,
      "2022-2023": 3
    }
  },
  "4": {
    "id": 2,
    "name": "BNP Paribas",
    "designation": "Namur",
    "address": "2 Jsp qsdkj qsdlkjqsd",
    "phoneNumber": "2027001313",
    "email": "",
    "isBlacklisted": false,
    "blacklistMotivation": "",
    "version": 1,
    "data": {
      "2023-2024": 3,
      "2022-2023": 3
    }
  },
  "5": {
    "id": 2,
    "name": "BNP Paribas",
    "designation": "Namur",
    "address": "2 Jsp qsdkj qsdlkjqsd",
    "phoneNumber": "2027001313",
    "email": "",
    "isBlacklisted": false,
    "blacklistMotivation": "",
    "version": 1,
    "data": {
      "2023-2024": 3,
      "2022-2023": 3
    }
  },
  "6": {
    "id": 2,
    "name": "BNP Paribas",
    "designation": "Namur",
    "address": "2 Jsp qsdkj qsdlkjqsd",
    "phoneNumber": "2027001313",
    "email": "",
    "isBlacklisted": false,
    "blacklistMotivation": "",
    "version": 1,
    "data": {
      "2023-2024": 3,
      "2022-2023": 3
    }
  },
  "7": {
    "id": 2,
    "name": "BNP Paribas",
    "designation": "Namur",
    "address": "2 Jsp qsdkj qsdlkjqsd",
    "phoneNumber": "2027001313",
    "email": "",
    "isBlacklisted": false,
    "blacklistMotivation": "",
    "version": 1,
    "data": {
      "2023-2024": 3,
      "2022-2023": 3
    }
  },
  "8": {
    "id": 2,
    "name": "BNP Paribas",
    "designation": "Namur",
    "address": "2 Jsp qsdkj qsdlkjqsd",
    "phoneNumber": "2027001313",
    "email": "",
    "isBlacklisted": false,
    "blacklistMotivation": "",
    "version": 1,
    "data": {
      "2023-2024": 3,
      "2022-2023": 3
    }
  },
  "9": {
    "id": 2,
    "name": "BNP Paribas",
    "designation": "Namur",
    "address": "2 Jsp qsdkj qsdlkjqsd",
    "phoneNumber": "2027001313",
    "email": "",
    "isBlacklisted": false,
    "blacklistMotivation": "",
    "version": 1,
    "data": {
      "2023-2024": 3,
      "2022-2023": 3
    }
  }
} */

let dataCompany;

const sortData = (data, sortingType) => Object.values(data).sort((a, b) => {
  const valueA = a[sortingType] ? a[sortingType].toLowerCase() : '';
  const valueB = b[sortingType] ? b[sortingType].toLowerCase() : '';

  if (sortingType === 'name' && valueA === valueB) {
    const designationA = a.designation ? a.designation.toLowerCase() : '';
    const designationB = b.designation ? b.designation.toLowerCase() : '';
    return designationA.localeCompare(designationB);
  }
  return valueA.localeCompare(valueB);
})

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

const renderCompanyList = (companyData) => {
  const listContainer = document.querySelector(
      '.adminCompanyListTileContainer');
  const selectedYear = document.querySelector('.year-list').value;

  listContainer.innerHTML = Object.values(companyData).map((data) => {
    let dataValue;
    if (selectedYear !== "Par défaut") {
      dataValue = data.data[selectedYear];
    } else {
      dataValue = Object.values(data.data).reduce((acc, curr) => acc + curr, 0);
    }
    return `
      <div class="w-100 d-flex align-items-center justify-content-center rounded-3 border mt-3 py-3 adminCompanyListTile">
        <div class="d-flex align-items-center justify-content-center" style="width: 30%; border-right: 2px solid white;">
          <p class="p-0 m-0 text-center">${data.name}</p>
        </div>
        <div class="d-flex align-items-center justify-content-center" style="width: 30%; border-right: 2px solid white;">
          <p class="p-0 m-0 text-center">${data.designation ? data.designation
        : '-'}</p>
        </div>
        <div class="d-flex align-items-center justify-content-center" style="width: 20%; border-right: 2px solid white;">
          <p class="p-0 m-0 text-center">${data.phoneNumber}</p>
        </div>
        <div class="d-flex align-items-center justify-content-center" style="width: 10%; border-right: 2px solid white;">
          <p class="p-0 m-0 text-center">${dataValue === undefined ? 0
        : dataValue}</p>
        </div>
        <div class="d-flex align-items-center justify-content-center" style="width: 10%">
          <p class="p-0 m-0 text-center">${data.isBlacklisted ? "OUI" : "NON"}</p>
        </div>
      </div>
    `;
  }).join('');
}

const renderYearOptions = (internshipStats) => {
  const selectYear = document.querySelector('.year-list');
  const years = Object.keys(internshipStats);

  years.unshift("Par défaut");

  selectYear.innerHTML = years.map(
      year => `<option value="${year}">${year}</option>`).join(
      '');

  selectYear.addEventListener('change', (e) => {
    const selectedYear = e.target.value;
    let selectedStats;

    if (selectedYear === "Par défaut") {
      /* const allYearsStats = Object.values(internshipStats).reduce(
          (acc, curr) => {
            acc.internshipCount += curr.internshipCount;
            acc.totalStudents += curr.totalStudents;
            return acc;
          }, {internshipCount: 0, totalStudents: 0});
      selectedStats = allYearsStats; */
      selectedStats = internshipStats[years[1]];
    } else {
      selectedStats = internshipStats[selectedYear];
    }

    renderChart(selectedStats);
    renderCaption(selectedStats);
    hideTooltip();

    renderCompanyList(dataCompany);
  })
}

const addColumnHeaderListeners = () => {
  const headers = document.querySelectorAll('.adminCompanyListTitle div');
  headers.forEach(header => {
    header.addEventListener('click', () => {
      const paragraph = header.querySelector('p');
      let sortingType;
      switch (paragraph.textContent.trim()) {
        case 'Nom':
          sortingType = 'name';
          break;
        case 'Appellation':
          sortingType = 'designation';
          break;
        case 'Numéro de téléphone':
          sortingType = 'phoneNumber';
          break;
        default:
          sortingType = 'name'; // Par défaut, tri par nom
      }
      const sortedData = sortData(dataCompany, sortingType);
      renderCompanyList(sortedData);
    });
  });
}

const fetchInternshipStat = async () => {
  const user = getToken();
  try {
    const response = await fetch('api/internships/stats/year', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': user
      }
    });

    if (!response.ok) {
      throw new Error(
          `fetch error : ${response.status} : ${response.statusText}`
      );
    }

    return response.json();
  } catch (error) {
    if (error instanceof Error && error.message.startsWith(
        "fetch error : 403")) {
      Redirect('/');
    }
    return null;
  }
}

const fetchCompaniesData = async () => {
  const user = getToken();
  try {
    const response = await fetch('api/companies/all', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': user
      }
    });

    if (!response.ok) {
      throw new Error(
          `fetch error : ${response.status} : ${response.statusText}`
      );
    }

    return response.json();
  } catch (error) {
    if (error instanceof Error && error.message.startsWith(
        "fetch error : 403")) {
      Redirect('/');
    }
    return null;
  }
}

const AdminDashboardPage = async () => {

  const main = document.querySelector('main');
  awaitFront();
  const internshipStats = await fetchInternshipStat();
  if (internshipStats === null) {
    return;
  }
  dataCompany = await fetchCompaniesData();
  console.log(internshipStats["2023-2024"]);
  showNavStyle("dashboard");

  const sortDataCompany = sortData(dataCompany, 'name');

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
            <div class="rounded-4 dash-row p-4" style="border: 2px solid #119cb8c7; margin-left: 4rem;">
              <div class="col-md-12 d-flex flex-column h-100">
              
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
                
                <div class="w-100 d-flex flex-column overflow-y-auto adminCompanyListTileContainer" style="scrollbar-width:none;">
                
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

  renderCompanyList(sortDataCompany);
  addColumnHeaderListeners();

}

export default AdminDashboardPage;