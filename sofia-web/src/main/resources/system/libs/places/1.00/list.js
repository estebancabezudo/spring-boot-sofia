const LIST_PLACES_URL = '/admin/places/list.html';

class PlaceList {
    constructor() {
    }
    loadPlacesList() {
        const createList = data => {
            const hasData = data => {
                return data !== undefined && data !== null && data.length > 0;
            };
            const getElement = v => {
                return hasData(v) ? `<div>${v}</div>` : '';
            };
            const getGroupElement = (colony, delegation, city) => {
                let text = hasData(colony) ? colony : '';
                text += hasData(delegation) ? text.length > 0 ? `, ${delegation}` : delegation : ''
                text += hasData(city) ? text.length > 0 ? `, ${city}` : city : ''
                return `<div>${text}</div>`;
            };
            data.list.forEach(place => {
                const getAdministrativeDivision = typeName => {
                    for (let administrativeDivision of place.administrativeDivisions) {
                        if (administrativeDivision.administrativeDivisionType.name === typeName) {
                            return administrativeDivision.name;
                        }
                    }
                };
                const placeElement = document.createElement('DIV');
                placeElement.className = 'cell dataCell';

                const streetName = place.street ? place.street.name : '';
                const name = `<div class="name">${place.name}</div>`;
                const address = `<div>${streetName} #${place.number} ${place.interiorNumber}</div>`;
                const cornerStreet = place.cornerStreet ? `<div>esquina ${place.cornerStreet.name}</div>` : '';
                const firstStreet = place.betweenStreets.firstStreet ? place.betweenStreets.firstStreet.name : null;
                const secondStreet = place.betweenStreets.secondStreet ? place.betweenStreets.secondStreet.name : null;
                let betweenStreets;
                if (firstStreet !== null && secondStreet !== null) {
                    betweenStreets = `<div>entre ${firstStreet} y ${secondStreet}</div>`;
                } else {
                    betweenStreets = '';
                }
                const references = hasData(place.references) ? `<div>${place.references}</div>` : '';
                const postalCode = hasData(place.postalCode) ? `<div>${place.postalCode}</div>` : '';
                const administrativeDivisions = getGroupElement(getAdministrativeDivision('colony'), getAdministrativeDivision('delegation'), getAdministrativeDivision('city'));
                const municipality = getElement(getAdministrativeDivision('municipality'));
                const stateCode = getAdministrativeDivision('state');

                const stateElement = document.createElement('DIV');
                Core.setText(stateElement, stateCode);

                const countryElement = document.createElement('DIV');
                Core.setText(countryElement, place.countryCode);

                const cellMenuDataElement = document.createElement('DIV');
                cellMenuDataElement.className = 'callData';
                cellMenuDataElement.innerHTML = `${name}${address}${cornerStreet}${betweenStreets}${references}${postalCode}${administrativeDivisions}${municipality}`;
                cellMenuDataElement.appendChild(stateElement);
                cellMenuDataElement.appendChild(countryElement);
                new LinkTo({
                    element: cellMenuDataElement,
                    onclick: `/admin/places/place.html?id=${place.id}`
                });


                placeElement.appendChild(cellMenuDataElement);

                const cellMenuElement = document.createElement('DIV');
                cellMenuElement.className = 'cellMenu';
                placeElement.appendChild(cellMenuElement);

                const cellMenuItemEditElement = document.createElement('DIV');
                Core.setText(cellMenuItemEditElement, 'edit');
                new LinkTo({
                    element: cellMenuItemEditElement,
                    onclick: `/admin/places/place.html?id=${place.id}`
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
                        fetch(`/v1/places/${place.id}`, { method: 'DELETE' })
                            .then(response => {
                                if (response.status === 200) {
                                    Core.showMessage('placeDeleted');
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

                listContainer.appendChild(placeElement);
            });
        }

        const listContainer = document.getElementById('listContainer');
        listContainer.innerText = '';

        const addPlaceElement = document.createElement('DIV');
        addPlaceElement.className = 'cell cellToAdd';
        listContainer.appendChild(addPlaceElement);
        addPlaceElement.innerHTML =
            '<svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" fill="gray" class="bi bi-plus" viewBox="0 0 16 16"> <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z"/></svg><div id="textForAddPlace"></div>';
        new LinkTo({
            element: addPlaceElement,
            onclick: `/admin/places/addPlace.html`
        });

        fetch('/v1/places?includeAdministrativeDivisionList=true')
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