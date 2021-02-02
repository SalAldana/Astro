package net.level3studios.astro.models

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.level3studios.astro.R
import net.level3studios.astro.ui.theme.*
import net.level3studios.astro.utilities.DatabaseController
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

class AstroViewModel(val databaseController: DatabaseController?) : ViewModel() {

    enum class Phase(var label: String) {
        NOON("NOON"),
        SUNRISE("SUNRISE"),
        SUNSET("SUNSET"),
        NIGHT("NIGHT");

        fun isNightTheme(): Boolean {
            return this == NIGHT
        }

        fun themeGradient(): Brush {
            return when (this) {
                NOON -> dayGradient
                NIGHT -> nightGradient
                else -> peakGradient
            }
        }

        fun themePrimaryColor(): Color {
            return when (this) {
                NOON -> fuzzyBrown200
                NIGHT -> portGore200
                else -> bouquet200
            }
        }

        fun themeSecondaryColor(): Color {
            return when (this) {
                NOON -> fuzzyBrown500
                NIGHT -> portGore500
                else -> bouquet500
            }
        }

        fun themeImage(): Int {
            return when (this) {
                NOON -> R.drawable.ic_day_scene
                NIGHT -> R.drawable.ic_night_scene
                else -> R.drawable.ic_sunset_scene
            }
        }

        fun themeTextColor(): Color {
            return when (this) {
                NOON -> pickledBluewood500
                NIGHT -> waterloo200
                else -> martinique500
            }
        }
    }

    //region Live Data
    val viewPhase = MutableLiveData<Phase>(Phase.NOON)
    val viewLocation = MutableLiveData<AstroLocation>(AstroLocation.defaultLocation())
    val viewDate = MutableLiveData<LocalDateTime>(LocalDateTime.now())
    //endregion

    //region Private variables
    private var validDates: Pair<LocalDate, LocalDate>
    //endregion

    init {
        val today = LocalDate.now()
        val period = Period.of(4, 0, 0)
        val pastDate = today.minus(period)
        val futureDate = today.plus(period)
        this.validDates = Pair(pastDate, futureDate)
    }

    fun resetModel() {
        this.viewPhase.value = Phase.NOON
        this.viewLocation.value = AstroLocation.defaultLocation()
        this.viewDate.value = LocalDateTime.now()
    }


    //region Phase functions
    fun toggleTheme() {
        if (this.viewPhase.value?.isNightTheme() == true) {
            this.viewPhase.value = Phase.NOON
        } else {
            this.viewPhase.value = Phase.NIGHT
        }
    }

    fun sunPhases(): List<String> {
        return listOf(Phase.SUNRISE.label, Phase.NOON.label, Phase.SUNSET.label)
    }

    fun setPhase(newPhase: Phase?) {
        this.viewPhase.value = newPhase
    }
    //endregion

    //region Location functions
    fun setLocation(id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val location = databaseController?.getLocationById(id)
            GlobalScope.launch(Dispatchers.Main) {
                viewLocation.value = location
            }
        }
    }

    fun getRandomLocations(): List<AstroLocation>? {
        return databaseController?.getRandomLocations()
    }

    fun getCountryCode(countryName: String?): String? {
        val codes = Locale.getISOCountries()
        return codes.find { Locale("", it).displayCountry == countryName }
    }
    //endregion

    //region Date functions
    fun dateValidation(input: String): Boolean {
        if (input.isEmpty()) {
            return false
        }
        val pastDate = validDates.first
        val futureDate = validDates.second
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        return try {
            val date = LocalDate.parse(input, formatter)
            assert(date != null)
            val pastDateCheck = date.isAfter(pastDate)
            val futureDateCheck = date.isBefore(futureDate)
            return pastDateCheck && futureDateCheck
        } catch (exception: Throwable) {
            false
        }
    }

    fun setNewDate(input: String) {
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        try {
            val newDate = LocalDate.parse(input, formatter)
            assert(newDate != null)
            viewDate.value = newDate.atStartOfDay()
        } catch (exception: Throwable) {
            return
        }
    }
    //endregion
}