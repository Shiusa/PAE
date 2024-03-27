import {showNavStyle, awaitFront} from "../../utils/function";
import {getAuthenticatedUser} from "../../utils/session";

const ContactPage = async () => {
    
    awaitFront();

    // eslint-disable-next-line
    const user = await getAuthenticatedUser();
    
    showNavStyle("contact");
    

};

export default ContactPage;