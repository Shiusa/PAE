import {showNavStyle, awaitFront} from "../../utils/function";
import {getAuthenticatedUser} from "../../utils/session";

document.addEventListener("DOMContentLoaded", getAuthenticatedUser);

const ContactPage = () => {
  
    
    
    showNavStyle("contact");
    awaitFront();

};

export default ContactPage;