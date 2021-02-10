import json from './worldcities.json'

export interface astroLocation {
    id: number;
    city: string;
    country: string;
    timezone: string;
    latitude: number;
    longitude: number
}

class LocationHelper {

    worldCities: astroLocation[]

    constructor() {
        this.worldCities = JSON.parse(JSON.stringify(json)) as astroLocation[]
    }

    defaultCity(): astroLocation {
        return {
            id: 297,
            city: "San Francisco",
            country: "United States",
            timezone: "America/Los_Angeles",
            latitude: 37.7749295,
            longitude: -122.4194183
        }
    }
}

export { LocationHelper }