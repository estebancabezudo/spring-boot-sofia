// core:setTexts
// core:webClientDataChange
// core:showMessage
// core:showErrorMessage

'use strict';

class User {
    constructor(data) {
        this.username = data.username;
        console.log(`Core::User: data.groups:`, data.groups, Core.isArray(data.groups));
        if (Core.isArray(data.groups)) {
            this.groups = data.groups;
        } else {
            this.groups = [];
            console.log(`Groups not found in user data.`, data);
        }

    };
}

const Core = {
    PLACEHOLDER: 1,
    VALUE: 2,
    texts: {},
    actualLanguage: null,
    user: null,
    requestId: 0,
    topics: [],
    reportSystemErrorFunction: null,

    queryParameters: new URLSearchParams(location.search),

    getActualWebClientDetails: () => {
        console.log(`core::getActualWebClientDetails`);
        fetch('/v1/web/clients/actual/details', {
            method: 'GET'
        })
            .then(response => response.json())
            .then(jsonData => {
                const user = jsonData.user;
                if (user === undefined || user === null) {
                    Core.user = null;
                } else {
                    Core.user = new User(user);
                }

                console.log(`commons.js::getActualWebClientDetails: `, jsonData);
                Core.publish('core:webClientDataChange', jsonData);
            });
    },

    getPreferredLanguage: () => {
        const defaultFromSiteLanguages = () => {
            if (siteLanguages.includes("en") || siteLanguages.length == 0) {
                return 'en';
            }
            return siteLanguages[0];
        };

        const browserLocale = window.navigator.userLanguage || window.navigator.language;
        if (siteLanguages.includes(browserLocale)) {
            return browserLocale;
        }
        if (siteLanguages.includes(browserLocale.substring(0, 2))) {
            return browserLocale;
        }
        if (browserLocale.length < 2) {
            return defaultFromSiteLanguages();
        }
        const browserLanguage = browserLocale.substring(1, 2).toLowerCase();
        if (siteLanguages.includes(browserLanguage)) {
            return browserLanguage;
        }
        return defaultFromSiteLanguages();
    },

    getGeoLocation: async () => {
        if (navigator.geolocation) {
            try {
                const getCurrentPositionPromiseFunction = (resolve, reject) => {
                    navigator.geolocation.getCurrentPosition(resolve, reject);
                };
                const locationData = await new Promise(getCurrentPositionPromiseFunction);
                return {
                    status: "OK",
                    message: "OK",
                    code: 0,
                    data: locationData
                };
            } catch (error) {
                return {
                    status: "ERROR",
                    message: error.message,
                    code: error.code,
                    data: null
                };
            }
        } else {
            console.log("*** get end without support");
            return {
                status: "NOT_SUPPORTED",
                message: "Geolocation is not supported by this browser.",
                code: 0,
                data: null
            }
                ;
        }
    },
    getCookie: name => {
        const stringToFind = `${name}=`;
        const uriDecoded = decodeURIComponent(document.cookie);
        const cookieArray = uriDecoded.split(';');
        cookieArray.forEach(cookie => {
            while (cookie.charAt(0) == ' ') {
                cookie = cookie.substring(1);
            }
            if (cookie.indexOf(stringToFind) === 0) {
                return cookie.substring(stringToFind.length);
            }
        })
    },
    getNextRequestId: () => {
        return Core.requestId++;
    },

    getText: (key, values) => {
        if (!key) {
            console.trace();
            throw new Error(`Missing parameter key: ${key}`);
        }
        let text = texts[key];
        if (!text) {
            console.log(`No text for key: ${key}`);
            text = `[${key}]`;
        }
        if (values) {
            if (Core.isArray(values)) {
                let i = 0;
                values.forEach(value => {
                    const RegExpText = "\\{" + i + "\\}";
                    const searchRegExp = new RegExp(RegExpText, 'g');
                    text = text.replace(searchRegExp, value);
                    i++;
                });
            } else {
                throw new Error('The second parameter MUST be an array');
            }
        }
        return text;
    },

    getTimezoneOffset: () => {
        return (new Date()).getTimezoneOffset();
    },

    isArray: v => {
        return Object.prototype.toString.call(v) === '[object Array]';
    },

    isFunction: v => {
        return Object.prototype.toString.call(v) === '[object Function]';
    },

    isLogged: () => {
        const user = Core.user;
        if (user === undefined || user === null) {
            return false;
        }
        const username = user.username;
        return username !== undefined && username !== null;
    },
    isNotLogged: () => {
        return !Core.isLogged();
    },
    isString: v => {
        return Object.prototype.toString.call(v) === '[object String]';
    },

    isHTMLDivElement: v => {
        try {
            return v instanceof HTMLElement;
        } catch (e) {
            return Object.prototype.toString.call(v) === '[object HTMLDivElement]';
        }
    },

    isValid: list => {
        if (!list || list === null) {
            return false;
        }
        for (let i = 0; i < list.length; i++) {
            const value = list[i];
            console.log(`Core::isValid: Value to check: ${value}`);
            if (value === 'status:logged' && Core.isLogged()) {
                console.log(`Core::isValid: The user is logged`);
                return true;
            }
            if (value === 'status:notLogged' && Core.isNotLogged()) {
                console.log(`Core::isValid: The user is not logged`);
                return true;
            }
            if (value.startsWith("page:")) {
                const page = value.substr(5);
                if (page.endsWith('**')) {
                    console.log(`${window.location.pathname} start with ${page.substring(0, page.length - 2)}: ${window.location.pathname === page.substring(0, -2)}`);
                    if (window.location.pathname.startsWith(page.substring(0, page.length - 2))) {
                        console.log(`Core::isValid: The page is correct: ${page}`);
                        return true;
                    }
                } else {
                    console.log(`${window.location.pathname} === ${page}: ${window.location.pathname === page}`);
                    if (window.location.pathname === page) {
                        console.log(`Core::isValid: The page is correct: ${page}`);
                        return true;
                    }
                }
            }
            if (value.startsWith("group:")) {
                const group = value.substr(6);
                console.log(`Core::isValid: search for group: ${group}`);
                if (Core.user === null) {
                    return false;
                }
                console.log('Core::isValid: user: ', Core.user);
                const groups = Core.user.groups;
                console.log('Core::isValid: groups: ', groups);
                for (g of groups) {
                    if (group === g.name) {
                        return true;
                    }
                };
                return false;
            }
            if (value === 'default') {
                console.log(`It is true by default`);
                return true;
            }
        };
        console.log(`   false`);
        return false;
    },

    loadLanguage: languageCode => {
        let fetchLanguage;
        if (siteLanguages.includes(languageCode)) {
            fetchLanguage = languageCode;
        }
        if (siteLanguages.includes(languageCode.substring(0, 2))) {
            fetchLanguage = languageCode.substring(0, 2);
        }
        const page = window.location.pathname;
        if (fetchLanguage) {
            fetch(`/v1/pages/actual/texts?language=${fetchLanguage}&page=${page}`, {
                headers: {
                    "Content-Type": "application/json"
                }
            })
                .then(response => response.json())
                .then(jsonData => {
                    console.log(jsonData.data);
                    texts = JSON.parse(jsonData.data);
                    console.log(jsonData);
                    console.log(texts);
                    Core.actualLanguage = fetchLanguage;
                    Core.setTexts(texts);
                })

                ;
        } else {
            throw Error(`Invalid language: ${languageCode}`);
        }
    },

    publish: (topicsName, data) => {
        console.log(`Publish on ${topicsName}`, data);
        const functions = Core.topics[topicsName];
        if (functions && Core.isArray(functions)) {
            functions.forEach(func => {
                func(data);
            });
        }
    },
    reportSystemError: error => {
        if (Core.isFunction(Core.reportSystemErrorFunction)) {
            Core.reportSystemErrorFunction(error);
        }
    },
    setReportSystemErrorFunction: reportSystemErrorFunction => {
        Core.reportSystemErrorFunction = reportSystemErrorFunction;
    },
    // This function avoid the race condition if the asignation of the text 
    // to the element is before the text web service is called
    setText: (element, key, options) => {
        console.log(`Core::setText:`, element, key);

        Core.subscribeTo('core:setTexts', () => {
            Core.setTextToElement(element, Core.texts[key], options);
        });
        if (Core.texts[key]) {
            Core.setTextToElement(element, Core.texts[key], options);
        };
    },
    setTexts: ts => {
        texts = Object.assign(Core.texts, ts);
        Core.setTextsById();
        Core.publish('core:setTexts', { language: Core.actualLanguage });
    },
    showErrorMessage: errorMessage => {
        let jsonMessage;
        if (Core.isString(errorMessage)) {
            jsonMessage = { errorMessage };
        } else {
            jsonMessage = errorMessage;
        }
        console.log(`Core::showErrorMessage:`, jsonMessage);
        Core.publish('core:showErrorMessage', jsonMessage);
    },
    showMessage: message => {
        let jsonMessage;
        if (Core.isString(message)) {
            jsonMessage = { message };
        } else {
            jsonMessage = errorMessage;
        }
        console.log(`Core::showMessage: ${jsonMessage}`, jsonMessage);
        Core.publish('core:showMessage', jsonMessage);
    },
    setTextsById: () => {
        console.log(`Core::setTextsById: Set text by id`);
        for (const [key, value] of Object.entries(Core.texts)) {
            const element = document.getElementById(key);
            Core.setTextToElement(element, value);
        }
    },
    setTextToElement: (element, text, options) => {
        if (Core.isHTMLDivElement(element)) {
            console.log(`Core::setTextToElement: Trying to set text to ${element.id} (${element.constructor.name})`);
            if (element instanceof HTMLSpanElement || element instanceof HTMLDivElement || element instanceof HTMLButtonElement || element instanceof HTMLOptionElement) {
                console.log(`Core::setTextToElement: Set text ${text} to element with id ${element.id}`);
                const childNodes = element.childNodes;
                const write = childNodes.length === 0 || (childNodes.length === 1 && childNodes[0].nodeType === Node.TEXT_NODE && element.textContent !== text);
                if (write) {
                    element.innerHTML = text;
                } else {
                    console.log(`Core::setTextToElement: WARNING. The element with the id ${element.id} is not empty.`, element);
                }
            }
            if (element instanceof HTMLInputElement) {
                console.log(`Core::setTextToElement: element type: ${element.type}`);
                switch (element.type) {
                    case 'submit':
                        element.value = text;
                        break;
                    case 'text':
                        if (options === undefined || options === null || options & Core.PLACEHOLDER === Core.PLACEHOLDER) {
                            element.placeholder = text;
                        }
                        if ((options & Core.VALUE) === Core.VALUE) {
                            element.value = text;
                        }
                        break;
                    case 'password':
                        element.placeholder = text;
                        break;
                    default:
                        console.log(`Core::setTextsById: WARNING. Can't manage the type for the input element ${key}.`, element);
                }
            }
        }
    },

    subscribeTo: (topicsName, functionToExecute) => {
        let functions = Core.topics[topicsName];
        if (!functions) {
            functions = [];
            Core.topics[topicsName] = functions;
        }
        functions.push(functionToExecute);
    }
}

const pageLoaded = async () => {
    console.log(`Core::pageLoaded::templateVariables: `, templateVariables);
    Core.getActualWebClientDetails();
    Core.subscribeTo('core:webClientDataChange', data => {
        console.log('Data from core:webClientDataChange topic: ', data);
        if (data.language === null) {
            Core.loadLanguage(Core.getPreferredLanguage());
        } else {
            Core.loadLanguage(data.language);
        }
    })
}

window.addEventListener('load', pageLoaded, false);
