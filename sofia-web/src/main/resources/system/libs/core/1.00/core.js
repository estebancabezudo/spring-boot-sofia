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
    socket: null,
    socketTimeoutHandler: null,
    socketTimeoutTime: 10000,
    texts: {},
    actualLanguage: null,
    user: null,
    requestId: 0,
    topics: [],
    reportSystemErrorFunction: null,
    screenBlocker: null,

    queryParameters: new URLSearchParams(location.search),

    initSocket: () => {
        return;
        const protocol = window.location.protocol === "https:" ? "wss://" : "ws://";
        const host = window.location.host;
        const port = window.location.port ? `:${window.location.port}` : "";
        const url = `${protocol}${host}${port}/v1/websocket`;
        console.log(`core::initSocket: Connect to ${url}`);
        Core.socket = new WebSocket(url);

        Core.socket.onopen = function (e) {
            console.log("core::initSocket: Connection established");
            clearTimeout(Core.socketTimeoutHandler);
            console.log(Core.socket);
        };

        Core.socket.onmessage = function (event) {
            const data = JSON.parse(event.data);
            console.log(`core::initSocket: Data received from the server`, data);
            if (data.type === 'NOTIFICATION') {
                // Verificar si el navegador soporta el API de Notificaciones
                console.log(`'Notification' in window: ${'Notification' in window}`);
                if ('Notification' in window) {
                    // Pedir permiso al usuario para mostrar notificaciones
                    Notification.requestPermission().then(function (permission) {
                        if (permission === 'granted') {
                            // Si el usuario otorgó el permiso, mostrar la notificación
                            const notificationOptions = {
                                title: 'Título de la notificación',
                                body: data.message,
                                icon: 'ruta/del/icono.png' // Ruta a una imagen para el icono de la notificación (opcional)
                            };

                            const notification = new Notification(notificationOptions.title, notificationOptions);
                        }
                    });
                } else {
                    console.log('El navegador no soporta el API de Notificaciones.');
                }
            }
        };

        Core.socket.onclose = function (event) {
            console.log(`core::initSocket: Connection shutdown. code=${event.code} reason=${event.reason}`);
            Core.socketTimeoutHandler = setTimeout(() => {
                Core.initSocket();
            }, Core.socketTimeoutTime);
        };

        Core.socket.onerror = function (error) {
            console.log(`[error]`, error);
        };
    },

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

                Core.subscribeTo('core:setTexts', data => {
                    if (jsonData.message) {
                        const message = jsonData.message;
                        if (message) {
                            console.log(message);
                            const jsonMessage = JSON.parse(message);
                            console.log(jsonMessage);
                            if (jsonMessage.type === 'error') {
                                Core.showErrorMessage(jsonMessage.data);
                            } else {
                                Core.showMessage(jsonMessage.data);
                            }

                        }
                    }
                });

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

    hideScreenBlocker: () => {
        if (Core.screenBlocker != null && screenBlocker.hide) {
            screenBlocker.hide();
        }
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
            if (Core.isFunction(value)) {
                return value();
            }
            if (value === 'status:logged' && Core.isLogged()) {
                return true;
            }
            if (value === 'status:notLogged' && Core.isNotLogged()) {
                return true;
            }
            if (value.startsWith("page:")) {
                const page = value.substr(5);
                if (page.endsWith('**')) {
                    console.log(`${window.location.pathname} start with ${page.substring(0, page.length - 2)}: ${window.location.pathname === page.substring(0, -2)}`);
                    if (window.location.pathname.startsWith(page.substring(0, page.length - 2))) {
                        return true;
                    }
                } else {
                    if (window.location.pathname === page) {
                        return true;
                    }
                }
            }
            if (value.startsWith("group:")) {
                const group = value.substr(6);
                if (Core.user === null) {
                    return false;
                }
                const groups = Core.user.groups;
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
        if (!fetchLanguage) {
            console.warn(`Invalid language: ${languageCode}`);
            fetchLanguage = "en";
        }
        fetch(`/v1/pages/actual/texts?language=${fetchLanguage}&page=${page}`, {
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(response => response.json())
            .then(jsonData => {
                console.log(jsonData.data);
                texts = jsonData.data;
                Core.actualLanguage = fetchLanguage;
                Core.setTexts(texts);
            })

            ;
    },

    publish: (topicsName, data) => {
        console.log(`Publish on ${topicsName}`, data);
        const functions = Core.topics[topicsName];
        if (functions && Core.isArray(functions) && functions.length > 0) {
            functions.forEach(func => {
                func(data);
            });
        } else {
            console.warn(`Data published in empty topic (${topicsName}): `, data);
        }
    },

    reportSystemError: error => {
        if (environment && environment.name !== 'prod') {
            throw error;
        }
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

    sendMessage: message => {
        if (socket.readyState === 1) {
            socket.send(message);
        } else {
            console.warn(`Socket closed`);
        }
    },

    setScreenBlocker(blocker) {
    },

    setTextsById: () => {
        console.log(`Core::setTextsById: Set text by id`);
        for (const [key, value] of Object.entries(Core.texts)) {
            try {
                const element = document.getElementById(key);
                Core.setTextToElement(element, value);
            } catch (e) {
                console.log(`Core::setTextsById: Error for: ${key}: ${e}`);
                if (environment.name !== 'prod') {
                    throw e;
                }
            }
        }
    },
    setTextToElement: (element, text, options) => {
        if (Core.isHTMLDivElement(element)) {
            do {
                if (
                    element instanceof HTMLSpanElement || element instanceof HTMLDivElement || element instanceof HTMLButtonElement || element instanceof HTMLOptionElement || element instanceof HTMLAnchorElement ||
                    element instanceof HTMLTitleElement || element instanceof HTMLParagraphElement || element instanceof HTMLHeadingElement || element instanceof HTMLLabelElement) {
                    const childNodes = element.childNodes;
                    const write = childNodes.length === 0 || (childNodes.length === 1 && childNodes[0].nodeType === Node.TEXT_NODE && element.textContent !== text);
                    element.innerHTML = text;
                    if (!write) {
                        console.log(`Core::setTextToElement: WARNING. The element with the id ${element.id} is not empty.`, element);
                    }
                    break;
                }
                if (element instanceof HTMLInputElement) {
                    switch (element.type) {
                        case 'submit':
                            element.value = text;
                            break;
                        case 'label':
                            element.innerHTML = text;
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
                            console.log(`Core::setTextsById: WARNING. Can't manage the type for the input element id='${element.id}' type='${element.type}'.`, element);
                    }
                    break;
                }
                if (element instanceof HTMLSelectElement) {
                    break;
                }
                throw Error(`Set text to unknown element with id ${element.id} (${element.constructor.name})`);
            } while (false);
        }
    },
    showScreenBlocker: () => {
        if (Core.screenBlocker != null && screenBlocker.show) {
            screenBlocker.show();
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
    Core.initSocket();
    Core.subscribeTo('core:setTexts', data => {
        const message = Core.queryParameters.get('message');
        if (message) {
            Core.showMessage(Core.getText(message));
        }
    });
    Core.subscribeTo('core:webClientDataChange', data => {
        console.log('Data from core:webClientDataChange topic: ', data);
        if (data.language === undefined || data.language === null) {
            Core.loadLanguage(Core.getPreferredLanguage());
        } else {
            Core.loadLanguage(data.language);
        }
    })
    Core.getActualWebClientDetails();
}

window.addEventListener('load', pageLoaded, false);
