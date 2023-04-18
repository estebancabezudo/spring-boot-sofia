/* global Core */
/* global fetch */
/* global variables */
/* global templateVariables */

'use strict';
const Core = {
  onSetLanguageFunctions: [],
  topics: {},
  queryParameters: new URLSearchParams(location.search),
  addOnSetLanguageFunction: (func) => {
    Core.onSetLanguageFunctions.push(func);
  },
  loadLanguage: parameter => {
    Core.sendGet(Core.getURLForLanguage(variables.site.language), response => {
      const texts = response.data;
      if (Core.isString(parameter)) {
        variables.site.language = parameter;
      }
      Core.addTexts(texts);
      if (Core.isFunction(parameter)) {
        parameter();
      }
    });
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
  getTimezoneOffset: () => {
    return  (new Date()).getTimezoneOffset();
  },
  getURLParameterByName: (name, url) => {
    if (!url) {
      url = window.location.href;
    }
    name = name.replace(/[\[\]]/g, '\\$&');
    const regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)');
    const results = regex.exec(url);
    if (!results) {
      return null;
    }
    if (!results[2]) {
      return '';
    }
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
  },
  inFocus: element => {
    return element === document.activeElement;
  },
  isArray: v => {
    return Object.prototype.toString.call(v) === '[object Array]';
  },
  isEnter: event => {
    return event.key === 'Enter';
  },
  isDIV: o => {
    return o !== null && o.tagName === 'DIV';
  },
  isObject: v => {
    return Object.prototype.toString.call(v) === '[object Object]';
  },
  isString: v => {
    return Object.prototype.toString.call(v) === '[object String]';
  },
  isTab: event => {
    return event.key === 'Tab';
  },
  isFunction: v => {
    return Object.prototype.toString.call(v) === '[object Function]';
  },
  isHTMLElement: target => {
    try {
      //Using W3 DOM2 (works for FF, Opera and Chrome)
      return target instanceof HTMLElement;
    } catch (e) {
      //Browsers not supporting W3 DOM2 don't have HTMLElement and an exception is thrown and we end up here. Testing some properties that all elements have (works on IE7)
      return (typeof target === "object") && (target.nodeType === 1) && (typeof target.style === "object") && (typeof target.ownerDocument === "object");
    }
  },
  isVisibleInScreen: element => {
    // TODO verificar cuando la imagen se debería de ver en la pantalla pero es mas grande que toda la pantalla y agregar el viewport
    const elementBounding = element.getBoundingClientRect();
    const viewPortTop = 0;
    const viewPortRight = window.innerWidth;
    const viewPortBottom = window.innerHeight;
    const viewPortLeft = 0;
    const pointInside = (x, y) => {
      return x >= viewPortLeft && x <= viewPortRight && y >= viewPortTop && y <= viewPortBottom;
    };
    const top = elementBounding.top;
    const right = elementBounding.right;
    const bottom = elementBounding.bottom;
    const left = elementBounding.left;
    if (pointInside(right, top))
      return true;
    if (pointInside(left, top))
      return true;
    if (pointInside(right, bottom))
      return true;
    if (pointInside(left, bottom))
      return true;
    return;
  },
  isLogged: () => {
    return !Core.isNotLogged();
  },
  isModifierKey: event => {
    const key = event.key;
    switch (key) {
      case "Alt":
      case "AltGraph":
      case "CapsLock":
      case "Control":
      case "Fn":
      case "FnLock":
      case "Hyper":
      case "Meta":
      case "NumLock":
      case "ScrollLock":
      case "Shift":
      case "Super":
      case "Symbol":
      case "SymbolLock":
        return true;
      default:
        return false;
    }
  },
  isNavigationKey: event => {
    const key = event.key;
    switch (key) {
      case 'Up':
      case 'ArrowUp':
      case 'Right':
      case 'ArrowRight':
      case 'Down':
      case 'ArrowDown':
      case 'Left':
      case 'ArrowLeft':
      case 'End':
      case 'Home':
      case 'PageDown':
      case 'PageUp':
      case 'Tab':
        return true;
      default:
        return false;
    }
  },
  isNotLogged: () => {
    return variables.user === null;
  },
  isRightLeft: event => {
    return (typeof event === 'object' && event.button === 0);
  },
  isRightClick: event => {
    return (typeof event === 'object' && event.button === 2);
  },
  isTouchStart: event => {
    return true;
  },
  isVisible: element => {
    return !element.hidden;
  },
  publish: (topicsName, data) => {
    const functions = Core.topics[topicsName];
    if (functions && Core.isArray(functions)) {
      functions.forEach(func => {
        func(data);
      });
    }
  },
  removeChilds: element => {
    while (element.firstChild) {
      element.removeChild(element.firstChild);
    }
  },
  screenBlocker: {
    create: () => {
      if (!Core.screenBlockerDiv) {
        Core.screenBlockerDiv = document.getElementById('screenBlocker');
        if (!Core.screenBlockerDiv) {
          Core.screenBlockerDiv = document.createElement("div");
          Core.screenBlockerDiv.id = 'screenBlocker';
          Core.screenBlockerDiv.style.position = "absolute";
          Core.screenBlockerDiv.style.top = "0px";
          Core.screenBlockerDiv.style.width = "100vw";
          Core.screenBlockerDiv.style.height = "100vh";
          Core.screenBlockerDiv.style.backgroundColor = "gray";
          Core.screenBlockerDiv.style.opacity = ".7";
          Core.screenBlockerDiv.style.zIndex = "1000";
          Core.screenBlockerDiv.focus();
          document.body.appendChild(Core.screenBlockerDiv);
        }
      } else {
        Core.screenBlockerDiv.style.display = "block";
      }
    },
    block: () => {
      Core.screenBlocker.create();
      Core.screenBlockerDiv.style.display = "block";
    },
    unblock: (options) => {
      Core.screenBlocker.create();
      Core.screenBlockerDiv.style.display = "none";
      if (options && options.focus) {
        options.focus.focus();
      }
    }
  },
  sendGet: (url, targetElement) => {
    const requestId = Core.getNextRequestId();
    fetch(url, {
      headers: {
        "Content-Type": "application/json",
        "RequestId": requestId
      }
    })
            .then(function (response) {
              const headers = response.headers;
              const requestId = parseInt(headers.get('RequestId'));
              response.text().then(text => {
                let jsonData;
                if (!text) {
                  throw new Error(`Empty response.`);
                }
                const data = JSON.parse(text);
                jsonData = {
                  requestId,
                  data
                };
                do {
                  if (targetElement) {
                    if (Core.isString(targetElement)) {
                      Core.publish(targetElement, jsonData);
                      break;
                    }
                    if (Core.isFunction(targetElement)) {
                      targetElement(jsonData);
                      break;
                    } else {
                      Core.trigger(targetElement, 'response', jsonData);
                      break;
                    }
                  }
                } while (false);
              })
                      ;
            })
            ;
    return {requestId}
    ;
  },
  sendDelete: (url, targetElement) => {
    const requestId = Core.getNextRequestId();
    fetch(url, {
      method: "DELETE",
      cache: "no-cache",
      credentials: "same-origin",
      headers: {
        "Content-Type": "application/json",
        "RequestId": requestId
      },
      redirect: "follow"
    })
            .then(function (response) {
              const headers = response.headers;
              const requestId = parseInt(headers.get('RequestId'));
              response.text().then(text => {
                let jsonData;
                try {
                  if (!text) {
                    throw new Error(`Empty response.`);
                  }
                  jsonData = {
                    requestId,
                    data: JSON.parse(text)
                  };
                  do {
                    if (Core.isString(targetElement)) {
                      Core.publish(targetElement, jsonData);
                      break;
                    }
                    if (Core.isFunction(targetElement)) {
                      targetElement(jsonData);
                      break;
                    }
                    if (targetElement) {
                      Core.trigger(targetElement, 'response', jsonData);
                      break;
                    }
                  } while (false);
                } catch (error) {
                  console.log(`%cCore : sendDelete : ${error.message}\n${text}`, 'color: red');
                }
              })
                      ;
            })
            ;
    return {requestId}
    ;
  },
  sendPost: (url, targetAction, data) => {
    const requestId = Core.getNextRequestId();
    let body;
    if (typeof data === 'object') {
      body = JSON.stringify(data);
    } else {
      body = data;
    }

    fetch(url, {
      method: "POST",
      cache: "no-cache",
      credentials: "same-origin",
      headers: {
        "Content-Type": "application/json",
        "RequestId": requestId
      },
      redirect: "follow",
      body
    })
            .then(function (response) {
              if (response.status === 200) {
                const headers = response.headers;
                const requestId = parseInt(headers.get('RequestId'));
                response.text().then(text => {
                  let jsonData;
                  try {
                    if (!text) {
                      throw new Error(`Empty response.`);
                    }
                    jsonData = {
                      requestId,
                      data: JSON.parse(text)
                    };
                    do {
                      if (Core.isString(targetAction)) {
                        Core.publish(targetAction, jsonData);
                        break;
                      }
                      if (Core.isFunction(targetAction)) {
                        targetAction(jsonData);
                        break;
                      }
                      if (targetAction) {
                        Core.trigger(targetAction, 'response', jsonData);
                        break;
                      }
                    } while (false);
                  } catch (error) {
                    console.log(`%cCore : sendPost : ${error.message}\n${text}`, 'color: red');
                  }
                });
              }
            })
            ;
    return {requestId};
  },
  sendPut: (url, targetElement, data) => {
    const requestId = Core.getNextRequestId();
    fetch(url, {
      method: "PUT",
      cache: "no-cache",
      credentials: "same-origin",
      headers: {
        "Content-Type": "application/json",
        "RequestId": requestId
      },
      redirect: "follow",
      body: JSON.stringify(data)
    })
            .then(function (response) {
              const headers = response.headers;
              const requestId = parseInt(headers.get('RequestId'));
              response.text().then(text => {
                let jsonData;
                try {
                  if (!text) {
                    throw new Error(`Empty response.`);
                  }
                  jsonData = {
                    requestId,
                    data: JSON.parse(text)
                  };
                  do {
                    if (Core.isString(targetElement)) {
                      Core.publish(targetElement, jsonData);
                      break;
                    }
                    if (Core.isFunction(targetElement)) {
                      targetElement(jsonData);
                      break;
                    }
                    if (targetElement) {
                      Core.trigger(targetElement, 'response', jsonData);
                      break;
                    }
                  } while (false);
                } catch (error) {
                  console.log(`%cCore : sendPut : ${error.message}\n${text}`, 'color: red');
                }
              });
            })
            ;
    return {requestId};
  },
  subscribeTo: (topicsName, functionToExecute) => {
    let functions = Core.topics[topicsName];
    if (!functions) {
      functions = [];
      Core.topics[topicsName] = functions;
    }
    functions.push(functionToExecute);
  },
  getElement: (id) => {
    if (id === null) {
      throw new Error(`The id is null.`);
    }
    if (id.tagName) {
      return id;
    }
    const element = document.getElementById(id);
    if (!element) {
      throw new Error(`Can't find element using ${id}`);
    }

  }
};

