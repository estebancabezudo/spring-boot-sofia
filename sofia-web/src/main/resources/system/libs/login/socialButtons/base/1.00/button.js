class SocialButton {
    constructor(options) {
        this.url = options.url;
        const getElement = target => {
            if (target === undefined || target == null) {
                throw new Error('You MUST specify an id or element.');
            }
            if (Core.isString(target)) {
                const id = target;
                console.log(`Using the id ${id}`);
                const element = document.getElementById(id);
                if (element === null || element === undefined) {
                    throw new Error(`Element NOT FOUND: ${target}`);
                }
                return element;
            }
            if (Core.isHTMLDivElement(target)) {
                console.log(`Using the element `, target);
                this.element = target;
                return element;
            }
        }
        this.element = getElement(options.element);
        const socialButtonContainer = document.createElement('div');
        socialButtonContainer.className = 'socialButtonContainer';
        this.element.appendChild(socialButtonContainer);

        const socialButtonLogoContainer = document.createElement('div');
        socialButtonLogoContainer.className = 'socialButtonLogoContainer'
        socialButtonContainer.appendChild(socialButtonLogoContainer);

        const socialButtonLogo = document.createElement('div');
        socialButtonLogo.className = 'socialButtonLogo'
        socialButtonLogo.style.backgroundImage = `url(${options.image})`;
        socialButtonLogoContainer.appendChild(socialButtonLogo);

        const socialButtonTextContainer = document.createElement('div');
        socialButtonTextContainer.className = 'socialButtonTextContainer'
        socialButtonContainer.appendChild(socialButtonTextContainer);

        const socialButtonText = document.createElement('div');
        socialButtonText.className = 'socialButtonText'
        socialButtonText.id = options.textKey;
        socialButtonTextContainer.appendChild(socialButtonText);

        Core.subscribeTo('core:setTexts', () => {
            socialButtonText.innerText = Core.getText(options.textKey);
        });

        this.element.addEventListener('click', event => {
            location.href = this.url;
        });
    }
}