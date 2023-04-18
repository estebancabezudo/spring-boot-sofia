const LIST_PLACES_URL = '/admin/places/list.html';

window.placeMX = class PlaceMX {
    constructor(data) {
        this.data = data;
    }
    setStateData(name) {
        this.placeStateSelector.lastValue = name;
        console.log(`lib::places::mx::1.00::index.js::setStateData::name: ${name}`);
        if (name === 'MX-CMX') {
            console.log(`lib::places::mx::1.00::index.js::setStateData::is MX-CMX`);
            this.placeMunicipalityLabel.classList.add('hide');
            this.placeMunicipalityInput.classList.add('hide');
            this.placeDelegationLabel.classList.remove('hide');
            this.placeDelegationInput.classList.remove('hide');
        } else {
            console.log(`lib::places::mx::1.00::index.js::setStateData::isn't MX-CMX`);
            this.placeMunicipalityLabel.classList.remove('hide');
            this.placeMunicipalityInput.classList.remove('hide');
            this.placeDelegationLabel.classList.add('hide');
            this.placeDelegationInput.classList.add('hide');
        }
    };

    sendRequest(method) {
        console.log('`lib::places::mx::1.00::index.js::sendRequest(${method})`');
        var data = {
            name: this.placeNameInput.value,
            street: this.placeStreetInput.value,
            number: this.placeNumberInput.value,
            interiorNumber: this.placeInteriorNumberInput.value,
            references: this.placeReferencesInput.value,
            postalCode: this.placepostalCodeInput.value,
            countryCode: this.placeCountrySelector.options[this.placeCountrySelector.selectedIndex].value,
            administrativeDivisions: [
                {
                    "name": this.placeColonyInput.value,
                    "administrativeDivisionType": {
                        "name": "colony"
                    }
                },
                {
                    "name": this.placeCityInput.value,
                    "administrativeDivisionType": {
                        "name": "city"
                    }
                },
                {
                    "name": this.placeStateSelector.options[this.placeStateSelector.selectedIndex].value,
                    "administrativeDivisionType": {
                        "name": "state"
                    }
                }
            ]
        };

        if (this.placeStateSelector.options[this.placeStateSelector.selectedIndex].value === 'MX-CMX') {
            data.administrativeDivisions.push({
                "name": this.placeDelegationInput.value,
                "administrativeDivisionType": {
                    "name": "delegation"
                }
            }
            );
        } else {
            data.administrativeDivisions.push(
                {
                    "name": this.placeMunicipalityInput.value,
                    "administrativeDivisionType": {
                        "name": "municipality"
                    }
                }
            );
        }

        console.log(`lib::places::mx::1.00::index.js::createGUI: `, data);

        const headers = new Headers();
        headers.append("Content-Type", "application/json");

        var requestOptions = {
            method,
            headers: headers,
            body: JSON.stringify(data),
            redirect: 'follow'
        };

        let url;
        if (method === 'POST') {
            url = `/v1/places`;
        } else {
            url = `/v1/places/${this.data.id}`
        }

        fetch(url, requestOptions)
            .then(response => response.json())
            .then(jsonResponse => {
                location.href = LIST_PLACES_URL;
            })
            .catch(error => Core.showErrorMessage(error.message));
    };

    sendDelete(id) {
        const headers = new Headers();
        headers.append("Content-Type", "application/json");

        var requestOptions = {
            method: 'DELETE',
            headers: headers,
            redirect: 'follow'
        };
        const url = `/v1/places/${id}`

        fetch(url, requestOptions)
            .then(response => response.json())
            .then(jsonResponse => {
                location.href = LIST_PLACES_URL;
            })
            .catch(error => Core.showErrorMessage(error.message));
    }

    createGUI(data, afterCreateGUI) {
        const setTextsInCountrySelectorsOptions = list => {
            for (let countryName of list) {
                const optionElement = document.createElement('option');
                optionElement.value = countryName;
                Core.setText(optionElement, countryName);
                this.placeCountrySelector.appendChild(optionElement);
            }
        };

        const setTextsInStateSelectorsOptions = list => {
            for (let stateName of list) {
                const optionElement = document.createElement('option');
                optionElement.value = stateName;
                Core.setText(optionElement, stateName);
                this.placeStateSelector.appendChild(optionElement);
            }
            this.placeStateSelector.removeAttribute('disabled');
        };

        const checkForChanges = () => {
            const checkInput = input => {
                console.log(input);
                console.log(input.lastValue, input.value);
                return input.lastValue === input.value || input.classList.contains('hide');
            }

            const checkSelector = input => {
                console.log(input);
                console.log(input.lastValue, input.options[input.selectedIndex].value);
                return input.lastValue === input.options[input.selectedIndex].value;
            }

            this.placeSaveInput.disabled =
                checkInput(this.placeNameInput) &&
                checkSelector(this.placeStateSelector) &&
                checkSelector(this.placeCountrySelector) &&
                checkInput(this.placeStreetInput) &&
                checkInput(this.placeNumberInput) &&
                checkInput(this.placeInteriorNumberInput) &&
                checkInput(this.placeReferencesInput) &&
                checkInput(this.placeColonyInput) &&
                checkInput(this.placeMunicipalityInput) &&
                checkInput(this.placeDelegationInput) &&
                checkInput(this.placeCityInput) &&
                checkInput(this.placepostalCodeInput)
                ;
        };

        const createLabel = id => {
            const input = document.createElement('div');
            input.id = id;
            input.className = 'adminLabel';
            placeFormContainer.appendChild(input);
            return input;
        };

        const createInput = id => {
            const input = document.createElement('input');
            input.id = id;
            input.className = 'textInput';
            input.addEventListener('keyup', () => checkForChanges());
            placeFormContainer.appendChild(input);
            return input;
        }

        const createCountrySelector = id => {
            const input = document.createElement('select');
            input.id = id;
            input.className = 'textInput';
            input.addEventListener('change', () => checkForChanges());
            placeFormContainer.appendChild(input);
            return input;
        }

        const createStateSelector = id => {
            const input = document.createElement('select');
            input.id = id;
            input.className = 'textInput';
            input.addEventListener('change', () => {
                checkForChanges();
                this.setStateData(this.placeStateSelector.options[this.placeStateSelector.selectedIndex].value);
            });
            placeFormContainer.appendChild(input);
            return input;
        }

        const createForm = () => {
            this.placeFormContainer = document.getElementById('placeFormContainer');

            this.placeNameInput = createInput('placeNameInput');

            createLabel('placeCountryLabel');
            this.placeCountrySelector = createCountrySelector('placeCountrySelector');
            this.placeCountrySelector.disabled = true;

            createLabel('placeStateLabel');
            this.placeStateSelector = createStateSelector('placeStateSelector');

            createLabel('placeStreetLabel');
            this.placeStreetInput = createInput('placeStreetInput');

            createLabel('placeNumberLabel');
            this.placeNumberInput = createInput('placeNumberInput');

            createLabel('placeInteriorNumberLabel');
            this.placeInteriorNumberInput = createInput('placeInteriorNumberInput');

            createLabel('placeReferencesLabel');
            this.placeReferencesInput = createInput('placeReferencesInput');

            createLabel('placeColonyLabel');
            this.placeColonyInput = createInput('placeColonyInput');

            this.placeMunicipalityLabel = createLabel('placeMunicipalityLabel');
            this.placeMunicipalityInput = createInput('placeMunicipalityInput');

            this.placeDelegationLabel = createLabel('placeDelegationLabel');
            this.placeDelegationInput = createInput('placeDelegationInput');

            createLabel('placeCityLabel');
            this.placeCityInput = createInput('placeCityInput');

            createLabel('placePostalCodeLabel');
            this.placepostalCodeInput = createInput('placepostalCodeInput');

            this.placeSaveInput = document.createElement('button');
            this.placeSaveInput.id = 'placeSaveInput';
            this.placeSaveInput.className = 'button greenButton';
            this.placeFormContainer.appendChild(this.placeSaveInput);

            if (this.data.id !== undefined) {
                this.placeDeleteInput = document.createElement('button');
                this.placeDeleteInput.id = 'placeDeleteInput';
                this.placeDeleteInput.className = 'button redButton';
                this.placeDeleteInput.addEventListener('click', () => {
                    this.sendDelete(this.data.id)
                });
                this.placeFormContainer.appendChild(this.placeDeleteInput);
            }
        }

        createForm();

        setTextsInCountrySelectorsOptions(['mx']);
        fetch(`/v1/countries/codes/mx/codes`)
            .then(response => {
                return response.json();
            })
            .then(jsonResponse => {
                setTextsInStateSelectorsOptions(jsonResponse.data.list);
                this.loadData(data);
                if (Core.isFunction(afterCreateGUI)) {
                    afterCreateGUI();
                }
            })
            .catch(error => {
                Core.showErrorMessage(error);
            });
        Core.setTextsById();
    }

    loadData(data) {
        console.log(`place.js::loadData::data: `, data)
        const setDataInForm = data => {
            this.placeSaveInput.disabled = true;
            let stateCode;
            for (let administrativeDivision of data.administrativeDivisions) {
                const typeName = administrativeDivision.administrativeDivisionType.name;
                const name = administrativeDivision.name;

                console.log(`lib::places::mx::1.00::index.js::typeName: ${typeName}`);
                switch (typeName) {
                    case 'state':
                        stateCode = name;
                        Core.setText(this.placeStateSelector, name, Core.VALUE);
                        this.setStateData(name);
                        break;
                    case 'colony':
                        this.placeColonyInput.value = name;
                        this.placeColonyInput.lastValue = name;
                        break;
                    case 'delegation':
                        this.placeDelegationInput.value = name;
                        this.placeDelegationInput.lastValue = name;
                        break;
                    case 'municipality':
                        this.placeMunicipalityInput.value = name;
                        this.placeMunicipalityInput.lastValue = name;
                        break;
                    case 'city':
                        this.placeCityInput.value = name;
                        this.placeCityInput.lastValue = name;
                        break;
                }
            }

            this.placeNameInput.value = data.name;
            this.placeNameInput.lastValue = data.name;

            Core.setText(this.placeCountrySelector, data.countryCode, Core.VALUE);
            this.placeCountrySelector.lastValue = data.countryCode;

            for (let optionElement of this.placeStateSelector.options) {
                if (optionElement.value === stateCode) {
                    optionElement.setAttribute('selected', 'selected');
                }
            }

            this.placeStreetInput.value = data.street;
            this.placeStreetInput.lastValue = data.street;
            this.placeNumberInput.value = data.number;
            this.placeNumberInput.lastValue = data.number;
            this.placeInteriorNumberInput.value = data.interiorNumber;
            this.placeInteriorNumberInput.lastValue = data.interiorNumber;
            this.placeReferencesInput.value = data.references;
            this.placeReferencesInput.lastValue = data.references;
            this.placepostalCodeInput.value = data.postalCode;
            this.placepostalCodeInput.lastValue = data.postalCode;
        }
        setDataInForm(data);
    };

    createGUIForAdd(callbackFunction) {
        this.createGUI(callbackFunction);
        this.placeSaveInput.addEventListener('click', () => {
            this.sendRequest('POST')
        });
    }

    createGUIForModify(data, callbackFunction) {
        this.createGUI(data, callbackFunction);
        this.placeSaveInput.addEventListener('click', () => {
            this.sendRequest('PUT')
        });
    }
}


