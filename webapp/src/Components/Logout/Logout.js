// eslint-disable-next-line import/no-cycle, import/extensions
import { Redirect } from "../Router/Router.js";
// eslint-disable-next-line import/extensions
import Navbar from "../Navbar/Navbar.js";
// eslint-disable-next-line import/extensions
import {removeSessionData} from "../../utils/session.js";


const Logout = () => {
  removeSessionData();
  // re-render the navbar for a non-authenticated user
  Navbar();
  Redirect("/");
};

export default Logout;