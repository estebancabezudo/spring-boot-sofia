class EMailInput {
    constructor(value) {
        this.valid = false;
        const getElement = value => {
            if (value === undefined || value == null) {
                throw new Error('You MUST specify an id or element.');
            }
            if (Core.isString(value)) {
                const id = value;
                console.log(`Using the id ${id}`);
                this.element = document.getElementById(id);
                if (this.element === null || this.element === undefined) {
                    throw new Error(`Element NOT FOUND: ${id}`);
                }
                return;
            }
            if (Core.isHTMLDivElement(value)) {
                console.log(`Linking the element `, value);
                this.element = value;
                return;
            }
        }
        const runFunctionOnInputEvent = value => {
            if (Core.isFunction(this.functionOnInputEvent)) {
                this.functionOnInputEvent(value);
            }
        }
        const runFunctionBeforeValidation = value => {
            if (Core.isFunction(this.functionBeforeValidation)) {
                this.functionBeforeValidation(value);
            }
        }
        const runFunctionAfterValidation = value => {
            if (Core.isFunction(this.functionAfterValidation)) {
                this.functionAfterValidation(value);
            }
        }

        getElement(value);
        this.element.addEventListener('input', () => {
            clearTimeout(this.timmerHandler);
            runFunctionOnInputEvent();
            this.timmerHandler = setTimeout(() => {
                const value = this.element.value;
                if (value.length < 3) {
                    this.setInvalid(false);
                    runFunctionAfterValidation();
                    return;
                }
                const validateEmailRegex = /^[A-Za-z0-9!#$%&'*+/=?^_`@{|}~\.-]+$/;
                if (!value.match(validateEmailRegex)) {
                    console.log(`${value} doesn't match the email regex validation.`);
                    this.setInvalid(true);
                    Core.showErrorMessage('emailInvalidCharacterOnAddress');
                    runFunctionAfterValidation();
                    return;
                }
                runFunctionBeforeValidation();
                fetch(`/v1/emails/${value}/info`)
                    .then(response => {
                        return response.json();
                    })
                    .then(jsonResponse => {
                        if (value === jsonResponse.data.address) {
                            console.log(jsonResponse);
                            if (jsonResponse.status == 1) {
                                this.setInvalid(false);
                            } else {
                                this.setInvalid(true);
                                Core.showErrorMessage(jsonResponse.message);
                            }
                            runFunctionAfterValidation(value);
                        } else {
                            console.log('Ignore response');
                        }

                    })
                    .catch(error => {
                        Core.showErrorMessage(error.message);
                        Core.reportSystemError(error);
                    });
            }, 2000);
        });
    }

    getElement() {
        return this.element;
    };

    setInvalid(value) {
        this.valid = !value;
        if (this.valid) {
            this.element.classList.remove('error');
        } else {
            this.element.classList.add('error');
        }
    }

    isValid() {
        return this.valid;
    };

    onInputEvent(functionOnInputEvent) {
        this.functionOnInputEvent = functionOnInputEvent;
    };

    beforeValidation(functionBeforeValidation) {
        this.functionBeforeValidation = functionBeforeValidation;
    };

    afterValidation(functionAfterValidation) {
        this.functionAfterValidation = functionAfterValidation;
    };
}