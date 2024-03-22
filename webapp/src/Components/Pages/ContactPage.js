import showNavStyle from "../../utils/function";

const ContactPage = () => {
    const main = document.querySelector('main');
    main.innerHTML = `        
        <div class="test"></div>
    `;

    showNavStyle("contact");
};

export default ContactPage;