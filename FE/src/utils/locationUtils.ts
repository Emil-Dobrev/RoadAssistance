export  const getCurrentLocation =   () => {
    return new Promise<GeolocationPosition>(async (resolve, reject) => {
        const permissionStatus = await navigator.permissions.query({name: 'geolocation'})

        if (permissionStatus.state === 'denied') {
            reject (new Error('Please enable location in your browser settings.'))
        }
        if (!navigator.geolocation) {
            reject(new Error('Geolocation is not supported by your browser'))
            return
        }
        navigator.geolocation.getCurrentPosition(resolve, reject, {
            enableHighAccuracy: true,
            timeout: 10000,
            maximumAge: 0
        })
    })
}

