const VAPID_PUBLIC_KEY = 'BEn6um5KmEhrlk6Xuyh2FsQF0sV46oZuqnkZG0yqPYUMwKpzldlgq74Vb9QfMfm9Pb7lEijReX_0JtygNmYbw40';

export async function subscribeUser(): Promise<void> {
    const permission = await Notification.requestPermission();
    if (permission !== 'granted') return;

    const registration = await navigator.serviceWorker.getRegistration();
    const subscribed = await registration.pushManager.getSubscription();
    if (subscribed) {
        console.info('User is already subscribed.');
        return;
    }


    const subscription = await registration.pushManager.subscribe({
        userVisibleOnly: true,
        applicationServerKey: urlB64ToUint8Array(VAPID_PUBLIC_KEY)
    });
    console.log(subscription)
    // Send subscription to the backend
    await fetch('/api/v1/subscriptions', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(subscription),
    });
    const unsubscribed = await subscription?.unsubscribe();
    if (unsubscribed) {
        console.info('Successfully unsubscribed from push notifications.');
        // todo trigger request
    }
}
function urlB64ToUint8Array(base64String) {
    const padding = '='.repeat((4 - base64String.length % 4) % 4);
    const base64 = (base64String + padding)
        .replace(/\-/g, '+')
        .replace(/_/g, '/');

    const rawData = window.atob(base64);
    const outputArray = new Uint8Array(rawData.length);

    for (let i = 0; i < rawData.length; ++i) {
        outputArray[i] = rawData.charCodeAt(i);
    }
    return outputArray;
}

export async function askNotificationPermission() {
    if ('Notification' in window) {
        // Check if the user has already granted permission
        if (Notification.permission === 'default') {
            // Request permission to show notifications
            const permission = await Notification.requestPermission();
            if (permission === 'granted') {
                console.log("Notification permission granted.");
                // Proceed to subscribe for push notifications
                subscribeUser();
            } else {
                console.log("Notification permission denied.");
            }
        } else if (Notification.permission === 'granted') {
            console.log("Notification permission already granted.");
            // Proceed to subscribe for push notifications
            subscribeUser();
        }
    } else {
        console.error("This browser does not support notifications.");
    }
}
