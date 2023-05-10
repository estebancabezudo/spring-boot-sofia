const LIST_USERS_URL = '/admin/users/list.html';

class UserList {
    constructor() {
    }
    loadUserList() {
        const createList = data => {
            const hasData = data => {
                return data !== undefined && data !== null && data.length > 0;
            };
            data.list.forEach(element => {
                const userElement = document.createElement('DIV');
                userElement.className = 'cell dataCell';

                const username = `<div class="name">${element.username}</div>`;
                let groupsInnerHTML = '';
                element.groups.forEach(element => {
                    groupsInnerHTML += `<div>${element.name}</div>`;
                });
                const groups = `<div>${groupsInnerHTML}</div>`;

                const cellMenuDataElement = document.createElement('DIV');
                cellMenuDataElement.className = 'callData';
                cellMenuDataElement.innerHTML = `${username}${groups}`;

                userElement.appendChild(cellMenuDataElement);

                const cellMenuElement = document.createElement('DIV');
                cellMenuElement.className = 'cellMenu';
                userElement.appendChild(cellMenuElement);

                const cellMenuItemEditElement = document.createElement('DIV');
                Core.setText(cellMenuItemEditElement, 'edit');
                new LinkTo({
                    element: cellMenuItemEditElement,
                    onclick: `/admin/users/user.html?id=${element.id}`
                });
                cellMenuElement.appendChild(cellMenuItemEditElement);

                const separatorNode = document.createElement('DIV');
                separatorNode.innerHTML = '&#124;';
                separatorNode.className = 'menuSeparator';
                cellMenuElement.appendChild(separatorNode);

                const cellMenuItemDeleteElement = document.createElement('DIV');
                Core.setText(cellMenuItemDeleteElement, 'delete');
                new LinkTo({
                    element: cellMenuItemDeleteElement,
                    onclick: () => {
                        fetch(`/v1/users/${element.id}`, { method: 'DELETE' })
                            .then(response => {
                                if (response.status === 200) {
                                    Core.showMessage('userDeleted');
                                    location.reload();
                                } else {
                                    Core.showErrorMessage(Core.getText('responseErrorWhenDeleting', [response.status]));
                                }
                            })
                            .catch(error => {
                                Core.showErrorMessage(error);
                            });
                    }
                });
                cellMenuElement.appendChild(cellMenuItemDeleteElement);

                listContainer.appendChild(userElement);
            });
        }

        const listContainer = document.getElementById('listContainer');
        listContainer.innerText = '';

        const addUserElement = document.createElement('DIV');
        addUserElement.className = 'cell cellToAdd';
        listContainer.appendChild(addUserElement);
        addUserElement.innerHTML =
            '<svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" fill="gray" class="bi bi-plus" viewBox="0 0 16 16"> <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z"/></svg><div id="textForAddUser"></div>';
        new LinkTo({
            element: addUserElement,
            onclick: `/admin/users/addUser.html`
        });

        fetch('/v1/users')
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                }
                throw new Error(response.statusText);
            })
            .then(jsonResponse => {
                createList(jsonResponse.data);
            })
            .catch(error => {
                Core.showErrorMessage(error);
            });
    };

}


