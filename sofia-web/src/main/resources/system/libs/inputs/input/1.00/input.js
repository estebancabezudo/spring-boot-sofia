class Input {
    constructor(options) {
        this.valid = false;
        this.validator = options.validator;
        this.url = options.url;
        this.invalidCharacterMessageKey = options.invalidCharacterMessageKey;
        const getElement = element => {
            if (element === undefined || element == null) {
                throw new Error('You MUST specify an id or element.');
            }
            if (Core.isString(element)) {
                const id = element;
                console.log(`Using the id ${id}`);
                this.element = document.getElementById(id);
                if (this.element === null || this.element === undefined) {
                    throw new Error(`Element NOT FOUND: ${id}`);
                }
                return;
            }
            if (Core.isHTMLDivElement(element)) {
                console.log(`Using the element `, element);
                this.element = element;
                return;
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

        getElement(options.element);
        this.lastValue = this.element.value;
        this.element.addEventListener('input', () => {
            runFunctionBeforeValidation();
            Core.publish('core:showWait');
            const value = this.element.value;
            if (this.lastValue == value && value.length < 3) {
                return;
            }
            this.lastValue = value;
            clearTimeout(this.timmerHandler);
            this.timmerHandler = setTimeout(() => {
                if (value.length < 3) {
                    this.setInvalid(false);
                    runFunctionAfterValidation();
                    Core.publish('core:hideWait');
                    return;
                }
                if (!this.validator(value)) {
                    console.log(`${value} doesn't match the regex validation.`);
                    this.setInvalid(true);
                    Core.showErrorMessage(Core.getText(this.invalidCharacterMessageKey));
                    runFunctionAfterValidation();
                    Core.publish('core:hideWait');
                    return;
                }
                fetch(this.url(value))
                    .then(response => {
                        return response.json();
                    })
                    .then(jsonResponse => {
                        Core.publish('core:hideWait');
                        if (jsonResponse.status == 1) {
                            this.setInvalid(false);
                        } else {
                            this.setInvalid(true);
                            Core.showErrorMessage(Core.getText(jsonResponse.message));
                        }
                        runFunctionAfterValidation(value);
                    })
                    .catch(error => {
                        Core.showErrorMessage(Core.getText(error.message));
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

    beforeValidation(functionBeforeValidation) {
        this.functionBeforeValidation = functionBeforeValidation;
    };

    afterValidation(functionAfterValidation) {
        this.functionAfterValidation = functionAfterValidation;
    };
}