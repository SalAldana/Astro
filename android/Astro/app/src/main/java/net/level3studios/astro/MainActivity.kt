package net.level3studios.astro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.rememberLottieAnimationState
import net.level3studios.astro.models.AstroLocation
import net.level3studios.astro.models.AstroViewModel
import net.level3studios.astro.utilities.AstronomyHelper
import net.level3studios.astro.utilities.DatabaseController
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val databaseController = DatabaseController(this)
        val viewModel = AstroViewModel(databaseController)
        setContent {
            AstroView(viewModel = viewModel)
        }
    }
}

//region UI Builder
@Composable
fun AstroView(viewModel: AstroViewModel) {
    Scaffold(topBar = { AstroTopBar(viewModel = viewModel) },
        bottomBar = { AstroBottomBar(viewModel = viewModel) }) {
        AstroDetailView(viewModel = viewModel)
    }
}
//endregion

//region Top App Bar
@Composable
fun AstroTopBar(viewModel: AstroViewModel) {
    val viewTheme = viewModel.viewPhase.observeAsState()
    val backgroundColor = viewTheme.value?.themePrimaryColor() ?: Color.Black
    val openSettings = remember { mutableStateOf(false) }
    TopAppBar(title = { Text(stringResource(id = R.string.app_name)) },
        backgroundColor = backgroundColor,
        contentColor = Color.White,
        actions = {
            IconButton(onClick = {
                viewModel.toggleTheme()
            }) {
                Icon(vectorResource(id = R.drawable.ic_baseline_360_24))
            }
            IconButton(onClick = {
                openSettings.value = true
            }) {
                Icon(vectorResource(id = R.drawable.ic_baseline_settings_24))
            }
        })
    if (openSettings.value) {
        SettingsDialog(viewModel = viewModel, isOpen = openSettings)
    }
}

@Composable
fun SettingsDialog(viewModel: AstroViewModel, isOpen: MutableState<Boolean>) {
    val buttonColor = viewModel.viewPhase.value?.themePrimaryColor() ?: Color.Black
    val colors = ButtonDefaults.buttonColors(
        backgroundColor = buttonColor,
        contentColor = Color.White
    )
    AlertDialog(onDismissRequest = {
        isOpen.value = false
    }, title = { Text(text = "Settings") },
        text = { Text(text = "Thanks for checking out Astro \uD83D\uDE0E") },
        confirmButton = {
            Button(onClick = {
                viewModel.resetModel()
                isOpen.value = false
            }, colors = colors) {
                Text(text = "Reset Astro")
            }
        },
        dismissButton = {
            Button(onClick = {
                isOpen.value = false
            }, colors = colors) {
                Text(text = "Close")
            }
        })
}

//endregion

//region Main View
private const val LOTTIE_VIEW_TAG = "lottieView"
private const val PHASE_DISPLAY_TAG = "phaseDisplay"
private const val PHASE_SUBTITLE_TAG = "phaseSubtitle"
private const val PHASE_CITY_TAG = "phaseCity"
private val detailConstraintSet = ConstraintSet {
    val lottieRef = createRefFor(LOTTIE_VIEW_TAG)
    val displayRef = createRefFor(PHASE_DISPLAY_TAG)
    val subtitleRef = createRefFor(PHASE_SUBTITLE_TAG)
    val cityRef = createRefFor(PHASE_CITY_TAG)

    constrain(lottieRef) {
        top.linkTo(parent.top, 12.dp)
        centerHorizontallyTo(parent)
    }
    constrain(displayRef) {
        top.linkTo(lottieRef.bottom, 12.dp)
        centerHorizontallyTo(parent)
    }
    constrain(subtitleRef) {
        top.linkTo(displayRef.bottom, 4.dp)
        centerHorizontallyTo(parent)
    }
    constrain(cityRef) {
        top.linkTo(subtitleRef.bottom, 12.dp)
        centerHorizontallyTo(parent)
    }

}

@Composable
fun AstroDetailView(viewModel: AstroViewModel) {
    val viewTheme = viewModel.viewPhase.observeAsState()
    val gradientColor = viewTheme.value?.themeGradient() ?: Brush.linearGradient()
    ConstraintLayout(
        constraintSet = detailConstraintSet,
        Modifier.background(gradientColor)
    ) {
        AstroLottieView(viewModel = viewModel)
        AstroInfoView(viewModel = viewModel)
        AstroCityView(viewModel = viewModel)
    }
}


