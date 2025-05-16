import {defineStore} from "pinia";
import type{Coordinates} from "../types";


export const useUserLocationStore = defineStore('userLocation', {
    state: () => ({
        location: null as Coordinates | null,
    }),
    actions: {
        setLocation(latitude: number, longitude: number) {
            this.location = { latitude, longitude } as Coordinates
        },
        clearLocation() {
            this.location = null
        }
    },
})