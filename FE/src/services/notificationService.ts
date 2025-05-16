import api from './api'

export interface PushSubscription {
  endpoint: string;
  keys: {
    p256dh: string;
    auth: string;
  };
}

const VAPID_PUBLIC_KEY = 'BPNBYPHNlkndzue9SQ4MfD6vv4BG7Ddnf5Z2_l2Ovk0pH4ZGRhG6-7tqcaJ40VDhd20xgGu5hx4REyM_IWASapU' // Replace with your actual VAPID public key

export const notificationService = {
  async registerServiceWorker() {
    if ('serviceWorker' in navigator) {
      try {
        const registration = await navigator.serviceWorker.register('/sw.js')
        return registration
      } catch (error) {
        console.error('Service Worker registration failed:', error)
        throw error
      }
    }
    throw new Error('Service Worker not supported')
  },


    async subscribeUserToPush(providerId: string) {
        try {
            // Register Service Worker
            const registration = await notificationService.registerServiceWorker();

            const existingSubscription = await registration.pushManager.getSubscription()
            if (existingSubscription) {
                //TODO add toast
                console.log('User already subscribed:', existingSubscription)
                return existingSubscription
            }

            const subscribeOptions = {
                userVisibleOnly: true,
                applicationServerKey: this.urlBase64ToUint8Array(VAPID_PUBLIC_KEY)
            }

            const pushSubscription = await registration.pushManager.subscribe(subscribeOptions)

            await this.saveSubscription(pushSubscription, providerId)

            return pushSubscription
        } catch (error) {
            console.error('Failed to subscribe to push:', error)
            throw error
        }
    },


  async saveSubscription(subscription: globalThis.PushSubscription, providerId: string) {
    try {
      await api.post(`/api/v1/subscriptions/${providerId}`, subscription)
    } catch (error) {
      console.error('Failed to save subscription to server:', error)
      throw error
    }
  },

  urlBase64ToUint8Array(base64String: string) {
    const padding = '='.repeat((4 - base64String.length % 4) % 4)
    const base64 = (base64String + padding)
      .replace(/\-/g, '+')
      .replace(/_/g, '/')

    const rawData = window.atob(base64)
    const outputArray = new Uint8Array(rawData.length)

    for (let i = 0; i < rawData.length; ++i) {
      outputArray[i] = rawData.charCodeAt(i)
    }
    return outputArray
  }
}

export async function enablePushNotifications() {
    try {
        // Ask user permission
        const permission = await Notification.requestPermission();
        if (permission !== 'granted') {
            console.warn('Permission not granted for notifications');
            return;
        }
    } catch (error) {
        console.error('Pushing push notifications:', error);
    }
}