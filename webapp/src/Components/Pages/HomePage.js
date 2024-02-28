
const HomePage = () => {



  const allNav = document.querySelectorAll(".nav-btn");

  allNav.forEach((nav) => {
    const newNav = nav;
    newNav.style.boxShadow = "0px 0px 0px";
  });

  const homeNav = document.getElementById("home");
  homeNav.style.boxShadow = "8px 8px 0px var(--ma-couleur)";
};

export default HomePage;