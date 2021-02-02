package net.level3studios.astro.utilities

import android.util.Log
import net.level3studios.astro.models.AstroLocation
import net.level3studios.astro.models.AstroViewModel
import org.shredzone.commons.suncalc.MoonIllumination
import org.shredzone.commons.suncalc.SunTimes
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class AstronomyHelper() {

    //region Phase Title Helpers
    fun getPhaseDisplay(phase: AstroViewModel.Phase?, date: LocalDateTime?): String {
        return when (phase) {
            AstroViewModel.Phase.NIGHT -> getMoonPhase(date)
            else -> phase?.label ?: ""
        }
    }

    enum class MoonPhase(var label: String) {
        NEW("NEW MOON"),
        WAXINGCRES(label = "WAXING CRESCENT"),
        FIRSTQ(label = "FIRST QUARTER"),
        WAXINGGIBB("WAXING GIBBOUS"),
        FULL("FULL MOON"),
        WANINGGIBB("WANING GIBBOUS"),
        LASTQ("LAST QUARTER"),
        WANINGCRES("WANING CRESCENT");
    }

    private fun getMoonPhase(date: LocalDateTime?): String {
        val moonCalc = MoonIllumination.compute().on(date).execute()
        val phase = moonCalc.phase
        val convertedValue = (phase / 45.0).toInt()
        var moonValue: MoonPhase = MoonPhase.NEW
        when (convertedValue) {
            -4 -> moonValue = MoonPhase.NEW
            -3 -> moonValue = MoonPhase.WAXINGCRES
            -2 -> moonValue = MoonPhase.FIRSTQ
            -1 -> moonValue = MoonPhase.WAXINGGIBB
            0 -> moonValue = MoonPhase.FULL
            1 -> moonValue = MoonPhase.WANINGGIBB
            2 -> moonValue = MoonPhase.LASTQ
            3 -> moonValue = MoonPhase.WANINGCRES
            4 -> moonValue = MoonPhase.NEW
        }
        return moonValue.label
    }


    //region Phase Subtitle Helpers
    fun getPhaseSubtitle(
        phase: AstroViewModel.Phase?,
        date: LocalDateTime?,
        location: AstroLocation?
    ): String {
        return when (phase) {
            AstroViewModel.Phase.NIGHT -> getMoonIllumination(date)
            else -> getSunTime(phase, date, location)
        }
    }

    private fun getSunTime(
        phase: AstroViewModel.Phase?,
        date: LocalDateTime?,
        location: AstroLocation?
    ): String {
        val lat = location?.coordinates?.latitude ?: 0.0
        val lng = location?.coordinates?.longitude ?: 0.0
        val sunTimes = SunTimes.compute().on(date).at(lat, lng).execute()
        val phaseDate = when (phase) {
            AstroViewModel.Phase.SUNRISE -> sunTimes.rise
            AstroViewModel.Phase.SUNSET -> sunTimes.set
            else -> sunTimes.noon
        }
        val dateFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
        val zone = ZoneId.of(location?.timeZone)
        dateFormatter.withZone(zone)
        val dateString = phaseDate?.format(dateFormatter)
        return "@ $dateString"
    }

    private fun getMoonIllumination(date: LocalDateTime?): String {
        val moonCalc = MoonIllumination.compute().on(date).execute()
        val fraction = moonCalc.fraction
        val numberFormatter = NumberFormat.getPercentInstance(Locale.getDefault())
        val percentValue = numberFormatter.format(fraction)
        return "$percentValue Illumination"
    }
    //endregion

}