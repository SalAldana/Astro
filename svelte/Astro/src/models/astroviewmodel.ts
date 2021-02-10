import dayjs from "dayjs"
import utc from 'dayjs/plugin/utc'
import timezone from 'dayjs/plugin/timezone'
import isBetween from 'dayjs/plugin/isBetween'
import numeral from 'numeral'
import suncalc, { GetTimesResult } from 'suncalc'
import { astroLocation, LocationHelper } from "./worldcities"

enum Phases {
    SUNRISE = "SUNRISE",
    NOON = "NOON",
    SUNSET = "SUNSET",
    NIGHT = "NIGHT"
}

namespace Phase {
    export function isNightTheme(phase: Phases): Boolean {
        return phase == Phases.NIGHT
    }
    export function stringVersion(phase: Phases): String {
        return Phases[phase]
    }
}

class astroViewModel {

    viewPhase: Phases
    viewDate: Date
    viewLocation: astroLocation
    locationService: LocationHelper

    constructor() {
        dayjs.extend(utc)
        dayjs.extend(timezone)
        dayjs.extend(isBetween)
        this.locationService = new LocationHelper()
        this.viewPhase = Phases.NOON
        this.viewDate = new Date()
        this.viewLocation = this.locationService.defaultCity()
    }

    //UI Function
    getGradientStart(): String {
        switch (this.viewPhase) {
            case Phases.NOON: return "from-indigo-100"
            case Phases.NIGHT: return "from-indigo-900"
            default: return "from-purple-400"
        }
    }
    getGradientStop(): String {
        switch (this.viewPhase) {
            case Phases.NOON: return "to-blue-50"
            case Phases.NIGHT: return "to-red-400"
            default: return "to-pink-400"
        }
    }

    getPrimaryColor(): String {
        switch (this.viewPhase) {
            case Phases.NOON: return "bg-yellow-700"
            case Phases.NIGHT: return "bg-purple-900"
            default: return "bg-red-700"
        }
    }

    getSecondaryColor(): String {
        switch (this.viewPhase) {
            case Phases.NOON: return "bg-yellow-100"
            case Phases.NIGHT: return "bg-indigo-500"
            default: return "bg-red-100"
        }
    }

    getPrimaryHoverColor(): String {
        switch (this.viewPhase) {
            case Phases.NOON: return "bg-yellow-800"
            case Phases.NIGHT: return "bg-purple-800"
            default: return "bg-red-800"
        }
    }

    getSecondaryHoverColor(): String {
        switch (this.viewPhase) {
            case Phases.NOON: return "bg-yellow-200"
            case Phases.NIGHT: return "bg-indigo-400"
            default: return "bg-red-200"
        }
    }

    getTextColor(): String {
        switch (this.viewPhase) {
            case Phases.NOON: return "text-yellow-700"
            case Phases.NIGHT: return "text-indigo-900"
            default: return "text-red-700"
        }
    }

    getCityImage(): String {
        switch (this.viewPhase) {
            case Phases.NOON: return "Day_Scene"
            case Phases.NIGHT: return "Night_Scene"
            default: return "Sunset_Scene"
        }
    }
    //Phase functions
    setNewPhase(phaseString: String) {
        switch (phaseString) {
            case "SUNRISE": this.viewPhase = Phases.SUNRISE; break;
            case "SUNSET": this.viewPhase = Phases.SUNSET; break;
            case "NOON": this.viewPhase = Phases.NOON; break;
            case "NIGHT": this.viewPhase = Phases.NIGHT; break;
        }
    }

    getPhaseTitle(): String {
        const isNight = Phase.isNightTheme(this.viewPhase)
        if (isNight) {
            return this.getMoonPhase()
        } else {
            return Phase.stringVersion(this.viewPhase)
        }
    }

    getSunPhaseSubtitle(): String {
        const isNight = Phase.isNightTheme(this.viewPhase)
        if (isNight) {
            return ""
        } else {
            return this.getSunPhaseTime()
        }
    }

    getSunPhaseTime(): String {
        const lat = this.viewLocation.latitude
        const lng = this.viewLocation.longitude
        const times = suncalc.getTimes(this.viewDate, lat, lng)
        const phaseDate = this.getDateForPhase(times)
        const locationZone = this.viewLocation.timezone
        const timeString = dayjs(phaseDate).tz(locationZone).format('hh:mm a')
        return `@ ${timeString}`
    }

    getDateForPhase(fromTimes: GetTimesResult): Date {
        switch (this.viewPhase) {
            case Phases.SUNSET: return fromTimes.sunset
            case Phases.SUNRISE: return fromTimes.sunrise
            default: return fromTimes.solarNoon
        }
    }

    getMoonPhaseSubtitle(): String {
        const isNight = Phase.isNightTheme(this.viewPhase)
        if (isNight) {
            return this.getMoonIllumination()
        } else {
            return ""
        }
    }

    getMoonPhase(): String {
        const moonTimes = suncalc.getMoonIllumination(this.viewDate)
        const phase = moonTimes.phase
        const convertedValue = ((phase * 100.0) / 45.0)
        const phaseInt = getIntValue(convertedValue)
        switch (phaseInt) {
            case -3: return "WAXING CRESCENT"
            case -2: return "FIRST QUARTER"
            case -1: return "WAXING GIBBOUS"
            case 0: return "FULL MOON"
            case 1: return "WANING GIBBOUS"
            case 2: return "LAST QUARTER"
            case 3: return "WANING CRESCENT"
            default: return "NEW MOON"
        }
    }

    getMoonIllumination(): String {
        const moonTimes = suncalc.getMoonIllumination(this.viewDate)
        const fraction = moonTimes.fraction
        const percent = numeral(fraction).format('0%')
        return `${percent} Illumination`
    }

    //Location Functions 
    getRandomLocations(count: number = 4): String[] {
        var arrayLength = this.locationService.worldCities.length
        var randomLocations = new Array(count)
        var takenLocations = new Array(arrayLength)
        while (count--) {
            var index = Math.floor(Math.random() * arrayLength)
            randomLocations[count] = this.locationService.worldCities[index in takenLocations ? takenLocations[index] : index]
            takenLocations[index] = --arrayLength in takenLocations ? takenLocations[arrayLength] : arrayLength
        }
        let cities = randomLocations.flatMap(element => element.city)
        return cities
    }

    setNewLocation(locationName: String) {
        let foundLocation = this.locationService.worldCities.find(element => element.city === locationName)
        if (foundLocation != undefined) {
            this.viewLocation = foundLocation
        }
    }

    //Date Functions
    setNewDate(date: Date) {
        if (dateIsInValidRange(date)) {
            this.viewDate = date
        } else {
            this.viewDate = new Date()
        }
    }
}

//Testable functions
export function dateIsInValidRange(date: Date): Boolean {
    dayjs.extend(isBetween)
    const today = new Date()
    const pastDate = dayjs(today).subtract(8, 'year')
    const futureDate = dayjs(today).add(8, 'year')
    return dayjs(date).isBetween(pastDate, futureDate, 'day')
}

export function getIntValue(fromNumber: number): number {
    if (fromNumber < 0) {
        return Math.ceil(fromNumber)
    } else {
        return Math.round(fromNumber)
    }
}

export { astroViewModel }