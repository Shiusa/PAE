import Navbar from "../Navbar/Navbar";
// eslint-disable-next-line import/extensions
import {removeSessionData} from "../../utils/session.js";
import Navigate from "../../utils/Navigate";

const Logout = () => {
  removeSessionData();
  // re-render the navbar for a non-authenticated user
  Navbar();
  Navigate("/");
};

export default Logout;