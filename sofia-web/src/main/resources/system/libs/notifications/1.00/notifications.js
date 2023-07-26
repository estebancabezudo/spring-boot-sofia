const initNotifications = () => {
    console.log(`'Notification' in window: ${'Notification' in window}`);
    if ('Notification' in window) {
        // Pedir permiso al usuario para mostrar notificaciones
        Notification.requestPermission()
            .then(permission => {
                console.log(`permission: ${permission}`);
                if (permission === 'granted') {
                    // Si el usuario otorgó el permiso, mostrar la notificación
                }
            });
    } else {
        console.log('El navegador no soporta el API de Notificaciones.');
    }
};

window.addEventListener('load', initNotifications, false);