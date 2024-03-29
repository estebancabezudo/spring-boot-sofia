/**
 * Configuration
 * id - The element to link to.
 * onclick - A string with the url to link or a function to execute when the link is clicked
 * disabled - true if the link is disabled
 * enable - enable when is login, or not login or in a certain page
 * disable - disable when is login, or not login, or in a certain page
 * show - show when is login, or not login, or in a certain page
 * hide - hide when is login, or not login or in a certain page
 * 
 *  */

class LinkTo {
    constructor(configuration) {
        const getElement = (id, element) => {
            if (id === undefined && element === undefined) {
                throw new Error('You MUST specify an id or element to apply the link to.');
            }
            if (Core.isString(id)) {
                const element = document.getElementById(id);
                console.log('FOUND element ', element, ` using ${id}`);
                if (element === null || element === undefined) {
                    throw new Error(`Element NOT FOUND: ${id}`);
                }
                return element;
            }
            return element;
        };
        this.element = getElement(configuration.id, configuration.element);
        if (!this.element) {
            console.log('configuration: ', configuration, this.element);
            throw new Error(`Element NOT FOUND`);
        }
        this.element.classList.add('linkTo');
        const click = (event, newWindow) => {
            if (this.enable && Core.isString(configuration.onclick)) {
                if (newWindow === true) {
                    window.open(configuration.onclick, '_blank');
                } else {
                    location.href = configuration.onclick;
                }
                return;
            }
            if (Core.isFunction(configuration.onclick)) {
                configuration.onclick(event);
                return;
            }
        };

        this.element.addEventListener('click', click);
        this.element.addEventListener('mouseup', event => {
            if (event.button === 1) {
                click(event, true);
            }
        });
        if (configuration.disabled) {
            configuration.disabled ? this.disable() : this.enable();
        } else {
            this.enable();
        }
        Core.subscribeTo('core:webClientDataChange', () => {

            do {
                if (configuration.show && Core.isValid(configuration.show)) {
                    this.show();
                    break;
                }

                if (configuration.hide && Core.isValid(configuration.hide)) {
                    this.hide();
                    break;
                }
            } while (false);

            do {
                if (configuration.enable && Core.isValid(configuration.enable)) {
                    this.enable();
                    break;
                }

                if (configuration.disable && Core.isValid(configuration.disable)) {
                    this.disable();
                    break;
                }
            } while (false);

            if (configuration.mark && Core.isValid(configuration.mark)) {
                this.mark();
                return;
            }
        });
    }

    mark() {
        this.element.classList.add('marked')
    }

    show() {
        this.element.classList.add('visible')
        this.element.classList.remove('hide')
    }

    hide() {
        this.element.classList.add('hide')
        this.element.classList.remove('visible')
    }

    disable() {
        this.isDisable = true;
        this.element.classList.add('disabled')
        this.element.classList.remove('enabled')
    }

    enable() {
        this.isDisable = false;
        this.element.classList.add('enabled')
        this.element.classList.remove('disabled')
    }
}