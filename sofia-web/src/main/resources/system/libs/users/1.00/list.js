const LIST_USERS_URL = '/admin/users/list.html';

class UserList {
    constructor() {
    }
    loadUserList() {
        const loadList = () => {
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

        const createList = data => {
            const elementsToDelete = document.getElementsByClassName('dataCell');
            while (elementsToDelete.length > 0) {
                elementsToDelete[0].parentNode.removeChild(elementsToDelete[0]);
            }
            data.list.forEach(element => {
                const userElement = document.createElement('DIV');
                userElement.className = 'cell dataCell';

                const username = `<div class="name">${element.username}</div>`;
                const locale = `<div class="locale">${element.locale}</div>`;



                let groupsInnerHTML = '';
                element.groups.forEach(element => {
                    groupsInnerHTML += `<div>${element.name}</div>`;
                });
                const groups = `<div>${groupsInnerHTML}</div>`;

                const cellMenuDataElement = document.createElement('DIV');
                cellMenuDataElement.className = 'callData';
                cellMenuDataElement.innerHTML = `${username}${locale}${groups}`;

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

                const firstSeparatorNode = document.createElement('DIV');
                firstSeparatorNode.innerHTML = '&#124;';
                firstSeparatorNode.className = 'menuSeparator';
                cellMenuElement.appendChild(firstSeparatorNode);

                const cellMenuItemDeleteElement = document.createElement('DIV');
                Core.setText(cellMenuItemDeleteElement, 'delete');
                new LinkTo({
                    element: cellMenuItemDeleteElement,
                    onclick: () => {
                        fetch(`/v1/users/${element.id}`, { method: 'DELETE' })
                            .then(response => {
                                if (response.status === 200) {
                                    loadList();
                                    if (Core.isFunction(this.afterDeleteFunction)) {
                                        this.afterDeleteFunction();
                                    }
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

                const secondSeparatorNode = document.createElement('DIV');
                secondSeparatorNode.innerHTML = '&#124;';
                secondSeparatorNode.className = 'menuSeparator';
                cellMenuElement.appendChild(secondSeparatorNode);

                const cellMenuItemPasswordElement = document.createElement('DIV');
                Core.setText(cellMenuItemPasswordElement, 'password');
                new LinkTo({
                    element: cellMenuItemPasswordElement,
                    onclick: `/admin/users/password.html?id=${element.id}`
                });
                cellMenuElement.appendChild(cellMenuItemPasswordElement);

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

        loadList();
    };
    afterDelete(afterDeleteFunction) {
        this.afterDeleteFunction = afterDeleteFunction;
    }
}