@Composable
fun AstroLottieView(viewModel: AstroViewModel) {
    val viewTheme = viewModel.viewPhase.observeAsState()
    val sunAnimation = remember { LottieAnimationSpec.RawRes(R.raw.sun_burst_weather_icon) }
    val moonAnimation = remember { LottieAnimationSpec.RawRes(R.raw.moon_icon) }
    val animationSpec =
        if (viewTheme.value?.isNightTheme() == true) moonAnimation else sunAnimation
    val animationState =
        rememberLottieAnimationState(autoPlay = true, repeatCount = Integer.MAX_VALUE)
    LottieAnimation(
        spec = animationSpec,
        animationState = animationState,
        modifier = Modifier
            .preferredSize(80.dp)
            .layoutId(LOTTIE_VIEW_TAG)
    )
}

@Composable
fun AstroInfoView(viewModel: AstroViewModel) {
    val viewPhase = viewModel.viewPhase.observeAsState()
    val viewDate = viewModel.viewDate.observeAsState()
    val viewLocation = viewModel.viewLocation.observeAsState()
    val astronomyHelper = AstronomyHelper()
    val textColor = viewPhase.value?.themeTextColor() ?: Color.Black
    val openPhasePicker = remember { mutableStateOf(false) }
    TextButton(
        onClick = {
            if (viewPhase.value?.isNightTheme() == false) {
                openPhasePicker.value = true
            }
        },
        modifier = Modifier.layoutId(PHASE_DISPLAY_TAG)
    ) {
        Text(
            text = astronomyHelper.getPhaseDisplay(viewPhase.value, viewDate.value),
            color = textColor,
            style = MaterialTheme.typography.button,
            fontSize = TextUnit.Sp(22)
        )
    }
    Text(
        text = astronomyHelper.getPhaseSubtitle(
            viewPhase.value,
            viewDate.value,
            viewLocation.value
        ),
        color = textColor,
        style = MaterialTheme.typography.h5,
        fontSize = TextUnit.Sp(18),
        modifier = Modifier.layoutId(PHASE_SUBTITLE_TAG)
    )
    if (openPhasePicker.value) {
        AstroPhaseDialog(viewModel = viewModel, isOpen = openPhasePicker)
    }
}

@Composable
fun AstroPhaseDialog(viewModel: AstroViewModel, isOpen: MutableState<Boolean>) {
    val primaryColor = viewModel.viewPhase.value?.themePrimaryColor() ?: Color.Black
    val buttonColors = ButtonDefaults.buttonColors(primaryColor, contentColor = Color.White)
    val radioColors = RadioButtonDefaults.colors(primaryColor, unselectedColor = primaryColor)
    val pickerItems = viewModel.sunPhases()
    val selectedPhase = remember { mutableStateOf(viewModel.viewPhase.value) }
    AlertDialog(onDismissRequest = {
        isOpen.value = false
    }, title = { Text("Select a Phase") },
        text = {
            LazyColumn {
                items(pickerItems) { phase ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = phase == selectedPhase.value?.label, onClick = {
                            selectedPhase.value = AstroViewModel.Phase.valueOf(phase)
                        }, colors = radioColors)
                        Text(text = phase)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                viewModel.setPhase(selectedPhase.value)
                isOpen.value = false
            }, colors = buttonColors) {
                Text("Update Phase")
            }
        },
        dismissButton = {
            Button(onClick = {
                isOpen.value = false
            }, colors = buttonColors) {
                Text("Cancel")
            }
        })
}

@Composable
fun AstroCityView(viewModel: AstroViewModel) {
    val viewTheme = viewModel.viewPhase.observeAsState()
    val cityImage = viewTheme.value?.themeImage() ?: 0
    Image(
        painterResource(id = cityImage),
        modifier = Modifier
            .layoutId(PHASE_CITY_TAG)
            .fillMaxWidth()
    )
}
//endregion

