const LIST_PLACES_URL = '/admin/places/list.html';

class Place {
    constructor(data, id) {
        this.data = data;
        this.id = id || 'sofia';
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
            street: {
                name: this.placeStreetInput.value
            },
            number: this.placeNumberInput.value,
            interiorNumber: this.placeInteriorNumberInput.value,
            cornerStreet: {
                name: this.placeCornerStreetInput.value
            },
            betweenStreets: {
                first: {
                    name: this.placeFirstStreetInput.value
                },
                second: {
                    name: this.placeSecondStreetInput.value
                }
            },
            references: this.placeReferencesInput.value,
            postalCode: this.placepostalCodeInput.value,
            countryCode: this.placeCountrySelector.options[this.placeCountrySelector.selectedIndex].value,
            administrativeDivisions: [
                {
                    "name": this.placeSettlementInput.value,
                    "administrativeDivisionType": {
                        "name": this.placeSettlementType
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
    };

    selectCornerStreet() {
        this.placeCornerStreetOption.checked = true;
        this.cornerStreetInputContainer.style.display = 'flex';
        this.betweenStreetsInputsContainer.style.display = 'none';
    };

    selectBetweenStreets() {
        this.placeBetweenStreetsOption.checked = true;
        this.cornerStreetInputContainer.style.display = 'none';
        this.betweenStreetsInputsContainer.style.display = 'flex';
    };

    createGUI(afterCreateGUI) {
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
                if (optionElement.value === this.stateCode) {
                    optionElement.setAttribute('selected', 'selected');
                }
            }
            this.placeStateSelector.removeAttribute('disabled');
        };


        const checkForChanges = () => {
            const theInputNotChange = input => {
                console.log(input);
                console.log(input.lastValue, input.value);
                return input.lastValue === input.value || input.classList.contains('hide');
            }

            const theSelectorDoNotChange = input => {
                console.log(input);
                console.log(input.lastValue, input.options[input.selectedIndex].value);
                return input.lastValue === input.options[input.selectedIndex].value;
            }

            const checkIntersectionOptions = () => {
                console.log(`${this.dataSelectedIntersection} === ${this.placeCornerStreetOption.value} ${this.placeBetweenStreetsOption.checked} ${(this.dataSelectedIntersection === this.placeCornerStreetOption.value && this.placeBetweenStreetsOption.checked)}`);
                console.log(`${this.dataSelectedIntersection} === ${this.placeBetweenStreetsOption.value} ${this.placeCornerStreetOption.checked} ${(this.dataSelectedIntersection === this.placeBetweenStreetsOption.value && this.placeCornerStreetOption.checked)}`);

                return (this.dataSelectedIntersection === this.placeCornerStreetOption.value && this.placeCornerStreetOption.checked) ||
                    (this.dataSelectedIntersection === this.placeBetweenStreetsOption.value && this.placeBetweenStreetsOption.checked);
            }


            this.placeSaveInput.disabled =
                theInputNotChange(this.placeNameInput) &&
                theSelectorDoNotChange(this.placeStateSelector) &&
                theSelectorDoNotChange(this.placeCountrySelector) &&
                theInputNotChange(this.placeStreetInput) &&
                theInputNotChange(this.placeNumberInput) &&
                theInputNotChange(this.placeInteriorNumberInput) &&
                theInputNotChange(this.placeReferencesInput) &&
                theInputNotChange(this.placeSettlementInput) &&
                theInputNotChange(this.placeMunicipalityInput) &&
                theInputNotChange(this.placeDelegationInput) &&
                theInputNotChange(this.placeCityInput) &&
                theInputNotChange(this.placepostalCodeInput) &&
                checkIntersectionOptions()
                ;
        };

        const createLabel = (name, container) => {
            const input = document.createElement('div');
            input.id = `${name}_${this.id}`;
            input.className = `adminLabel ${name}`;
            container.appendChild(input);
            return input;
        };

        const createInput = name => {
            const input = document.createElement('input');
            input.className = name;
            input.name = `${name}_${this.id}`;
            input.className = `textInput ${name}`;
            input.addEventListener('keyup', () => checkForChanges());
            this.placeFormContainer.appendChild(input);
            return input;
        }

        const createIntersectionSelector = id => {
            const placeIntersectionContainer = document.createElement('div');
            placeIntersectionContainer.className = 'placeIntersectionContainer';
            this.placeFormContainer.appendChild(placeIntersectionContainer);

            const placeIntersectionSelectorContainer = document.createElement('div');
            placeIntersectionSelectorContainer.className = 'placeIntersectionSelectorContainer';

            const placeCornerStreetOptionContainer = document.createElement('div');
            placeCornerStreetOptionContainer.className = "placeOptionSelectionContainer"
            placeIntersectionSelectorContainer.appendChild(placeCornerStreetOptionContainer);

            this.placeCornerStreetOption = document.createElement('input');
            this.placeCornerStreetOption.type = 'radio';
            this.placeCornerStreetOption.name = 'intersectionSelector';
            this.placeCornerStreetOption.value = 'cornerStreet';
            placeCornerStreetOptionContainer.appendChild(this.placeCornerStreetOption);
            this.placeCornerStreetOptionLabel = document.createElement('label');
            placeCornerStreetOptionContainer.appendChild(this.placeCornerStreetOptionLabel);

            this.placeBetweenStreetsOptionContainer = document.createElement('div');
            this.placeBetweenStreetsOptionContainer.className = "placeOptionSelectionContainer"
            placeIntersectionSelectorContainer.appendChild(this.placeBetweenStreetsOptionContainer);

            this.placeBetweenStreetsOption = document.createElement('input');
            this.placeBetweenStreetsOption.type = 'radio';
            this.placeBetweenStreetsOption.name = 'intersectionSelector';
            this.placeBetweenStreetsOption.value = 'betweenStreets';
            this.placeBetweenStreetsOptionContainer.appendChild(this.placeBetweenStreetsOption);
            this.placeBetweenStreetsOptionLabel = document.createElement('label');
            this.placeBetweenStreetsOptionContainer.appendChild(this.placeBetweenStreetsOptionLabel);

            placeIntersectionContainer.appendChild(placeIntersectionSelectorContainer);

            this.cornerStreetInputContainer = document.createElement('div');
            this.cornerStreetInputContainer.className = 'cornerStreetInputContainer';
            this.cornerStreetInputContainer.style.display = 'none';
            placeIntersectionContainer.appendChild(this.cornerStreetInputContainer);

            this.betweenStreetsInputsContainer = document.createElement('div');
            this.betweenStreetsInputsContainer.className = 'betweenStreetsInputsContainer';
            this.betweenStreetsInputsContainer.style.display = 'none';
            placeIntersectionContainer.appendChild(this.betweenStreetsInputsContainer);

            this.cornerStreetInputLabel = createLabel('cornerStreetInputLabel', this.cornerStreetInputContainer);
            this.placeCornerStreetInput = document.createElement('input');
            this.placeCornerStreetInput.className = 'textInput';
            this.placeCornerStreetInput.addEventListener('keyup', () => checkForChanges());
            this.cornerStreetInputContainer.appendChild(this.placeCornerStreetInput);

            this.firstStreetInputLabel = createLabel('firstStreetInputLabel', this.betweenStreetsInputsContainer);
            this.placeFirstStreetInput = document.createElement('input');
            this.placeFirstStreetInput.className = 'textInput';
            this.placeFirstStreetInput.addEventListener('keyup', () => checkForChanges());
            this.betweenStreetsInputsContainer.appendChild(this.placeFirstStreetInput);

            this.secondStreetInputLabel = createLabel('secondStreetInputLabel', this.betweenStreetsInputsContainer);
            this.placeSecondStreetInput = document.createElement('input');
            this.placeSecondStreetInput.className = 'textInput';
            this.placeSecondStreetInput.addEventListener('keyup', () => checkForChanges());
            this.betweenStreetsInputsContainer.appendChild(this.placeSecondStreetInput);

            this.placeCornerStreetOption.addEventListener('click', () => {
                this.selectCornerStreet();
                checkForChanges();
            });

            this.placeBetweenStreetsOption.addEventListener('click', () => {
                this.selectBetweenStreets();
                checkForChanges();
            });
        }

        const createCountrySelector = name => {
            const input = document.createElement('select');
            input.id = `${name}_${this.id}`;
            input.className = `textInput ${name}`;
            input.addEventListener('change', () => checkForChanges());
            this.placeFormContainer.appendChild(input);
            return input;
        }

        const createStateSelector = name => {
            const input = document.createElement('select');
            input.id = `${name}_${this.id}`;
            input.className = `textInput ${name}`;
            input.addEventListener('change', () => {
                checkForChanges();
                this.setStateData(this.placeStateSelector.options[this.placeStateSelector.selectedIndex].value);
            });
            this.placeFormContainer.appendChild(input);
            return input;
        }

        const createForm = () => {
            const loadCountrySubdivision = async () => {
                try {
                    const response = await fetch(`/v1/countries/codes/mx/codes`);
                    const jsonResponse = await response.json();
                    setTextsInStateSelectorsOptions(jsonResponse.data.list);
                } catch (error) {
                    console.log(`place.js :: createGUI :: createForm :: loadCountrySubdivision: error `, error);
                    Core.showErrorMessage(error);
                }
            };
            const placeFormContainerId = `placeFormContainer_${this.id}`;
            this.placeFormContainer = document.getElementById(placeFormContainerId);
            this.placeFormContainer.className = 'placeFormContainer';

            this.placeNameInput = createInput('placeNameInput');

            this.placeCountryLabel = createLabel('placeCountryLabel', this.placeFormContainer);
            this.placeCountrySelector = createCountrySelector('placeCountrySelector');
            this.placeCountrySelector.disabled = true;

            this.placeStateLabel = createLabel('placeStateLabel', this.placeFormContainer);
            this.placeStateSelector = createStateSelector('placeStateSelector');

            this.placeStreetLabel = createLabel('placeStreetLabel', this.placeFormContainer);
            this.placeStreetInput = createInput('placeStreetInput');

            this.placeNumberLabel = createLabel('placeNumberLabel', this.placeFormContainer);
            this.placeNumberInput = createInput('placeNumberInput');

            this.placeInteriorNumberLabel = createLabel('placeInteriorNumberLabel', this.placeFormContainer);
            this.placeInteriorNumberInput = createInput('placeInteriorNumberInput');

            this.placeIntersectionLabel = createLabel('placeIntersectionLabel', this.placeFormContainer);
            createIntersectionSelector();

            this.placeReferencesLabel = createLabel('placeReferencesLabel', this.placeFormContainer);
            this.placeReferencesInput = createInput('placeReferencesInput');

            this.placeSettlementLabel = createLabel('placeSettlementLabel', this.placeFormContainer);
            this.placeSettlementInput = createInput('placeSettlementInput');

            this.placeMunicipalityLabel = createLabel('placeMunicipalityLabel', this.placeFormContainer);
            this.placeMunicipalityInput = createInput('placeMunicipalityInput', this.placeFormContainer);

            this.placeDelegationLabel = createLabel('placeDelegationLabel', this.placeFormContainer);
            this.placeDelegationInput = createInput('placeDelegationInput');

            this.placeCityLabel = createLabel('placeCityLabel', this.placeFormContainer);
            this.placeCityInput = createInput('placeCityInput');

            this.placePostalCodeLabel = createLabel('placePostalCodeLabel', this.placeFormContainer);
            this.placepostalCodeInput = createInput('placepostalCodeInput');

            this.placeSaveInput = document.createElement('button');
            this.placeSaveInput.className = 'button greenButton placeSaveInput';
            this.placeFormContainer.appendChild(this.placeSaveInput);

            if (this.data.id !== undefined) {
                this.placeDeleteInput = document.createElement('button');
                this.placeDeleteInput.className = 'button redButton placeDeleteInput';
                this.placeDeleteInput.addEventListener('click', () => {
                    this.sendDelete(this.data.id)
                });
                this.placeFormContainer.appendChild(this.placeDeleteInput);
            }
            loadCountrySubdivision();
        }

        const setTexts = () => {
            setTextsInCountrySelectorsOptions(['mx']);

            if (Core.isFunction(afterCreateGUI)) {
                afterCreateGUI();
            }
            Core.setTextsById();
            Core.setText(this.placeCountryLabel, `placeCountryLabel`);
            Core.setText(this.placeStateLabel, 'placeStateLabel');
            Core.setText(this.placeStreetLabel, 'placeStreetLabel');
            Core.setText(this.placeNumberLabel, 'placeNumberLabel');
            Core.setText(this.placeInteriorNumberLabel, 'placeInteriorNumberLabel');
            Core.setText(this.placeIntersectionLabel, 'placeIntersectionLabel');
            Core.setText(this.cornerStreetInputLabel, 'cornerStreetInputLabel');
            Core.setText(this.firstStreetInputLabel, 'firstStreetInputLabel');
            Core.setText(this.secondStreetInputLabel, 'secondStreetInputLabel');
            Core.setText(this.placeReferencesLabel, 'placeReferencesLabel');
            Core.setText(this.placeSettlementLabel, this.placeSettlementType);
            Core.setText(this.placeMunicipalityLabel, 'placeMunicipalityLabel');
            Core.setText(this.placeDelegationLabel, 'placeDelegationLabel');
            Core.setText(this.placeCityLabel, 'placeCityLabel');
            Core.setText(this.placePostalCodeLabel, 'placePostalCodeLabel');
            Core.setText(this.placeSaveInput, 'placeSaveInput');
            Core.setText(this.placeDeleteInput, 'placeDeleteInput');
            Core.setText(this.placeCornerStreetOptionLabel, 'placeCornerStreetOptionLabel');
            Core.setText(this.placeBetweenStreetsOptionLabel, 'placeBetweenStreetsOptionLabel');
        }

        const setDataInForm = () => {
            console.log(`place.js :: setDataInForm :: data: `, this.data)
            this.placeSaveInput.disabled = true;
            for (let administrativeDivision of this.data.administrativeDivisions) {
                const typeName = administrativeDivision.administrativeDivisionType.name;
                const name = administrativeDivision.name;

                console.log(`lib:: places:: mx:: 1.00:: index.js:: typeName: ${typeName}`);
                switch (typeName) {
                    case 'state':
                        this.stateCode = name;
                        Core.setText(this.placeStateSelector, name, Core.VALUE);
                        this.setStateData(name);
                        break;
                    case 'colony':
                    case 'residentialDevelopment':
                    case 'town':
                    case 'neighborhood':
                        this.placeSettlementType = typeName;
                        this.placeSettlementInput.value = name;
                        this.placeSettlementInput.lastValue = name;
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

            this.placeNameInput.value = this.data.name;
            this.placeNameInput.lastValue = this.data.name;

            Core.setText(this.placeCountrySelector, this.data.countryCode, Core.VALUE);
            this.placeCountrySelector.lastValue = this.data.countryCode;

            this.placeStreetInput.value = this.data.street ? this.data.street.name : '';
            this.placeStreetInput.lastValue = this.data.street === null ? null : this.data.street.name;
            this.placeNumberInput.value = this.data.number;
            this.placeNumberInput.lastValue = this.data.number;
            this.placeInteriorNumberInput.value = this.data.interiorNumber;
            this.placeInteriorNumberInput.lastValue = this.data.interiorNumber;

            this.placeCornerStreetInput.value = this.data.cornerStreet === null ? null : this.data.cornerStreet.name;
            this.placeCornerStreetInput.lastValue = this.data.cornerStreet === null ? null : this.data.cornerStreet.name;

            this.placeFirstStreetInput.value = this.data.betweenStreets.firstStreet === null ? null : this.data.betweenStreets.firstStreet.name;
            this.placeFirstStreetInput.lastValue = this.data.betweenStreets.firstStreet === null ? null : this.data.betweenStreets.firstStreet.name;

            this.placeSecondStreetInput.value = this.data.betweenStreets.secondStreet === null ? null : this.data.betweenStreets.secondStreet.name;
            this.placeSecondStreetInput.lastValue = this.data.betweenStreets.secondStreet === null ? null : this.data.betweenStreets.secondStreet.name;

            this.placeReferencesInput.value = this.data.references;
            this.placeReferencesInput.lastValue = this.data.references;
            this.placepostalCodeInput.value = this.data.postalCode;
            this.placepostalCodeInput.lastValue = this.data.postalCode;
            if (this.data.cornerStreet !== null) {
                this.selectCornerStreet();
                this.dataSelectedIntersection = this.placeCornerStreetOption.value;
            }
            if (this.data.betweenStreets.firstStreet !== null && this.data.betweenStreets.secondStreet !== null) {
                this.selectBetweenStreets();
                this.dataSelectedIntersection = this.placeBetweenStreetsOption.value;
            }
            console.log(this.dataSelectedIntersection);
        };

        createForm();
        setDataInForm();
        setTexts();
    }

    prepareDataToSend() {
        if (this.placeCornerStreetOption.checked) {
            this.placeFirstStreetInput.value = '';
            this.placeSecondStreetInput.value = '';
        } else {
            this.placeCornerStreetInput.value = '';
        }
    };

    createGUIForAdd(callbackFunction) {
        this.createGUI(callbackFunction);
        this.placeSaveInput.addEventListener('click', () => {
            this.prepareDataToSend();
            this.sendRequest('POST')
        });
    }

    createGUIForModify(callbackFunction) {
        this.createGUI(callbackFunction);
        this.placeSaveInput.addEventListener('click', () => {
            this.prepareDataToSend();
            this.sendRequest('PUT')
        });
    }
}


