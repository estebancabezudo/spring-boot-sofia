/*
 * Created on: 11/05/2021
 * Author:     Esteban Cabezudo
 */

class TwoLanguageFlag {
    constructor(configuration) {
        this.languages = configuration.languages;
        this.onchange = configuration.onchange;
        const validateOptions = () => {
            this.container = document.getElementById(configuration.id);
            if (!this.container) {
                throw new Error(`Can't find the element with the id ${configuration.id}`);
            }
        }
        const setFlagToShow = () => {
            if (Core.actualLanguage === this.languages[0].code) {
                this.languages[1].element.classList.add('twoLanguageFlagVisible');
                this.languages[0].element.classList.remove('twoLanguageFlagVisible');
            } else {
                this.languages[0].element.classList.add('twoLanguageFlagVisible');
                this.languages[1].element.classList.remove('twoLanguageFlagVisible');
            }
        };
        const createGUI = () => {
            this.container.className = 'twoLanguageFlagContainer';
            const setLanguageFlag = (languageToSet) => {
                languageToSet.element.className = 'twoLanguageFlagFlag';
                languageToSet.element.classList.add(languageToSet.code);
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
            setLanguageFlag(this.languages[0]);
            setLanguageFlag(this.languages[1]);
            Core.subscribeTo('core:setTexts', setFlagToShow);
        };
        validateOptions();
        createGUI();
    };
    getActualLanguage = () => {
        return Core.actualLanguage;
    };
}