//region Bottom App Bar
@Composable
fun AstroBottomBar(viewModel: AstroViewModel) {
    val phase = viewModel.viewPhase.observeAsState()
    val location = viewModel.viewLocation.observeAsState()
    val date = viewModel.viewDate.observeAsState()
    val primaryColor = phase.value?.themePrimaryColor() ?: Color.Black
    val secondaryColor = phase.value?.themeSecondaryColor() ?: Color.Blue
    val colors = ButtonDefaults.buttonColors(
        backgroundColor = secondaryColor,
        contentColor = Color.White
    )
    val openDatePicker = remember { mutableStateOf(false) }
    val openLocationPicker = remember { mutableStateOf(false) }
    BottomAppBar(backgroundColor = primaryColor) {
        Button(onClick = {
            openLocationPicker.value = true
        }, colors = colors, modifier = Modifier.padding(end = 12.dp)) {
            Image(vectorResource(id = R.drawable.ic_baseline_pin_drop_24))
            var locationText: String
            location.value.let {
                val country = viewModel.getCountryCode(it?.country)
                locationText = "${it?.city}, $country"
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = locationText)
        }
        Button(onClick = {
            openDatePicker.value = true
        }, colors = colors) {
            Image(vectorResource(id = R.drawable.ic_baseline_calendar_today_24))
            var dateText: String
            date.value.let {
                val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                dateText = it?.format(formatter) ?: ""
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = dateText)
        }
    }
    if (openDatePicker.value) {
        AstroDateDialog(viewModel = viewModel, isOpen = openDatePicker)
    }
    if (openLocationPicker.value) {
        AstroLocationPicker(viewModel = viewModel, isOpen = openLocationPicker)
    }
}

@Composable
fun AstroDateDialog(viewModel: AstroViewModel, isOpen: MutableState<Boolean>) {
    val buttonColor = viewModel.viewPhase.value?.themePrimaryColor() ?: Color.Black
    val colors = ButtonDefaults.buttonColors(
        backgroundColor = buttonColor,
        contentColor = Color.White
    )
    val dateText = remember { mutableStateOf("") }
    val isValid = viewModel.dateValidation(dateText.value)
    AlertDialog(onDismissRequest = {},
        title = { Text(text = "Enter new date") },
        text = {
            OutlinedTextField(value = dateText.value,
                onValueChange = {
                    dateText.value = it
                }, label = {
                    Text(text = "MM/DD/YYYY")
                },
                isErrorValue = !isValid
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isValid) {
                        viewModel.setNewDate(dateText.value)
                        isOpen.value = false
                    }
                }, colors = colors,
                enabled = isValid
            ) {
                Text(text = "Update Date")
            }
        },
        dismissButton = {
            Button(onClick = {
                isOpen.value = false
            }, colors = colors) {
                Text(text = "Cancel")
            }
        }
    )
}

@Composable
fun AstroLocationPicker(viewModel: AstroViewModel, isOpen: MutableState<Boolean>) {
    val buttonColor = viewModel.viewPhase.value?.themePrimaryColor() ?: Color.Black
    val colors = ButtonDefaults.buttonColors(
        backgroundColor = buttonColor,
        contentColor = Color.White
    )
    val selectedLocationId = remember { mutableStateOf(0) }
    AlertDialog(onDismissRequest = {
        isOpen.value = false
    },
        title = { Text(text = "Select new location") },
        text = {
            val locations = viewModel.getRandomLocations() ?: emptyList()
            LazyColumn(content = {
                items(locations) { location ->
                    AstroLocationItem(
                        location = location,
                        selectedId = selectedLocationId,
                        color = buttonColor
                    )
                }
            })
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.setLocation(selectedLocationId.value)
                    isOpen.value = false
                },
                colors = colors,
                enabled = selectedLocationId.value > 0
            ) {
                Text(text = "Update Location")
            }
        },
        dismissButton = {
            Button(onClick = {
                isOpen.value = false
            }, colors = colors) {
                Text(text = "Cancel")
            }
        })
}

@Composable
fun AstroLocationItem(location: AstroLocation, selectedId: MutableState<Int>, color: Color) {
    val buttonColors = RadioButtonDefaults.colors(
        selectedColor = color,
        unselectedColor = color
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selectedId.value == location.id,
            onClick = {
                selectedId.value = location.id
            },
            colors = buttonColors
        )
        Text(text = "${location.city}, ${location.country}")
    }
}
//endregion

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AstroView(AstroViewModel(databaseController = null))
}