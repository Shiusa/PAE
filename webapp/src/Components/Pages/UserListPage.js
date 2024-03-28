import {showNavStyle, awaitFront} from "../../utils/function";
import {getAuthenticatedUser} from "../../utils/session";

const UserListPage = async () => {

  const main = document.querySelector('main');

  awaitFront();

  // eslint-disable-next-line
  const user = await getAuthenticatedUser();

  showNavStyle("userList");

  const readAllUsers = async () => {
      const options = {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': user.token
        }
      }
      const response = await fetch('api/users/all', options);

      if (!response.ok) {
        throw new Error(
            `fetch error : ${response.status} : ${response.statusText}`);
      }

      const userInfo = await response.json();
      return userInfo;
  };

  const usersTable = await readAllUsers();

  main.innerHTML = `
    <div class="d-flex justify-content-center align-items-center">
      <div class="users-container w-75 d-flex justify-content-center align-items-center mt-5 flex-column mb-5">
        <h1>Il n'y a aucun user</h1>
      </div>
    </div>
  `;

  const usersContainer = document.querySelector('.users-container');

  showUsersList(usersTable)

  function showUsersList(users) {
    usersContainer.innerHTML = ``;

    let u = 0;
    let info = ``;
    while (u < users.length) {
      info += `
            <div class="user-info-box d-flex justify-content-center align-items-center mt-2">
              <p>${users[u].firstname} ${users[u].lastname}</p>
              <p>${users[u].email}</p>
              <p>${users[u].phoneNumber}</p>
              <p>${users[u].role}</p>
            </div>
        `;
      u += 1;
    }
    usersContainer.innerHTML = info;
  }


};

export default UserListPage;
