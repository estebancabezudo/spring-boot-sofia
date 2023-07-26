const initNotifications = async () => {
    console.log(`'Notification' in window: ${'Notification' in window}`);
    if ('Notification' in window) {
        // Pedir permiso al usuario para mostrar notificaciones


        const permission = await navigator.permissions.query({ name: "notifications" });
        if (permission.state === "granted") {
            // TODO agregar en una variable global que fué aceptado
            console.log(`notifications.js::initNotifications: granted`);
        }



        const handler = setTimeout(() => {
            const img = "/images/icons/places.png";
            const text = `HEY! Your task is now overdue.`;
            const notification = new Notification("To do list", { body: text, icon: img });
            clearTimeout(handler);
        }, 5000);





    } else {
        console.log('El navegador no soporta el API de Notificaciones.');
    }
};

window.addEventListener('load', initNotifications, false);