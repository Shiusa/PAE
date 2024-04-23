import {awaitFront, showNavStyle} from "../../utils/function";
/* eslint-disable prefer-template */
// eslint-disable-next-line import/no-cycle
import {Redirect} from "../Router/Router";

import {getAuthenticatedUser,} from "../../utils/session";

const CreateInternshipPage = async () => {

  const main = document.querySelector('main');
  awaitFront();

  const user = await getAuthenticatedUser();

  showNavStyle("dashboard");

  const readUserInfo = async () => {
    const options = {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': user.token
      }
    }
    const response = await fetch('api/users/' + user.user.id, options);

    if (!response.ok) {
      throw new Error(
          `fetch error : ${response.status} : ${response.statusText}`);
    }

    const userInfo = await response.json();
    return userInfo;
  };


}