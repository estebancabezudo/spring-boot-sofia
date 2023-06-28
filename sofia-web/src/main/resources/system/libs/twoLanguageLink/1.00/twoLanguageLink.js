/*
 * Created on: 11/05/2021
 * Author:     Esteban Cabezudo
 */

class TwoLanguageLink {
    constructor(configuration) {
        this.languages = configuration.languages;
        this.onchange = configuration.onchange;
        const validateOptions = () => {
            this.container = document.getElementById(configuration.id);
            if (!this.container) {
                throw new Error(`Can't find the element with the id ${configuration.id}`);
            }
        }
        const setLinkToShow = () => {
            if (Core.actualLanguage === this.languages[0].code) {
                this.languages[1].element.classList.add('twoLanguageLinkVisible');
                this.languages[0].element.classList.remove('twoLanguageLinkVisible');
            } else {
                this.languages[0].element.classList.add('twoLanguageLinkVisible');
                this.languages[1].element.classList.remove('twoLanguageLinkVisible');
            }
        };
        const createGUI = () => {
            this.container.className = 'twoLanguageContainer';
            const setLanguageLink = (languageToSet) => {
                languageToSet.element.className = 'twoLanguageLink';
                languageToSet.element.innerHTML = languageToSet.text;
                languageToSet.element.addEventListener('click', event => {
                    Core.loadLanguage(languageToSet.code);
                    if (Core.isFunction(this.onchange)) {
                        this.onchange(languageToSet.code);
                    }
                    event.stopPropagation();
                });
                this.container.appendChild(languageToSet.element);
            }
            this.languages[0].element = document.createElement('DIV');
            this.languages[1].element = document.createElement('DIV');
            setLanguageLink(this.languages[0]);
            setLanguageLink(this.languages[1]);
            Core.subscribeTo('core:setTexts', setLinkToShow);
        };
        validateOptions();
        createGUI();
        setLinkToShow();
    };
    getActualLanguage = () => {
        return Core.actualLanguage;
    };
}
