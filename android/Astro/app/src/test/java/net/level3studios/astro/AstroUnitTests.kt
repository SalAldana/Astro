package net.level3studios.astro

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import net.level3studios.astro.models.AstroViewModel
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class AstroUnitTests {
    private val viewModel = AstroViewModel(databaseController = null)
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testThemeToggle() {
        viewModel.viewPhase.value = AstroViewModel.Phase.NOON
        assertEquals(viewModel.viewPhase.value?.isNightTheme(), false)
        viewModel.toggleTheme()
        assertEquals(viewModel.viewPhase.value?.isNightTheme(), true)
    }

    @Test
    fun sunPhaseList() {
        val sunPhases= viewModel.sunPhases()
        assertTrue(sunPhases.contains(AstroViewModel.Phase.NOON.label))
        assertFalse(sunPhases.contains(AstroViewModel.Phase.NIGHT.label))
    }

    @Test
    fun countryCodeTest() {
        val usTest = viewModel.getCountryCode("United States")
        assertEquals(usTest, "US")
    }

    @Test
    fun dateValidation() {
        val shortPeriod = Period.of(3, 0, 0)
        val longPeriod = Period.of(6, 0, 0)
        val today = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        val todayString = today.format(dateFormatter)
        assertTrue(viewModel.dateValidation(todayString))
        val shortDate = today.plus(shortPeriod)
        val shortDateString = shortDate.format(dateFormatter)
        assertTrue(viewModel.dateValidation(shortDateString))
        val longDate = today.plus(longPeriod)
        val longDateString = longDate.format(dateFormatter)
        assertFalse(viewModel.dateValidation(longDateString))
    }

}