const checkDuplicateId = () => {
  console.log(`Core :: checkDuplicateId.`);
  const nodes = document.querySelectorAll('[id]');
  const ids = {};
  const totalNodes = nodes.length;
  for (var i = 0; i < totalNodes; i++) {
    var currentId = nodes[i].id ? nodes[i].id : "undefined";
    if (ids[currentId] === true) {
      throw Error(`Duplicate id: ${currentId}`);
    } else {
      ids[currentId] = true;
    }
  }
};

const pageLoaded = () => {
  Core.onLoadFunctions.forEach(func => {
    console.log(`Run on load function ${func.name}`);
    func();
  });
  console.log(`Core :: pageLoaded :: templateVariables: `, templateVariables);

  console.log(`Core :: startPage`);
  if (Core.queryParameters.has('section')) {
    Core.changeSectionTo(Core.queryParameters.get('section'));
  }
  if (variables.environment.name === 'dev') {
    checkDuplicateId();
  }
  Core.testFunctions.forEach(func => {
    func();
  });

  const loadLanguageCallBack = () => {
    console.log(`Core :: startPage :: loadLanguageCallBack`);
    Core.loadLanguage(loadLanguageCallBack);
  };
}

window.addEventListener('load', pageLoaded, false);
window.addEventListener('DOMContentLoaded', () => {
  // TODO if the div is found in the html code take the html code
  console.log('DOM fully loaded and parsed');
  Core.loadingPageBlockerDiv = document.createElement("div");
  Core.loadingPageBlockerDiv.id = 'loadingPageBlocker';
  Core.loadingPageBlockerDiv.style.position = "absolute";
  Core.loadingPageBlockerDiv.style.display = "flex";
  Core.loadingPageBlockerDiv.style.top = "0px";
  Core.loadingPageBlockerDiv.style.width = "100vw";
  Core.loadingPageBlockerDiv.style.height = "100vh";
  Core.loadingPageBlockerDiv.style.backgroundColor = "white";
  Core.loadingPageBlockerDiv.style.zIndex = "20000000";
  Core.loadingPageBlockerDiv.focus();
  Core.loadingPageBlockerDiv.style.justifyContent = "space-evenly";
  Core.loadingPageBlockerDiv.style.alignItems = "center";
  document.body.appendChild(Core.loadingPageBlockerDiv);
  // TODO Create the Throbber using svg and rotating
  const progressIndicatorContainer = document.createElement("div");
  progressIndicatorContainer.innerHTML = "Loading...";
  progressIndicatorContainer.style.fontSize = "26px";

  Core.loadingPageBlockerDiv.appendChild(progressIndicatorContainer);
});