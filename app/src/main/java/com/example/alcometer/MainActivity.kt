package com.example.alcometer.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
//import androidx.compose.material3.menuAnchor
import androidx.compose.material3.*
import androidx.compose.runtime.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlcometerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Alcometer(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/* Documentation:
- https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/-exposed-dropdown-menu-box.html
- https://developer.android.com/reference/kotlin/androidx/compose/material3/ExposedDropdownMenuDefaults
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Alcometer(modifier: Modifier = Modifier) {

    // state variables for inputs and result
    //var weight by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf<Int?>(null) }

    // states for radio buttons gender selection
    val genderOptions = listOf("Female", "Male")
    var selectedGender by remember { mutableStateOf(genderOptions[0]) } // first option in index 0 is selected by default


    // states for dropdown options (number of bottles consumed and hours since drinking started)
    val bottleOptions = (1..12).map { it.toString() } // max 12 bottles (2 x sixpack)
    val hourOptions = (0..12).toList()   // max 12 h

    // states for dropdown menus visibility
    var showPortionMenu by remember { mutableStateOf(false) }
    var showHoursMenu by remember { mutableStateOf(false) }

    // state for selected values of dropdown menu options
    var portionQuantity by remember { mutableStateOf<Int?>(1) } // selected bottles amount
    var hoursConsumed by remember {  mutableStateOf<Int?>(1)} // selected hours amount

    // result state variable
    var result by remember { mutableDoubleStateOf(0.0) }
    // error state variable for button
    var showError by remember { mutableStateOf(false) }

    // all content inside column
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
        // horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)

    ) {
        // heading
        Text(
            text = "Alcometer",
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        // input field for weight
        OutlinedTextField(
            value = weight?.toString() ?: "",
            onValueChange = { weight = it.toIntOrNull() },
            label = { Text("Your weight") },
            singleLine = true,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        // error handling for weight input
        if (weight != null && weight!! < 1) {
            Text(
                text = "Please enter a valid weight",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        // radio button for gender selection, both selections are on their own row
        // loop over gender options to render each button with correct label
        genderOptions.forEach { option ->
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = (option == selectedGender), // set selected state based on option
                    onClick = {
                        selectedGender = option
                    }
                )
                Text(
                    text = option // label according to selection
                )
            }
        }

        // dropdown menu for bottles selection
        ExposedDropdownMenuBox(
            expanded = showPortionMenu,
            onExpandedChange = { showPortionMenu = !showPortionMenu }
        ) {

            OutlinedTextField(
                value = portionQuantity?.toString() ?: "",
                onValueChange = {}, // no handwriting
                readOnly = true,
                label = { Text("Number of bottles drunk (0.33 l)") },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
                    .padding(16.dp),
                //.clickable { showPortionMenu = true }, // menu visible when clicked
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = showPortionMenu) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.primary )
            )

            ExposedDropdownMenu(
                expanded = showPortionMenu,
                onDismissRequest = { showPortionMenu = false }, // menu disappears when clicked outside
            ) {
                // loop over options to render each one
                bottleOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.toString()) },
                        onClick = {
                            portionQuantity = option.toIntOrNull()
                            showPortionMenu = false
                        }
                    )
                }
            }
        }
        // dropdown menu for hours selection
        ExposedDropdownMenuBox(
            expanded = showHoursMenu,
            onExpandedChange = { showHoursMenu = !showHoursMenu }
        ) {
            OutlinedTextField(
                value = hoursConsumed?.toString() ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Hours since drinking started") },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
                    .padding(16.dp),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = showHoursMenu) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.primary )
            )

            ExposedDropdownMenu(
                expanded = showHoursMenu,
                onDismissRequest = { showHoursMenu = false },
            ) {
                // again looping over options
                hourOptions.forEach { hour ->
                    DropdownMenuItem(
                        text = { Text(hour.toString()) },
                        onClick = {
                            hoursConsumed = hour
                            showHoursMenu = false
                        }
                    )
                }
            }
        }
        // button to perform the blood alcohol level calculation
        Button(
            onClick = {
                if (weight != null ) {

                    val litres = portionQuantity!!.toDouble() * (0.33)
                    val grams = litres * (8) * (4.5)
                    val burning = weight!!.toDouble() / 10
                    val gramsLeftToBurn = grams - ((burning * hoursConsumed!!.toDouble()))

                    // genders have different blood water rates
                    result = if(selectedGender == "Female") {
                        gramsLeftToBurn / (weight!!.toDouble() * 0.6)
                    } else {
                        gramsLeftToBurn / (weight!!.toDouble() * 0.7)
                    }
                    showError = false
                } else {
                    showError = true
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        {
            Text(
                text = "Calculate",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        // error message if weight is not given but button is pressed (dropdowns have default values)
        if (showError ) {
            Text(
                "Please input your weight in order to calculate your blood alcohol level",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        // display result (not visible initially)
        if (result != 0.0) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .border(
                        BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            )
            {
                Text(
                    text = "Your blood alcohol level is ${"%.2f".format(result)} ‰",
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AlcometerPreview() {
    AlcometerTheme {
        Alcometer()
    }
}

