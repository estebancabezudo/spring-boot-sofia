const isPushNotificationSupported = () => {
    return 'serviceWorker' in navigator && 'PushManager' in window;
}

const initializePushNotifications = () => {
    return Notification.requestPermission(function (result) {
        return result;
    });
}

const sendNotification = (image, action, title, text) => {
    const options = {
        body: text,
        icon: image,
        vibrate: [200, 100, 200],
        tag: title,
        image,
        badge: image,
        actions: [{ action, title: title, icon: image }]
    };
    navigator.serviceWorker.ready.then(function (serviceWorker) {
        serviceWorker.showNotification(title, options);
    });
}

function registerServiceWorker() {
    navigator.serviceWorker.register("/sw.js").then(swRegistration => {
        // now, you can do something with the service worker registration (swRegistration)
        console.log(swRegistration);
    });
    console.log(navigator.serviceWorker);
}

function notifyMe_(event) {
    navigator.serviceWorker.notifyMe(event);
    // if (isPushNotificationSupported()) {
    //     initializePushNotifications().then(function (consent) {
    //         if (consent === 'granted') {
    //             sendNotification('/images/favicon.png', 'action', 'Test notification', 'This is a test notification');
    //         }
    //     });
    // } else {
    //     alert('Push is not supported by the browser');
    // }
}
const initNotifications = () => {
    registerServiceWorker();
};

window.addEventListener('load', initNotifications, false);